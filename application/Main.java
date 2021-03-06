import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Main extends Application
{
	//GUI Size
	public static final float GUI_WIDTH = 1200;
	public static final float GUI_HEIGHT = 650;
	
	public static FoodData foodDataBase;
	public static AppPane foodPane;
	public static AppPane mealPane;
	public static BorderPane infoPane;
	public static BorderPane info = new BorderPane();
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
        infoPane = AppPaneFactory.createInfoPane();

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
        
        
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        MenuItem save = new MenuItem("Save");
        MenuItem load = new MenuItem("Load");
        save.setOnAction(click -> {
        	Stage popup = new Stage();
        	FileChooser saveWindow = new FileChooser();
        	saveWindow.setTitle("Save");
       	 	saveWindow.getExtensionFilters().addAll(
    	         new ExtensionFilter("Text File (.txt)", "*.txt"),
    	         new ExtensionFilter("CSV File (.csv)", "*.csv"));    
        	
        	File selectedFile = saveWindow.showSaveDialog(popup);
        	if (selectedFile == null) return; //closed window before saving
       	 	Main.foodDataBase.saveFoodItems(selectedFile.getAbsolutePath());
        	popup.close();
        });
        load.setOnAction(click -> {
        	Stage popup = new Stage();
        	FileChooser loadWindow = new FileChooser();
        	loadWindow.setTitle("Load");
       	 	loadWindow.getExtensionFilters().addAll(
       	 		 new ExtensionFilter("CSV File (.csv)", "*.csv"), 
    	         new ExtensionFilter("Text File (.txt)", "*.txt"));
    	           
        	
        	File selectedFile = loadWindow.showOpenDialog(popup);
        	if (selectedFile == null) return; //closed window before loading
       	 	Main.foodDataBase.loadFoodItems(selectedFile.getAbsolutePath());
        	popup.close();
        });
	
	
        // --- Menu Help
        Menu menuHelp = new Menu("Help");
        MenuItem addFood = new MenuItem("Add food");
        MenuItem removeFood = new MenuItem("Remove food");
        MenuItem submitQuery = new MenuItem("Submit query");
        MenuItem selectFood = new MenuItem("Select food");
        MenuItem cancelFood = new MenuItem("Cancel food");
        MenuItem viewFood = new MenuItem("View food");
        MenuItem analysis = new MenuItem("Analyze food");
        
        // A popup window displaying instructions to add new food
        addFood.setOnAction(new EventHandler<ActionEvent>() {
        	
        	@Override
        	public void handle(ActionEvent e) {
        		Stage popup = new Stage();
            	popup.setTitle("Add food in Food pane?");
            	Text text = new Text();
            	text.setText("Click '+' in the Food pane, then enter name and nutrient information of food");
            	text.setFont(Font.font("verdana", 20));
            	text.setX(50);
            	text.setY(50);
            	Group textGroup = new Group(text);
            	Scene addFood = new Scene(textGroup, 600, 300);
            	popup.setScene(addFood);
            	popup.show();
        	}
        	
        });
        
        // A popup window displaying instructions to remove existing food
        removeFood.setOnAction(new EventHandler<ActionEvent>() {
        	
        	@Override
        	public void handle(ActionEvent e) {
        		Stage popup = new Stage();
        		popup.setTitle("Remove food from Food pane?");
        		Text text = new Text();
        		text.setText("Click '-' in the Food pane, then select food to remove");
        		text.setFont(Font.font("verdana", 20));
        		text.setX(50);
        		text.setY(50);
        		Group textGroup = new Group(text);
        		Scene removeFood = new Scene(textGroup, 600, 300);
        		popup.setScene(removeFood);
        		popup.show();
        	}
        	
        });
        
        // A popup window displaying instructions to submit food query
        submitQuery.setOnAction(new EventHandler<ActionEvent>() {
        	
        	@Override
        	public void handle(ActionEvent e) {
        		Stage popup = new Stage();
        		popup.setTitle("Submit food query to Food pane?");
        		Text text = new Text();
        		text.setText("Enter food name in the textfield of the Food pane to query for details");
        		text.setFont(Font.font("verdana", 20));
        		text.setX(50);
        		text.setY(50);
        		Group textGroup = new Group(text);
        		Scene submitQuery = new Scene(textGroup, 600, 300);
        		popup.setScene(submitQuery);
        		popup.show();
        	}
        	
        });
        
        // A popup window displaying instructions to view existing food
        viewFood.setOnAction(new EventHandler<ActionEvent>() {
        	
        	@Override
        	public void handle(ActionEvent e) {
        		Stage popup = new Stage();
        		popup.setTitle("View food in Meal pane?");
        		Text text = new Text();
        		text.setText("Scroll down to see added food in the Meal pane");
        		text.setFont(Font.font("verdana", 20));
        		text.setX(50);
        		text.setY(50);
        		Group textGroup = new Group(text);
        		Scene viewFood = new Scene(textGroup, 600, 300);
        		popup.setScene(viewFood);
        		popup.show();
        	}
        	
        });
        
        // A popup window displaying instructions to analyze nutrition information of food
        analysis.setOnAction(new EventHandler<ActionEvent>() {
        	
        	@Override
        	public void handle(ActionEvent e) {
        		Stage popup = new Stage();
        		popup.setTitle("Analyze food?");
        		Text text = new Text();
        		text.setText("Click 'Analyze' button in the Info pane to analyze nutrient values of food");
        		text.setFont(Font.font("verdana", 20));
        		text.setX(50);
        		text.setY(50);
        		Group textGroup = new Group(text);
        		Scene analysis = new Scene(textGroup, 600, 300);
        		popup.setScene(analysis);
        		popup.show();
        	}
        });
        
        // A popup window displaying instructions to 
        // select and add food from the Food pane to the Meal pane
        selectFood.setOnAction(new EventHandler<ActionEvent>() {
        	
        	@Override
        	public void handle(ActionEvent e) {
        		Stage popup = new Stage();
        		popup.setTitle("Select and add food to Meal pane?");
        		Text text = new Text();
        		text.setText("Click a food item in the Food pane to add it to the Meal pane.");
        		text.setFont(Font.font("verdana", 20));
        		text.setX(50);
        		text.setY(50);
        		Group textGroup = new Group(text);
        		Scene selectFood = new Scene(textGroup, 600, 300);
        		popup.setScene(selectFood);
        		popup.show();
        	}
        });
        
        
        // A popup window displaying instructions to cancel food in the Meal pane
        cancelFood.setOnAction(new EventHandler<ActionEvent>() {
        	
        	@Override
        	public void handle(ActionEvent e) {
        		Stage popup = new Stage();
        		popup.setTitle("Cancel food in Meal pane?");
        		Text text = new Text();
        		text.setText("Click a food item in the Meal pane to cancel it.");
        		text.setFont(Font.font("verdana", 20));
        		text.setX(50);
        		text.setY(50);
        		Group textGroup = new Group(text);
        		Scene cancelFood = new Scene(textGroup, 600, 300);
        		popup.setScene(cancelFood);
        		popup.show();
        	}
        	
        });


        
        
        menuHelp.getItems().addAll(addFood, removeFood, submitQuery, viewFood, analysis, selectFood, cancelFood);
        menuFile.getItems().addAll(load,save);
        menuBar.getMenus().addAll(menuFile, menuHelp);
        root.setTop(menuBar);
        
        /* ----------------------------------------------------------------------------------- */
        
        //init the application
        Scene scene = new Scene(root);
        scene.getStylesheets().add("styles.css");
        stage.setScene(scene);
        stage.setTitle("Food Query");
        stage.show();
        
        
        
    }
}
