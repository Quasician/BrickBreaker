package breakout;

import javafx.scene.paint.Paint;

/**
 * @author Thomas Chemmanoor
 * The purpose of the Ball class is to have an object with the same behaviors and attributes as a ball in breakout.
 * This class is well designed since it shows the simple, atomic methodology of methods inside of a class
 * The only situation that the class would fail would be if the user fails to put the required arguments in a constructor when instantiating an object of this type.
 * This class depends on the package breakout and the class GameObject.
 * To use this class simply create an object by putting the required arguments inside of the constructor.
 */
public class Ball extends GameObject{

    public Ball(int x, int y, int width, int height, Paint color) {
        super(x, y, width, height);
        super.setFill(color);
    }
    // decreases the balls hp
    public void decreaseHP()
    {
        hp--;
    }

    // gets the ball's hp
    public int getHP()
    {
        return hp;
    }

}
