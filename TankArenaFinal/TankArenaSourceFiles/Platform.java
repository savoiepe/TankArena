package TankArenaSourceFiles;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Platform {
    private ImageView platformImage = new ImageView(new Image("finalprojectImages/platforms.png"));
    private double[] coordinates = new double[3];
    
    public Platform(double width, double height){
        platformImage.setFitWidth(width);
        platformImage.setFitHeight(height);
    }
    
    public ImageView getPlatformImage(){
        return platformImage;
    }
    
    public void setCoordinates(double x1, double x2, double y){
        coordinates[0] = x1;
        coordinates[1] = x2;
        coordinates[2] = y;
    }
}
