package breakout;

import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 * A basic example JavaFX program for the first lab.
 *
 * @author Robert C. Duvall
 */
public class GameStatusUpdate extends Application {
    public int BALL_SPEED_X = 120;
    public int BALL_SPEED_Y = 160;
    public static int BALL_SPEED_TOTAL = 200;
    public static final int PADDLE_WIDTH = 75;
    public static final int PADDLE_HEIGHT = 50/3;
    public static final int PADDLE_SPEED = 5;

    public static final String TITLE = "Thomas's Breakout Game";
    public static final int SIZE = 600;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final Paint BACKGROUND = Color.AZURE;

    private boolean playerLost;
    private boolean playerWon;
    private int levelNumber;
    private int score;
    private int width;
    private int height;
    private Ball ball;
    private Paddle myPADDLE;
    private Brick[] brickArray;
    private Text scoreHUD;


    // some things needed to remember during game
    private Scene myScene;



    /**
     * Initialize what will be displayed and how it will be updated.
     */

    @Override
    public void start (Stage stage) throws InterruptedException {
        Level1 l1 = new Level1(SIZE, SIZE, BACKGROUND);
        myScene = l1.setUpLevel();
        stage.setScene(myScene);
        stage.setTitle(TITLE);
        stage.show();


        // attach "game loop" to timeline to play it (basically just calling step() method repeatedly forever)
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> l1.step(SECOND_DELAY,scoreHUD));
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }// attach scene to the stage and display it


    /**
     * Start the program.
     */
    public static void main (String[] args) {
        launch(args);
    }

    public void step (double elapsedTime,Text text) {


        updateBallWallSpeed();
        updateBallPaddleSpeed();
        updateBrickBallSpeed(elapsedTime);


        ball.setX(ball.getX()+ BALL_SPEED_X * elapsedTime);
        ball.setY(ball.getY()+ BALL_SPEED_Y * elapsedTime);


    }

    public void  updateBallWallSpeed() {
        if(ball.getX()<=0  || ball.getX() + ball.getWidth()>= width)
        {
            System.out.println("HELLO");
            BALL_SPEED_X *= -1;
        }
        if(ball.getY()<=0)
        {
            BALL_SPEED_Y *= -1;
        }
    }

    public void  updateBallPaddleSpeed() {
        Shape intersection = Shape.intersect(myPADDLE, ball);
        if (intersection.getBoundsInLocal().getWidth() != -1) {
            if(ball.getY() + ball.getHeight()/2>= myPADDLE.getY() && ball.getY() + ball.getHeight()/2<= myPADDLE.getY()+myPADDLE.getHeight())
            {
                BALL_SPEED_X *= -1;
            }
            if(ball.getX() + ball.getWidth()/2 >= myPADDLE.getX()  && ball.getX() + ball.getWidth()/2<= myPADDLE.getX()+myPADDLE.getWidth()){
//                double temp =  paddle.getX()+paddle.getWidth();
//                System.out.println(ball.getCenterX() + " " + paddle.getX() + " " +  temp);
                BALL_SPEED_Y *= -1;
            }

        }
    }

    public void  updateBrickBallSpeed(double elapsedTime) {
        for (Brick i : brickArray) {
            Shape intersection = Shape.intersect(i, ball);
            if (intersection.getBoundsInLocal().getWidth() != -1 && i.getHP() > 0) {
                if(ball.getY() + ball.getHeight()/2>= i.getY()+i.getHeight() || ball.getY() + ball.getHeight()/2<= i.getY())
                {
                    BALL_SPEED_Y *= -1;
                }
                if(ball.getX() + ball.getWidth()/2>= i.getX()+i.getWidth() || ball.getX() + ball.getWidth()/2<= i.getX()){
                    BALL_SPEED_X *= -1;
                }
                ball.setX(ball.getX() + 4*BALL_SPEED_X * elapsedTime);
                ball.setY(ball.getY() + 4*BALL_SPEED_Y * elapsedTime);
                i.decreaseHP();
                updateScore(100);
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

    public void writeHUD(Text text, String s, int font, int x, int y)
    {
        text.setFont(new Font(font));
        text.setText(s);
        text.setX(x);
        text.setY(y);
    }

    public void updateScore(int value) {
        score += value;
        writeHUD(scoreHUD,"Score: "+score,30,0,height/20);
    }

}
