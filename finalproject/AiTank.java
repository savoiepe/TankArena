package finalproject;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
public class AiTank extends TankPane {
    Timeline aiAni;
    double tankOneX,tankOneY,aiX,aiY,distanceX,distanceY;
    AiTank(){
    }
    AiTank(String tankColor, int playerNumber){
        super(tankColor, playerNumber);
        aiAnimation();
    }
    public void aiAnimation(){
        aiAni = new Timeline(new KeyFrame(Duration.millis(50),f->{
            tankOneX = FinalProject.game.getTankOne().getTankImage().getX();
            tankOneY = FinalProject.game.getTankOne().getTankImage().getY();
            aiX = getTankImage().getX();
            aiY = getTankImage().getY();
            distanceX = Math.abs(aiX - tankOneX);
            distanceY = aiY - tankOneY;
        //tank will move left or right
            if(powerUp != null){
                if(powerUp.getName().equals("armor")){
                    if(!armor)
                        useInventory();
                }
                else if(powerUp.getName().equals("health")){
                    if(health != 3)
                        useInventory(); 
                }
                else{
                    useInventory();
                }
            }
            if(distanceY > 80 && FinalProject.gameRunning){
                moveUp(true);
            }else{
                moveUp(false);
            }
            
            if(tankOneX < aiX && FinalProject.gameRunning){

                if(FinalProject.settings.getMap().equals(FinalProject.settings.getMapList()[4])){
                    moveLeft(true);
                    if(FinalProject.game.getTankTwo().movingRight){
                        moveRight(false);
                    }
                }
                else if(distanceX > 150){
                    moveLeft(true);
                    moveRight(false);
                }
                else{
                    int temp = (int)(Math.random()*3);
                    switch(temp){
                        case 0 : moveLeft(true);moveRight(false);break;
                        case 1 : moveLeft(false);moveRight(true);break;
                        case 2 : moveUp(true); moveUp(false);break;
                    }
                    if(FinalProject.game.getTankOne().tankImage.getImage().equals(FinalProject.game.getTankOne().leftSide) || FinalProject.game.getTankOne().tankImage.getImage().equals(FinalProject.game.getTankOne().leftSideUpgraded)){
                        setVelocity(2);
                        fireBullet();
                    }
                }
                if(distanceX < 200 && distanceX > 150){
                    shootLeft(2);
                }
                else if(distanceX < 400 && distanceX > 350){
                    shootLeft(3);
                }
                else if(distanceX < 550 && distanceX > 500){
                    shootLeft(4);
                }
                else if(distanceX < 850 && distanceX > 800){
                    shootLeft(5);
                }

            } 
            else if(tankOneX > aiX && FinalProject.gameRunning){
                if(FinalProject.settings.getMap().equals(FinalProject.settings.getMapList()[4])){
                    moveRight(true);
                    if(FinalProject.game.getTankTwo().movingLeft){
                        moveLeft(false);
                    }
                }
                else if(distanceX > 150){
                    moveLeft(false);
                    moveRight(true);
                }
                else{
                    int temp = (int)(Math.random()*3);
                    switch(temp){
                        case 0 : moveLeft(true);moveRight(false);break;
                        case 1 : moveLeft(false);moveRight(true);break;
                        case 2 : moveUp(true); moveUp(false);break;
                    }
                    if(FinalProject.game.getTankOne().tankImage.getImage().equals(FinalProject.game.getTankOne().rightSide) || FinalProject.game.getTankOne().tankImage.getImage().equals(FinalProject.game.getTankOne().rightSideUpgraded)){
                        setVelocity(2);
                        fireBullet();
                    }
                }
                if(distanceX < 200 && distanceX > 150){
                    shootRight(2);
                }
                else if(distanceX < 400 && distanceX > 350){
                    shootRight(3);
                }
                else if(distanceX < 550 && distanceX > 500){
                    shootRight(4);
                }
                else if(distanceX < 850 && distanceX > 800){
                    shootRight(5);
                }
            }
        //avoids falling in lava
            if (FinalProject.settings.getMap().equals(FinalProject.settings.getMapList()[1]) && FinalProject.gameRunning && floor==2){
                moveUp(true);
            }
        
        }));
        aiAni.setCycleCount(Animation.INDEFINITE);
        aiAni.play();
    }
    public void shootRight(int velocity){
        if(!FinalProject.settings.getMap().equals(FinalProject.settings.getMapList()[4])){
            if(FinalProject.game.getTankOne().tankImage.getImage().equals(FinalProject.game.getTankOne().leftSide) || FinalProject.game.getTankOne().tankImage.getImage().equals(FinalProject.game.getTankOne().leftSideUpgraded)){
                moveRight(true);
            }
            moveLeft(false);
            moveRight(false);
        }
        if(FinalProject.settings.getMap().equals(FinalProject.settings.getMapList()[4])){
            setVelocity(velocity - 1);
        }
        else setVelocity(velocity);
        fireBullet();
    }
    public void shootLeft(int velocity){
        if(!FinalProject.settings.getMap().equals(FinalProject.settings.getMapList()[4])){
            if(FinalProject.game.getTankOne().tankImage.getImage().equals(FinalProject.game.getTankOne().rightSide) || FinalProject.game.getTankOne().tankImage.getImage().equals(FinalProject.game.getTankOne().rightSideUpgraded)){
                moveLeft(true);
            }
            moveLeft(false);
            moveRight(false);
        }
        if(FinalProject.settings.getMap().equals(FinalProject.settings.getMapList()[4])){
            setVelocity(velocity - 1);
        }
        else setVelocity(velocity);
        fireBullet();
    }
    
    @Override
    public String getName(){
        return "AITank";
    }
    
    @Override
    public Timeline getAni(){
        return aiAni;
    }
}