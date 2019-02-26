package finalproject;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

public class PowerUp {
    private String itemName = "";
    private int itemNumber = 0;
    private ImageView itemImage = new ImageView();
    private ImageView projectileImage = new ImageView();
    private Point2D coordinates;
    
    public PowerUp(){
    }
    
    public PowerUp(int itemNumber){
        this.itemNumber = itemNumber;
        switch (itemNumber){
            //NEW
            case 0: this.itemName = "bounce"; this.itemImage = new ImageView("finalprojectImages/Powerup_Bounce.png"); this.projectileImage = new ImageView("finalprojectImages/Projectile_Bounce.png"); break;
            case 1: this.itemName = "damage"; this.itemImage = new ImageView("finalprojectImages/Powerup_Damage.png");this.projectileImage = new ImageView("finalprojectImages/Projectile_Damage.png"); break;
            case 2: this.itemName = "fire"; this.itemImage = new ImageView("finalprojectImages/Powerup_Fire.png");this.projectileImage = new ImageView("finalprojectImages/Projectile_Fire.png");break;
            case 3: this.itemName = "health"; this.itemImage = new ImageView("finalprojectImages/Powerup_Health.png"); break;
            case 4: this.itemName = "ice"; this.itemImage = new ImageView("finalprojectImages/Powerup_Ice.png"); this.projectileImage = new ImageView("finalprojectImages/Projectile_ice.png");break;
            case 5: this.itemName = "knockback"; this.itemImage = new ImageView("finalprojectImages/Powerup_Knockback.png");this.projectileImage = new ImageView("finalprojectImages/Projectile_Knockback.png"); break;
            case 6: this.itemName = "armor"; this.itemImage = new ImageView("finalprojectImages/Powerup_Armor.png"); break;
            case 7: this.itemName = "random"; this.itemImage = new ImageView("finalprojectImages/Powerup_Random.png"); break;
        }
    }
    
    public ImageView getItemImage(){
        return itemImage;
    }
    public ImageView getProjectileImage(){
        return projectileImage;
    }
    public String getName(){
        return itemName;
    }
}
