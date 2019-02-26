package finalproject;

import javafx.scene.control.Button;
import javafx.scene.text.Font;

public class Arrow extends Button{
    public Arrow(String name, double width, double height, Font font){
        this.setText(name);
        this.setWidth(width);
        this.setHeight(height);
        this.setFont(font);
    }
}
