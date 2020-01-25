package breakout;

/**
 * @author Thomas Chemmanoor
 * The purpose of the LevelInfo class is to have an object that can be loaded up with all the pertinent data elements that the level text files will specify an entire level.
 * This class is well designed since it shows the simple, atomic methodology of methods inside of a class
 * A situation that the class would fail would be if the user fails to put the required arguments in a constructor when instantiating an object of this type. This could happen if a level text file
 * is not set correctly.
 * This class depends on the package breakout.
 * To use this class simply create an object by putting the required arguments inside of the constructor.
 */
public class LevelInfo {
    private RingInfo[] rings;
    private int ringNumber;
    public LevelInfo(int ringNumber)
    {
        rings = new RingInfo[ringNumber];
        this.ringNumber = ringNumber;
    }

    public int getRingNumber()
    {
        return ringNumber;
    }

    public RingInfo[] getRingArray()
    {
        return rings;
    }
}
