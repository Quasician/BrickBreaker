package breakout;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class PowerUp extends GameObject{

    public final static int POWERUP_WIDTH = 15;
    private boolean speedUpPaddle;
    private boolean slowDownBalls;
    private boolean increasedPaddle;

    public PowerUp(int x_init, int y_init, int type) {
        super(x_init, y_init, POWERUP_WIDTH, POWERUP_WIDTH);
        this.x_init = x_init;
        this.y_init = y_init;
        int powerUpType = type % 3;
        initType(powerUpType);
    }

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

    public void destroyPowerUp()
    {
        super.setFill(Color.TRANSPARENT);
    }

    public boolean[] getTypeArray()
    {
        boolean[] type = new boolean[3];
        type[0] = speedUpPaddle;
        type[1] = slowDownBalls;
        type[2] = increasedPaddle;
        return type;
    }
}
