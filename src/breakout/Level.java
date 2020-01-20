package breakout;


import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.scene.Group;
import javafx.util.Duration;
import java.util.ArrayList;


/**
 * @author Thomas Chemmanoor
 * The purpose of the Level class is to have an object that creates a game level when instantiated.
 * It loads the correct levelInfo object from the levelInfo array and adds those bricks dictated by the levelInfo object into the current group.
 * The only situation that the class would fail would be if the user fails to put the required arguments in a constructor when instantiating an object of this type.
 * This class is also assuming that the leveloInfo array is not null.
 * This class depends on the package breakout.
 * To use this class simply create an object by putting the required arguments inside of the constructor.
 */
public class Level {
    public Level(int levelNumber, Group root, ArrayList <Brick> brickList, LevelInfo[] levels) {
        for (int i = 0; i<levels[levelNumber-1].getRingArray().length;i++) {
            Group rotateGroup1 = new Group();
            ArrayList <Brick> ringList = createBricksInCircles (levels[levelNumber-1].getRingArray()[i]);
            rotateGroup1.getChildren().addAll(ringList);
            brickList.addAll(ringList);

            RotateTransition grouprt1 = new RotateTransition(Duration.millis(3000), rotateGroup1);
            grouprt1.setInterpolator(Interpolator.LINEAR);
            grouprt1.setByAngle(360);
            grouprt1.setCycleCount(Animation.INDEFINITE);
            grouprt1.setAutoReverse(false);
            grouprt1.play();
            grouprt1.setRate(.3);
            if(levels[levelNumber-1].getRingArray()[i].getCCW() == 1) {
                grouprt1.setRate(-.3);
            }
            root.getChildren().add(rotateGroup1);
        }
    }


    /**
     * createBricksInCircles creates a ring of bricks at certain x & y coordinates around a central point.
     * All the method takes in is a ringInfo object and all variables the method needs are stored inside this object.
     * @param ring
     * @return ArrayList of Bricks
     */
    public ArrayList createBricksInCircles (RingInfo ring) {
        ArrayList<Brick> list = new ArrayList<Brick>();
        for( int i = 0; i<ring.getCircleNumber(); i++) {
            int x = (int)(ring.getX() + ring.getRadiusOfPath() * Math.cos(2 * Math.PI * i / ring.getCircleNumber()));
            int y = (int)(ring.getY() + ring.getRadiusOfPath() * Math.sin(2 * Math.PI * i / ring.getCircleNumber()));
            list.add(new Brick(x-ring.getBrickWidth()/2,y-ring.getBrickHeight()/2, ring.getBrickWidth(), ring.getBrickHeight(), ring.getHp()));
        }
        return list;
    }
}
