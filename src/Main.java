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
	public static final float GUI_SIZE = 500;
	
    public static void main(String[] args)
    {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage)
    {
        BorderPane root = new BorderPane();
        root.setPrefSize(GUI_SIZE, GUI_SIZE);

        // Split the application up into three panes
        AppPane foodPane = AppPaneFactory.createPane(PaneType.FOOD_PANE);
        AppPane mealPane = AppPaneFactory.createPane(PaneType.MEAL_PANE);
        AppPane infoPane = AppPaneFactory.createPane(PaneType.INFO_PANE);

        foodPane.content.addAll("Food1", "Food2", "Food3", "Food4");
        mealPane.content.addAll("Food5", "Food6", "Food7", "Food8");
        foodPane.content.removeAll("Food1", "Food2");
        
        //adjusts widths of panes so food and meal pane take up half
        //and info pane takes up the other of the GUI
        foodPane.setPrefWidth(GUI_SIZE/4);
        mealPane.setPrefWidth(GUI_SIZE/4);
        infoPane.setPrefWidth(GUI_SIZE/2);

        // Set the alignment of the panes
        root.setLeft(foodPane);
        root.setCenter(mealPane);
        root.setRight(infoPane);

        //init the application
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Food Query");

        stage.show();
    }
}