package breakout;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 * @author Thomas Chemmanoor
 * The purpose of the GameObject class is to be the root class for all objects in the game.
 * Therefore, all objects in the game should at least have access to an individual hp, x & y starting coordinate, width & height, and color.
 * The only situation that the class would fail would be if the user fails to put the required arguments in a constructor when instantiating an object of this type.
 * This class depends on the package breakout and the class Rectangle.
 * To use this class simply create an object by putting the required arguments inside of the constructor (makes it a sub-class of rectangle).
 */
public class GameObject extends Rectangle {
    protected int hp;
    protected int x_init;
    protected int y_init;
    protected int width;
    protected int height;
    protected Paint color;

    // constructor for root class
    public GameObject(int x, int y, int width, int height) {
        super(x, y, width, height);
    }
}
