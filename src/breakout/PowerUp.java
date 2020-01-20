package breakout;

import javafx.scene.paint.Color;

/**
 * @author Thomas Chemmanoor
 * The purpose of the PowerUp class is to have an object with the same behaviors and attributes as a power up in breakout.
 * The PowerUp class conatins a boolean array that designates what type of power up it is.
 * The only situation that the class would fail would be if the user fails to put the required arguments in a constructor when instantiating an object of this type.
 * This class depends on the package breakout and the class GameObject.
 * To use this class simply create an object by putting the required arguments inside of the constructor.
 */
public class PowerUp extends GameObject{

    public final static int POWERUP_WIDTH = 15;
    private boolean speedUpPaddle;
    private boolean slowDownBalls;
    private boolean increasedPaddle;

    public PowerUp(int x_init, int y_init, int randomType) {
        super(x_init, y_init, POWERUP_WIDTH, POWERUP_WIDTH);
        this.x_init = x_init;
        this.y_init = y_init;
        int powerUpType = randomType % 3;
        initType(powerUpType);
    }

    // based on a number from 0 to 2, the power up's type changes
    public void initType(int powerUpType) {
        if(powerUpType == 0) {
            speedUpPaddle = true;
            super.setFill(Color.ORANGE);
        }
        else if (powerUpType ==1) {
            slowDownBalls = true;
            super.setFill(Color.RED);
        }
        else if (powerUpType ==2) {
            increasedPaddle = true;
            super.setFill(Color.BLUE);
        }
    }

    //makes power up invisible (destroys it)
    public void destroyPowerUp()
    {
        super.setFill(Color.TRANSPARENT);
    }

    //get the power up's boolean array to check what type of power up it is
    public boolean[] getTypeArray()
    {
        boolean[] type = new boolean[3];
        type[0] = speedUpPaddle;
        type[1] = slowDownBalls;
        type[2] = increasedPaddle;
        return type;
    }
}
