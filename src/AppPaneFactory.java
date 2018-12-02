import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;


public class AppPaneFactory {
    public static AppPane createPane(PaneType paneType) {
        AppPane appPane = new AppPane();

        switch (paneType) {
            case INFO_PANE:
                appPane = buildInfoPane(appPane);
                appPane = consolidate(appPane);
                break;

            default:
                appPane = buildPane(appPane, paneType);
                appPane = consolidate(appPane);
                break;
        }

        return appPane;
    }



    private static AppPane initPane(AppPane appPane, String labelString) {
        appPane.top = new VBox();
        appPane.topLabel = new Label(labelString);

        return appPane;
    }

    public static AppPane buildPane(AppPane appPane, PaneType paneType) {
        switch (paneType) {
            case FOOD_PANE:
                initPane(appPane, "Food");
                appPane.topTextField = new TextField("Query");
                appPane.addRemoveFoodButton = new Button("-/+");
                appPane.addRemoveFoodButton.setOnAction(event-> { 
                	Stage popUp = new Stage();
                	popUp.setHeight(325.0);
                	popUp.setWidth(325.0);
                	popUp.setResizable(false);
                	popUp.setTitle("Add/remove food");
                	
                	Label nameLabel = new Label("Name:");
                	Label caloriesLabel = new Label("Calories:");
                	Label fatLabel = new Label("Fat:");
                	Label carbsLabel = new Label("Carbs:");
                	Label fiberLabel = new Label("Fiber:");
                	Label proteinLabel = new Label("Protein");
                	
                	TextField nameInput = new TextField();
                	TextField caloriesInput = new TextField();
                	TextField fatInput = new TextField();
                	TextField carbsInput = new TextField();
                	TextField fiberInput = new TextField();
                	TextField proteinInput = new TextField();
                	
                	Button addRemove = new Button("Add/Remove");
                	
                	VBox root = new VBox();
                	root.setPrefSize(325.0, 325.0);
                	root.getChildren().addAll(nameLabel,nameInput,caloriesLabel,caloriesInput,
                			fatLabel,fatInput,carbsLabel,carbsInput,fiberLabel,fiberInput,
                			proteinLabel,proteinInput,addRemove);
                	
                	Scene scene = new Scene(root);
                	popUp.setScene(scene);
                	popUp.show();
                });
                break;

            case INFO_PANE:
                initPane(appPane, "Meal");
                break;

            default:
                initPane(appPane, "Food");
                break;
        }

        appPane.contentScrollPane = new ScrollPane();

        //init observable array list and set its onChanged()
        appPane.content = FXCollections.observableArrayList();
        appPane.contentLabels = FXCollections.observableArrayList();
        appPane.content.addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> c) {
                c.next();       //wont work without this idk why...

                if (c.wasAdded()) {
                    System.out.println("Added");
                    for (String name : c.getList()) {
                        Label label = new Label(name);
                        appPane.contentLabels.add(label);
                        appPane.contentVBox.getChildren().add(label);
                    }
                }

                if (c.wasRemoved()) {
                    System.out.println("Removed");
                    for (String name : c.getList()) {
                        //appPane.contentLabels should be replaced in favor of a dictionary of <foodItem : label>
                        Label label = appPane.contentLabels.stream().filter((l) -> l.getText() == name).findFirst().get();
                        appPane.contentVBox.getChildren().remove(label);
                    }
                }
            }
        });


        //puts the labels into appPane.contentVBox
        appPane.contentVBox = new VBox();
        for(Label spFoodLabel : appPane.contentLabels) {
            appPane.contentVBox.getChildren().add(spFoodLabel);
        }
        appPane.contentScrollPane.setContent(appPane.contentVBox);

        //sets scroll bars
        appPane.contentScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        appPane.contentScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        return appPane;
    }

    public static AppPane buildInfoPane(AppPane appPane) {
        appPane.top = new VBox();
        appPane.topLabel = new Label("Info");

        //init bar chart
        appPane.xAxis = new CategoryAxis();
        appPane.yAxis = new NumberAxis();
        appPane.bc = new BarChart<String,Number>(appPane.xAxis, appPane.yAxis);

        appPane.bc.setTitle("Meal Summary");
        appPane.xAxis.setLabel("Nutrient");
        appPane.yAxis.setLabel("Percentage");

        appPane.image = new Image("/nutritional_facts.png", true);
        appPane.imageView = new ImageView(appPane.image);

        appPane.imageView.setPreserveRatio(true);
        appPane.imageView.setSmooth(true);
        appPane.imageView.setCache(true);

        //set preferred size of image
        appPane.imageView.setFitHeight(Main.GUI_SIZE/2);
        appPane.imageView.setFitWidth(Main.GUI_SIZE/2);

        appPane.infoButton = new Button(("Analyze"));

        return appPane;
    }

    public static AppPane consolidate(AppPane appPane) {
        for (Field field : appPane.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            try {
                Object o = field.get(appPane);
                if (o == null) continue;

                appPane.getChildren().add((Node) o);
            }

            //this is a little bit of a hack but idk how else to do it
            //im using this catch ex ception to do a little bit of logic handling, basically
            //if Object o is not of type Node then continue the for loop
            catch (ClassCastException e) {
                continue;
            }

            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return appPane;
    }
}
