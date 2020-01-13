package breakout;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.awt.*;

public class Brick extends GameObject {


    public Brick (int x, int y, int width, int height, int hp){
        super(x, y, width, height);
        x_init = x;
        y_init = y;
        this.width = width;
        this.height = height;
        this.hp = hp;
        updateColor();
    }

    public void decreaseHP()
    {
        hp--;
        updateColor();
    }

    public int getHP()
    {
        return hp;
    }

    public void updateColor()
    {
        if(hp == 3)
        {
            setColor(Color.BLACK);
        }
        else if(hp == 2)
        {
            setColor(Color.SILVER);
        }
        else if(hp == 1)
        {
            setColor(Color.GREEN);
        }
        else if(hp == 0)
        {
            setColor(Color.TRANSPARENT);
        }
    }

    public void setColor(Paint color)
    {
        this.color = color;
        super.setFill(color);
    }
}
