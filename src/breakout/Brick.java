package breakout;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * @author Thomas Chemmanoor
 * The purpose of the Brick class is to have an object with the same behaviors and attributes as a brick in breakout.
 * The only situation that the class would fail would be if the user fails to put the required arguments in a constructor when instantiating an object of this type.
 * This class depends on the package breakout and the class GameObject.
 * To use this class simply create an object by putting the required arguments inside of the constructor.
 */
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

    //decreases the hp of the brick and updates the color of the brick
    public void decreaseHP(){
        hp--;
        updateColor();
    }

    // gets the brick's hp
    public int getHP()
    {
        return hp;
    }

    //depending on the hp of the brick, its color will change
    public void updateColor() {
        if(hp == 3) {
            setColor(Color.BLACK);
        }
        else if(hp == 2) {
            setColor(Color.SILVER);
        }
        else if(hp == 1) {
            setColor(Color.GREEN);
        }
        else if(hp == 0) {
            setColor(Color.TRANSPARENT);
        }
    }

    // set the brick's color
    public void setColor(Paint color) {
        this.color = color;
        super.setFill(color);
    }
}
