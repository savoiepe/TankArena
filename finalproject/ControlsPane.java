package finalproject;

import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

public class ControlsPane extends BorderPane {
    private VBox top = new VBox();
    private HBox fillPane = new HBox();
    private HBox instructionsPane = new HBox();
    private GridPane controlsPane = new GridPane();
    private Label player1 = new Label("Player 1");
    private Label[] askControls1 = new Label[5];
    private TextField[] setControls1 = new TextField[5];
    private Label player2 = new Label("Player 2");
    private Label[] askControls2 = new Label[5];
    private TextField[] setControls2 = new TextField[5];
    private ArrayList<String> controlsUsed = new ArrayList<>();
    private Label instructions = new Label("Choose either letters or arrow keys");
    Circle fill = new Circle(80,Color.TRANSPARENT);
    public ControlsPane(double screenWidth, double screenHeight){
        fillPane.getChildren().add(fill);
        instructionsPane.setAlignment(Pos.CENTER);
        instructionsPane.setPadding(new Insets(0,30,0,30));
        instructionsPane.getChildren().add(instructions);
        top.getChildren().addAll(fillPane,instructionsPane);
        this.setTop(top);
    //instructions
        instructions.setFont(new Font("Cooper Black", 20));
        controlsPane.setAlignment(Pos.TOP_CENTER);
        controlsPane.setHgap(15);
        controlsPane.setVgap(15);
        this.setBottom(controlsPane);
        for (int i=0; i<askControls1.length; i++){
            askControls1[i] = new Label();
            askControls1[i].setFont(new Font("Cooper Black", 20));
            controlsPane.add(askControls1[i],0,i);
        }
        for (int i=0; i<setControls1.length; i++){
            setControls1[i] = new TextField();
            setControls1[i].setMaxWidth(250);
            setControls1[i].setFont(new Font("Cooper Black", 20));
            controlsPane.add(setControls1[i],1,i);
        }
        player1.setFont(new Font("Cooper Black", 20));
        player1.setTextFill(Color.RED);
        controlsPane.add(player1,1,5);
        for (int i=0; i<askControls2.length; i++){
            askControls2[i] = new Label();
            askControls2[i].setFont(new Font("Cooper Black", 20));
            controlsPane.add(askControls2[i],2,i);
        }
        for (int i=0; i<setControls2.length; i++){
            setControls2[i] = new TextField();
            setControls2[i].setMaxWidth(250);
            setControls2[i].setFont(new Font("Cooper Black", 20));
            controlsPane.add(setControls2[i],3,i);
        }
        player2.setTextFill(Color.RED);
        player2.setFont(new Font("Cooper Black", 20));
        controlsPane.add(player2,3,5);
    //default
        askControls1[0].setText("Move Left");
        askControls1[1].setText("Move Right");
        askControls1[2].setText("Jump");
        askControls1[3].setText("Fire");
        askControls1[4].setText("Inventory");
        setControls1[0].setText("A");
        setControls1[1].setText("D");
        setControls1[2].setText("W");
        setControls1[3].setText("F");
        setControls1[4].setText("G");
        askControls2[0].setText("Move Left");
        askControls2[1].setText("Move Right");
        askControls2[2].setText("Jump");
        askControls2[3].setText("Fire");
        askControls2[4].setText("Inventory");
        setControls2[0].setText("LEFT");
        setControls2[1].setText("RIGHT");
        setControls2[2].setText("UP");
        setControls2[3].setText("L");
        setControls2[4].setText("K");
    //used
        controlsUsed.add("A"); controlsUsed.add("D"); controlsUsed.add("W"); controlsUsed.add("F"); controlsUsed.add("G");
        controlsUsed.add("LEFT"); controlsUsed.add("RIGHT"); controlsUsed.add("UP"); controlsUsed.add("K"); controlsUsed.add("L");
    //background
        this.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image("finalprojectImages/Settings_Background.jpg")), CornerRadii.EMPTY, Insets.EMPTY)));
     //dimensions
        controlsPane.setMinWidth(screenWidth);
        controlsPane.setMinHeight(screenHeight);
        controlsPane.setMaxWidth(screenWidth);
        controlsPane.setMaxHeight(screenHeight);
    }
    
    public TextField[] getSetControls1(){
        return setControls1;
    }
    
    public TextField[] getSetControls2(){
        return setControls2;
    }
    
    public ArrayList<String> getControlsUsed(){
        return controlsUsed;
    }
}
