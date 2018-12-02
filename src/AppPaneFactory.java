import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class AppPaneFactory {
    public static AppPane createPane(PaneType paneType) {
        AppPane appPane = new AppPane();

        switch (paneType) {
            case FOOD_PANE:
                appPane.top = new VBox();
                appPane.topLabel = new Label("Query");
                appPane.topTextField = Optional.of(new TextField());

                appPane.contentScrollPane = Optional.of(new ScrollPane());

                //init observable array list and set its onChanged()
                appPane.content = Optional.of(FXCollections.observableArrayList());
                appPane.contentLabels = Optional.of(FXCollections.observableArrayList());
                appPane.content.get().addListener(new ListChangeListener<String>() {
                    @Override
                    public void onChanged(Change<? extends String> c) {
                        c.next();       //wont work without this idk why...

                        if (c.wasAdded()) {
                            System.out.println("Added");
                            for (String name : c.getList()) {
                                Label label = new Label(name);
                                appPane.contentLabels.get().add(label);
                                appPane.contentVBox.get().getChildren().add(label);
                            }
                        }

                        if (c.wasRemoved()) {
                            System.out.println("Removed");
                            for (String name : c.getList()) {
                                //appPane.contentLabels should be replaced in favor of a dictionary of <foodItem : label>
                                Label label = appPane.contentLabels.get().stream().filter((l) -> l.getText() == name).findFirst().get();
                                appPane.contentVBox.get().getChildren().remove(label);
                            }
                        }
                    }
                });


                //puts the labels into appPane.contentVBox
                appPane.contentVBox = Optional.of(new VBox());
                for(Label spFoodLabel : appPane.contentLabels.get()) {
                    appPane.contentVBox.get().getChildren().add(spFoodLabel);
                }
                appPane.contentScrollPane.get().setContent(appPane.contentVBox.get());

                //sets scroll bars
                appPane.contentScrollPane.get().setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                appPane.contentScrollPane.get().setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                
                //Button toward the button to add/remove foods
                appPane.addRemoveFoodButton = Optional.of(new Button(("-/+")));
                
                //Add corresponding objects to the food pane
                appPane.top.getChildren().addAll(appPane.topLabel, appPane.topTextField.get());
                appPane.getChildren().addAll(appPane.top, appPane.contentScrollPane.get(),
                		appPane.addRemoveFoodButton.get());
                
                break;

            case MEAL_PANE:
                appPane.top = new VBox();
                appPane.topLabel = new Label("Meal");
                appPane.contentScrollPane = Optional.of(new ScrollPane());

                //init observable array list and set its onChanged() Listener
                appPane.content = Optional.of(FXCollections.observableArrayList());
                appPane.contentLabels = Optional.of(FXCollections.observableArrayList());
                appPane.content.get().addListener(new ListChangeListener<String>() {
                    @Override
                    public void onChanged(Change<? extends String> c) {
                        c.next();       //wont work without this idk why...

                        if (c.wasAdded()) {
                            System.out.println("Added");
                            for (String name : c.getList()) {
                                Label label = new Label(name);
                                appPane.contentLabels.get().add(label);
                                appPane.contentVBox.get().getChildren().add(label);
                            }
                        }

                        if (c.wasRemoved()) {
                            System.out.println("Removed");
                            for (String name : c.getList()) {
                                //appPane.contentLabels should be replaced in favor of a dictionary of <foodItem : label>
                                Label label = appPane.contentLabels.get().stream().filter((l) -> l.getText() == name).findFirst().get();
                                appPane.contentVBox.get().getChildren().remove(label);
                            }
                        }
                    }
                });

                //puts the labels into appPane.contentVBox
                appPane.contentVBox = Optional.of(new VBox());
                for(Label spMealLabel : appPane.contentLabels.get()) {
                    appPane.contentVBox.get().getChildren().add(spMealLabel);
                }
                appPane.contentScrollPane.get().setContent(appPane.contentVBox.get());

                //sets scroll bars
                appPane.contentScrollPane.get().setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                appPane.contentScrollPane.get().setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


                appPane.top.getChildren().addAll(appPane.topLabel);
                appPane.getChildren().addAll(appPane.top, appPane.contentScrollPane.get());
                break;

            case INFO_PANE:
                appPane.top = new VBox();
                appPane.topLabel = new Label("Info");

                //init bar chart
                appPane.xAxis = Optional.of(new CategoryAxis());
                appPane.yAxis = Optional.of(new NumberAxis());
                appPane.bc = Optional.of(
                        new BarChart<String,Number>(appPane.xAxis.get(),appPane.yAxis.get()));

                appPane.bc.get().setTitle("Meal Summary");
                appPane.xAxis.get().setLabel("Nutrient");
                appPane.yAxis.get().setLabel("Percentage");

                appPane.image = Optional.of(new Image("/nutritional_facts.png", true));
                appPane.imageView = Optional.of(new ImageView(appPane.image.get()));

                appPane.imageView.get().setPreserveRatio(true);
                appPane.imageView.get().setSmooth(true);
                appPane.imageView.get().setCache(true);

                //set preferred size of image
                appPane.imageView.get().setFitHeight(Main.GUI_SIZE/2);
                appPane.imageView.get().setFitWidth(Main.GUI_SIZE/2);

                appPane.infoButton = Optional.of(new Button(("Analyze")));

                appPane.top.getChildren().addAll(appPane.topLabel);
                appPane.getChildren().addAll(appPane.top, appPane.bc.get(), appPane.imageView.get(), appPane.infoButton.get());
        }

        return appPane;
    }
}
