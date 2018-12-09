import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.lang.reflect.Field;
import java.util.Random;
import java.util.concurrent.locks.Condition;


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
        appPane.content.addListener(new ListChangeListener<FoodItem>() {
            @Override
            public void onChanged(Change<? extends FoodItem> c) {
                c.next();       //wont work without this idk why...

                if (c.wasAdded()) {
                    //System.out.println("Added");
                    for (FoodItem foodItem : c.getList()) {
                        Label label = new Label(foodItem.getName());
                        appPane.contentLabels.add(label);
                        appPane.contentVBox.getChildren().add(label);
                    }
                }

                if (c.wasRemoved()) {
                    System.out.println("Removed");
                    for (FoodItem foodItem : c.getList()) {
                        //appPane.contentLabels should be replaced in favor of a dictionary of <foodItem : label>
                        Label label = appPane.contentLabels.stream().filter((l) -> l.getText() == foodItem.getName()).findFirst().get();
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

    /**
     * This static method creates the pop-ups to add or remove a food from the list shown
     * on the info pane. Key tasks include:
     * 	- Show a new window when the add ("+") or remove ("-") button is pushed.
     * 	- Show text fields on these new windows for the user to enter food info (of which
     * will be used to either add/remove food from the list of food shown).
     * 	- The new window should also have a button to use user input in the text fields to 
     * add remove/food a food from the observable list.
     * 			**user input must be complete and in proper form to be used
     * 	- When adding food, add to observable list and the underlying data structures
     * 			**Two cases to consider: if duplicate food, you CAN add to observable list, but
     * 				to the underlying data structure.
     * 	- When removing food, keep food in the underlying data structures but just don't show it.
     * 	- Close the window when button is pushed (and conditions are met).
     * @param appPane
     * @return appPane
     */
    public static AppPane buildPopup(AppPane appPane) {
        appPane.addRemoveFood = new HBox(); //use HBox object to display add ("+") and remove ("-)
        Button addFoodButton = new Button("+");
        appPane.addRemoveFood.getChildren().add(addFoodButton);
        addFoodButton.setOnAction(event-> { //event listener for when add button is pushed
            /* Create new Stage to constitute the window. Set its shape */
        	Stage popUp = new Stage();
            popUp.setHeight(325.0);
            popUp.setWidth(325.0);
            popUp.setResizable(false);
            popUp.setTitle("Add food");
            /* Add labels to indicate text fields */
            Label nameLabel = new Label("Name:");
            Label caloriesLabel = new Label("Calories:");
            Label fatLabel = new Label("Fat:");
            Label carbsLabel = new Label("Carbs:");
            Label fiberLabel = new Label("Fiber:");
            Label proteinLabel = new Label("Protein");
            /* Text fields in which the user will enter input of a new food to add */
            TextField nameInput = new TextField();
            TextField caloriesInput = new TextField();
            TextField fatInput = new TextField();
            TextField carbsInput = new TextField();
            TextField fiberInput = new TextField();
            TextField proteinInput = new TextField();
            /* The button that will use user input in the text fields to add new food */
            Button addButton = new Button("Add");
            addButton.setOnAction(add -> {
            	/* Before we add the food, we should validate the user's input, check for
            	 * duplicate foods and generate a unique ID (if it is a new food).	*/
            	
            	/* First, check for empty input. If empty, just have the button pop-up alert. */
            	if (nameInput.getText().length() == 0 || caloriesInput.getText().length() == 0 ||
            			fatInput.getText().length() == 0 || carbsInput.getText().length() == 0 || 
            			fiberInput.getText().length() == 0 || 
            			proteinInput.getText().length() == 0) {
            		Alert emptyFieldsError = new Alert
            				(AlertType.ERROR,"Add food error: missing field(s)");
            		emptyFieldsError.show();
            		return; //don't add anything
            	}
            	
            	/* User input is proper, prepare unique ID and prepare a new FoodItem object. */
            	String newID = generateID();
            	FoodItem newFood = new FoodItem(newID, nameInput.getText());
            	newFood.addNutrient("calories" , Double.valueOf(caloriesInput.getText()));
            	newFood.addNutrient("fat" , Double.valueOf(fatInput.getText()));
            	newFood.addNutrient("carbohydrate" , Double.valueOf(carbsInput.getText()));
            	newFood.addNutrient("fiber" , Double.valueOf(fiberInput.getText()));
            	newFood.addNutrient("protein" , Double.valueOf(proteinInput.getText()));
            	
            	Main.foodDataBase.addFoodItem(newFood);
            	popUp.close();
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
            /* Add a listener for when the button is pressed. Remove from observable list. */
            removeButton.setOnAction(click -> {
            	Main.foodPane.content.removeIf(food -> food.getName().equals(nameInput.getText()));
            });

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
    
    /**
     * Private helper method intended to generate an ID that is unique (i.e., an ID
     * that no other food item in FoodData has.
     * @return a unique ID (length is 24 characters)
     */
    private static String generateID() {
    	StringBuilder sb = null;
    	String id = null;
    	while (true) { //loop until we generate a unique id
    		sb = new StringBuilder();
    		String alphabet = "abcdefghijklmnopqrstuvwxyzZ1234567890";
        	Random r = new Random();
        	while (sb.length() < 24) { //keep adding random character until desired length
        		int index = (int) (r.nextFloat() * alphabet.length());
        		sb.append(alphabet.charAt(index));
        	}
        	id = sb.toString();
        	for (int i = 0; i < Main.foodDataBase.getAllFoodItems().size(); ++i) { //check ID's
        		/* If it is a duplicate, generate a new ID */
        		if (id.equals(Main.foodDataBase.getAllFoodItems().get(i).getName())) continue;
        	}
        	break; //break the loop, since the ID we generated didn't match any of the existing
    	}
        return id;
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
