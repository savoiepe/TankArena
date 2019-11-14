package TankArenaSourceFiles;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HealthBar {
    private ImageView healthImage = new ImageView(new Image(("/finalprojectImages/Tank_HealthBar.png")));
    private int healthAmount = 3;
    private boolean armor = false;
    
    public HealthBar(){  
    }

    public ImageView getHealthImage() {
        return healthImage;
    }

    public void setHealthImage(ImageView healthImage) {
        this.healthImage = healthImage;
    }

    public int getHealthAmount() {
        return healthAmount;
    }

    public void setHealthAmount(int healthAmount) {
        this.healthAmount = healthAmount;
    }

    public boolean isArmor() {
        return armor;
    }

    public void setArmor(boolean armor) {
        this.armor = armor;
    }
    
    
}
