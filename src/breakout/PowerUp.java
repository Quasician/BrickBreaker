package breakout;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class PowerUp extends GameObject{
    public final static int POWERUP_WIDTH = 15;
    private boolean speedUpPaddle;
    private boolean slowDownBalls;
    private boolean increasedPaddle;
    public PowerUp(int x_init, int y_init, Paint color, int type)
    {
        super(x_init, y_init, POWERUP_WIDTH, POWERUP_WIDTH);
        super.setFill(color);
        this.x_init = x_init;
        this.y_init = y_init;
        hp = 1;
        int powerUpType = type % 3;
        initType(powerUpType);
    }
    public void initType(int powerUpType)
    {
        if(powerUpType == 0)
        {
            speedUpPaddle = true;
        }
        else if (powerUpType ==1)
        {
            slowDownBalls = true;
        }
        else if (powerUpType ==2)
        {
            increasedPaddle = true;
        }
    }
    public void destroyPowerUp()
    {
        super.setFill(Color.TRANSPARENT);
    }

    public void decreaseHP()
    {
        hp--;
    }

}
