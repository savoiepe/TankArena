package TankArenaSourceFiles;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FinalProject extends Application {
    public static Pane main;
    public static MainMenuPane mainMenu;
    public static GamePane game;
    public static SettingsPane settings;
    public static ControlsPane controls;
    public static FadeTransition[] increase;
    public static FadeTransition[] decrease;
    public static Label[] startLabels = {new Label("3"), new Label("2"), new Label("1"), new Label("FIGHT")};
    private Thread spawnItems = new Thread();
    private Thread countTime = new Thread();
    private static int timerInSeconds = 1;
    private static int count = 0;
    public static Label displayRounds = new Label("");
    public static Label displayTimer = new Label("");
    public static boolean gameRunning = false;
    public static boolean tankOneAlive = true;
    public static boolean tankTwoAlive = true;
    public static ArrayList<Rectangle> powerUpHitboxList = new ArrayList<>();
    public static ArrayList<PowerUp> powerUpList = new ArrayList<>();
    public static SequentialTransition startGameAnimation = new SequentialTransition();;
    public static FadeTransition fadeBackground = new FadeTransition();
    public static Rectangle shade = new Rectangle(1000,1000,Color.GRAY);
    public static Rectangle hitboxRock1, hitboxRock2, hitboxRock3;
    private static Menu menu1 = new Menu("Options");
    private static MenuBar menuBar = new MenuBar();
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        //main.setCursor(new ImageCursor(new Image("finalprojectImages/Tank_Cursor.gif")));
    //panes
        main = new Pane();
        main.setMaxWidth(980);
        main.setMaxHeight(570);
        mainMenu = new MainMenuPane(1000,590);
        game = new GamePane(1000,570);
        settings = new SettingsPane(1000,590);
        controls = new ControlsPane(1000,570);
    //store the controls that were changed in the settings to change the inputs of the game
        String[] controls1 = {controls.getSetControls1()[0].getText(),controls.getSetControls1()[1].getText(),controls.getSetControls1()[2].getText(),
                              controls.getSetControls1()[3].getText(),controls.getSetControls1()[4].getText()};
        String[] controls2 = {controls.getSetControls2()[0].getText(),controls.getSetControls2()[1].getText(),controls.getSetControls2()[2].getText(),
                              controls.getSetControls2()[3].getText(),controls.getSetControls2()[4].getText()};
    //toolbar on each pane
        menuBar.getMenus().add(menu1);
        menuBar.setMinWidth(1000);
        MenuItem menuItem1 = new MenuItem("Back to main menu");
        MenuItem menuItem2 = new MenuItem("Settings");
        MenuItem menuItem3 = new MenuItem("Controls");
        MenuItem menuItem4 = new MenuItem("Exit game");
        menu1.getItems().addAll(menuItem1,menuItem2,menuItem3,menuItem4);            
        menuItem1.setOnAction(a->{
            main.getChildren().clear();
            removeObjects();
            main.getChildren().add(mainMenu);
            main.getChildren().add(menuBar);});
        menuItem2.setOnAction(b->{
            main.getChildren().clear();
            removeObjects();
            main.getChildren().add(settings);
            main.getChildren().add(menuBar);});
        menuItem3.setOnAction(c->{
            main.getChildren().clear();
            removeObjects();
            main.getChildren().add(controls);
            main.getChildren().add(menuBar);});
        menuItem4.setOnAction(d->{Platform.exit();
        spawnItems.stop();
        countTime.stop();});
//MAIN MENU
        mainMenu.setPlayDimensions(120, 70);
        mainMenu.setPlayCoordinates(425, 130);
        mainMenu.setSettingsDimensions(120, 70);
        mainMenu.setSettingsCoordinates(355, 380);
        mainMenu.setControlsDimensions(120, 70);
        mainMenu.setControlsCoordinates(495, 380);
        main.getChildren().addAll(mainMenu,menuBar);
    //start of game animation       
        increase = new FadeTransition[4];
        decrease = new FadeTransition[4];
        startGame(startGameAnimation,increase,decrease,startLabels,fadeBackground,shade); //line 744
//THREAD 1: SPAWN ITEMS
    //items spawning randomly every 5 seconds
        spawnItems = new Thread(() -> {
            while (true){
                try{
                    Platform.runLater(() -> spawn(game)); //line 708
                    Thread.sleep(5000);
                } catch(InterruptedException ex){
                }
            }
        });
    //convert timer from minutes to seconds
        if (!settings.getTimer().equals(settings.getTimerList()[0])){
            timerInSeconds = Integer.parseInt(settings.getTimer().charAt(0) + "")*60 + Integer.parseInt(settings.getTimer().substring(2));
        }
//THREAD 2: TIMER
    //show countdown from timer to 0
        countTime = new Thread(()-> {
            while (true){
                try{
                    Platform.runLater(()-> {
                        if (timerInSeconds > 0 && gameRunning==true && !settings.getTimer().equals(settings.getTimerList()[0])){
                            timerInSeconds--;
                            displayTimer.setText(timerInSeconds + "");
                        }else if (timerInSeconds==0 && gameRunning){
                        //time's up
                            //game over
                            if (settings.getRoundsRemaining().equals(settings.getRoundsList()[0])){
                                gameOver(main,game,mainMenu,settings,startGameAnimation,fadeBackground,shade); //line 767
                            //game continues (rounds remaining)
                            }else{
                                settings.setRoundsRemaining(settings.getRoundsList()[new ArrayList<>(Arrays.asList(settings.getRoundsList())).indexOf(settings.getRoundsRemaining())-1]);
                                if (game.getTankOne().getHealth()>game.getTankTwo().getHealth()){
                                    game.setScoreOne(game.getScoreOne()+1);
                                }else if(game.getTankOne().getHealth()<game.getTankTwo().getHealth()){
                                    game.setScoreTwo(game.getScoreTwo()+1);
                                }
                                displayRounds.setText("Rounds Remaining: " + settings.getRoundsRemaining() + "     Score: " + game.getScoreOne() + " - " + game.getScoreTwo());
                                playAgain();
                            }
                        }
                    });
                    Thread.sleep(1000);
                } catch (InterruptedException ex){
                }
            }
        });
    //start game
        mainMenu.getPlay().setOnAction(e -> {
            gameRunning = false;
            powerUpList.clear();
            powerUpHitboxList.clear();
            main.getChildren().clear();
            game.reset();
            main.getChildren().addAll(game,menuBar);
        //1,2,3,fight
            main.getChildren().add(shade);
            for (int i=0; i<startLabels.length; i++){
                startLabels[i].setFont(new Font("Cooper Black", 100));
                startLabels[i].setOpacity(0);
                startLabels[i].setLayoutX(470);
                startLabels[i].setLayoutY(200);
                main.getChildren().add(startLabels[i]);
            }
            startLabels[3].setLayoutX(350);
            startGameAnimation.play();
            fadeBackground.play();
            startGameAnimation.setOnFinished(f -> {
                gameRunning = true;
                tankOneAlive = true;
                tankTwoAlive = true;
            });
            fadeBackground.setOnFinished(f -> {
                main.getChildren().remove(shade);
            });
        //rounds & timer
            settings.setRoundsRemaining(settings.getNumberOfRounds());
            game.setScoreOne(0);
            game.setScoreTwo(0);
            displayRounds.setText("Rounds Remaining: " + settings.getRoundsRemaining() + "     Score: " + game.getScoreOne() + " - " + game.getScoreTwo());
            displayRounds.setFont(new Font("Cooper Black",10));
            if (!settings.getTimer().equals(settings.getTimerList()[0])){
            //reset timer
                timerInSeconds = Integer.parseInt(settings.getTimer().charAt(0) + "")*60 + Integer.parseInt(settings.getTimer().substring(2));
                displayTimer.setText(timerInSeconds + "");
            } else{
                timerInSeconds = 1;
                displayTimer.setText(settings.getTimer());
            }
            displayTimer.setFont(new Font("Cooper Black",10));
        //make label texts white on certain maps to be more visible
            if (settings.getMap() == settings.getMapList()[1] || settings.getMap() == settings.getMapList()[2] || settings.getMap() == settings.getMapList()[4]){
                displayRounds.setTextFill(Color.WHITE);
                displayTimer.setTextFill(Color.WHITE);
            }else{
                displayRounds.setTextFill(Color.BLACK);
                displayTimer.setTextFill(Color.BLACK);
            }
            game.getRoundsPane().getChildren().clear();
            game.getRoundsPane().getChildren().add(displayRounds);
            game.getTimerPane().getChildren().clear();
            game.getTimerPane().getChildren().add(displayTimer);
        //paint tanks
            try{
                game.getTankOne().getAni().stop();
                game.getTankTwo().getAni().stop();
            }catch(Exception ex){
            }
            if (settings.getOpponent().getFill().equals(Color.RED)){
                game.setTankTwo(new TankPane(settings.getColorStringTwo(), 2));
            } else{
                game.setTankTwo(new AiTank(settings.getColorStringTwo(), 2));
            }
            game.setTankOne(new TankPane(settings.getColorStringOne(), 1));
            game.getTankOne().getTankImage().setX(20);
            game.getTankTwo().getTankImage().setX(900);
        //spawn one platform higher in hell map
            if (settings.getMap().equals(settings.getMapList()[1])){
                game.getTankOne().getTankImage().setY(235);
                game.getTankTwo().getTankImage().setY(235);
            }else{
                game.getTankOne().getTankImage().setY(335);
                game.getTankTwo().getTankImage().setY(335);
            }
            game.getPlayPane().getChildren().addAll(game.getTankOne(), game.getTankTwo());
        //paint map
            game.setBackground(new Background(new BackgroundFill(new ImagePattern(settings.getMap()), CornerRadii.EMPTY, Insets.EMPTY)));
        //lava platforms (map 2)
            if (settings.getMap().equals(settings.getMapList()[1])){
                for (int i=0; i<game.getLavaImages().length; i++){
                    game.getPlayPane().getChildren().add(game.getLavaImages()[i]);
                }
        //rocks (map 4)
            }else if(settings.getMap().equals(settings.getMapList()[3])){
                hitboxRock1 = new Rectangle(game.rock1.getX() + 10,game.rock1.getY() + 40,game.rock1Image.getWidth() - 20,game.rock1Image.getHeight() - 40);
                hitboxRock2 = new Rectangle(game.rock2.getX() + 10,game.rock2.getY() + 45,game.rock2Image.getWidth() - 20,game.rock2Image.getHeight() - 45);
                hitboxRock3 = new Rectangle(game.rock3.getX() + 10,game.rock3.getY() + 35,game.rock3Image.getWidth() - 30,game.rock3Image.getHeight() - 25);
                game.getPlayPane().getChildren().addAll(game.rock1,game.rock2,game.rock3);
            }
        //ice platforms (map 5)
            if (settings.getMap().equals(settings.getMapList()[4])){
                for (int i=0; i<game.getIceImages().length; i++){
                    game.getIceImages()[i].setLayoutX(game.getPlatformList()[i].getPlatformImage().getX());
                    game.getIceImages()[i].setLayoutY(game.getPlatformList()[i].getPlatformImage().getY()-10);
                    game.getPlayPane().getChildren().add(game.getIceImages()[i]);
                    game.getPlayPane().getChildren().remove(game.getPlatformList()[i].getPlatformImage());
                }
            }
        //start spawning items and counting time
            try{
                spawnItems.start();
            } catch(Exception ex){
            }
            try{
                countTime.start();
            } catch(Exception ex){
            }
        //controls (pressed)
            main.setOnKeyPressed(f->{
                if(gameRunning){
                //tank one
                    if(f.getCode().equals(KeyCode.valueOf(controls1[0]))){
                        game.getTankOne().moveLeft(true);
                    }
                    if(f.getCode().equals(KeyCode.valueOf(controls1[1]))){
                        game.getTankOne().moveRight(true);
                    }
                    if(f.getCode().equals(KeyCode.valueOf(controls1[2]))){
                        game.getTankOne().moveUp(true);
                    }
                    if(f.getCode().equals(KeyCode.valueOf(controls1[3]))){
                        game.getTankOne().increaseVelocity(true);
                    }
                    if(f.getCode().equals(KeyCode.valueOf(controls1[4]))){
                        game.getTankOne().useInventory();
                    }
                //tank two
                    if (game.getTankTwo().getName().equals("TankPane")){
                        if(f.getCode().equals(KeyCode.valueOf(controls2[0]))){
                            game.getTankTwo().moveLeft(true);
                        }
                        if(f.getCode().equals(KeyCode.valueOf(controls2[1]))){
                            game.getTankTwo().moveRight(true);
                        }
                        if(f.getCode().equals(KeyCode.valueOf(controls2[2]))){
                            game.getTankTwo().moveUp(true);
                        }
                        if(f.getCode().equals(KeyCode.valueOf(controls2[3]))){
                            game.getTankTwo().increaseVelocity(true);
                        }
                        if(f.getCode().equals(KeyCode.valueOf(controls2[4]))){
                            game.getTankTwo().useInventory();
                        }
                    }
                }
            });
        //controls (released)
            main.setOnKeyReleased(f->{
                if (gameRunning){
                //tank one
                    if(f.getCode().equals(KeyCode.valueOf(controls1[0]))){
                        game.getTankOne().moveLeft(false);
                    }
                    if(f.getCode().equals(KeyCode.valueOf(controls1[1]))){
                        game.getTankOne().moveRight(false);
                    }
                    if(f.getCode().equals(KeyCode.valueOf(controls1[2]))){
                        game.getTankOne().moveUp(false);
                    }
                    if(f.getCode().equals(KeyCode.valueOf(controls1[3]))){
                        game.getTankOne().fireBullet();
                    }
                //tank two
                    if (game.getTankTwo().getName().equals("TankPane")){
                        if(f.getCode().equals(KeyCode.valueOf(controls2[0]))){
                            game.getTankTwo().moveLeft(false);
                        }
                        if(f.getCode().equals(KeyCode.valueOf(controls2[1]))){
                            game.getTankTwo().moveRight(false);
                        }
                        if(f.getCode().equals(KeyCode.valueOf(controls2[2]))){
                            game.getTankTwo().moveUp(false);
                        }            
                        if(f.getCode().equals(KeyCode.valueOf(controls2[3]))){
                            game.getTankTwo().fireBullet();
                        }
                    }
                }
            });
        });
    //open settings
        mainMenu.getSettings().setOnAction(e -> {
            main.getChildren().clear();
            main.getChildren().addAll(settings,menuBar);
        });
    //open controls
        mainMenu.getControls().setOnAction(e -> {
            for (int i=0; i<controls.getSetControls1().length; i++){
                controls.getSetControls1()[i].setText(controls1[i]);
                controls.getSetControls2()[i].setText(controls2[i]);
            }
            main.getChildren().clear();
            main.getChildren().addAll(controls,menuBar);
        });
        
//GAME
    //open menu from game
        game.getMenu().setOnAction(e -> {
            removeObjects();
            main.getChildren().clear();
            main.getChildren().addAll(mainMenu,menuBar);
        });
        
//SETTINGS
    //color one
        settings.getLeftC1().setOnAction(e -> {
            if (new ArrayList<>(Arrays.asList(settings.getColorList())).indexOf(settings.getColorOne()) != 0 ){
                settings.setColorOne(settings.getColorList()[new ArrayList<>(Arrays.asList(settings.getColorList())).indexOf(settings.getColorOne())-1]);
                settings.setColorStringOne(settings.getColorStringList()[new ArrayList<>(Arrays.asList(settings.getColorStringList())).indexOf(settings.getColorStringOne())-1]);
            } else{
                settings.setColorOne(settings.getColorList()[settings.getColorList().length-1]);
                settings.setColorStringOne(settings.getColorStringList()[settings.getColorStringList().length-1]);
            }
            settings.getColorDisplayedOne().setFill(settings.getColorOne());
        });
        
        settings.getRightC1().setOnAction(e -> {
            if (new ArrayList<>(Arrays.asList(settings.getColorList())).indexOf(settings.getColorOne()) != 4 ){
                settings.setColorOne(settings.getColorList()[new ArrayList<>(Arrays.asList(settings.getColorList())).indexOf(settings.getColorOne())+1]);
                settings.setColorStringOne(settings.getColorStringList()[new ArrayList<>(Arrays.asList(settings.getColorStringList())).indexOf(settings.getColorStringOne())+1]);
            } else{
                settings.setColorOne(settings.getColorList()[0]);
                settings.setColorStringOne(settings.getColorStringList()[0]);
            }
            settings.getColorDisplayedOne().setFill(settings.getColorOne());
        });   
    //color two
        settings.getLeftC2().setOnAction(e -> {
            if (new ArrayList<>(Arrays.asList(settings.getColorList())).indexOf(settings.getColorTwo()) != 0 ){
                settings.setColorTwo(settings.getColorList()[new ArrayList<>(Arrays.asList(settings.getColorList())).indexOf(settings.getColorTwo())-1]);
                settings.setColorStringTwo(settings.getColorStringList()[new ArrayList<>(Arrays.asList(settings.getColorStringList())).indexOf(settings.getColorStringTwo())-1]);
            } else{
                settings.setColorTwo(settings.getColorList()[settings.getColorList().length-1]);
                settings.setColorStringTwo(settings.getColorStringList()[settings.getColorStringList().length-1]);
            }
            settings.getColorDisplayedTwo().setFill(settings.getColorTwo());
        });    
        
        settings.getRightC2().setOnAction(e -> {
            if (new ArrayList<>(Arrays.asList(settings.getColorList())).indexOf(settings.getColorTwo()) != 4 ){
                settings.setColorTwo(settings.getColorList()[new ArrayList<>(Arrays.asList(settings.getColorList())).indexOf(settings.getColorTwo())+1]);
                settings.setColorStringTwo(settings.getColorStringList()[new ArrayList<>(Arrays.asList(settings.getColorStringList())).indexOf(settings.getColorStringTwo())+1]);
            } else{
                settings.setColorTwo(settings.getColorList()[0]);
                settings.setColorStringTwo(settings.getColorStringList()[0]);
            }
            settings.getColorDisplayedTwo().setFill(settings.getColorTwo());
        });      
    //timer
        settings.getLeftTimer().setOnAction(e -> {
            if (new ArrayList<>(Arrays.asList(settings.getTimerList())).indexOf(settings.getTimer()) != 0 ){
                settings.setTimer(settings.getTimerList()[new ArrayList<>(Arrays.asList(settings.getTimerList())).indexOf(settings.getTimer())-1]);
            } else{
                settings.setTimer(settings.getTimerList()[settings.getTimerList().length-1]);
            }
            settings.getTimerDisplayed().setText(settings.getTimer());
            if (!settings.getTimer().equals(settings.getTimerList()[0])){
                timerInSeconds = Integer.parseInt(settings.getTimer().charAt(0) + "")*60 + Integer.parseInt(settings.getTimer().substring(2));
            }
        });       
        settings.getRightTimer().setOnAction(e -> {
            if (new ArrayList<>(Arrays.asList(settings.getTimerList())).indexOf(settings.getTimer()) != 6 ){
                settings.setTimer(settings.getTimerList()[new ArrayList<>(Arrays.asList(settings.getTimerList())).indexOf(settings.getTimer())+1]);
            } else{
                settings.setTimer(settings.getTimerList()[0]);
            }
            settings.getTimerDisplayed().setText(settings.getTimer());
            if (!settings.getTimer().equals(settings.getTimerList()[0])){
                timerInSeconds = Integer.parseInt(settings.getTimer().charAt(0) + "")*60 + Integer.parseInt(settings.getTimer().substring(2));
            }
        });       
    //rounds
        settings.getLeftRounds().setOnAction(e -> {
            if (new ArrayList<>(Arrays.asList(settings.getRoundsList())).indexOf(settings.getNumberOfRounds()) != 0){
                settings.setNumberOfRounds(settings.getRoundsList()[new ArrayList<>(Arrays.asList(settings.getRoundsList())).indexOf(settings.getNumberOfRounds())-1]);
            } else{
                settings.setNumberOfRounds(settings.getRoundsList()[settings.getRoundsList().length-1]);
            }
            settings.setRoundsRemaining(settings.getNumberOfRounds());
            settings.getRoundsDisplayed().setText(settings.getRoundsRemaining());
        });       
        settings.getRightRounds().setOnAction(e -> {
            if (new ArrayList<>(Arrays.asList(settings.getRoundsList())).indexOf(settings.getNumberOfRounds()) != 4){
                settings.setNumberOfRounds(settings.getRoundsList()[new ArrayList<>(Arrays.asList(settings.getRoundsList())).indexOf(settings.getNumberOfRounds())+1]);
            } else{
                settings.setNumberOfRounds(settings.getRoundsList()[0]);
            }
            settings.setRoundsRemaining(settings.getNumberOfRounds());
            settings.getRoundsDisplayed().setText(settings.getNumberOfRounds());
        });        
    //map
        settings.getLeftMap().setOnAction(e -> {
            if (new ArrayList<>(Arrays.asList(settings.getMapList())).indexOf(settings.getMap()) != 0 ){
            settings.setMap(settings.getMapList()[new ArrayList<>(Arrays.asList(settings.getMapList())).indexOf(settings.getMap())-1]);
            } else{
                settings.setMap(settings.getMapList()[settings.getMapList().length-1]);
            }
            settings.setDisplayMap(new ImageView(settings.getMap()));
            settings.refreshMap();
        });
        settings.getRightMap().setOnAction(e -> {
            if (new ArrayList<>(Arrays.asList(settings.getMapList())).indexOf(settings.getMap()) != 4 ){
            settings.setMap(settings.getMapList()[new ArrayList<>(Arrays.asList(settings.getMapList())).indexOf(settings.getMap())+1]);
            } else{
                settings.setMap(settings.getMapList()[0]);
            }
            settings.setDisplayMap(new ImageView(settings.getMap()));
            settings.refreshMap();
        });
        
//CONTROLS
    //player one
        controls.getSetControls1()[0].setOnKeyPressed(e ->{
            if (e.getCode().equals(KeyCode.ENTER)){
                if (!controls.getControlsUsed().contains(controls.getSetControls1()[0].getText().toUpperCase()) && ((Character.isLetter(controls.getSetControls1()[0].getText().charAt(0)) && controls.getSetControls1()[0].getText().length()==1) ||
                (controls.getSetControls1()[0].getText().toUpperCase().equals("LEFT") || controls.getSetControls1()[0].getText().toUpperCase().equals("RIGHT") ||
                 controls.getSetControls1()[0].getText().toUpperCase().equals("UP") || controls.getSetControls1()[0].getText().toUpperCase().equals("DOWN")))){
                    controls.getControlsUsed().remove(controls1[0]);
                    controls1[0] = controls.getSetControls1()[0].getText().toUpperCase();
                    controls.getControlsUsed().add(controls1[0]);
                }else if(controls.getSetControls1()[0].getText().toUpperCase().equals(controls1[0])){
                }
                else if(controls.getControlsUsed().contains(controls.getSetControls1()[0].getText().toUpperCase())){
                    controls.getSetControls1()[0].setText("Already Used");
                }
                else controls.getSetControls1()[0].setText("Invalid");
            }
        });
        controls.getSetControls1()[1].setOnKeyPressed(e ->{
            if (e.getCode().equals(KeyCode.ENTER)){
                if (!controls.getControlsUsed().contains(controls.getSetControls1()[1].getText().toUpperCase()) && ((Character.isLetter(controls.getSetControls1()[1].getText().charAt(0)) && controls.getSetControls1()[1].getText().length()==1) ||
                (controls.getSetControls1()[1].getText().toUpperCase().equals("LEFT") || controls.getSetControls1()[1].getText().toUpperCase().equals("RIGHT") ||
                 controls.getSetControls1()[1].getText().toUpperCase().equals("UP") || controls.getSetControls1()[1].getText().toUpperCase().equals("DOWN")))){
                    controls.getControlsUsed().remove(controls1[1]);
                    controls1[1] = controls.getSetControls1()[1].getText().toUpperCase();
                    controls.getControlsUsed().add(controls1[1]);
                }else if(controls.getSetControls1()[1].getText().toUpperCase().equals(controls1[1])){
                }
                else if(controls.getControlsUsed().contains(controls.getSetControls1()[1].getText().toUpperCase())){
                    controls.getSetControls1()[1].setText("Already Used");
                }
                else controls.getSetControls1()[1].setText("Invalid");
            }
        });
        controls.getSetControls1()[2].setOnKeyPressed(e ->{
            if (e.getCode().equals(KeyCode.ENTER)){
                if (!controls.getControlsUsed().contains(controls.getSetControls1()[2].getText().toUpperCase()) && ((Character.isLetter(controls.getSetControls1()[2].getText().charAt(0)) && controls.getSetControls1()[2].getText().length()==1) ||
                (controls.getSetControls1()[2].getText().toUpperCase().equals("LEFT") || controls.getSetControls1()[2].getText().toUpperCase().equals("RIGHT") ||
                 controls.getSetControls1()[2].getText().toUpperCase().equals("UP") || controls.getSetControls1()[2].getText().toUpperCase().equals("DOWN")))){
                    controls.getControlsUsed().remove(controls1[2]);
                    controls1[2] = controls.getSetControls1()[2].getText().toUpperCase();
                    controls.getControlsUsed().add(controls1[2]);
                }else if(controls.getSetControls1()[2].getText().toUpperCase().equals(controls1[2])){
                }
                else if(controls.getControlsUsed().contains(controls.getSetControls1()[2].getText().toUpperCase())){
                    controls.getSetControls1()[2].setText("Already Used");
                }
                else controls.getSetControls1()[2].setText("Invalid");
            }
        });
        controls.getSetControls1()[3].setOnKeyPressed(e ->{
            if (e.getCode().equals(KeyCode.ENTER)){
                if (!controls.getControlsUsed().contains(controls.getSetControls1()[3].getText().toUpperCase()) && ((Character.isLetter(controls.getSetControls1()[3].getText().charAt(0)) && controls.getSetControls1()[3].getText().length()==1) ||
                (controls.getSetControls1()[3].getText().toUpperCase().equals("LEFT") || controls.getSetControls1()[3].getText().toUpperCase().equals("RIGHT") ||
                 controls.getSetControls1()[3].getText().toUpperCase().equals("UP") || controls.getSetControls1()[3].getText().toUpperCase().equals("DOWN")))){
                    controls.getControlsUsed().remove(controls1[3]);
                    controls1[3] = controls.getSetControls1()[3].getText().toUpperCase();
                    controls.getControlsUsed().add(controls1[3]);
                }else if(controls.getSetControls1()[3].getText().toUpperCase().equals(controls1[3])){
                }
                else if(controls.getControlsUsed().contains(controls.getSetControls1()[3].getText().toUpperCase())){
                    controls.getSetControls1()[3].setText("Already Used");
                }
                else controls.getSetControls1()[3].setText("Invalid");
            }
        });
        controls.getSetControls1()[4].setOnKeyPressed(e ->{
            if (e.getCode().equals(KeyCode.ENTER)){
                if (!controls.getControlsUsed().contains(controls.getSetControls1()[4].getText().toUpperCase()) && ((Character.isLetter(controls.getSetControls1()[4].getText().charAt(0)) && controls.getSetControls1()[4].getText().length()==1) ||
                (controls.getSetControls1()[4].getText().toUpperCase().equals("LEFT") || controls.getSetControls1()[4].getText().toUpperCase().equals("RIGHT") ||
                 controls.getSetControls1()[4].getText().toUpperCase().equals("UP") || controls.getSetControls1()[4].getText().toUpperCase().equals("DOWN")))){
                    controls.getControlsUsed().remove(controls1[4]);
                    controls1[4] = controls.getSetControls1()[4].getText().toUpperCase();
                    controls.getControlsUsed().add(controls1[4]);
                }else if(controls.getSetControls1()[4].getText().toUpperCase().equals(controls1[4])){
                }
                else if(controls.getControlsUsed().contains(controls.getSetControls1()[4].getText().toUpperCase())){
                    controls.getSetControls1()[4].setText("Already Used");
                }
                else controls.getSetControls1()[4].setText("Invalid");
            }
        });
    //reset texts
        controls.getSetControls1()[0].setOnMouseClicked(e ->{
            for (int i=0; i<5; i++){
                controls.getSetControls1()[i].setText(controls1[i]);
                controls.getSetControls2()[i].setText(controls2[i]);
            }
            controls.getSetControls1()[0].setText("");
        });
        controls.getSetControls1()[1].setOnMouseClicked(e ->{
            for (int i=0; i<5; i++){
                controls.getSetControls1()[i].setText(controls1[i]);
                controls.getSetControls2()[i].setText(controls2[i]);
            }
            controls.getSetControls1()[1].setText("");
        });
        controls.getSetControls1()[2].setOnMouseClicked(e ->{
            for (int i=0; i<5; i++){
                controls.getSetControls1()[i].setText(controls1[i]);
                controls.getSetControls2()[i].setText(controls2[i]);
            }
            controls.getSetControls1()[2].setText("");
        });
        controls.getSetControls1()[3].setOnMouseClicked(e ->{
            for (int i=0; i<5; i++){
                controls.getSetControls1()[i].setText(controls1[i]);
                controls.getSetControls2()[i].setText(controls2[i]);
            }
            controls.getSetControls1()[3].setText("");
        });
        controls.getSetControls1()[4].setOnMouseClicked(e ->{
            for (int i=0; i<5; i++){
                controls.getSetControls1()[i].setText(controls1[i]);
                controls.getSetControls2()[i].setText(controls2[i]);
            }
            controls.getSetControls1()[4].setText("");
        });
    //player two
        controls.getSetControls2()[0].setOnKeyPressed(e ->{
            if (e.getCode().equals(KeyCode.ENTER)){
                if (!controls.getControlsUsed().contains(controls.getSetControls2()[0].getText().toUpperCase()) && ((Character.isLetter(controls.getSetControls2()[0].getText().charAt(0)) && controls.getSetControls2()[0].getText().length()==1) ||
                (controls.getSetControls2()[0].getText().toUpperCase().equals("LEFT") || controls.getSetControls2()[0].getText().toUpperCase().equals("RIGHT") ||
                 controls.getSetControls2()[0].getText().toUpperCase().equals("UP") || controls.getSetControls2()[0].getText().toUpperCase().equals("DOWN")))){
                    controls.getControlsUsed().remove(controls2[0]);
                    controls2[0] = controls.getSetControls2()[0].getText().toUpperCase();
                    controls.getControlsUsed().add(controls2[0]);
                }else if(controls.getSetControls2()[0].getText().toUpperCase().equals(controls2[0])){
                }
                else if(controls.getControlsUsed().contains(controls.getSetControls2()[0].getText().toUpperCase())){
                    controls.getSetControls2()[0].setText("Already Used");
                }
                else controls.getSetControls2()[0].setText("Invalid");
            }
        });
        controls.getSetControls2()[1].setOnKeyPressed(e ->{
            if (e.getCode().equals(KeyCode.ENTER)){
                if (!controls.getControlsUsed().contains(controls.getSetControls2()[1].getText().toUpperCase()) && ((Character.isLetter(controls.getSetControls2()[1].getText().charAt(0)) && controls.getSetControls2()[1].getText().length()==1) ||
                (controls.getSetControls2()[1].getText().toUpperCase().equals("LEFT") || controls.getSetControls2()[1].getText().toUpperCase().equals("RIGHT") ||
                 controls.getSetControls2()[1].getText().toUpperCase().equals("UP") || controls.getSetControls2()[1].getText().toUpperCase().equals("DOWN")))){
                    controls.getControlsUsed().remove(controls2[1]);
                    controls2[1] = controls.getSetControls2()[1].getText().toUpperCase();
                    controls.getControlsUsed().add(controls2[1]);
                }else if(controls.getSetControls2()[1].getText().toUpperCase().equals(controls2[1])){
                }
                else if(controls.getControlsUsed().contains(controls.getSetControls2()[1].getText().toUpperCase())){
                    controls.getSetControls2()[1].setText("Already Used");
                }
                else controls.getSetControls2()[1].setText("Invalid");
            }
        });
        controls.getSetControls2()[2].setOnKeyPressed(e ->{
            if (e.getCode().equals(KeyCode.ENTER)){
                if (!controls.getControlsUsed().contains(controls.getSetControls2()[2].getText().toUpperCase()) && ((Character.isLetter(controls.getSetControls2()[2].getText().charAt(0)) && controls.getSetControls2()[2].getText().length()==1) ||
                (controls.getSetControls2()[2].getText().toUpperCase().equals("LEFT") || controls.getSetControls2()[2].getText().toUpperCase().equals("RIGHT") ||
                 controls.getSetControls2()[2].getText().toUpperCase().equals("UP") || controls.getSetControls2()[2].getText().toUpperCase().equals("DOWN")))){
                    controls.getControlsUsed().remove(controls2[2]);
                    controls2[2] = controls.getSetControls2()[2].getText().toUpperCase();
                    controls.getControlsUsed().add(controls2[2]);
                }else if(controls.getSetControls2()[2].getText().toUpperCase().equals(controls2[2])){
                }
                else if(controls.getControlsUsed().contains(controls.getSetControls2()[2].getText().toUpperCase())){
                    controls.getSetControls2()[2].setText("Already Used");
                }
                else controls.getSetControls2()[2].setText("Invalid");
            }
        });
        controls.getSetControls2()[3].setOnKeyPressed(e ->{
            if (e.getCode().equals(KeyCode.ENTER)){
                if (!controls.getControlsUsed().contains(controls.getSetControls2()[3].getText().toUpperCase()) && ((Character.isLetter(controls.getSetControls2()[3].getText().charAt(0)) && controls.getSetControls2()[3].getText().length()==1) ||
                (controls.getSetControls2()[3].getText().toUpperCase().equals("LEFT") || controls.getSetControls2()[3].getText().toUpperCase().equals("RIGHT") ||
                 controls.getSetControls2()[3].getText().toUpperCase().equals("UP") || controls.getSetControls2()[3].getText().toUpperCase().equals("DOWN")))){
                    controls.getControlsUsed().remove(controls2[3]);
                    controls2[3] = controls.getSetControls2()[3].getText().toUpperCase();
                    controls.getControlsUsed().add(controls2[3]);
                }else if(controls.getSetControls2()[3].getText().toUpperCase().equals(controls2[3])){
                }
                else if(controls.getControlsUsed().contains(controls.getSetControls2()[3].getText().toUpperCase())){
                    controls.getSetControls2()[3].setText("Already Used");
                }
                else controls.getSetControls2()[3].setText("Invalid");
            }
        });
        controls.getSetControls2()[4].setOnKeyPressed(e ->{
            if (e.getCode().equals(KeyCode.ENTER)){
                if (!controls.getControlsUsed().contains(controls.getSetControls2()[4].getText().toUpperCase()) && ((Character.isLetter(controls.getSetControls2()[4].getText().charAt(0)) && controls.getSetControls2()[4].getText().length()==1) ||
                (controls.getSetControls2()[4].getText().toUpperCase().equals("LEFT") || controls.getSetControls2()[4].getText().toUpperCase().equals("RIGHT") ||
                 controls.getSetControls2()[4].getText().toUpperCase().equals("UP") || controls.getSetControls2()[4].getText().toUpperCase().equals("DOWN")))){
                    controls.getControlsUsed().remove(controls2[4]);
                    controls2[4] = controls.getSetControls2()[4].getText().toUpperCase();
                    controls.getControlsUsed().add(controls2[4]);
                }else if(controls.getSetControls2()[4].getText().toUpperCase().equals(controls2[4])){
                }
                else if(controls.getControlsUsed().contains(controls.getSetControls2()[4].getText().toUpperCase())){
                    controls.getSetControls2()[4].setText("Already Used");
                }
                else controls.getSetControls2()[4].setText("Invalid");
            }
        });
    //reset texts
        controls.getSetControls2()[0].setOnMouseClicked(e ->{
            for (int i=0; i<5; i++){
                controls.getSetControls1()[i].setText(controls1[i]);
                controls.getSetControls2()[i].setText(controls2[i]);
            }
            controls.getSetControls2()[0].setText("");
        });
        controls.getSetControls2()[1].setOnMouseClicked(e ->{
            for (int i=0; i<5; i++){
                controls.getSetControls1()[i].setText(controls1[i]);
                controls.getSetControls2()[i].setText(controls2[i]);
            }
            controls.getSetControls2()[1].setText("");
        });
        controls.getSetControls2()[2].setOnMouseClicked(e ->{
            for (int i=0; i<5; i++){
                controls.getSetControls1()[i].setText(controls1[i]);
                controls.getSetControls2()[i].setText(controls2[i]);
            }
            controls.getSetControls2()[2].setText("");
        });
        controls.getSetControls2()[3].setOnMouseClicked(e ->{
            for (int i=0; i<5; i++){
                controls.getSetControls1()[i].setText(controls1[i]);
                controls.getSetControls2()[i].setText(controls2[i]);
            }
            controls.getSetControls2()[3].setText("");
        });
        controls.getSetControls2()[4].setOnMouseClicked(e ->{
            for (int i=0; i<5; i++){
                controls.getSetControls1()[i].setText(controls1[i]);
                controls.getSetControls2()[i].setText(controls2[i]);
            }
            controls.getSetControls2()[4].setText("");
        });
    //stage & scene
        Scene scene = new Scene(main);
        primaryStage.setTitle("Game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
    public void spawn(GamePane game){
    if (gameRunning){
        //select random item
            int itemNumber = (int)(Math.random()*8);
            PowerUp powerup = new PowerUp(itemNumber);
            ImageView view = powerup.getItemImage();
        //select random platform
            int platformIndex = 0;
            if (settings.getMap().equals(settings.getMapList()[1])){
                platformIndex = (int)(Math.random()*(game.getPlatformList().length-10));
            }else{
                platformIndex = (int)(Math.random()*game.getPlatformList().length);
            }
        //select random position on platform
            double positionOnPlatform = Math.random()*game.getPlatformList()[platformIndex].getPlatformImage().getFitWidth();       
            view.setX(game.getPlatformList()[platformIndex].getPlatformImage().getX() + positionOnPlatform - 20);
            switch(itemNumber){
                case 0:view.setY(game.getPlatformList()[platformIndex].getPlatformImage().getY() + 118);break;
                case 1:view.setY(game.getPlatformList()[platformIndex].getPlatformImage().getY() + 118);break;
                case 2:view.setY(game.getPlatformList()[platformIndex].getPlatformImage().getY() + 120);break;
                case 3:view.setY(game.getPlatformList()[platformIndex].getPlatformImage().getY() + 127);break;
                case 4:view.setY(game.getPlatformList()[platformIndex].getPlatformImage().getY() + 122);break;
                case 5:view.setY(game.getPlatformList()[platformIndex].getPlatformImage().getY() + 125);break;
                case 6:view.setY(game.getPlatformList()[platformIndex].getPlatformImage().getY() + 126);break;
                case 7:view.setY(game.getPlatformList()[platformIndex].getPlatformImage().getY() + 139);break;
            }
            
            game.getChildren().add(view);
            powerUpList.add(powerup);

            Rectangle hitbox = new Rectangle(view.getX(),view.getY() - (510-335),view.getFitWidth(),view.getFitHeight());
            hitbox.setId(powerup.getName());
            powerUpHitboxList.add(hitbox);
        }
    }
    
    public static void startGame(SequentialTransition startGameAnimation, FadeTransition[] increase, FadeTransition[] decrease, Label[] startLabels, FadeTransition fadeBackground, Rectangle shade){
    //make text more visible
        if (settings.getMap().equals(settings.getMapList()[2])){
            for (int i=0; i<startLabels.length; i++){
                startLabels[i].setTextFill(Color.WHITE);
            }
        }else{
            for (int i=0; i<startLabels.length; i++){
                startLabels[i].setTextFill(Color.BLACK);
            }
        }
        for (int i=0; i<increase.length; i++){
            increase[i] = new FadeTransition();
            increase[i].setDuration(Duration.millis(700));
            increase[i].setNode(startLabels[i]);
            increase[i].setFromValue(0);
            increase[i].setToValue(1);
        }
        for (int i=0; i<decrease.length; i++){
            decrease[i] = new FadeTransition();
            decrease[i].setDuration(Duration.millis(700));
            decrease[i].setNode(startLabels[i]);
            decrease[i].setFromValue(1);
            decrease[i].setToValue(0);
        }
        startGameAnimation.getChildren().addAll(increase[0],decrease[0],increase[1],decrease[1],increase[2],decrease[2],increase[3],decrease[3]);
        shade.setOpacity(0);
        fadeBackground.setDuration(Duration.millis(5600));
        fadeBackground.setNode(shade);
        fadeBackground.setFromValue(0.5);
        fadeBackground.setToValue(0);
    }
    
    public static void gameOver(Pane main,GamePane game, MainMenuPane mainMenu, SettingsPane settings, SequentialTransition startGameAnimation, FadeTransition fadeBackground, Rectangle shade){
    if (gameRunning){
            gameRunning=false;
            powerUpList.clear();
            powerUpHitboxList.clear();
            shade.setOpacity(0.5);
            displayRounds.setText("Rounds Remaining: Zero     Score: " + game.getScoreOne() + " - " + game.getScoreTwo());
            ImageView gameOver = new ImageView(new Image("finalprojectImages/Game_Over.png"));
            Button playAgain = new Button("Play Again");
            Button returnToMenu = new Button("Return To Menu");
            Label gameOutcome = new Label();
            gameOver.setLayoutX(330);
            gameOver.setLayoutY(100);
            gameOver.setFitWidth(350);
        //announce the outcome of the game
            if (game.getScoreOne()>game.getScoreTwo()){
                gameOutcome.setText("WINNER: PLAYER ONE");
            }else if (game.getScoreOne()<game.getScoreTwo()){
                if (game.getTankTwo().getName().equals("AITank"))
                    gameOutcome.setText("WINNER: COMPUTER");
                else gameOutcome.setText("WINNER: PLAYER TWO");
            }else{
                if (game.getTankOne().getHealth()>game.getTankTwo().getHealth()){
                    gameOutcome.setText("WINNER: PLAYER ONE");
                }else if(game.getTankOne().getHealth()<game.getTankTwo().getHealth()){
                    if (game.getTankTwo().getName().equals("AITank"))
                        gameOutcome.setText("WINNER: COMPUTER");
                    else gameOutcome.setText("WINNER: PLAYER TWO");
                }else gameOutcome.setText("TIE");
            }
            gameOutcome.setFont(new Font("Cooper Black",60));
            if (gameOutcome.getText().equals("TIE")){
                gameOutcome.setLayoutX(440);
            }else if(gameOutcome.getText().equals("")){
                gameOutcome.setLayoutX(460);
            }else{
                gameOutcome.setLayoutX(140);
            }
            gameOutcome.setLayoutY(300);
            gameOutcome.setMinSize(350,60);
            gameOutcome.setTextAlignment(TextAlignment.CENTER);
            Thread switchColors = new Thread(() -> {
                while (true){
                    try{
                        Platform.runLater(() -> {
                            if (count%2==0){
                                gameOutcome.setTextFill(Color.RED);
                            }else gameOutcome.setTextFill(Color.BLACK);
                            count++;
                        });
                        Thread.sleep(800);
                    } catch(InterruptedException ex){
                    }
                }
            });
            switchColors.start();
        //return to menu
            returnToMenu.setFont(new Font("Cooper Black", 15));
            returnToMenu.setLayoutX(410);
            returnToMenu.setLayoutY(380);
            returnToMenu.setPrefSize(180,40);
            returnToMenu.setOnAction(e -> {
                game.getPlayPane().getChildren().removeAll(shade,gameOver,gameOutcome,playAgain,returnToMenu);
                switchColors.stop();
                removeObjects();
                main.getChildren().clear();
                main.getChildren().addAll(mainMenu,menuBar);
            });
        //play again
            playAgain.setFont(new Font("Cooper Black", 15));
            playAgain.setLayoutX(410);
            playAgain.setLayoutY(425);
            playAgain.setPrefSize(180,40);
            playAgain.setOnAction(e -> {
                switchColors.stop();
                main.getChildren().removeAll(gameOutcome, gameOver, playAgain, returnToMenu);
                settings.setRoundsRemaining(settings.getNumberOfRounds());
                game.setScoreOne(0);
                game.setScoreTwo(0);
                displayRounds.setText("Rounds Remaining: " + settings.getRoundsRemaining() + "     Score: " + game.getScoreOne() + " - " + game.getScoreTwo());
                playAgain();
            });

            main.getChildren().removeAll(shade,gameOver,gameOutcome,playAgain,returnToMenu);
            main.getChildren().addAll(shade,gameOver,gameOutcome,playAgain,returnToMenu);
        }
    }
    
    public static void playAgain(){
        gameRunning = false;
        game.reset();
        tankOneAlive = true;
        tankTwoAlive = true;
        startGameAnimation.play();
        fadeBackground.play();
        try{
            main.getChildren().add(shade);
        }catch (Exception ex){
        }        
         if (!settings.getTimer().equals(settings.getTimerList()[0])){
            timerInSeconds = Integer.parseInt(settings.getTimer().charAt(0) + "")*60 + Integer.parseInt(settings.getTimer().substring(2));
            displayTimer.setText(timerInSeconds + "");
        } else{
            timerInSeconds = 1;
            displayTimer.setText(settings.getTimer());
        }
    //reset tanks
        game.getPlayPane().getChildren().removeAll(game.getTankOne(), game.getTankTwo());
        try{
            game.getTankOne().getAni().stop();
            game.getTankTwo().getAni().stop();
        }catch(Exception ex){
        }
        if (settings.getOpponent().getFill().equals(Color.RED)){
            game.setTankTwo(new TankPane(settings.getColorStringTwo(), 2));
        } else{
            game.setTankTwo(new AiTank(settings.getColorStringTwo(), 2));
        }
        game.setTankOne(new TankPane(settings.getColorStringOne(), 1));
        game.getPlayPane().getChildren().addAll(game.getTankOne(), game.getTankTwo());
    //put rocks in front of tanks
        if(settings.getMap().equals(settings.getMapList()[3])){
            game.getPlayPane().getChildren().removeAll(game.rock1,game.rock2,game.rock3);
            game.getPlayPane().getChildren().addAll(game.rock1,game.rock2,game.rock3);
        }
    //spawn one platform higher in hell map
        if (settings.getMap().equals(settings.getMapList()[1])){
            game.getTankOne().getTankImage().setY(235);
            game.getTankTwo().getTankImage().setY(235);
        }else{
            game.getTankOne().getTankImage().setY(335);
            game.getTankTwo().getTankImage().setY(335);
        }
        game.getTankOne().getTankImage().setX(20);
        game.getTankTwo().getTankImage().setX(900);
        startGameAnimation.setOnFinished(f -> {
           gameRunning = true;
        });
        fadeBackground.setOnFinished(f -> {
            main.getChildren().remove(shade);
        });
    }
    
    public static void playGameOver() {
        gameOver(main,game,mainMenu,settings,startGameAnimation,fadeBackground,shade);
    }
    
    public static void removeObjects(){
    //remove the objects only found in specific maps
        gameRunning = false;
            game.getPlayPane().getChildren().removeAll(game.getTankOne(), game.getTankTwo());
        //remove lava floor
            if (settings.getMap().equals(settings.getMapList()[1])){
                for (int i=0; i<game.getLavaImages().length; i++){
                    game.getPlayPane().getChildren().remove(game.getLavaImages()[i]);
                }
        //remove rocks
            }else if(settings.getMap().equals(settings.getMapList()[3])){
                game.getPlayPane().getChildren().removeAll(game.rock1,game.rock2,game.rock3);
            }
        //remove ice platforms
            if (settings.getMap().equals(settings.getMapList()[4])){
                for (int i=0; i<game.getIceImages().length; i++){
                    game.getPlayPane().getChildren().remove(game.getIceImages()[i]);
                    try{
                        game.getPlayPane().getChildren().add(game.getPlatformList()[i].getPlatformImage());
                    }catch(Exception ex){
                    }
                }
            }
    }
}