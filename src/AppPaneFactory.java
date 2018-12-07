import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.lang.reflect.Field;


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

        //apply styles
        appPane.top.setId("top");
        appPane.topLabel.setId("topLabel");
        appPane.setId("pane");

        return appPane;
    }

    public static AppPane buildPane(AppPane appPane, PaneType paneType) {
        switch (paneType) {
            case FOOD_PANE:
                initPane(appPane, "Food");
                appPane.topTextField = new TextField("Query");
                buildPopup(appPane);
                break;

            case MEAL_PANE:
                initPane(appPane, "Meal");
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
        initPane(appPane, "Info");

        //init bar chart
        appPane.xAxis = new CategoryAxis();
        appPane.yAxis = new NumberAxis();
        appPane.bc = new BarChart<String,Number>(appPane.xAxis, appPane.yAxis);

        appPane.bc.setTitle("Info");
        appPane.xAxis.setLabel("N");
        appPane.yAxis.setLabel("%");

        appPane.image = new Image("/nutritional_facts.png", true);
        appPane.imageView = new ImageView(appPane.image);
        appPane.imageView.setId("imageView");

        appPane.imageView.setPreserveRatio(true);
        appPane.imageView.setSmooth(true);
        appPane.imageView.setCache(true);

        //set preferred size of image
        appPane.imageView.setFitHeight(Main.GUI_HEIGHT/3);
        appPane.imageView.setFitWidth(Main.GUI_WIDTH/3);

        appPane.infoButton = new Button(("Analyze"));

        return appPane;
    }

    public static AppPane buildPopup(AppPane appPane) {
        appPane.addRemoveFood = new HBox();
        Button addFoodButton = new Button("+");
        appPane.addRemoveFood.getChildren().add(addFoodButton);
        addFoodButton.setOnAction(event-> {
            Stage popUp = new Stage();
            popUp.setHeight(325.0);
            popUp.setWidth(325.0);
            popUp.setResizable(false);
            popUp.setTitle("Add food");

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

            Button addButton = new Button("Add");
            addButton.setOnAction(add -> {
            	FoodItem newFood = new FoodItem(nameInput.getText(), "asdfasdfasdfasdf");
            	newFood.addNutrient("calories" , Double.valueOf(caloriesInput.getText()));
            	newFood.addNutrient("fat" , Double.valueOf(fatInput.getText()));
            	newFood.addNutrient("carbohydrate" , Double.valueOf(carbsInput.getText()));
            	newFood.addNutrient("fiber" , Double.valueOf(fiberInput.getText()));
            	newFood.addNutrient("protein" , Double.valueOf(proteinInput.getText()));
            	
            	
            });

            VBox root = new VBox();
            root.setPrefSize(325.0, 325.0);
            root.getChildren().addAll(nameLabel,nameInput,caloriesLabel,caloriesInput,
                    fatLabel,fatInput,carbsLabel,carbsInput,fiberLabel,fiberInput,
                    proteinLabel,proteinInput,addButton);

            Scene scene = new Scene(root);
            popUp.setScene(scene);
            popUp.show();
        });

        Button removeFoodButton = new Button("-");
        appPane.addRemoveFood.getChildren().add(removeFoodButton);
        removeFoodButton.setOnAction(event-> {
            Stage popUp = new Stage();
            popUp.setHeight(325.0);
            popUp.setWidth(325.0);
            popUp.setResizable(false);
            popUp.setTitle("Remove food");

            Label nameLabel = new Label("Name:");
            Text or = new Text("\nOR\n");
            Label idLabel = new Label("ID:");


            TextField nameInput = new TextField();
            TextField idInput = new TextField();

            Button removeButton = new Button("Remove");

            VBox root = new VBox();
            root.setPrefSize(325.0, 325.0);
            root.getChildren().addAll(nameLabel,nameInput,or,idLabel,idInput,
                    removeButton);

            Scene scene = new Scene(root);
            popUp.setScene(scene);
            popUp.show();
        });

        return appPane;
    }

    public static AppPane consolidate(AppPane appPane) {
        appPane.top.getChildren().add(appPane.topLabel);

        for (Field field : appPane.getClass().getDeclaredFields()) {
            if (field.getName() == "topLabel") continue;

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
