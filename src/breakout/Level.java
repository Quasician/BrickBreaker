package breakout;

import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

abstract public class Level {
    protected int width;
    protected int height;
    protected Paint background;
    protected int score;
    protected int maxScore;
    protected Brick[] brickArray;


    public Level(int width, int height, Paint background)
    {
        this.width = width;
        this.height = height;
        this.background = background;
    }

    public abstract Scene setUpLevel();
    public abstract boolean step (double elapsedTime, Text text);


    // What to do each time a key is pressed
//    private void handleMouseInput (double x, double y) {
//        if (myGrower.contains(x, y)) {
//            myGrower.setScaleX(myGrower.getScaleX() * GROWER_RATE);
//            myGrower.setScaleY(myGrower.getScaleY() * GROWER_RATE);
//        }
//    }

}
