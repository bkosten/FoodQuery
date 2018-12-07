import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class Main extends Application
{
	//GUI Size
	public static final float GUI_WIDTH = 640;
	public static final float GUI_HEIGHT = 480;
	
	public static FoodData foodDataBase;
	public static AppPane foodPane;
	public static AppPane mealPane;
	public static AppPane infoPane;
    public static void main(String[] args)
    {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage)
    {
        BorderPane root = new BorderPane();
        root.setPrefSize(GUI_WIDTH, GUI_HEIGHT);
        
        foodDataBase = new FoodData();
        
        // Split the application up into three panes
        foodPane = AppPaneFactory.createPane(PaneType.FOOD_PANE);
        mealPane = AppPaneFactory.createPane(PaneType.MEAL_PANE);
        infoPane = AppPaneFactory.createPane(PaneType.INFO_PANE);

        foodPane.content.addAll();
        //mealPane.content.addAll("Food5", "Food6", "Food7", "Food8");
        //foodPane.content.removeAll("Food1", "Food2");
        
        //adjusts widths of panes so food and meal pane take up half
        //and info pane takes up the other of the GUI
        foodPane.setPrefWidth(GUI_WIDTH/4);
        mealPane.setPrefWidth(GUI_WIDTH/4);
        infoPane.setPrefWidth(GUI_WIDTH/2);

        // Set the alignment of the panes
        root.setLeft(foodPane);
        root.setCenter(mealPane);
        root.setRight(infoPane);

        //init the application
        Scene scene = new Scene(root);
        scene.getStylesheets().add("styles.css");
        stage.setScene(scene);
        stage.setTitle("Food Query");

        stage.show();
        
        
        
    }
}