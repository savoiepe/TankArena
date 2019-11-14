package TankArenaSourceFiles;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class TankPane extends Pane {
    public int playerNumber,health,floor,deathCounter,slipperyFactor;
    public Timeline ani,jumpAni,fallAni,deathAni,slipAni;
    public double velocity,time,previousY,gravity;
    public Image leftSide,rightSide,leftSideUpgraded,rightSideUpgraded,leftSideDamaged,rightSideDamaged,leftSideDamagedUpgraded,rightSideDamagedUpgraded,frozenRight,frozenLeft;
    public ImageView tankImage;
    public PowerUp powerUp;
    public Rectangle hitbox;
    public boolean movingLeft,movingRight,movingUp,airborn,armor,increasingVelocity,readyToFire,goingDownward,usingInventory,damaged,frozen,slippingLeft,slippingRight;

    public TankPane(){
    }
    
    public TankPane(String tankColor, int playerNumber){ //creates the tank object with specified image and player number
        leftSide = new Image("finalprojectImages/Tank_"+tankColor+"_Left.png");
        rightSide = new Image("finalprojectImages/Tank_"+tankColor+"_Right.png");
        leftSideUpgraded = new Image("finalprojectImages/Tank_"+tankColor+"_Left_Upgraded.png");
        rightSideUpgraded = new Image("finalprojectImages/Tank_"+tankColor+"_Right_Upgraded.png");
        rightSideDamaged = new Image("finalprojectImages/Tank_Damage_Right.png");
        leftSideDamaged = new Image("finalprojectImages/Tank_Damage_Left.png");
        rightSideDamagedUpgraded = new Image("finalprojectImages/Tank_Damage_Right_Upgraded.png");
        leftSideDamagedUpgraded = new Image("finalprojectImages/Tank_Damage_Left_Upgraded.png");
        frozenRight = new Image("finalprojectImages/Tank_Frozen_Right.png");
        frozenLeft = new Image("finalprojectImages/Tank_Frozen_Left.png");;
        this.playerNumber = playerNumber;
        if (FinalProject.settings.getMap().equals(FinalProject.settings.getMapList()[1])){
            floor = 2;
        }else floor = 1;
        ani = new Timeline();
        jumpAni = new Timeline();
        fallAni = new Timeline();
        slipAni = new Timeline();
        draw(); //calls the draw function to draw the tank in the game at it's starting position
    }
    
    public ImageView getTankImage(){
        return tankImage;
    }
    
    public void fireBullet(){
        if(readyToFire && !isBehindRock()){
            readyToFire = false;
            if(movingLeft ^ movingRight) //bullet will have more velocity if the tank is also moving
                velocity +=1;
            increasingVelocity = false; //stop the part of the animation which periodicaly increases the velocity
            velocity *= 0.1*9.81; //we use 9.81 instead of gravity since the x-component of the velocity of the bullet is never affected
            Projectile projectile; //Creates new projectile
            if(tankImage.getImage().equals(rightSide) || tankImage.getImage().equals(rightSideUpgraded)){
                if(usingInventory){
                    projectile = new Projectile(tankImage.getX()-10,tankImage.getY(),velocity,powerUp,"Right",playerNumber);
                    powerUp = null;
                    usingInventory = false;
                    
                    if(playerNumber == 1)
                        FinalProject.game.getInventoryOne().setPowerUp(powerUp);
                    else
                        FinalProject.game.getInventoryTwo().setPowerUp(powerUp);
                }
                else
                    projectile = new Projectile(tankImage.getX()-10,tankImage.getY(),velocity,null,"Right",playerNumber);
            }
            else{
                if(usingInventory){
                    projectile = new Projectile(tankImage.getX()-10,tankImage.getY(),velocity,powerUp,"Left",playerNumber);
                    powerUp = null;
                    
                    if(playerNumber == 1)
                        FinalProject.game.getInventoryOne().setPowerUp(powerUp);
                    else
                        FinalProject.game.getInventoryTwo().setPowerUp(powerUp);
                }
                else
                    projectile = new Projectile(tankImage.getX(),tankImage.getY(),velocity,null,"Left",playerNumber);
            }
            this.getChildren().add(projectile);
            PauseTransition pause = new PauseTransition();    
            pause.setDuration(Duration.seconds(0.8));
            pause.play();
            pause.setOnFinished(e->{setReadyToFire(true);});
        }
        velocity = 1.5; //resets the velocity to 1.5
    }
    
    public void moveLeft(boolean isMoving){
        movingLeft = isMoving;
        if (FinalProject.settings.getMap().equals(FinalProject.settings.getMapList()[4])){
            if (isMoving==false){
            //start slipping on key released
                declineVelocity("Left");
            }else if(movingLeft && movingRight){
            //stop slipping on both keys pressed
                declineVelocity("Right");
            }
        }
    }
    public void moveRight(boolean isMoving){
        movingRight = isMoving;
        if (FinalProject.settings.getMap().equals(FinalProject.settings.getMapList()[4])){
            if (isMoving==false){
            //start slipping on key released
                declineVelocity("Right");
            }else if(movingRight && movingLeft){
            //start slipping on both keys pressed
                declineVelocity("Left");
            }
        }
    }
    public void moveUp(boolean isMoving){
        movingUp = isMoving;
    }
    
    public void increaseVelocity(boolean isIncreasing){
        increasingVelocity = isIncreasing;
    }
    public double getVelocity(){
        return velocity;
    }
    
    public void useInventory(){
        if(powerUp == null){
        }
        else if(powerUp.getName().equals("armor")){
            if(!armor){
                armor = true;
                if(playerNumber == 1){
                    FinalProject.game.setArmor1(armor);
                    FinalProject.game.getInventoryOne().setPowerUp(null);
                }
                else{
                    FinalProject.game.setArmor2(armor);
                    FinalProject.game.getInventoryTwo().setPowerUp(null);
                }
            }
            powerUp = null;
            
        }      
        else if(powerUp.getName().equals("health")){
            if(health != 3)
                health += 1;
            powerUp = null;
            
            if(playerNumber == 1){
                FinalProject.game.setHealth1(health);
                FinalProject.game.getInventoryOne().setPowerUp(null);
            }
            else{
                FinalProject.game.setHealth2(health);
                FinalProject.game.getInventoryTwo().setPowerUp(null);
            }
        }
        else if(powerUp.getName().equals("random")){
            powerUp = new PowerUp((int)(Math.random()*7));
            
            if(playerNumber == 1)
                FinalProject.game.getInventoryOne().setPowerUp(powerUp);
            else
                FinalProject.game.getInventoryTwo().setPowerUp(powerUp);
        }
        else{
            usingInventory = true;
        }
    }
    
    public void setAirborn(boolean isAirborn){
        airborn = isAirborn;
    }
    public void setReadyToFire(boolean isReady){
        readyToFire = isReady;
    }
    
    public void projectileBounce(String side,double x,double y,double velocity){
        if(velocity > 1.2){
            Projectile projectile;
        
            if(side.equals("Right")){
                projectile = new Projectile(x-100,y-14,velocity-0.8,new PowerUp(0),side,playerNumber);
            }
            else{
                projectile = new Projectile(x+20,y-14,velocity-0.8,new PowerUp(0),side,playerNumber);
            }
            this.getChildren().add(projectile);
        }
    }
    
    public void takeDamage(PowerUp powerUp, String side){ //function that the projectile will call if it collides with the tank
        damaged = true;
        PauseTransition pause = new PauseTransition();
        pause.setDuration(Duration.seconds(0.25));
        pause.play();
        pause.setOnFinished(e->{
            damaged = false;
        });
                
        if(armor == true){
            armor = false;
            if(playerNumber == 1)
                FinalProject.game.setArmor1(armor);
            else
                FinalProject.game.setArmor2(armor);            
        }
        else{
            if(powerUp != null && powerUp.getName().equals("damage"))
                health -=2;
            else
                health -= 1;
        }
        if(powerUp != null && powerUp.getName().equals("knockback")){
            airborn = true;
            jumpingAnimation(true,side);
        }else if(powerUp != null && powerUp.getName().equals("ice")){
            if(side.equals("Right")) 
                tankImage.setImage(frozenRight);
            else
                tankImage.setImage(frozenLeft);
            frozen = true;
            PauseTransition freeze = new PauseTransition();    
            pause.setDuration(Duration.millis(2000));
            pause.play();
            pause.setOnFinished(e->{
            frozen = false;
            damaged = false;
        });
        }
        
        if (playerNumber == 1)
            FinalProject.game.setHealth1(health);
        else
            FinalProject.game.setHealth2(health);
        
        if (health<=0){
            tankDefeated();
        }
    }
    
    public void tankDefeated(){
        if (this.equals(FinalProject.game.getTankOne()) && FinalProject.tankTwoAlive && FinalProject.tankOneAlive || 
        this.equals(FinalProject.game.getTankTwo()) && FinalProject.tankOneAlive && FinalProject.tankTwoAlive){
            if (this.equals(FinalProject.game.getTankOne()))
                FinalProject.tankOneAlive = false;
            else FinalProject.tankTwoAlive = false;
            ani.stop();
            deathCounter = 1;
            deathAni = new Timeline(new KeyFrame(Duration.millis(100),f->{
                if(deathCounter < 8){
                    tankImage.setImage(new Image("finalprojectImages/Tank_Explosion"+deathCounter+".png"));
                    tankImage.setScaleX(deathCounter+1);
                    tankImage.setScaleY(deathCounter);
                    deathCounter++;
                }
                else
                    tankImage.setImage(null);
            }));
            deathAni.setCycleCount(8);
            deathAni.play();

            if (this.equals(FinalProject.game.getTankOne())){
                FinalProject.game.setScoreTwo(FinalProject.game.getScoreTwo()+1);
            }else{
                FinalProject.game.setScoreOne(FinalProject.game.getScoreOne()+1);
            }        
        //game over
            if (FinalProject.settings.getRoundsRemaining().equals(FinalProject.settings.getRoundsList()[0])){
                deathAni.setOnFinished(e->{FinalProject.playGameOver();});
        //game continues (rounds remaining)
            }else{
                deathAni.setOnFinished(e ->{
                    FinalProject.settings.setRoundsRemaining(FinalProject.settings.getRoundsList()[new ArrayList<>(Arrays.asList(FinalProject.settings.getRoundsList())).indexOf(FinalProject.settings.getRoundsRemaining())-1]);
                    FinalProject.displayRounds.setText("Rounds Remaining: " + FinalProject.settings.getRoundsRemaining() + "     Score: " + FinalProject.game.getScoreOne() + " - " + FinalProject.game.getScoreTwo());
                    FinalProject.playAgain();
                });
            }
        }
    }
    
    public void draw(){ //draws the tank on the map at the correct spawning location
        try{
            ani.stop();
        }catch(Exception ex){
        }
        if(playerNumber == 1){
            tankImage = new ImageView(rightSide); //player 1 starts on the left side and will face towards the other player on the right
        }
        else{
            tankImage = new ImageView(leftSide);
        }
        this.tankImage.setFitWidth(65);
        this.tankImage.setFitHeight(65);
        this.getChildren().clear();
        this.getChildren().add(tankImage);
        hitbox = new Rectangle(tankImage.getX()+5,tankImage.getY()+5,tankImage.getFitWidth()-10,tankImage.getFitHeight()-10);
        hitbox.xProperty().bind(tankImage.xProperty().add(5));
        hitbox.yProperty().bind(tankImage.yProperty().add(5));
       
        health = 3; //sets or resets the default health, armor and powerUp settings of the tank  
        armor = false;
        velocity = 2;
        time = 0;
        slipperyFactor = 1;
        powerUp = new PowerUp(0);
        movingLeft = false;
        movingRight = false;
        movingUp = false;
        airborn = false;
        readyToFire = true;
        goingDownward = false;
        usingInventory = false;
        frozen = false;
        slippingLeft = false;
        slippingRight = false;
        if(playerNumber == 1){
            FinalProject.game.setArmor1(armor);
            FinalProject.game.setHealth1(health);
        }
        else{
            FinalProject.game.setArmor2(armor);
            FinalProject.game.setHealth2(health);
        }
        
        if(FinalProject.settings.getMap().equals(FinalProject.settings.getMapList()[2]))
            gravity = 6;
        else gravity = 9.81;
        
        
        startAnimation(); //starts the main animation
    }
    
    public void startAnimation(){ //main animation used to move and fire bullets
        ani = new Timeline(new KeyFrame(Duration.millis(5),f->{
            if (!frozen){
            //tank will move left or right
                if((movingLeft && movingRight) || (!movingLeft && !movingRight)){
                    if(damaged){
                        if(tankImage.getImage() == rightSide)
                            tankImage.setImage(rightSideDamaged);
                        else if(tankImage.getImage() == leftSide)
                            tankImage.setImage(leftSideDamaged);
                        else if(tankImage.getImage() == rightSideUpgraded)
                            tankImage.setImage(rightSideDamagedUpgraded);
                        else if(tankImage.getImage() == leftSideUpgraded)
                            tankImage.setImage(leftSideDamagedUpgraded);
                    }
                    else{
                        if(tankImage.getImage() == rightSideDamaged)
                            tankImage.setImage(rightSide);
                        else if(tankImage.getImage() == leftSideDamaged)
                            
                            tankImage.setImage(leftSide);
                        else if(tankImage.getImage() == rightSideDamagedUpgraded)
                            tankImage.setImage(rightSideUpgraded);
                        else if(tankImage.getImage() == leftSideDamagedUpgraded)
                            tankImage.setImage(leftSideUpgraded);
                    }

                    if(armor){
                        if(tankImage.getImage() == rightSide)
                            tankImage.setImage(rightSideUpgraded);
                        else if(tankImage.getImage() == leftSide)
                            tankImage.setImage(leftSideUpgraded);
                    }
                    else{
                        if(tankImage.getImage() == rightSideUpgraded)
                            tankImage.setImage(rightSide);
                        else if(tankImage.getImage() == leftSideUpgraded)
                            tankImage.setImage(leftSide);
                    }

                }
                else if(movingLeft && !movingRight){
                    if(armor){
                        if(damaged)
                            tankImage.setImage(leftSideDamagedUpgraded);
                        else tankImage.setImage(leftSideUpgraded);
                    }
                    else{
                        if(damaged)
                            tankImage.setImage(leftSide);
                        else tankImage.setImage(leftSide);
                    }
                    if(!airborn) {
                    //reposition tank on edge of screen
                        if (tankImage.getX() <= -tankImage.getFitWidth()){
                            tankImage.setX(1000);
                        } else if (FinalProject.settings.getMap().equals(FinalProject.settings.getMapList()[4])){
                            if (!slippingRight){
                                slippingLeft = false;
                                try{
                                    slipAni.stop();
                                }catch(Exception ex){
                                }
                                tankImage.setX(tankImage.getX()-slipperyFactor*0.005);
                                if (slipperyFactor<300){
                                    slipperyFactor++;
                                }
                            }
                        } else{
                           tankImage.setX(tankImage.getX()-0.15*gravity);
                        }
                    }
                }
                else if(movingRight && !movingLeft){
                    if(armor){
                        if(damaged)
                            tankImage.setImage(rightSideDamagedUpgraded);
                        else tankImage.setImage(rightSideUpgraded);
                    }
                    else{
                        if(damaged)
                            tankImage.setImage(rightSideDamaged);
                        else tankImage.setImage(rightSide);
                    }
                    if(!airborn){
                    //reposition tank on edge of screen
                        if (tankImage.getX() >= 1000){
                            tankImage.setX(-tankImage.getFitWidth());
                        } else if (FinalProject.settings.getMap().equals(FinalProject.settings.getMapList()[4])){
                            if (!slippingLeft){
                                slippingRight = false;
                                try{
                                    slipAni.stop();
                                }catch(Exception ex){
                                }
                                tankImage.setX(tankImage.getX()+slipperyFactor*0.005);
                                if (slipperyFactor<300){
                                    slipperyFactor++;
                                }
                            }
                        } else{
                            tankImage.setX(tankImage.getX()+0.15*gravity);
                        }
                    }
                }

            //jumping
                if(movingUp && !airborn){
                    airborn = true;
                    jumpingAnimation(false,"");
                }

            //fall
                if (floor==2 && !airborn && ((tankImage.getX()>180 && tankImage.getX()<350) || (tankImage.getX()>580 && tankImage.getX()<750))
                || (floor==3 && !airborn && ((tankImage.getX()>0 && tankImage.getX()<150) || (tankImage.getX()>380 && tankImage.getX()<550) || (tankImage.getX()>780 && tankImage.getX()<950)))
                || (floor==4 && !airborn && ((tankImage.getX()>0 && tankImage.getX()<50) || (tankImage.getX()>180 && tankImage.getX()<750) || (tankImage.getX()>880)))){
                    airborn = true;
                    fallAnimation();
                }

            //increasing the velocity if increasingVelocity is true and velocity is under 5
                if(increasingVelocity && velocity <2.5)
                    velocity += 0.05;
                else if(increasingVelocity && velocity <5)
                    velocity += 0.015;

            //powerUps
                for(int i = 0;i<FinalProject.powerUpHitboxList.size();i++){
                    if(hitbox.intersects(FinalProject.powerUpHitboxList.get(i).getBoundsInParent())){
                        FinalProject.game.getChildren().remove(FinalProject.powerUpList.get(i).getItemImage());
                        powerUp = FinalProject.powerUpList.get(i);
                        if(playerNumber == 1)
                            FinalProject.game.getInventoryOne().setPowerUp(powerUp);
                        else
                            FinalProject.game.getInventoryTwo().setPowerUp(powerUp);

                        FinalProject.powerUpHitboxList.remove(i);
                        FinalProject.powerUpList.remove(i);
                    }
                }
            }
        }));
        ani.setCycleCount(Animation.INDEFINITE);
        ani.play();
    }
    public void jumpingAnimation(boolean knockback,String side){
        double x = tankImage.getX();
        double y = tankImage.getY();
    //knockback
        double airbornY = 435 - floor * 100 - y;
        previousY = tankImage.getY();
        goingDownward = false;
        time = 0;
        if(knockback && side.equals("Right")){
        //knockback right
        //stop previous jump or fall animation
            try{
                jumpAni.stop();
            }catch(Exception ex){
            }
            try{
                fallAni.stop();
            }catch(Exception ex){
            }
            jumpAni = new Timeline(new KeyFrame(Duration.millis(5),f->{
                tankImage.setY(y - 2.2 * time  + gravity * 0.004  * time*time);
                tankImage.setX(x + 0.17 * gravity * time);
                time++;
                checkCoordinates(y,airbornY,"Right");
            }));
            jumpAni.setCycleCount(Animation.INDEFINITE);
            jumpAni.play();
        }
        else if(knockback && side.equals("Left")){
        //knockback left
        //stop previous jump or fall animation
            try{
                jumpAni.stop();
            }catch(Exception ex){
            }
            try{
                fallAni.stop();
            }catch(Exception ex){
            }
            jumpAni = new Timeline(new KeyFrame(Duration.millis(5),f->{
                tankImage.setY(y - 2.2 * time  + gravity * 0.004  * time*time);
                tankImage.setX(x - 0.17 * gravity * time);
                time++;
                checkCoordinates(y,airbornY,"Left");
            }));
            jumpAni.setCycleCount(Animation.INDEFINITE);
            jumpAni.play();
        }
        else if(movingRight && !movingLeft && !slippingLeft){
            jumpAni = new Timeline(new KeyFrame(Duration.millis(5),f->{
                tankImage.setY(y - 5 * time  + gravity * 0.004  * time*time);
                if (!slippingRight){
                    if (FinalProject.settings.getMap().equals(FinalProject.settings.getMapList()[4]))
                        tankImage.setX(tankImage.getX()+slipperyFactor*0.005);
                    else
                        tankImage.setX(x + 0.1 * gravity * time);
                }
                time++;            
                checkCoordinates(y,airbornY,"");
            }));
            jumpAni.setCycleCount(Animation.INDEFINITE);
            jumpAni.play();
        }
        else if(movingLeft && !movingRight && !slippingRight){
            jumpAni = new Timeline(new KeyFrame(Duration.millis(5),f->{
                tankImage.setY(y - 5 * time  + gravity * 0.004  * time*time);
                if (!slippingLeft){
                    if (FinalProject.settings.getMap().equals(FinalProject.settings.getMapList()[4]))
                        tankImage.setX(tankImage.getX()-slipperyFactor*0.005);
                    else
                        tankImage.setX(x - 0.1 * gravity * time);
                }
                time++;
                checkCoordinates(y,airbornY,"");
            }));
            jumpAni.setCycleCount(Animation.INDEFINITE);
            jumpAni.play();
        }
        else{
            jumpAni = new Timeline(new KeyFrame(Duration.millis(5),f->{
                tankImage.setY(y - 5 * time  + gravity * 0.004  * time*time);
                time++;
                checkCoordinates(y,airbornY,"");
            }));
            jumpAni.setCycleCount(Animation.INDEFINITE);
            jumpAni.play();
        }
    }
    
    public void fallAnimation(){
        double x = tankImage.getX();
        double y = tankImage.getY();
        time = 0;
        if(((movingRight && !movingLeft) || slippingRight) && !slippingLeft){
            fallAni = new Timeline(new KeyFrame(Duration.millis(5),f->{
                tankImage.setY(y + gravity * 0.004  * time*time);
                if (!slippingRight){
                    if (FinalProject.settings.getMap().equals(FinalProject.settings.getMapList()[4]))
                        tankImage.setX(tankImage.getX()+slipperyFactor*0.005);
                    else
                        tankImage.setX(x + 0.1 * gravity * time);
                }                    
                time++;
            //stop animation on lower platform
                if (tankImage.getY() <= y+105 && tankImage.getY() >= y+95 && floor==3 && FinalProject.settings.getMap().equals(FinalProject.settings.getMapList()[4]) && tankImage.getX()<950){
                    tankImage.setY(y+100);
                    floor--;
                    fallAni.stop();
                    setAirborn(false);
                }else if (tankImage.getY() <= y+105 && tankImage.getY() >= y+95 && tankImage.getX()<850){
                //hell map death
                    if (FinalProject.settings.getMap().equals(FinalProject.settings.getMapList()[1]) && floor==2){
                        tankDefeated();
                    }
                    tankImage.setY(y+100);
                    floor--;
                    fallAni.stop();
                    setAirborn(false);
                }else if (tankImage.getY() <= y+205 && tankImage.getY() >= y+195 && tankImage.getX()>850){
                    tankImage.setY(y+200);
                    floor-=2;
                    fallAni.stop();
                    setAirborn(false);
                }
            }));
            fallAni.setCycleCount(Animation.INDEFINITE);
            fallAni.play();
        } else if(((movingLeft && !movingRight) || slippingLeft) && !slippingRight){
            fallAni = new Timeline(new KeyFrame(Duration.millis(5),f->{
                tankImage.setY(y + gravity * 0.004  * time*time);
                if (!slippingLeft){
                    if (FinalProject.settings.getMap().equals(FinalProject.settings.getMapList()[4]))
                        tankImage.setX(tankImage.getX()-slipperyFactor*0.005);
                    else
                        tankImage.setX(x - 0.1 * gravity * time);
                }
                time++;
            //stop animation on lower platform
                if (tankImage.getY() <= y+105 && tankImage.getY() >= y+95 && tankImage.getX()>80){
                    //hell map death
                    if (FinalProject.settings.getMap().equals(FinalProject.settings.getMapList()[1]) && floor==2){
                        tankDefeated();
                    }
                    tankImage.setY(y+100);
                    floor--;
                    fallAni.stop();
                    setAirborn(false);
                }else if ((tankImage.getY() <= y+205 && tankImage.getY() >= y+195 && tankImage.getX()<80)){
                    tankImage.setY(y+200);
                    floor-=2;
                    fallAni.stop();
                    setAirborn(false);
                }
            }));
            fallAni.setCycleCount(Animation.INDEFINITE);
            fallAni.play();
        }
    }

    public Rectangle getHitbox(){
        return hitbox;
    }
    
    public void setGoingDownward(boolean goingDownward){
        this.goingDownward = goingDownward;
    }
    
    public void clear(){
        this.getChildren().clear(); //clears the pane
    }  

    public void setVelocity(double velocity){
        this.velocity = velocity;
    }
    
    public int getHealth(){
        return health;
    }
    
    public void setSlipperyFactor(int slipperyFactor){
        this.slipperyFactor = slipperyFactor;
    }
    
    public boolean isSlippingLeft(){
        return slippingLeft;
    }
    
    public boolean isSlippingRight(){
        return slippingRight;
    }
    
     public Timeline getAni(){
        return ani;
    }
    
    public void declineVelocity(String direction){
    //the tank slips on the ice platforms of the winter map
        try{
            slipAni.stop();
        }catch(Exception ex){
        }
        if (direction.equals("Right") && slippingLeft==false){
            slippingRight = true;
            slipAni = new Timeline(new KeyFrame(Duration.millis(5),f->{
            //fall
                if (!airborn && ((floor==2 && ((tankImage.getX()>=180 && tankImage.getX()<=350) || (tankImage.getX()>=580 && tankImage.getX()<=750))) || (floor==3 && (tankImage.getX()<=150 || (tankImage.getX()>=380 && tankImage.getX()<=550) || tankImage.getX()>=780)) || (floor==4 && ((tankImage.getX()>=180 && tankImage.getX()<=750) || tankImage.getX()<=80 || tankImage.getX()>=880)))){
                    airborn = true;
                    fallAnimation();
                }
                if (slipperyFactor>0){
                    if (tankImage.getX() >= 1000){
                            tankImage.setX(-tankImage.getFitWidth());
                        }else tankImage.setX(tankImage.getX()+slipperyFactor*0.005);
                    if (!airborn)
                        slipperyFactor--;
                }else if (slipperyFactor<=0 && !airborn){
                    slipAni.stop();
                    slippingRight = false;
                }                    
            }));
        }else if (direction.equals("Left") && slippingRight==false){
            slippingLeft = true;
            slipAni = new Timeline(new KeyFrame(Duration.millis(5),f->{
            //fall
                if (!airborn && ((floor==2 && ((tankImage.getX()>=180 && tankImage.getX()<=350) || (tankImage.getX()>=580 && tankImage.getX()<=750))) || (floor==3 && (tankImage.getX()<=150 || (tankImage.getX()>=380 && tankImage.getX()<=550) || tankImage.getX()>=780)) || (floor==4 && ((tankImage.getX()>=180 && tankImage.getX()<=750) || tankImage.getX()<=80 || tankImage.getX()>=880)))){
                    airborn = true;
                    fallAnimation();
                }
                if (slipperyFactor>0){
                    if (tankImage.getX() <= -tankImage.getFitWidth()){
                            tankImage.setX(1000);
                        }else tankImage.setX(tankImage.getX()-slipperyFactor*0.005);
                    if (!airborn)
                        slipperyFactor--;
                }else if (slipperyFactor<=0 && !airborn){
                    slipAni.stop();
                    slippingLeft = false;
                }                    
            }));
        }
        slipAni.setCycleCount(Animation.INDEFINITE);
        slipAni.play();
    }
    
    public void checkCoordinates(double y, double airbornY, String direction){
    //the tank checks for nearby platforms to land on them when jumping, falling or knocked back
    //moon map
        if (tankImage.getY() <= y-195+airbornY && tankImage.getY() >= y-205+airbornY && goingDownward){
            if((floor==2 && ((tankImage.getX()>=50 && tankImage.getX()<=180) || (tankImage.getX()>=750 && tankImage.getX()<=880))) ||
            (floor==1 && ((tankImage.getX()>=150 && tankImage.getX()<=380) || (tankImage.getX()>=550 && tankImage.getX()<=780)))){
                tankImage.setY(y-200+airbornY);
                floor+=2;
                jumpAni.stop();
                setAirborn(false);
                goingDownward=false;
                resetSlipDirection(direction);
            }
        }
        if (floor==3 & tankImage.getY() <= y+105+airbornY && tankImage.getY() >= y+95+airbornY && tankImage.getX()<=180 && goingDownward){
            tankImage.setY(y+100+airbornY);
            floor-=1;
            jumpAni.stop();
            setAirborn(false);
            goingDownward=false;
            resetSlipDirection(direction);
        }
    //1 platform higher
        if (tankImage.getY() <= y-95+airbornY && tankImage.getY() >= y-105+airbornY && goingDownward){
            if ((floor==1 && ((tankImage.getX()<=180) || (tankImage.getX()>=350 && tankImage.getX()<=580) || (tankImage.getX()>=750))) ||
            (floor==2 && ((tankImage.getX()>=150 && tankImage.getX()<=380) || (tankImage.getX()>=550 && tankImage.getX()<=780))) ||
            (floor==3 && ((tankImage.getX()>=50 && tankImage.getX()<=180) || (tankImage.getX()>=750 && tankImage.getX()<=880)))){
                tankImage.setY(y-100+airbornY);
                floor++;
                jumpAni.stop();
                setAirborn(false);
                goingDownward=false;
                resetSlipDirection(direction);
            }
        }
    //hell map death
        if (FinalProject.settings.getMap().equals(FinalProject.settings.getMapList()[1]) && floor==2 &&
        tankImage.getY()<=y+105+airbornY && tankImage.getY()>=y+95+airbornY && ((tankImage.getX()>=180 && tankImage.getX()<=350) || (tankImage.getX()>=580 && tankImage.getX()<=750))){
            tankDefeated();
        }
    //1 platform lower
        if ((floor==2 && tankImage.getY()<=y+105+airbornY && tankImage.getY()>=y+95+airbornY && ((tankImage.getX()>=180 && tankImage.getX()<=350) || (tankImage.getX()>=580 && tankImage.getX()<=750))) ||
        (floor==3 && tankImage.getY()<=y+105+airbornY && tankImage.getY()>=y+95+airbornY && ((tankImage.getX()>=350 && tankImage.getX()<=580) || tankImage.getX()>=750)) ||
        (floor==4 && tankImage.getY()<=y+105+airbornY && tankImage.getY()>=y+95+airbornY && (tankImage.getX()>=180 && tankImage.getX()<=750))){
            tankImage.setY(y+100+airbornY);
            floor-=1;
            jumpAni.stop();
            setAirborn(false);
            goingDownward=false;
            resetSlipDirection(direction);
    //2 platforms lower
        }else if (floor==4 && tankImage.getY()<=y+205+airbornY && tankImage.getY()>=y+195+airbornY && (tankImage.getX()<=80 || tankImage.getX()>=880)){
            tankImage.setY(y+200+airbornY);
            floor-=2;
            jumpAni.stop();
            setAirborn(false);
            goingDownward=false;
            resetSlipDirection(direction);
        }
    //no platform found
        else if (tankImage.getY() > y+airbornY && airborn==true && (floor==1 ||
        (floor==2 && (tankImage.getX()<=180 || (tankImage.getX()>=350 && tankImage.getX()<=580) || tankImage.getX()>=750)) ||
        (floor==3 && ((tankImage.getX()>=180 && tankImage.getX()<=370) || (tankImage.getX()>=580 && tankImage.getX()<=770))) ||
        (floor==4 && ((tankImage.getX()>=50 && tankImage.getX()<=180) || (tankImage.getX()>=750 && tankImage.getX()<=880))))){
            tankImage.setY(y+airbornY);
        //floor remains the same
            jumpAni.stop();
            setAirborn(false);
            goingDownward=false;
            resetSlipDirection(direction);
        }
        
    //is the tank going downward?
        if (previousY < tankImage.getY()){
            goingDownward = true;
        }else previousY = tankImage.getY();
    }
    
    public String getName(){
        return "TankPane";
    }
    
    public void addFire(double x, double y){
        Fire fire = new Fire(x,y);
        this.getChildren().add(fire);
    }
    
    public void resetSlipDirection(String direction){
    //change direction of the slipping animation when the tank is knocked back
        if (FinalProject.settings.getMap().equals(FinalProject.settings.getMapList()[4])){
            if (direction.equals("Right")){
                slippingLeft = false;
                declineVelocity("Right");
            }else if (direction.equals("Left")){
                slippingRight = false;
                declineVelocity("Left");
            }
        }
    }
    
    public boolean isBehindRock(){
    //disable shooting when the tank is behind a rock (for fairness)
        if (FinalProject.settings.getMap().equals(FinalProject.settings.getMapList()[3]) && ((floor==1 && tankImage.getX()>=350 && tankImage.getX()<=380) || (floor==2 && tankImage.getX()>=800 && tankImage.getX()<=830) || floor==3 && tankImage.getX()>=250 && tankImage.getX()<=280))
            return true;
        else
            return false;
    }
}