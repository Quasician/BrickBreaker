package breakout;


import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.awt.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class Level {
    protected int width;
    protected int height;
    protected Paint background;
    protected int score;
    protected int maxScore;
    protected ArrayList <Brick> brickList;


    public static final Paint HIGHLIGHT = Color.OLIVEDRAB;
    public static final String BALL_IMAGE = "ball.gif";
    public static int BALL_SPEED_X = 120;
    public static int BALL_SPEED_Y = -160;
    public static int BALL_SPEED_TOTAL = 200;
    public static final int BALL_DIAMETER = 12;
    public static final Paint PADDLE_COLOR = Color.PLUM;
    public static final int PADDLE_WIDTH = 75;
    public static final int PADDLE_HEIGHT = 50/3;
    public static final int PADDLE_SPEED = 5;
    public static final double PADDLE_CORNER_THRESHOLD = BALL_DIAMETER/1.5;
    public static final Paint GROWER_COLOR = Color.BISQUE;
    public static final double GROWER_RATE = 1.1;
    public static final int GROWER_SIZE = 50;
    public static final int NUMBER_OF_BRICKS = 20;
    private Text scoreHUD;
    private Text livesHUD;
    private int levelNumber;

    private Paddle myPADDLE;
    private Ball ball;
    private Group root;
    private levelInfo[] levels;

    public Level(int levelNumber, int width, int height, Paint background, Group root, Ball ball, Paddle paddle, Text scoreHUD, Text livesHUD, ArrayList <Brick> brickList, levelInfo[] levels) {
        this.levelNumber = levelNumber;
        this.width = width;
        this.height = height;
        this.background = background;
        this.root = root;
        this.ball = ball;
        this.myPADDLE = paddle;
        this.scoreHUD = scoreHUD;
        this.brickList = brickList;
        this.livesHUD = livesHUD;
        this.levels = levels;
        setUpLevel(levelNumber-1);
    }


    public void setUpLevel(int levelNumber) {
        // create one top level collection to organize the things in the scene
        for (int i = 0; i<levels[levelNumber].getRingArray().length;i++)
        {
            //BorderPane rotatePane = new BorderPane();
            //rotatePane.setPrefSize(width, height*.7);
            Group rotateGroup1 = new Group();

            ArrayList <Brick> ringList = createBricksInCircles (levels[levelNumber].getRingArray()[i]);
            rotateGroup1.getChildren().addAll(ringList);
            //rotatePane.setCenter(rotateGroup1);
            //root.getChildren().add(rotatePane);
            brickList.addAll(ringList);

            RotateTransition grouprt1 = new RotateTransition(Duration.millis(3000), rotateGroup1);
            grouprt1.setInterpolator(Interpolator.LINEAR);
            grouprt1.setByAngle(360);
            grouprt1.setCycleCount(Animation.INDEFINITE);
            grouprt1.setAutoReverse(false);
            grouprt1.play();
            grouprt1.setRate(.3);
            if(levels[levelNumber].getRingArray()[i].getCCW() == 1)
            {
                grouprt1.setRate(-.3);
            }
            root.getChildren().add(rotateGroup1);
        }

        root.getChildren().add(ball);
        root.getChildren().add(myPADDLE);
        root.getChildren().add(scoreHUD);
        root.getChildren().add(livesHUD);

//
//
//        BorderPane rotatePane1 = new BorderPane();
//        BorderPane rotatePane2 = new BorderPane();
//        rotatePane1.setPrefSize(width, height*.7);
//        rotatePane2.setPrefSize(width, height*.7);
//        Group rotateGroup1 = new Group();
//        Group rotateGroup2 = new Group();
//
//
//
//        ArrayList<Brick> innerCircle = createBricksInCircles (300, 250, 4, 50, 15, 15, 2);
//        rotateGroup1.getChildren().addAll(innerCircle);
//        rotatePane1.setCenter(rotateGroup1);
//
//
//        ArrayList<Brick> outerCircle = createBricksInCircles (300, 250, 16, 150, 15, 15, 3);
//        rotateGroup2.getChildren().addAll(outerCircle);
//        rotatePane2.setCenter(rotateGroup2);
//
//        brickList.addAll(outerCircle);
//        brickList.addAll(innerCircle);
//
//        RotateTransition grouprt1 = new RotateTransition(Duration.millis(3000), rotateGroup1);
//        grouprt1.setInterpolator(Interpolator.LINEAR);
//        grouprt1.setByAngle(360);
//        grouprt1.setCycleCount(Animation.INDEFINITE);
//        grouprt1.setAutoReverse(false);
//        grouprt1.play();
//
//        RotateTransition grouprt2 = new RotateTransition(Duration.millis(3000), rotateGroup2);
//        grouprt2.setInterpolator(Interpolator.LINEAR);
//        grouprt2.setByAngle(360);
//        grouprt2.setCycleCount(Animation.INDEFINITE);
//        grouprt2.setRate(-.3);
//        grouprt2.play();
//
//
//        root.getChildren().add(ball);
//        root.getChildren().add(myPADDLE);
//        root.getChildren().add(rotatePane1);
//        root.getChildren().add(rotatePane2);
//        root.getChildren().add(scoreHUD);
//        root.getChildren().add(livesHUD);
        //root.getChildren().add(rect);
        // create a place to see the shapes

        return;
    }


    public ArrayList createBricksInCircles (ringInfo ring)
    {
        ArrayList<Brick> list = new ArrayList<Brick>();
        for( int i = 0; i<ring.getCircleNumber(); i++)
        {
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
