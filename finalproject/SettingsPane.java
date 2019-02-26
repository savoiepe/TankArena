package finalproject;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class SettingsPane extends BorderPane {
    final private VBox top = new VBox(10);
    final private HBox backPane = new HBox(10);
    final private HBox colorPane = new HBox(50);
    final private HBox color1Pane = new HBox(10);
    final private VBox colorBox1 = new VBox(5);
    final private HBox color2Pane = new HBox(10);
    final private VBox colorBox2 = new VBox(5);
    final private VBox opponentBox = new VBox(5);
    final private HBox center = new HBox(50);
    final private HBox timerPane = new HBox(10);
    final private VBox timerBox = new VBox(5);
    final private HBox roundsPane = new HBox(10);
    final private VBox roundsBox = new VBox(5);
    final private HBox bottom = new HBox(10);
    final private Circle fill = new Circle(20, Color.TRANSPARENT);
    final private Color[] colorList = {Color.GREEN, Color.BLUE, Color.RED, Color.YELLOW, Color.GRAY};
    final private String[] colorStringList = {"Green", "Blue", "Red", "Yellow", "Gray"};
    private Color colorOne = colorList[0];
    private String colorStringOne = colorStringList[0];
    final private Rectangle colorDisplayedOne = new Rectangle(40, 40, colorOne);
    private Color colorTwo = colorList[1];
    private String colorStringTwo = colorStringList[1];
    final private Rectangle colorDisplayedTwo = new Rectangle(40, 40, colorTwo);
    private final Label displayOpponent = new Label("Play Against Computer");
    private Circle chooseOpponent = new Circle(20,Color.RED);
    final private String[] timerList = {"No limit", "0:30", "1:00", "1:30", "2:00", "2:30", "3:00"};
    private String gameTimer = timerList[0];
    private final Label timerDisplayed = new Label(gameTimer);
    final private String[] roundsList = {"One", "Two", "Three", "Four", "Five"};
    private String numberOfRounds = roundsList[0];
    private String roundsRemaining = numberOfRounds;
    private final Label roundsDisplayed = new Label(numberOfRounds);
    final private Image[] mapList = {new Image("finalprojectMaps/DefaultMap.PNG"), new Image("finalprojectMaps/HellMap.PNG"), new Image("finalprojectMaps/MoonMap.PNG"), new Image("finalprojectMaps/MountainsMap.PNG"), new Image("finalprojectMaps/WinterMap.PNG")};
    private Image map = mapList[0];
    private ImageView displayMap = new ImageView(map);
    final private Arrow leftC1 = new Arrow("<", 50, 50, new Font("Cooper Black", 20));
    final private Arrow rightC1 = new Arrow(">", 50, 50, new Font("Cooper Black", 20));
    final private Arrow leftC2 = new Arrow("<", 50, 50, new Font("Cooper Black", 20));
    final private Arrow rightC2 = new Arrow(">", 50, 50, new Font("Cooper Black", 20));
    final private Arrow leftTimer = new Arrow("<", 50, 50, new Font("Cooper Black", 20));
    final private Arrow rightTimer = new Arrow(">", 50, 50, new Font("Cooper Black", 20));
    final private Arrow leftRounds = new Arrow("<", 50, 50, new Font("Cooper Black", 20));
    final private Arrow rightRounds = new Arrow(">", 50, 50, new Font("Cooper Black", 20));
    final private Arrow leftMap = new Arrow("<", 50, 50, new Font("Cooper Black", 20));
    final private Arrow rightMap = new Arrow(">", 50, 50, new Font("Cooper Black", 20));
    
    public SettingsPane(double screenWidth, double screenHeight){
        this.setTop(top);
        top.getChildren().addAll(fill, backPane, colorPane);
        backPane.setAlignment(Pos.CENTER_LEFT);
        colorPane.getChildren().addAll(color1Pane, color2Pane, opponentBox);
        colorPane.setAlignment(Pos.CENTER);
        color1Pane.setAlignment(Pos.TOP_CENTER);
        color1Pane.getChildren().addAll(leftC1, colorBox1, rightC1);
        Label player1Label = new Label("Player 1");
        player1Label.setFont(new Font("Cooper Black", 15));
        colorBox1.getChildren().addAll(player1Label, colorDisplayedOne);
        colorBox1.setAlignment(Pos.CENTER);
        colorDisplayedOne.setStroke(Color.BLACK);
        color2Pane.setAlignment(Pos.TOP_CENTER);
        color2Pane.getChildren().addAll(leftC2, colorBox2, rightC2);
        Label player2Label = new Label("Player 2");
        player2Label.setFont(new Font("Cooper Black", 15));
        colorBox2.getChildren().addAll(player2Label, colorDisplayedTwo);
        colorBox2.setAlignment(Pos.CENTER);
        colorDisplayedTwo.setStroke(Color.BLACK);
        displayOpponent.setFont(new Font("Cooper Black", 15));
        opponentBox.getChildren().addAll(displayOpponent, chooseOpponent);
        this.setCenter(center);
        center.getChildren().addAll(timerPane, roundsPane);
        center.setAlignment(Pos.CENTER);
        timerPane.setAlignment(Pos.CENTER);
        timerPane.getChildren().addAll(leftTimer, timerBox, rightTimer);
        Label gameTimerLabel = new Label("Game Timer");
        gameTimerLabel.setFont(new Font("Cooper Black", 15));
        timerDisplayed.setFont((new Font("Cooper Black", 15)));
        timerBox.getChildren().addAll(gameTimerLabel, timerDisplayed);
        timerBox.setAlignment(Pos.CENTER);
        roundsPane.setAlignment(Pos.CENTER);
        roundsPane.getChildren().addAll(leftRounds, roundsBox, rightRounds);
        Label numberOfRoundsLabel = new Label("Number Of Rounds");
        numberOfRoundsLabel.setFont(new Font("Cooper Black", 15));
        roundsDisplayed.setFont((new Font("Cooper Black", 15)));
        roundsBox.getChildren().addAll(numberOfRoundsLabel, roundsDisplayed);
        roundsBox.setAlignment(Pos.CENTER);
        this.setBottom(bottom);
        bottom.setAlignment(Pos.CENTER);
        displayMap.setFitWidth(500);
        displayMap.setFitHeight(300);
        bottom.getChildren().addAll(leftMap, displayMap, rightMap);
        this.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image("finalprojectImages/Settings_Background.jpg")), CornerRadii.EMPTY, Insets.EMPTY)));
        
        chooseOpponent.setOnMouseClicked(e -> {
            if (chooseOpponent.getFill().equals(Color.GREEN)){
                chooseOpponent.setFill(Color.RED);
            }
            else {
                chooseOpponent.setFill(Color.GREEN);
            }
        });
    //dimensions
        this.setMinWidth(screenWidth);
        this.setMinHeight(screenHeight);
        this.setMaxWidth(screenWidth);
        this.setMaxHeight(screenHeight);
    }
    
    public Color[] getColorList(){
        return colorList;
    }
    
    public String[] getColorStringList(){
        return colorStringList;
    }
    
    public Color getColorOne(){
        return colorOne;
    }
    
    public void setColorOne(Color color){
        this.colorOne = color;
    }
    
    public Color getColorTwo(){
        return colorTwo;
    }
    
    public void setColorTwo(Color color){
        this.colorTwo = color;
    }
    
    public String getColorStringOne(){
        return colorStringOne;
    }
    
    public void setColorStringOne(String colorStringOne){
        this.colorStringOne = colorStringOne;
    }
    
    public String getColorStringTwo(){
        return colorStringTwo;
    }
    
    public void setColorStringTwo(String colorStringTwo){
        this.colorStringTwo = colorStringTwo;
    }
    
    public Rectangle getColorDisplayedOne(){
        return colorDisplayedOne;
    }
    
    public Rectangle getColorDisplayedTwo(){
        return colorDisplayedTwo;
    }
    
    public Arrow getLeftC1(){
        return leftC1;
    }
    
    public Arrow getRightC1(){
        return rightC1;
    }
    
    public Arrow getLeftC2(){
        return leftC2;
    }
    
    public Arrow getRightC2(){
        return rightC2;
    }
    
    public Circle getOpponent(){
        return chooseOpponent;
    }
    
    public String[] getTimerList(){
        return timerList;
    }
    
    public String getTimer(){
        return gameTimer;
    }
    
    public void setTimer(String gameTimer){
        this.gameTimer = gameTimer;
    }
    
    public Label getTimerDisplayed(){
        return timerDisplayed;
    }
    
    public Arrow getLeftTimer(){
        return leftTimer;
    }
    
    public Arrow getRightTimer(){
        return rightTimer;
    }
    
    public String[] getRoundsList(){
        return roundsList;
    }
    
    public String getNumberOfRounds(){
        return numberOfRounds;
    }
    
    public void setNumberOfRounds( String numberOfRounds){
        this.numberOfRounds = numberOfRounds;
    }
    
    public String getRoundsRemaining(){
        return roundsRemaining;
    }
    
    public void setRoundsRemaining(String roundsRemaining){
        this.roundsRemaining = roundsRemaining;
    }
    
    public Label getRoundsDisplayed(){
        return roundsDisplayed;
    }
    
    public Arrow getLeftRounds(){
        return leftRounds;
    }
    
    public Arrow getRightRounds(){
        return rightRounds;
    }
    
    public Image[] getMapList(){
        return mapList;
    }
    
    public Image getMap(){
        return map;
    }
    
    public void setMap(Image map){
        this.map = map;
    }
    
    public void setDisplayMap(ImageView displayMap){
        this.displayMap = displayMap;
    }
    
    public Arrow getLeftMap(){
        return leftMap;
    }
    
    public Arrow getRightMap(){
        return rightMap;
    }
    
    public void refreshMap(){
        displayMap.setFitWidth(500);
        displayMap.setFitHeight(300);
        bottom.getChildren().clear();
        bottom.getChildren().addAll(leftMap, displayMap, rightMap);
    }
}
