package breakout;

public class levelInfo {
    private ringInfo[] rings;
    private int ringNumber;
    public levelInfo(int ringNumber)
    {
        rings = new ringInfo[ringNumber];
        this.ringNumber = ringNumber;
    }

    public int getRingNumber()
    {
        return ringNumber;
    }

    public ringInfo[] getRingArray()
    {
        return rings;
    }
}
