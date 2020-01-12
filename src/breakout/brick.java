package breakout;

import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;

import java.awt.*;

public class brick extends Circle{
    private int hp;
    private double x_init;
    private double y_init;
    private double radius;
    private Paint color;


    public brick (double x, double y, double rad, int hp, Paint color){
        super(x, y, rad, color);
        x_init = x;
        y_init = y;
        radius = rad;
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
