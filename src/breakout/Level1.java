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

public class Level1 extends Level{

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
    public static Text scoreHUD;

    private Paddle myPADDLE;
    private Ball ball;

    public Level1(int width, int height, Paint background) {
        super(width, height, background);
        super.brickArray = new Brick[NUMBER_OF_BRICKS];
    }

    @Override
    public Scene setUpLevel() {
        // create one top level collection to organize the things in the scene
        Group root = new Group();
        BorderPane rotatePane1 = new BorderPane();
        BorderPane rotatePane2 = new BorderPane();
        rotatePane1.setPrefSize(width, height*.7);
        rotatePane2.setPrefSize(width, height*.7);
        Group rotateGroup1 = new Group();
        Group rotateGroup2 = new Group();

        ball = new Ball(width / 2 - PADDLE_HEIGHT / 2 ,(int)(3.5* height / 5) ,BALL_DIAMETER, BALL_DIAMETER, Color.BLACK);
        ball.setArcHeight(BALL_DIAMETER);
        ball.setArcWidth(BALL_DIAMETER);

        myPADDLE = new Paddle(width / 2 - PADDLE_WIDTH / 2, 4* height / 5, PADDLE_WIDTH, PADDLE_HEIGHT, 3, PADDLE_COLOR);

        scoreHUD = new Text();

        ArrayList<Brick> innerCircle = createBricksInCircles (300, 250, 4, 50, 15, 15, 2);
        rotateGroup1.getChildren().addAll(innerCircle);
        rotatePane1.setCenter(rotateGroup1);


        ArrayList<Brick> outerCircle = createBricksInCircles (300, 250, 16, 150, 15, 15, 3);
        rotateGroup2.getChildren().addAll(outerCircle);
        rotatePane2.setCenter(rotateGroup2);

        innerCircle.addAll(outerCircle);
        brickArray = innerCircle.toArray(new Brick[0]);

        RotateTransition grouprt1 = new RotateTransition(Duration.millis(3000), rotateGroup1);
        grouprt1.setInterpolator(Interpolator.LINEAR);
        grouprt1.setByAngle(360);
        grouprt1.setCycleCount(Animation.INDEFINITE);
        grouprt1.setAutoReverse(false);
        grouprt1.play();

        RotateTransition grouprt2 = new RotateTransition(Duration.millis(3000), rotateGroup2);
        grouprt2.setInterpolator(Interpolator.LINEAR);
        grouprt2.setByAngle(360);
        grouprt2.setCycleCount(Animation.INDEFINITE);
        grouprt2.setRate(-.3);
        grouprt2.play();


        root.getChildren().add(ball);
        root.getChildren().add(myPADDLE);
        root.getChildren().add(rotatePane1);
        root.getChildren().add(rotatePane2);
        root.getChildren().add(scoreHUD);
        //root.getChildren().add(rect);
        // create a place to see the shapes
        Scene scene = new Scene(root, width, height, background);
        // respond to input
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        //scene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
        return scene;
    }


    public ArrayList createBricksInCircles (int x_center, int y_center,int number, int radiusPath, int brickWidth, int brickHeight, int hp)
    {
        ArrayList<Brick> list = new ArrayList<Brick>();
        for( int i = 0; i<number; i++)
        {
            int x = (int)(x_center + radiusPath * Math.cos(2 * Math.PI * i / number));
            int y = (int)(y_center + radiusPath * Math.sin(2 * Math.PI * i / number));
            //System.out.println(x + " " + y + " " + radiusBall);
            //Circle c = new Circle (x, y, radiusBall, Color.RED);
            list.add(new Brick(x-brickWidth/2,y-brickWidth/2, brickWidth, brickHeight, hp));
        }
        return list;
    }



    @Override
    public boolean step (double elapsedTime,Text text) {
        // update "actors" attributes

//        GameStateUpdate ubermensch = new GameStateUpdate(ball, myPADDLE, brickArray,width, height, score, scoreHUD);
//        ubermensch.updateBallWallSpeed();
//        ubermensch.updateBallPaddleSpeed();
//        ubermensch.updateBrickBallSpeed(elapsedTime);
        // NEW Java 10 syntax that simplifies things (but watch out it can make code harder to understand)
        // \var intersection = Shape.intersect(myPADDLE, myGrower);


//        // with images can only check bounding box
//        if (myGrower.getBoundsInParent().intersects(myBouncer.getBoundsInParent())) {
//            myGrower.setFill(HIGHLIGHT);
//        }
//        else {
//            myGrower.setFill(GROWER_COLOR);
//        }
        ball.setX(ball.getX()+ BALL_SPEED_X * elapsedTime);
        ball.setY(ball.getY()+ BALL_SPEED_Y * elapsedTime);

        return score >= maxScore;
    }

    public void handleKeyInput(KeyCode code) {
        if (code == KeyCode.LEFT || code == KeyCode.RIGHT) {
            Shape intersection = Shape.intersect(myPADDLE, ball);
            if (intersection.getBoundsInLocal().getWidth() == -1) {
                if (code == KeyCode.RIGHT && myPADDLE.getX() < width - myPADDLE.getBoundsInLocal().getWidth()) {
                    myPADDLE.setX(myPADDLE.getX() + PADDLE_SPEED);
                } else if (code == KeyCode.LEFT && myPADDLE.getX() > 0) {
                    myPADDLE.setX(myPADDLE.getX() - PADDLE_SPEED);
                }
            } else {
                if ((myPADDLE.getX() + PADDLE_WIDTH / 2) > ball.getX() + ball.getWidth()/2) {
                    myPADDLE.setX(myPADDLE.getX() + 3 * PADDLE_SPEED);
                } else if ((myPADDLE.getX() + PADDLE_WIDTH / 2) < ball.getX() + ball.getWidth()/2) {
                    myPADDLE.setX(myPADDLE.getX() - 3 * PADDLE_SPEED);
                }
            }
        }

        if (code == KeyCode.R) {
            ball.setX(width / 2 - PADDLE_HEIGHT / 2);
            ball.setY(3.5* height / 5);
            myPADDLE.setX(width / 2 - PADDLE_WIDTH / 2);
            myPADDLE.setY(4* height / 5);
            BALL_SPEED_X = 100;
            BALL_SPEED_Y = 100;
        }

    }

}
