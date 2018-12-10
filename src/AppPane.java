import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Node;

import javax.swing.plaf.OptionPaneUI;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Optional;

public class AppPane extends VBox {
    //Head of each pane
    public VBox top;
    public Label topLabel;
    public TextField topTextField;    

    //food / meal pane
    public ScrollPane contentScrollPane;
    public VBox contentVBox;
    public ObservableList<FoodItem> content;
    public ObservableList<Label> contentLabels;
    public HBox addRemoveFood;
    public ListView<FoodItem> foodList;
    public ListView<FoodItem> mealList;
    
    //info pane
    public CategoryAxis xAxis;
    public NumberAxis yAxis;
    public BarChart<String,Number> bc;
    public XYChart.Series<String, Number> nutrients;
    //public Image image;
   // public ImageView imageView;
    public VBox infoValueVBox;
    
    public Button infoButton;
    
    
}
