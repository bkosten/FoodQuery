import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
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
    
    private static List<FoodItem> meal;
    
    public static Double mealFat;
    public static Double mealFiber;
    public static Double mealCalories;
    public static Double mealCarbs;
    public static Double mealProtein;
    
    public static final Double DVFat = 65.0;
    public static final Double DVFiber = 25.0;
    public static final Double DVCals = 2000.0;
    public static final Double DVCarbs = 300.0;
    public static final Double DVProtein = 50.0;

    // Map of nutrients and their corresponding index
    private HashMap<String, BPTree<Double, FoodItem>> indexes;
    
    
    /**
     * Public constructor: initialize private fields. Prepare indexes.
     */
    public FoodData() {
        foodItemList = new ArrayList<FoodItem>();
        meal = new ArrayList<FoodItem>();
        indexes = new HashMap<String,BPTree<Double,FoodItem>>();
        
        indexes.put("fat", new BPTree<Double,FoodItem>(3)); //BRANCHING FACTOR IS SUBJECT TO CHANGE
        indexes.put("fiber", new BPTree<Double,FoodItem>(3));
        indexes.put("calories", new BPTree<Double,FoodItem>(3));
        indexes.put("carbohydrate", new BPTree<Double,FoodItem>(3));
        indexes.put("protein", new BPTree<Double,FoodItem>(3));
        
        //these originally will be set to 0 but are set to arbitrary values to test bar chart
        mealFat = 0.0;
        mealFiber = 0.0;
        mealCalories =  0.0;
        mealCarbs =  0.0;
        mealProtein =  0.0;
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
    		Main.foodPane.content.add(newFood);
    		indexes.get("fat").insert(newFood.getNutrientValue("fat"), newFood);
    		indexes.get("carbohydrate").insert(newFood.getNutrientValue("carbohydrate"), newFood);
    		indexes.get("protein").insert(newFood.getNutrientValue("protein"), newFood);
    		indexes.get("fiber").insert(newFood.getNutrientValue("fiber"), newFood);
    		indexes.get("calories").insert(newFood.getNutrientValue("calories"), newFood);
    	}

		Main.foodPane.cachedContent = new ArrayList<>();
		for (int i = 0; i < Main.foodPane.content.size(); i++) {
			Main.foodPane.cachedContent.add(Main.foodPane.content.get(i));
		}
    }

    /*
     * Gets all the food items that have name containing the substring.
     * CASE-INSENSITIVE
     * 
     * @param substring substring to be searched
     * @return list of filtered food items; if no food item matched, return empty list
     */
    @Override
    public List<FoodItem> filterByName(String substring) {
        String foodName;
        String lowerCaseSubstring = substring.toLowerCase();
        List<FoodItem> matchingFoods = new ArrayList<FoodItem>();
        
        //loops through the foodItemList and adds any foods that
        //contain the substring in their name to matchingFoods
        for(int i = 0; i< foodItemList.size(); i++) {
        	foodName = foodItemList.get(i).getName().toLowerCase();
        	if(foodName.contains(lowerCaseSubstring)) {
        		matchingFoods.add(foodItemList.get(i));
        	}
        }
        return matchingFoods;
    }

    /*
     * Gets all the food items that fulfill ALL the provided rules
     *
     * Format of a rule:
     *     "<nutrient> <comparator> <value>"
     *     
     * @param rules list of rules
     * @return list of filtered food items; if no food item matched, return empty list
     */
    @Override
    public List<FoodItem> filterByNutrients(List<String> rules) {
    	
    	List<FoodItem> ruleMatchingFoods = new ArrayList<FoodItem>();
    	List<FoodItem> compareMatchingFoods = new ArrayList<FoodItem>();
    	List<FoodItem> finalMatchingFoods = new ArrayList<FoodItem>();
    	
    	BPTree<Double, FoodItem> currNutrientTree;
    	
    	String[] currRule;
    	String currNutrient;
    	String currComparator;
    	Double currValue;
    	
    	String currID;
    	
    	for(int i = 0; i < rules.size(); i++) {
    		//parse the current rule
    		currRule = rules.get(i).split(" ");
    		currNutrient = currRule[0].toLowerCase();
    		currComparator = currRule[1];
    		currValue = Double.valueOf(currRule[2]);
    		
    		//access correct nutrient tree
    		currNutrientTree = indexes.get(currNutrient);
    		
    		//get the values that match this rule
    		ruleMatchingFoods = currNutrientTree.rangeSearch(currValue, currComparator);
    		
    		//when 1st rule is applied or none of the rules have retrieved matching foods
    		if(finalMatchingFoods.isEmpty()) {
    			finalMatchingFoods = ruleMatchingFoods;
    		}
    		//compares the finalMatchingFoods with the current ruleMatchingFoods
    		else {
    			for(int k = 0; k < ruleMatchingFoods.size(); k++) {
    				for(int j = 0; j < finalMatchingFoods.size(); j++) {
    					currID = finalMatchingFoods.get(j).getID();
    					//adds food from the current rule to a temp list if it matches a final food 
    					if(currID.equals(ruleMatchingFoods.get(k).getID())) {
    						compareMatchingFoods.add(ruleMatchingFoods.get(k));
    					}
    				}
    			}
    			//replaces the old final matching foods with an updated list
    			finalMatchingFoods = compareMatchingFoods;
    		}
    	}
        return finalMatchingFoods;
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
    	Main.foodPane.content.add(foodItem);
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

    /**
     * Save the list of food items in ascending order by name.
     * @param fileName: the name of the file to which the food items will be saved 
     */
    @Override
    public void saveFoodItems(String filename) {
    	/* Prepare the necessary I/O objects */
    	File file = new File(filename);
    	PrintWriter writer = null;
    	try { 
    		writer = new PrintWriter(filename);
    	} 
    	catch (FileNotFoundException e) {
    		e.printStackTrace();
    	}
    	
    	/* Sort foodItems list to have it in ascending order by name */
    	foodItemList.sort((foodOne,foodTwo) -> {return foodOne.getName().compareTo(foodTwo.getName());});
    	
    	/* Iterate through foods. Print them in the order as the sample .csv file*/
    	for (int i = 0; i < foodItemList.size(); ++i) {
    		FoodItem food = foodItemList.get(i);
    		writer.print(food.getID() + ",");
    		writer.print(food.getName() + ",");
    		writer.print("calories," + Double.toString(food.getNutrientValue("calories")) + ",");
    		writer.print("fat," + Double.toString(food.getNutrientValue("fat")) + ",");
    		writer.print("carbohydrate," + 
    				Double.toString(food.getNutrientValue("carbohydrate")) + ",");
    		writer.print("fiber," + Double.toString(food.getNutrientValue("fiber")) + ",");
    		writer.print("protein," + Double.toString(food.getNutrientValue("protein")) + "\n");
    	}
    	writer.flush();
    }
    
    public static void updateMealInfo() {
    	Double newFat = 0.0;
    	Double newFiber = 0.0;
    	Double newCalories = 0.0;
    	Double newCarbs = 0.0;
    	Double newProtein = 0.0;
    	for(int i = 0; i < Main.mealPane.content.size(); i++) {
    		newFat += Main.mealPane.content.get(i).getNutrientValue("fat");
    		newFiber += Main.mealPane.content.get(i).getNutrientValue("fiber");
    		newCalories += Main.mealPane.content.get(i).getNutrientValue("calories");
    		newCarbs += Main.mealPane.content.get(i).getNutrientValue("carbohydrate");
    		newProtein += Main.mealPane.content.get(i).getNutrientValue("protein");
    	}
    	
    	mealFat = newFat;
    	mealFiber = newFiber;
    	mealCalories = newCalories;
    	mealCarbs = newCarbs;
    	mealProtein = newProtein;
    }
}