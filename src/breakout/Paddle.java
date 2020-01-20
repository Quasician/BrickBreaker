package breakout;

import javafx.scene.paint.Paint;

/**
 * @author Thomas Chemmanoor
 * The purpose of the Paddle class is to have an object with the same behaviors and attributes as a paddle in breakout.
 * However, the paddle's hp is the player's hp.
 * The only situation that the class would fail would be if the user fails to put the required arguments in a constructor when instantiating an object of this type.
 * This class depends on the package breakout and the class GameObject.
 * To use this class simply create an object by putting the required arguments inside of the constructor.
 */
public class Paddle extends GameObject{
    public Paddle(int x, int y, int width, int height, int hp, Paint color) {
        super(x, y, width, height);
        this.width = width;
        this.height = height;
        super.setFill(color);
        this.hp = hp;
    }

    // decreases the paddle/player's hp
    public void decreaseHP()
    {
        hp--;
    }

    // increases the paddle/player's hp for the cheat key
    public void increaseHP()
    {
        hp++;
    }

    // get paddle's hp
    public int getHP()
    {
        return hp;
    }

}
