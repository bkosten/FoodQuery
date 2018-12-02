import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Optional;

public class AppPane extends VBox {
    //Head of each pane
    public VBox top;
    public Label topLabel;
    public Optional<TextField> topTextField;

    //food / meal pane
    public Optional<ScrollPane> contentScrollPane;
    public Optional<VBox> contentVBox;
    public Optional<ObservableList<String>> content;
    public Optional<ObservableList<Label>> contentLabels;
    public Optional<Button> addRemoveFoodButton;

    //info pane
    public Optional<CategoryAxis> xAxis;
    public Optional<NumberAxis > yAxis;
    public Optional<BarChart<String,Number>> bc;
    public Optional<Image> image;
    public Optional<ImageView> imageView;
    public Optional<Button> infoButton;

}
