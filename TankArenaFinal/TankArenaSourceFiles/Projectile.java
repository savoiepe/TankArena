package TankArenaSourceFiles;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Projectile extends Pane {
    double x,y,velocity,time,previousY,gravity;
    int deathCounter,playerNumber;
    boolean goingDownward;
    Circle hitbox;
    String side;
    ImageView projectileImage;
    PowerUp powerUp;
    Timeline ani;
    Projectile(double x,double y, double velocity,PowerUp powerUp,String side,int playerNumber){
        this.playerNumber = playerNumber;
        this.powerUp = powerUp;
        this.side = side;
        if(side.equals("Right")){
            this.x = x + 100;
            this.y = y + 14;
        }
        else{
            this.x = x - 20;
            this.y = y + 14; 
        }
        time = 0;
        this.velocity = velocity;
        draw();
    }
    void draw(){
        if(FinalProject.settings.getMap().equals(FinalProject.settings.getMapList()[2]))
            gravity = 5;
        else gravity = 9.81;
        
        
        if(powerUp != null)
            this.projectileImage = powerUp.getProjectileImage();
        else    
            this.projectileImage = new ImageView(new Image("finalprojectImages/Projectile_normal.png"));
        if(powerUp != null && powerUp.getName().equals("fire")){
            projectileImage.setScaleX(2);
            projectileImage.setScaleY(2);
        }
        projectileImage.setX(x);
        projectileImage.setY(y);
        projectileImage.setFitWidth(20);
        projectileImage.setFitHeight(20);
        hitbox = new Circle(projectileImage.getX() + projectileImage.getFitWidth()/2,projectileImage.getY() + projectileImage.getFitHeight()/2,projectileImage.getFitWidth()/2-3);
        this.getChildren().add(projectileImage);
        hitbox.centerXProperty().bind(projectileImage.xProperty().add(projectileImage.getFitWidth()/2));
        hitbox.centerYProperty().bind(projectileImage.yProperty().add(projectileImage.getFitHeight()/2));
        startAnimation();
    }
    void startAnimation(){
        previousY = projectileImage.getY()+1;
        goingDownward = false;
        ani = new Timeline(new KeyFrame(Duration.millis(5),f->{
            if(side.equals("Right"))
                projectileImage.setX(x + velocity * time * Math.sin(20));
            else
                projectileImage.setX(x - velocity * time * Math.sin(20));
            projectileImage.setY(y - velocity * time * Math.cos(20) + gravity * 0.0012 * time*time);
            incrementTime();
            
            
            if(hitbox.intersects(FinalProject.game.getTankOne().getHitbox().getBoundsInParent()) && playerNumber!=1){
                ani.stop();
                deathAnimation();
                FinalProject.game.getTankOne().takeDamage(powerUp,side);
            }
            else if (hitbox.intersects(FinalProject.game.getTankTwo().getHitbox().getBoundsInParent()) && playerNumber!=2){
                ani.stop();
                deathAnimation();
                FinalProject.game.getTankTwo().takeDamage(powerUp,side);
            }
            if(FinalProject.settings.getMap().equals(FinalProject.settings.getMapList()[3])){
                if(hitbox.intersects(FinalProject.hitboxRock1.getBoundsInParent()) || hitbox.intersects(FinalProject.hitboxRock2.getBoundsInParent()) || hitbox.intersects(FinalProject.hitboxRock3.getBoundsInParent())){
                    ani.stop();
                    deathAnimation();
                }
            }
            if(goingDownward){
                if(powerUp != null && powerUp.getName().equals("bounce") && this.projectileImage.getY() > 380 && velocity > 1.2){
                    FinalProject.game.getTankTwo().projectileBounce(side,projectileImage.getX(),projectileImage.getY(),velocity);
                    ani.stop();
                    FinalProject.game.getTankOne().getChildren().remove(this);
                    FinalProject.game.getTankTwo().getChildren().remove(this);
                }
            }
            
            if(this.projectileImage.getX()> 990 || this.projectileImage.getX() < 0 || this.projectileImage.getY()> 395){
                ani.stop();
                deathAnimation();
                if(powerUp != null && powerUp.getName().equals("fire")){
                    FinalProject.game.getTankOne().addFire(projectileImage.getX() + projectileImage.getFitWidth()/2,projectileImage.getY() + projectileImage.getFitHeight()/2);
                }
            }
            if(previousY < projectileImage.getY())
                goingDownward = true;
            else previousY = projectileImage.getY();
                
        }));
        ani.setCycleCount(Animation.INDEFINITE);
        ani.play();
    }
    void incrementTime(){
        time++;
    }
    void deathAnimation(){
        Timeline deathAni;
            deathCounter = 1;
            deathAni = new Timeline(new KeyFrame(Duration.millis(50),f->{
            if(deathCounter < 8){
                try{
                    projectileImage.setImage(new Image("finalprojectImages/Tank_Explosion"+deathCounter+".png"));
                }catch(Exception ex){
                }
                projectileImage.setScaleX(3);
                projectileImage.setScaleY(2);
                deathCounter++;
            }
            else{
                FinalProject.game.getTankOne().getChildren().remove(this);
                FinalProject.game.getTankTwo().getChildren().remove(this);
            }
        }));
        deathAni.setCycleCount(8);
        deathAni.play();
        deathAni.setOnFinished(e->{deathCounter = 0;});
    }
}