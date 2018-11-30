import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;


import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public class Main extends Application
{
	//GUI Size
	public static final int size = 500;
	
    public static void main(String[] args)
    {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage)
    {
        BorderPane root = new BorderPane();
        root.setPrefSize(size, size);

        // Split the application up into three panes
        VBox foodPane = new VBox();
        VBox mealPane = new VBox();
        VBox infoPane = new VBox();
        
        //adjusts widths of panes so food and meal pane take up half
        //and info pane takes up the other of the GUI
        foodPane.setPrefWidth(size/4);
        mealPane.setPrefWidth(size/4);
        infoPane.setPrefWidth(size/2);

        // Set the alignment of the panes
        root.setLeft(foodPane);
        root.setCenter(mealPane);
        root.setRight(infoPane);

        //food pane setup
        VBox queryVBox = new VBox();
        Label queryLabel = new Label("Query");
        TextField queryTextField = new TextField();

        ScrollPane foodScrollPane = new ScrollPane();

        //Use table view to make life easy
        
        ObservableList<String> foods = FXCollections.observableArrayList("Food1", "Food2", "Food3");
        ArrayList<Label> foodLabels = new ArrayList<>();
        foods.stream().forEach((name) -> foodLabels.add(new Label(name)));
       
        //puts the labels into foodScrollPane
        VBox foodScrollPaneVBox = new VBox();
        for(Label spFoodLabel : foodLabels) {
        	foodScrollPaneVBox.getChildren().add(spFoodLabel);
        }
        foodScrollPane.setContent(foodScrollPaneVBox);
        
        //sets scroll bars
        foodScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        foodScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        queryVBox.getChildren().addAll(queryLabel, queryTextField);
        foodPane.getChildren().addAll(queryVBox, foodScrollPane);

        //meal pane setup
        VBox mealVBox = new VBox();
        Label mealLabel = new Label("Meal");
        ScrollPane mealScrollPane = new ScrollPane();

        
        ObservableList<String> meals = FXCollections.observableArrayList("Food4", "Food5", "Food6");
        ArrayList<Label> mealLabels = new ArrayList<>();
        meals.stream().forEach((name) -> mealLabels.add(new Label(name)));
        
        //puts the labels into mealScrollPane
        VBox mealScrollPaneVBox = new VBox();
        for(Label spMealLabel : mealLabels) {
        	mealScrollPaneVBox.getChildren().add(spMealLabel);
        }
        mealScrollPane.setContent(mealScrollPaneVBox);
        
        //sets scroll bars
        mealScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        mealScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        mealVBox.getChildren().addAll(mealLabel);
        mealPane.getChildren().addAll(mealVBox, mealScrollPane);

        //info pane setup
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc =
                new BarChart<String,Number>(xAxis,yAxis);
        bc.setTitle("Meal Summary");
        xAxis.setLabel("Nutrient");
        yAxis.setLabel("Percentage");

        Image nutritionalFacts = new Image("/nutritional_facts.png", true);
        ImageView imageView = new ImageView(nutritionalFacts);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
        
        //set preferred size of image
        imageView.setFitHeight(size/2);
        imageView.setFitWidth(size/2);


        Button analyzeButton = new Button("Analyze");

        infoPane.getChildren().addAll(bc, imageView, analyzeButton);


        //init the application
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Food Query");

        stage.show();
    }
}