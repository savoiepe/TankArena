package finalproject;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class InventoryPane extends StackPane{
    private ImageView inventoryImage = new ImageView(new Image(("/finalprojectImages/Tank_Inventory.png")));
    private ImageView powerUpImage;
    public InventoryPane(){
        inventoryImage.setFitHeight(50);
        inventoryImage.setFitWidth(50);
        this.getChildren().add(inventoryImage);
    }
    
    public ImageView getInventoryImage(){
        return inventoryImage;
    }
    
    public void setInventoryImage(ImageView inventoryImage){
        this.inventoryImage = inventoryImage;
    }
    
    public void setPowerUp(PowerUp powerUp){
        this.getChildren().clear();
        this.getChildren().add(inventoryImage);
        if(powerUp != null){
            this.getChildren().add(powerUp.getItemImage());
        }
    }
}