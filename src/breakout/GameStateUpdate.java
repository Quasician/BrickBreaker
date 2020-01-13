package breakout;

import javafx.scene.input.KeyCode;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class GameStateUpdate {
    public int BALL_SPEED_X = 120;
    public int BALL_SPEED_Y = 160;
    public static int BALL_SPEED_TOTAL = 200;
    public static final int PADDLE_WIDTH = 75;
    public static final int PADDLE_HEIGHT = 50/3;
    public static final int PADDLE_SPEED = 5;

    private boolean playerLost;
    private boolean playerWon;
    private int levelNumber;
    private int score;
    private int width;
    private int height;
    private Ball ball;
    private Paddle paddle;
    private Brick[] brickArray;
    private Text scoreHUD;

    public GameStateUpdate(Ball ball, int ballSpeed,Paddle paddle, Brick[] brickArray, int width, int height, int score, Text scoreHUD)
    {
        this.ball = ball;
        this.paddle = paddle;
        this.brickArray = brickArray;
        this.width = width;
        this.height = height;
        this.score = score;
        this.scoreHUD = scoreHUD;
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
        Shape intersection = Shape.intersect(paddle, ball);
        if (intersection.getBoundsInLocal().getWidth() != -1) {
            if(ball.getY() + ball.getHeight()/2>= paddle.getY() && ball.getY() + ball.getHeight()/2<= paddle.getY()+paddle.getHeight())
            {
                BALL_SPEED_X *= -1;
            }
            if(ball.getX() + ball.getWidth()/2 >= paddle.getX()  && ball.getX() + ball.getWidth()/2<= paddle.getX()+paddle.getWidth()){
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
