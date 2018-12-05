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
     * Public constructor: initialize private fields.
     */
    public FoodData() {
        foodItemList = new ArrayList<FoodItem>();
        indexes = new HashMap<String,BPTree<Double,FoodItem>>();
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
        // TODO : Complete
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#getAllFoodItems()
     */
    @Override
    public List<FoodItem> getAllFoodItems() {
        // TODO : Complete
        return null;
    }

    @Override
    public void saveFoodItems(String filename) {

    }

}
