package finalproject;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;

public class MainMenuPane extends Pane {
    private Button play = new Button("Play");
    private Button settings = new Button("Settings");
    private Button controls = new Button("Controls");
    
    public MainMenuPane(double screenWidth, double screenHeight){
        this.setMinWidth(screenWidth);
        this.setMinHeight(screenHeight);
        this.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image("finalprojectImages/Menu_Background.PNG")), CornerRadii.EMPTY, Insets.EMPTY)));       
        play.setFont(new Font("Cooper Black", 20));
        play.setTextFill(Color.DARKGRAY);
        play.setBackground(new Background(new BackgroundFill(Color.DARKRED, CornerRadii.EMPTY, Insets.EMPTY)));
        settings.setFont(new Font("Cooper Black", 20));
        settings.setTextFill(Color.DARKGRAY);
        settings.setBackground(new Background(new BackgroundFill(Color.DARKRED, CornerRadii.EMPTY, Insets.EMPTY)));
        controls.setFont(new Font("Cooper Black", 20));
        controls.setTextFill(Color.DARKGRAY);
        controls.setBackground(new Background(new BackgroundFill(Color.DARKRED, CornerRadii.EMPTY, Insets.EMPTY)));
        this.getChildren().addAll(play,settings,controls);
        play.setOnMouseEntered(e -> {
            play.setTextFill(Color.WHITE);
            play.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        });
        play.setOnMouseExited(e -> {
            play.setTextFill(Color.DARKGRAY);
            play.setBackground(new Background(new BackgroundFill(Color.DARKRED, CornerRadii.EMPTY, Insets.EMPTY)));
        });
        settings.setOnMouseEntered(e -> {
            settings.setTextFill(Color.WHITE);
            settings.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        });
        settings.setOnMouseExited(e -> {
            settings.setTextFill(Color.DARKGRAY);
            settings.setBackground(new Background(new BackgroundFill(Color.DARKRED, CornerRadii.EMPTY, Insets.EMPTY)));
        });
        controls.setOnMouseEntered(e -> {
            controls.setTextFill(Color.WHITE);
            controls.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        });
        controls.setOnMouseExited(e -> {
            controls.setTextFill(Color.DARKGRAY);
            controls.setBackground(new Background(new BackgroundFill(Color.DARKRED, CornerRadii.EMPTY, Insets.EMPTY)));
        });
    }
    
    public void setPlayDimensions(double sizeX, double sizeY){
        play.setPrefSize(sizeX, sizeY);
    }
    
    public void setPlayCoordinates(double X, double Y){
        play.setLayoutX(X);
        play.setLayoutY(Y);
    }
    
    public void setSettingsDimensions(double sizeX, double sizeY){
        settings.setPrefSize(sizeX, sizeY);
    }
    
    public void setSettingsCoordinates(double X, double Y){
        settings.setLayoutX(X);
        settings.setLayoutY(Y);
    }
    
    public void setControlsDimensions(double sizeX, double sizeY){
        controls.setPrefSize(sizeX, sizeY);
    }
    
    public void setControlsCoordinates(double X, double Y){
        controls.setLayoutX(X);
        controls.setLayoutY(Y);
    }
    
    public Button getPlay(){
        return play;
    }
    
    public Button getSettings(){
        return settings;
    }
    
    public Button getControls(){
        return controls;
    }
}
