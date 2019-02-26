package finalproject;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Fire extends Pane {
    Image fire1 = new Image("finalprojectImages/Fire_Animation1.png");
    Image fire2 = new Image("finalprojectImages/Fire_Animation2.png");
    Image fire3 = new Image("finalprojectImages/Fire_Animation3.png");
    Image fire4 = new Image("finalprojectImages/Fire_Animation4.png");
    Image fire5 = new Image("finalprojectImages/Fire_Animation5.png");
    Image fire6 = new Image("finalprojectImages/Fire_Animation6.png");
    ImageView fireImage = new ImageView(fire1);
    int imageNumber = 1;
    double x,y;
    Rectangle hitbox;
    Timeline fireAni;
    
    Fire(double x, double y){
        this.x = x;
        this.y = y;
        fireImage.setX(x - fire1.getWidth()/2);
        fireImage.setY(y - fire1.getHeight());
        hitbox = new Rectangle(fireImage.getX()-2,fireImage.getY()-2,fire1.getWidth()+4,fire1.getHeight()+4);
        this.getChildren().addAll(fireImage);
        fireAnimation();        
    }
    
    void fireAnimation(){
        fireAni = new Timeline(new KeyFrame(Duration.millis(100),f->{
            switch(imageNumber){
                case 1: fireImage.setImage(fire2);imageNumber = 2;break;
                case 2: fireImage.setImage(fire3);imageNumber = 3;break;
                case 3: fireImage.setImage(fire4);imageNumber = 4;break;
                case 4: fireImage.setImage(fire5);imageNumber = 5;break;
                case 5: fireImage.setImage(fire1);imageNumber = 6;break;
                case 6: fireImage.setImage(fire1);imageNumber = 1;break;
            }
            
            if(hitbox.intersects(FinalProject.game.getTankOne().getHitbox().getBoundsInParent())){
                fireAni.stop();
                fireImage.setImage(null);
                if(FinalProject.game.getTankOne().tankImage.getImage().equals(FinalProject.game.getTankOne().rightSide) || FinalProject.game.getTankOne().tankImage.getImage().equals(FinalProject.game.getTankOne().rightSideUpgraded) || (FinalProject.game.getTankOne().isSlippingRight() && !FinalProject.game.getTankOne().isSlippingLeft()))
                    FinalProject.game.getTankOne().takeDamage(new PowerUp(5),"Left");
                else if(FinalProject.game.getTankOne().tankImage.getImage().equals(FinalProject.game.getTankOne().leftSide) || FinalProject.game.getTankOne().tankImage.getImage().equals(FinalProject.game.getTankOne().leftSideUpgraded) || (FinalProject.game.getTankOne().isSlippingLeft() && !FinalProject.game.getTankOne().isSlippingRight()))
                    FinalProject.game.getTankOne().takeDamage(new PowerUp(5),"Right");
            }
            else if (hitbox.intersects(FinalProject.game.getTankTwo().getHitbox().getBoundsInParent())){
                fireAni.stop();
                fireImage.setImage(null);
                if(FinalProject.game.getTankTwo().tankImage.getImage().equals(FinalProject.game.getTankTwo().rightSide) || FinalProject.game.getTankTwo().tankImage.getImage().equals(FinalProject.game.getTankTwo().rightSideUpgraded) || (FinalProject.game.getTankTwo().isSlippingRight() && !FinalProject.game.getTankTwo().isSlippingLeft()))
                    FinalProject.game.getTankTwo().takeDamage(new PowerUp(5),"Left");
                else if(FinalProject.game.getTankTwo().tankImage.getImage().equals(FinalProject.game.getTankTwo().leftSide) || FinalProject.game.getTankTwo().tankImage.getImage().equals(FinalProject.game.getTankTwo().leftSideUpgraded) || (FinalProject.game.getTankTwo().isSlippingLeft() && !FinalProject.game.getTankTwo().isSlippingRight()))
                    FinalProject.game.getTankTwo().takeDamage(new PowerUp(5),"Right");
            }
            else if (!FinalProject.tankOneAlive || !FinalProject.tankTwoAlive || !FinalProject.gameRunning){
                fireAni.stop();
            }
        }));
        fireAni.setCycleCount(Animation.INDEFINITE);
        fireAni.play();
    }
}