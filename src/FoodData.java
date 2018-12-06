import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * This class represents the back-end for managing all 
 * the operations associated with FoodItems
 * 
 * @authors Jack Mallmann, John Swanke, Ben Kosten, Diquan Xian
 */
public class FoodData implements FoodDataADT<FoodItem> {
    	
	// List of all the food items.
    private List<FoodItem> foodItemList;

    // Map of nutrients and their corresponding index
    private HashMap<String, BPTree<Double, FoodItem>> indexes;
    
    
    /**
     * Public constructor: initialize private fields. Prepare indexes.
     */
    public FoodData() {
        foodItemList = new ArrayList<FoodItem>();
        indexes = new HashMap<String,BPTree<Double,FoodItem>>();
        
        indexes.put("fat", new BPTree<Double,FoodItem>(2)); //BRANCHING FACTOR IS SUBJECT TO CHANGE
        indexes.put("fiber", new BPTree<Double,FoodItem>(2));
        indexes.put("calories", new BPTree<Double,FoodItem>(2));
        indexes.put("carbohydrate", new BPTree<Double,FoodItem>(2));
        indexes.put("protein", new BPTree<Double,FoodItem>(2));
    }
    
    
    /*
     * Load food from a .csv file. Form for each food/line:
     * <id>,<name>,<nutrient>,<value>,<nutrient>,<value>
     * 
     * Add each food/line to both the foodItemList and add to HashMap.
     * 
     * @param filePath path of data file 
     */
    @Override
    public void loadFoodItems(String filePath) {
        /* First, import file's contents */
    	File file = new File(filePath);
    	Scanner sc = null;
    	try {
    		sc = new Scanner(file);
    	}
    	catch(FileNotFoundException e) { 
    		e.printStackTrace(); 
       	}
    	
    	/* Next, parse each line and add to foodItemList and indexes */
    	while (sc.hasNextLine()) {
    		String line = sc.nextLine();
    		String[] properties = line.split(",");
    		if (properties.length == 0) break; //end of data
    		/* Prepare a FoodItem object to be added */
    		FoodItem newFood = new FoodItem(properties[0],properties[1]);
    		for (int i = 2; i <= 11; i = i + 2) {
    			newFood.addNutrient(properties[i], Double.parseDouble(properties[i+1]));
    		}
    		/* Add new FoodItem to foodItemList and to B+ trees in indexes */
    		foodItemList.add(newFood);
    		indexes.get("fat").insert(newFood.getNutrientValue("fat"), newFood);
    		indexes.get("carbohydrate").insert(newFood.getNutrientValue("carbohydrate"), newFood);
    		indexes.get("protein").insert(newFood.getNutrientValue("protein"), newFood);
    		indexes.get("fiber").insert(newFood.getNutrientValue("fiber"), newFood);
    		indexes.get("calories").insert(newFood.getNutrientValue("calories"), newFood);
    	}
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#filterByName(java.lang.String)
     */
    @Override
    public List<FoodItem> filterByName(String substring) {
        // TODO : Complete
        return null;
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#filterByNutrients(java.util.List)
     */
    @Override
    public List<FoodItem> filterByNutrients(List<String> rules) {
        // TODO : Complete
        return null;
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#addFoodItem(skeleton.FoodItem)
     */
    @Override
    public void addFoodItem(FoodItem foodItem) {
        BPTree<Double, FoodItem> currNutrient;
        String[] nutrientNames = {"fat", "fiber", "calories", "carbohydrate", "protein"};
    	//add foodItem to the foodItemList
    	foodItemList.add(foodItem);
    	//add the foodItem to the nutrient trees
    	for(int i = 0; i < 5; i++) {
    		currNutrient = indexes.get(nutrientNames[i]);
    		currNutrient.insert(foodItem.getNutrientValue(nutrientNames[i]), foodItem);
    	}
    	
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#getAllFoodItems()
     */
    @Override
    public List<FoodItem> getAllFoodItems() {
        return foodItemList;
    }

    @Override
    public void saveFoodItems(String filename) {

    }

}
