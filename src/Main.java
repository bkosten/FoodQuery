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
    public static void main(String[] args)
    {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage)
    {
        BorderPane root = new BorderPane();

        // Split the application up into three panes
        VBox foodPane = new VBox();
        VBox mealPane = new VBox();
        VBox infoPane = new VBox();


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
        /*
        ObservableList<String> foods = FXCollections.observableArrayList("Food1", "Food2", "Food3");
        ArrayList<Label> foodLabels = new ArrayList<>();
        foods.stream().forEach((name) -> foodLabels.add(new Label(name)));
        */

        queryVBox.getChildren().addAll(queryLabel, queryTextField);
        foodPane.getChildren().addAll(queryVBox);

        //meal pane setup
        VBox mealVBox = new VBox();
        Label mealLabel = new Label("Meal");
        ScrollPane mealScrollPane = new ScrollPane();

        /*
        ObservableList<String> foods = FXCollections.observableArrayList("Food1", "Food2", "Food3");
        ArrayList<Label> foodLabels = new ArrayList<>();
        foods.stream().forEach((name) -> foodLabels.add(new Label(name)));
        */

        mealVBox.getChildren().addAll(mealLabel);
        mealPane.getChildren().addAll(mealVBox);

        //info pane setup
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc =
                new BarChart<String,Number>(xAxis,yAxis);
        bc.setTitle("Country Summary");
        xAxis.setLabel("Country");
        yAxis.setLabel("Value");

        Image nutritionalFacts = new Image("/nutritional_facts.png", true);
        ImageView imageView = new ImageView(nutritionalFacts);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);


        Button analyzeButton = new Button("Analyze");

        infoPane.getChildren().addAll(bc, imageView, analyzeButton);


        //init the application
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Food Query");

        stage.show();
    }
}