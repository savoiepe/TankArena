package finalproject;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Hitbox {
    Rectangle Hitbox;
    Hitbox(){
    }
    Hitbox(double x, double y, double height,double width,Pane pane){
        Hitbox = new Rectangle(x,y,width,height);
        Hitbox.setStroke(Color.BLACK);
        Hitbox.setFill(Color.TRANSPARENT);
        pane.getChildren().add(Hitbox);
    }
    Rectangle getHibox(){
        return Hitbox;
    }
    
}