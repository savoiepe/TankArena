package TankArenaSourceFiles;

import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GamePane extends BorderPane{
    private VBox top = new VBox();
    private HBox roundsPane = new HBox();
    private HBox timerPane = new HBox();
    private HBox menuPane = new HBox();
    private HBox healthPane = new HBox(400);
    private HBox topLeft = new HBox();
    private HBox topRight = new HBox();
    private StackPane healthPane1 = new StackPane();
    private StackPane healthPane2 = new StackPane();
    private HBox rectanglePane1 = new HBox(5);
    private HBox rectanglePane2 = new HBox(5);
    private HBox center = new HBox(500);
    private HBox centerLeft = new HBox();
    private HBox centerRight = new HBox();
    private Pane playPane = new Pane();
    private Platform[] platformList = new Platform[22];
    private Image lava = new Image("finalprojectImages/lava.gif");
    private ImageView[] lavaImages = new ImageView[10];
    private Image ice = new Image("finalprojectImages/platforms_ice.png");
    private ImageView[] iceImages = new ImageView[22];
    public Image rock1Image = new Image("finalprojectImages/Rock_1.png");
    public Image rock2Image = new Image("finalprojectImages/Rock_2.png");
    public Image rock3Image = new Image("finalprojectImages/Rock_3.png");
    public ImageView rock1 = new ImageView(rock1Image);
    public ImageView rock2 = new ImageView(rock2Image);
    public ImageView rock3 = new ImageView(rock3Image);
    private TankPane tankOne = new TankPane();
    private TankPane tankTwo = new TankPane();
    final private int platformX = 100;
    final private int platformY = 30;
    private HealthBar healthBar1 = new HealthBar();
    private ImageView healthImage1 = healthBar1.getHealthImage();
    private ArrayList<Rectangle> healthCubes1 = new ArrayList<>();
    private HealthBar healthBar2 = new HealthBar();
    private ImageView healthImage2 = healthBar2.getHealthImage();
    private ArrayList<Rectangle> healthCubes2 = new ArrayList<>();
    private ImageView armor1;
    private ImageView armor2;
    private InventoryPane inventory1 = new InventoryPane();
    private InventoryPane inventory2 = new InventoryPane();
    private int scoreOne = 0;
    private int scoreTwo = 0;
    private ImageView map = new ImageView();
    final private Button menu = new Button("Menu");
    
    public GamePane(double screenWidth, double screenHeight){
    //panes
        top.setAlignment(Pos.CENTER);
        menuPane.setAlignment(Pos.CENTER_LEFT);
        menuPane.getChildren().add(menu);
        roundsPane.setAlignment(Pos.CENTER);
        timerPane.setAlignment(Pos.CENTER);
        healthPane.setAlignment(Pos.CENTER);
        top.getChildren().addAll(menuPane, roundsPane, timerPane, healthPane);
        rectanglePane1.setPadding(new Insets(25,10,10,35));
        rectanglePane2.setPadding(new Insets(25,10,10,35));
        topLeft.getChildren().add(healthPane1);
        topRight.getChildren().add(healthPane2);
        healthPane.getChildren().addAll(topLeft, topRight);
        center.setAlignment(Pos.CENTER);
        center.getChildren().addAll(centerLeft, centerRight);
    //healthbar & armor
        healthImage1.setFitHeight(60);
        healthImage1.setFitWidth(platformX);
        healthImage2.setFitHeight(60);
        healthImage2.setFitWidth(platformX);
        armor1 = new ImageView(new Image(("/finalprojectImages/Tank_Armor.png")));
        armor1.setFitHeight(platformY+10);
        armor1.setFitWidth(platformY+10);
        armor2 = new ImageView(new Image(("/finalprojectImages/Tank_Armor.png")));
        armor2.setFitHeight(platformY+10);
        armor2.setFitWidth(platformY+10);
        healthPane1.getChildren().add(healthImage1);
        //topLeft.getChildren().add(armor1);
        healthPane2.getChildren().add(healthImage2);
        //topRight.getChildren().add(armor2);
        for (int i=0; i<3; i++){
            Rectangle r1 = new Rectangle(12,12); r1.setFill(Color.RED); r1.setStroke(Color.BLACK);
            Rectangle r2 = new Rectangle(12,12); r2.setFill(Color.RED); r2.setStroke(Color.BLACK);
            healthCubes1.add(r1);
            healthCubes2.add(r2);
            rectanglePane1.getChildren().add(r1);
            rectanglePane2.getChildren().add(r2);
        }
        healthPane1.getChildren().add(rectanglePane1);
        healthPane2.getChildren().add(rectanglePane2);
    //inventory      
        centerLeft.getChildren().add(inventory1);
        centerRight.getChildren().add(inventory2);
    //platforms
        for (int i=0; i<platformList.length; i++){
            platformList[i] = new Platform(platformX, platformY);
            playPane.getChildren().add(platformList[i].getPlatformImage());
        }
        platformList[0].getPlatformImage().setX(100);
        platformList[0].getPlatformImage().setY(100);
        platformList[1].getPlatformImage().setX(100+7*platformX);
        platformList[1].getPlatformImage().setY(100);
        platformList[2].getPlatformImage().setX(100+platformX);
        platformList[2].getPlatformImage().setY(200);
        platformList[3].getPlatformImage().setX(100+2*platformX);
        platformList[3].getPlatformImage().setY(200);
        platformList[4].getPlatformImage().setX(100+5*platformX);
        platformList[4].getPlatformImage().setY(200);
        platformList[5].getPlatformImage().setX(100+6*platformX);
        platformList[5].getPlatformImage().setY(200);
        platformList[6].getPlatformImage().setX(100+3*platformX);
        platformList[6].getPlatformImage().setY(300);
        platformList[7].getPlatformImage().setX(100+4*platformX);
        platformList[7].getPlatformImage().setY(300);        
        platformList[8].getPlatformImage().setX(100-platformX);
        platformList[8].getPlatformImage().setY(300);
        platformList[9].getPlatformImage().setX(100);
        platformList[9].getPlatformImage().setY(300);
        platformList[10].getPlatformImage().setX(100+7*platformX);
        platformList[10].getPlatformImage().setY(300);
        platformList[11].getPlatformImage().setX(100+8*platformX);
        platformList[11].getPlatformImage().setY(300);        
        platformList[12].getPlatformImage().setX(100-platformX);
        platformList[12].getPlatformImage().setY(400);
        platformList[13].getPlatformImage().setX(100);
        platformList[13].getPlatformImage().setY(400);
        platformList[14].getPlatformImage().setX(100+platformX);
        platformList[14].getPlatformImage().setY(400);
        platformList[15].getPlatformImage().setX(100+2*platformX);
        platformList[15].getPlatformImage().setY(400);
        platformList[16].getPlatformImage().setX(100+3*platformX);
        platformList[16].getPlatformImage().setY(400);
        platformList[17].getPlatformImage().setX(100+4*platformX);
        platformList[17].getPlatformImage().setY(400);
        platformList[18].getPlatformImage().setX(100+5*platformX);
        platformList[18].getPlatformImage().setY(400);
        platformList[19].getPlatformImage().setX(100+6*platformX);
        platformList[19].getPlatformImage().setY(400);
        platformList[20].getPlatformImage().setX(100+7*platformX);
        platformList[20].getPlatformImage().setY(400);
        platformList[21].getPlatformImage().setX(100+8*platformX);
        platformList[21].getPlatformImage().setY(400);
        
    //lava platforms (hell map)
        for (int i=0; i<lavaImages.length; i++){
            lavaImages[i] = new ImageView(lava);
            lavaImages[i].setFitWidth(platformX);
            lavaImages[i].setFitHeight(platformY);
        }
        lavaImages[0].setX(100-platformX);
        lavaImages[0].setY(400);
        lavaImages[1].setX(100);
        lavaImages[1].setY(400);
        lavaImages[2].setX(100+platformX);
        lavaImages[2].setY(400);
        lavaImages[3].setX(100+platformX*2);
        lavaImages[3].setY(400);
        lavaImages[4].setX(100+platformX*3);
        lavaImages[4].setY(400);
        lavaImages[5].setX(100+platformX*4);
        lavaImages[5].setY(400);
        lavaImages[6].setX(100+platformX*5);
        lavaImages[6].setY(400);
        lavaImages[7].setX(100+platformX*6);
        lavaImages[7].setY(400);
        lavaImages[8].setX(100+platformX*7);
        lavaImages[8].setY(400);
        lavaImages[9].setX(100+platformX*8);
        lavaImages[9].setY(400);
        
    //ice platforms (winter map)
        for (int i=0; i<iceImages.length; i++){
            iceImages[i] = new ImageView(ice);
            iceImages[i].setFitWidth(platformX);
            iceImages[i].setFitHeight(platformX+20  );
        }
    //Mountain map rocks
        rock1.setX(350);
        rock1.setY(300);
        rock2.setX(250);
        rock2.setY(100);
        rock3.setX(800);
        rock3.setY(200);
    //dimensions
        playPane.setMinWidth(screenWidth);
        playPane.setMinHeight(screenHeight);
        playPane.setMaxWidth(screenWidth);
        playPane.setMaxHeight(screenHeight);
    }
    
    public Pane getPlayPane(){
        return playPane;
    }
    
    public HBox getRoundsPane(){
        return roundsPane;
    }
    
    public HBox getTimerPane(){
        return timerPane;
    }
    
    public Platform[] getPlatformList(){
        return platformList;
    }
    
    public ImageView[] getLavaImages(){
        return lavaImages;
    }
    
    public ImageView[] getIceImages(){
        return iceImages;
    }

    public TankPane getTankOne() {
        return tankOne;
    }

    public void setTankOne(TankPane tankOne) {
        this.tankOne = tankOne;
    }

    public TankPane getTankTwo() {
        return tankTwo;
    }

    public void setTankTwo(TankPane tankTwo) {
        this.tankTwo = tankTwo;
    }

    public ImageView getMap() {
        return map;
    }

    public void setMap(ImageView map) {
        this.map = map;
    }
    
    public Button getMenu(){
        return menu;
    }
    
    public void reset(){
        inventory1.setPowerUp(null);
        inventory2.setPowerUp(null);
        this.getChildren().clear();
        this.setTop(top);
        this.setCenter(center);
        this.setBottom(playPane);
    }
    
    public InventoryPane getInventoryOne(){
        return inventory1;
    }
    public InventoryPane getInventoryTwo(){
        return inventory2;
    }
    public void setHealth1(int health){
        rectanglePane1.getChildren().clear();
        for(int i = 0;i<health;i++){
            Rectangle r1 = new Rectangle(12,12); r1.setFill(Color.RED); r1.setStroke(Color.BLACK);
            rectanglePane1.getChildren().add(r1);
        }
    }
    public void setArmor1(boolean armor){
        if(armor)
            topLeft.getChildren().add(armor1);
        else
            topLeft.getChildren().remove(armor1);
    }
    public void setHealth2(int health){
        rectanglePane2.getChildren().clear();
        for(int i = 0;i<health;i++){
            Rectangle r2 = new Rectangle(12,12); r2.setFill(Color.RED); r2.setStroke(Color.BLACK);
            rectanglePane2.getChildren().add(r2);
        }
    }
    public void setArmor2(boolean armor){
        if(armor)
            topRight.getChildren().add(armor2);
        else
            topRight.getChildren().remove(armor2);
    }
    
    public int getScoreOne(){
        return scoreOne;
    }
    
    public int getScoreTwo(){
        return scoreTwo;
    }
    
    public void setScoreOne(int scoreOne){
        this.scoreOne = scoreOne;
    }
    
    public void setScoreTwo(int scoreTwo){
        this.scoreTwo = scoreTwo;
    }
}
