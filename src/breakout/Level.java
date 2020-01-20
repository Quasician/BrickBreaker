package breakout;


import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.ArrayList;

public class Level {
    protected int width;
    protected int height;
    protected Paint background;
    protected ArrayList <Brick> brickList;

    private Group root;
    private levelInfo[] levels;

    public Level(int levelNumber, Group root, ArrayList <Brick> brickList, levelInfo[] levels) {
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


    public ArrayList createBricksInCircles (ringInfo ring) {
        ArrayList<Brick> list = new ArrayList<Brick>();
        for( int i = 0; i<ring.getCircleNumber(); i++) {
            int x = (int)(ring.getX() + ring.getRadiusOfPath() * Math.cos(2 * Math.PI * i / ring.getCircleNumber()));
            int y = (int)(ring.getY() + ring.getRadiusOfPath() * Math.sin(2 * Math.PI * i / ring.getCircleNumber()));
            //System.out.println(x + " " + y + " " + radiusBall);s
            //Circle c = new Circle (x, y, radiusBall, Color.RED);
            //System.out.println(x);
            list.add(new Brick(x-ring.getBrickWidth()/2,y-ring.getBrickHeight()/2, ring.getBrickWidth(), ring.getBrickHeight(), ring.getHp()));
        }
        return list;
    }
}
