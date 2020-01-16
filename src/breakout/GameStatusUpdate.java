package breakout;

import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;


/**
 * A basic example JavaFX program for the first lab.
 *
 * @author Robert C. Duvall
 */
public class GameStatusUpdate extends Application {
    public levelInfo[] levels;

    public final static int BALL_SPEED_X_INIT = 100; //120
    public final static int BALL_SPEED_Y_INIT = 160;  //160
    public int BALL_SPEED_X = BALL_SPEED_X_INIT; //120
    public int BALL_SPEED_Y = BALL_SPEED_Y_INIT;  //160
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
    private static int width = 600;
    private static int height = 600;
    private Ball ball;
    private Paddle myPADDLE;
    private Text scoreHUD;
    private Text livesHUD;

    private Group root;

    public static final int BALL_DIAMETER = 12;

    public static final Paint PADDLE_COLOR = Color.PLUM;
    private ArrayList<Brick> brickList;
    private Scene startScene;
    private Scene endScene;
    private Scene winScene;
    private Stage stage;
    private boolean pressedEnter;
    private int currentLevel = -1;

    /**
     * Initialize what will be displayed and how it will be updated.
     */

    @Override
    public void start (Stage stage) throws FileNotFoundException {
        this.stage = stage;
        playerLost = false;
        score = 0;
        initScenes();
        showStartScreen();
//        stage.setScene(scene);
//        stage.setTitle(TITLE);
//        stage.show();



        // attach "game loop" to timeline to play it (basically just calling step() method repeatedly forever)
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY,scoreHUD));
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

    public void initScenes() throws FileNotFoundException {
        int fileNumber = new File("./resources/").listFiles().length;
        //System.out.println(fileNumber);
        levels = new levelInfo[fileNumber];

        File directory = new File("./resources/");
        File[] files = directory.listFiles();
        if (files != null) {
            for (int x = 0; x <files.length; x++) {
                // Do something with child
                Scanner sc = new Scanner(files[x]);
                int firstNumInLine = sc.nextInt();
                while (firstNumInLine == 0) {
                    sc.nextLine();
                    firstNumInLine = sc.nextInt();
                    //System.out.println("Found Comment");
                }
                levelInfo levelText = new levelInfo(firstNumInLine);
                for(int i = 0; i< levelText.getRingNumber();i++)
                {
                    int x_init = sc.nextInt();
                    int y_init = sc.nextInt();
                    int circleNumber = sc.nextInt();
                    int radiusOfPath = sc.nextInt();
                    int brickWidth = sc.nextInt();
                    int brickHeight = sc.nextInt();
                    int hp = sc.nextInt();
                    int ccw = sc.nextInt();
                    ringInfo ringText = new ringInfo(x_init,y_init,circleNumber,radiusOfPath,brickWidth,brickHeight,hp, ccw);
                    levelText.getRingArray()[i] = ringText;
                }
                levels[x]= levelText;
            }
        } else {
            System.out.println("THERE ARE NO FILES IN RESOURCE TO MAKE GAME LEVELS. ADD MORE FILES IN THE CORRECT FORMAT TO PLAY THE GAME");
        }
    }

    public void showStartScreen(){
        pressedEnter = false;
        playerLost = false;
        playerWon = false;
        Group startGroup = new Group();
        String message = "Press Enter To Start\n Second row Test\n Third row Test\n";
        Text startMessage = new Text();
        startGroup.getChildren().add(startMessage);
        startScene = new Scene(startGroup, width, height, BACKGROUND);

        stage.setScene(startScene);
        stage.setTitle(TITLE);
        stage.show();

        writeHUD(startMessage, message, 20, (int)(width / 3.8), height / 2);
        startScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
    }

    public void createLevel() {
        playerLost = false;
        playerWon = false;
        BALL_SPEED_X = BALL_SPEED_X_INIT;
        BALL_SPEED_Y = BALL_SPEED_Y_INIT;
        brickList = new ArrayList <Brick>();
        ball = new Ball(width / 2 - PADDLE_HEIGHT / 2 ,(int)(3.5* height / 5) ,BALL_DIAMETER, BALL_DIAMETER, Color.BLACK);
        ball.setArcHeight(BALL_DIAMETER);
        ball.setArcWidth(BALL_DIAMETER);
        myPADDLE = new Paddle(width / 2 - PADDLE_WIDTH / 2, 4* height / 5, PADDLE_WIDTH, PADDLE_HEIGHT, 1, PADDLE_COLOR);
        scoreHUD = new Text();
        livesHUD = new Text();
        root = new Group();

        Level level = new Level(currentLevel, SIZE, SIZE, BACKGROUND, root, ball, myPADDLE, scoreHUD, livesHUD, brickList, levels);
        Scene scene = new Scene(root, width, height, BACKGROUND);
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        updateScore(0);
        updateLivesText();
        stage.setScene(scene);
    }

    public void checkPlayerLoss () {

        if(myPADDLE.getHP() <= 0) {
            System.out.println(myPADDLE.getHP());
            playerLost = true;
            Group endGroup = new Group();
            Text endMessage = new Text();
            endGroup.getChildren().add(endMessage);
            endScene = new Scene(endGroup, width, height, BACKGROUND);
            stage.setScene(endScene);
            writeHUD(endMessage, "GAME OVER\nPRESS ENTER\nTO PLAY AGAIN", 50, (int)(width / 8), height / 2);
            endScene.setOnKeyPressed(e->handleKeyInput(e.getCode()));
        }
    }
    public void drawWinScreen () {
        playerWon = true;
        Group winGroup = new Group();
        Text winMessage = new Text();
        winGroup.getChildren().add(winMessage);
        winScene = new Scene(winGroup, width, height, BACKGROUND);
        stage.setScene(winScene);
        writeHUD(winMessage, "YOU WIN\nYOUR SCORE:\n" + score, 50, (int)(width / 8), height / 2);
        winScene.setOnKeyPressed(e->handleKeyInput(e.getCode()));

    }

    public void step (double elapsedTime,Text text) {
        if(pressedEnter && !playerWon && !playerLost) {
            deleteDeadBricks();
            updateBallWallSpeed();
            updateBallPaddleSpeed();
            updateBrickBallSpeed(elapsedTime);
            updateOnLostBall();
            checkPlayerLoss();

            ball.setX(ball.getX() + BALL_SPEED_X * elapsedTime);
            ball.setY(ball.getY() + BALL_SPEED_Y * elapsedTime);
        }

    }

    public void  updateBallWallSpeed() {
        if(ball.getX()<=0  || ball.getX() + ball.getWidth()>= width)
        {
            //System.out.println(ball.getX());
            BALL_SPEED_X *= -1;
        }
        if(ball.getY()<=0)
        {
            BALL_SPEED_Y *= -1;
        }
    }

    public void  updateOnLostBall() {
        if(ball.getY()>=height) {
            myPADDLE.decreaseHP();
            updateLivesText();
            if(myPADDLE.getHP() > 0) {
                handleKeyInput(KeyCode.R);
            }
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
        for (Brick i : brickList) {
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


    public Iterator<Brick> iterator() {
        Iterator<Brick> it = new Iterator<Brick>() {

            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return brickList.get(currentIndex+1) != null;
            }

            @Override
            public Brick next() {
                return brickList.get(currentIndex++);
            }

            @Override
            public void remove() {
                brickList.remove(currentIndex);
            }
        };
        return it;
    }

    public void  deleteDeadBricks() {

        Iterator<Brick> iterator = brickList.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getHP() == 0) {
                iterator.remove();
            }
        }
        if(brickList.size() == 0)
        {
            if(currentLevel == levels.length)
            {
                drawWinScreen();
            }else
            {
                currentLevel++;
                createLevel();
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
        if (code == KeyCode.ENTER) {
            pressedEnter = true;
            currentLevel = 1;
            score = 0;
            createLevel();
        }

        if (code.isDigitKey()) {
            int desiredLevel = Integer.parseInt(code.getChar());
            if((desiredLevel)>=0 && (desiredLevel)<=levels.length)
            {
                currentLevel = desiredLevel;
                score = 0;
                createLevel();
            }
            else if(desiredLevel >levels.length)
            {
                currentLevel = levels.length;
                score = 0;
                createLevel();
            }
        }

        if (code == KeyCode.R) {
            createLevel();
        }
        if (code == KeyCode.L) {
            myPADDLE.increaseHP();
            updateLivesText();
        }
    }

    public void writeHUD(Text text, String s, int fontSize, int x, int y)
    {
        text.setFont(new Font(fontSize));
        text.setText(s);
        text.setX(x);
        text.setY(y);
    }

    public void updateScore(int value) {
        score += value;
        writeHUD(scoreHUD,"Score: "+score,30,0,height/20);
    }

    public void updateLivesText() {
        writeHUD(livesHUD,"Lives: "+ myPADDLE.getHP(),30,0,height/10);
    }
}
