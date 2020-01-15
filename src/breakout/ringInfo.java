package breakout;

public class ringInfo {
    private int x_init;
    private int y_init;
    private int circleNumber;
    private int radiusOfPath;
    private int brickWidth;
    private int brickHeight;
    private int hp;

    public ringInfo(int x_init, int y_init, int circleNumber, int radiusOfPath, int brickWidth, int brickHeight, int hp)
    {
        this.x_init = x_init;
        this.y_init = y_init;
        this.circleNumber = circleNumber;
        this.radiusOfPath = radiusOfPath;
        this.brickWidth = brickWidth;
        this.brickHeight = brickHeight;
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

    public int getBrickWidth()
    {
        return brickWidth;
    }

    public int getBrickHeight()
    {
        return brickHeight;
    }

    public int getHp()
    {
        return hp;
    }
}
