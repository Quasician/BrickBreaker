package breakout;

import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.awt.*;

public class brick extends Rectangle {
    private int hp;
    private int x_init;
    private int y_init;
    private int width;
    private int height;
    private Paint color;


    public brick (int x, int y, int width, int height, int hp, Paint color){
        super(x, y, width, height);
        super.setFill(color);
        x_init = x;
        y_init = y;
        this.width = width;
        this.height = height;
        this.hp = hp;
        this.color = color;
    }

    public void decreaseHP()
    {
        hp--;
    }

    public int getHP()
    {
        return hp;
    }
}
