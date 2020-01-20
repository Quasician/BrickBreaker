package breakout;

/**
 * @author Thomas Chemmanoor
 * The purpose of the RingInfo class is to have an object that can be loaded up with all the pertinent data elements that the level text files will specify for each ring of bricks.
 * A situation that the class would fail would be if the user fails to put the required arguments in a constructor when instantiating an object of this type. This could happen if a level text file
 * is not set correctly.
 * This class depends on the package breakout.
 * To use this class simply create an object by putting the required arguments inside of the constructor.
 */
public class RingInfo {
    private int x_init;
    private int y_init;
    private int circleNumber;
    private int radiusOfPath;
    private int brickWidth;
    private int brickHeight;
    private int hp;
    private int ccw;

    public RingInfo(int x_init, int y_init, int circleNumber, int radiusOfPath, int brickWidth, int brickHeight, int hp, int ccw)
    {
        this.x_init = x_init;
        this.y_init = y_init;
        this.circleNumber = circleNumber;
        this.radiusOfPath = radiusOfPath;
        this.brickWidth = brickWidth;
        this.brickHeight = brickHeight;
        this.hp = hp;
        this.ccw = ccw;
    }

    public int getX()
    {
        return x_init;
    }

    public int getY()
    {
        return y_init;
    }

    public int getCircleNumber()
    {
        return circleNumber;
    }

    public int getRadiusOfPath()
    {
        return radiusOfPath;
    }

    public int getBrickWidth() {  return brickWidth; }

    public int getBrickHeight()
    {
        return brickHeight;
    }

    public int getHp()
    {
        return hp;
    }

    public int getCCW()
    {
        return ccw;
    }
}
