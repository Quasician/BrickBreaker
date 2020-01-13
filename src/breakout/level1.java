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
import javafx.util.Duration;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class level1 extends level{

    public static final Paint HIGHLIGHT = Color.OLIVEDRAB;
    public static final String BALL_IMAGE = "ball.gif";
    public static int BALL_SPEED_X = 120;
    public static int BALL_SPEED_Y = 160;
    public static int BALL_SPEED_TOTAL = 200;
    public static final int BALL_RADIUS = 6;
    public static final Paint PADDLE_COLOR = Color.PLUM;
    public static final int PADDLE_WIDTH = 75;
    public static final int PADDLE_HEIGHT = 50/3;
    public static final int PADDLE_SPEED = 5;
    public static final double PADDLE_CORNER_THRESHOLD = BALL_RADIUS/1.3;
    public static final Paint GROWER_COLOR = Color.BISQUE;
    public static final double GROWER_RATE = 1.1;
    public static final int GROWER_SIZE = 50;
    public static final int NUMBER_OF_BRICKS = 20;

    private Rectangle myPADDLE;
    private Circle ball;

    public level1(int width, int height, Paint background) {
        super(width, height, background);
        super.brickArray = new brick[NUMBER_OF_BRICKS];
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

        ball = new Circle(width / 2 - PADDLE_HEIGHT / 2 ,3.5* height / 5 ,BALL_RADIUS);

        myPADDLE = new Rectangle(width / 2 - PADDLE_WIDTH / 2, 4* height / 5, PADDLE_WIDTH, PADDLE_HEIGHT);
        myPADDLE.setFill(PADDLE_COLOR);

        ArrayList<brick> innerCircle = createBricksInCircles (300, 250, 4, 50, 15, 15, 1,Color.GREEN);
        rotateGroup1.getChildren().addAll(innerCircle);
        rotatePane1.setCenter(rotateGroup1);


        ArrayList<brick> outerCircle = createBricksInCircles (300, 250, 16, 150, 15, 15, 1, Color.RED);
        rotateGroup2.getChildren().addAll(outerCircle);
        rotatePane2.setCenter(rotateGroup2);

        innerCircle.addAll(outerCircle);
        brickArray = innerCircle.toArray(new brick[0]);
//        RotateTransition rt2 = new RotateTransition(Duration.millis(3000), rect2);
//        rt2.setByAngle(180);
//        rt2.setCycleCount(Animation.INDEFINITE);
//        rt2.setAutoReverse(true);
//
//        RotateTransition rt = new RotateTransition(Duration.millis(3000), rect);
//        rt.setByAngle(180);
//        rt.setCycleCount(Animation.INDEFINITE);
//        rt.setAutoReverse(true);

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

//        rt2.play();
//        rt.play();




        root.getChildren().add(ball);
        root.getChildren().add(myPADDLE);
        root.getChildren().add(rotatePane1);
        root.getChildren().add(rotatePane2);
        //root.getChildren().add(rect);
        // create a place to see the shapes
        Scene scene = new Scene(root, width, height, background);
        // respond to input
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        //scene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
        return scene;
    }


    public ArrayList createBricksInCircles (int x_center, int y_center,int number, int radiusPath, int brickWidth, int brickHeight, int hp, Paint color)
    {
        ArrayList<brick> list = new ArrayList<brick>();
        for( int i = 0; i<number; i++)
        {
            int x = (int)(x_center + radiusPath * Math.cos(2 * Math.PI * i / number));
            int y = (int)(y_center + radiusPath * Math.sin(2 * Math.PI * i / number));
            //System.out.println(x + " " + y + " " + radiusBall);
            //Circle c = new Circle (x, y, radiusBall, Color.RED);
            list.add(new brick(x-brickWidth/2,y-brickWidth/2, brickWidth, brickHeight, hp, color));
        }
        return list;
    }



    @Override
    public boolean step (double elapsedTime) {
        // update "actors" attributes

        updateBallSpeed();
        updateBrickBallSpeed(elapsedTime);

        // NEW Java 10 syntax that simplifies things (but watch out it can make code harder to understand)
        // \var intersection = Shape.intersect(myPADDLE, myGrower);


//        // with images can only check bounding box
//        if (myGrower.getBoundsInParent().intersects(myBouncer.getBoundsInParent())) {
//            myGrower.setFill(HIGHLIGHT);
//        }
//        else {
//            myGrower.setFill(GROWER_COLOR);
//        }
        ball.setCenterX(ball.getCenterX() + BALL_SPEED_X * elapsedTime);
        ball.setCenterY(ball.getCenterY() + BALL_SPEED_Y * elapsedTime);

        return score >= maxScore;
    }

    public void  updateBallSpeed() {
        if(ball.getCenterX()-ball.getRadius()<=0  || ball.getCenterX() + ball.getRadius()>= width)
        {
            BALL_SPEED_X *= -1;
        }
        if(ball.getCenterY() - ball.getRadius()<=0)
        {
            BALL_SPEED_Y *= -1;
        }
        Shape intersection = Shape.intersect(myPADDLE, ball);
        if (intersection.getBoundsInLocal().getWidth() != -1) {
            if(ball.getCenterY()>= myPADDLE.getY()-PADDLE_CORNER_THRESHOLD && ball.getCenterY()<= myPADDLE.getY()+myPADDLE.getHeight()+ PADDLE_CORNER_THRESHOLD)
            {
                BALL_SPEED_X *= -1;
            }
            if(ball.getCenterX()>= myPADDLE.getX()-PADDLE_CORNER_THRESHOLD  && ball.getCenterX()<= myPADDLE.getX()+myPADDLE.getWidth()+ PADDLE_CORNER_THRESHOLD){
                BALL_SPEED_Y *= -1;
            }

        }
    }

    public void  updateBrickBallSpeed(double elapsedTime) {
        for (brick i : brickArray) {
            Shape intersection = Shape.intersect(i, ball);
            if (intersection.getBoundsInLocal().getWidth() != -1 && i.getHP() > 0) {
                if(ball.getCenterY()>= i.getY()+i.getHeight() || ball.getCenterY()<= i.getY())
                {
                    BALL_SPEED_Y *= -1;
                }
                if(ball.getCenterX()>= i.getX()+i.getWidth() || ball.getCenterX()<= i.getX()){
                    BALL_SPEED_X *= -1;
                }
                i.decreaseHP();
                if(i.getHP()==0)
                {
                    i.setColor(Color.TRANSPARENT);
                }
            }
        }
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
                if ((myPADDLE.getX() + PADDLE_WIDTH / 2) > ball.getCenterX()) {
                    myPADDLE.setX(myPADDLE.getX() + 3 * PADDLE_SPEED);
                } else if ((myPADDLE.getX() + PADDLE_WIDTH / 2) < ball.getCenterX()) {
                    myPADDLE.setX(myPADDLE.getX() - 3 * PADDLE_SPEED);
                }
            }
        }

        if (code == KeyCode.R) {
            ball.setCenterX(width / 2 - PADDLE_HEIGHT / 2);
            ball.setCenterY(3.5* height / 5);
            myPADDLE.setX(width / 2 - PADDLE_WIDTH / 2);
            myPADDLE.setY(4* height / 5);
            BALL_SPEED_X = 100;
            BALL_SPEED_Y = 100;
        }
//        else if (code == KeyCode.UP) {
//            myPADDLE.setY(myPADDLE.getY() - PADDLE_SPEED);
//        }
//        else if (code == KeyCode.DOWN) {
//            myPADDLE.setY(myPADDLE.getY() + PADDLE_SPEED);
//        }
        // NEW Java 12 syntax that some prefer (but watch out for the many special cases!)
        //   https://blog.jetbrains.com/idea/2019/02
        //   /java-12-and-intellij-idea/
        // Note, must set Project Language Level to "13 Preview" under File -> Project Structure
        // switch (code) {
        //     case RIGHT -> myPADDLE.setX(myPADDLE.getX() + PADDLE_SPEED);
        //     case LEFT -> myPADDLE.setX(myPADDLE.getX() - PADDLE_SPEED);
        //     case UP -> myPADDLE.setY(myPADDLE.getY() - PADDLE_SPEED);
        //     case DOWN -> myPADDLE.setY(myPADDLE.getY() + PADDLE_SPEED);
        // }
    }

    @Override
    public void updateScore(int value) {
        score += value;
    }
}
