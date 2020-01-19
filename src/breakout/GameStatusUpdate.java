package breakout;

import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.io.FileNotFoundException;
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

    private boolean playerLost;
    private boolean playerWon;
    private int score;
    private final static int width = 600;
    private final static int height = 600;

    public Ball ball;
    public  Paddle myPADDLE;
    public Text scoreHUD;
    public Text livesHUD;



    public final static double MAX_BOUNCE_ANGLE = Math.PI*.7;
    public final static int BALL_SPEED_X_INIT = 0; //120
    public final static int BALL_SPEED_Y_INIT = 200;  //160
    public int BALL_SPEED_X = BALL_SPEED_X_INIT; //120
    public int BALL_SPEED_Y = BALL_SPEED_Y_INIT;  //160
    public static int BALL_SPEED_TOTAL = 200;
    public static final int PADDLE_WIDTH_INIT = 75;
    public int PADDLE_WIDTH = PADDLE_WIDTH_INIT;
    public static final int PADDLE_HEIGHT = 50/3;
    public static final int PADDLE_SPEED_INIT = 5;
    public int PADDLE_SPEED = PADDLE_SPEED_INIT;

    public static final int PADDLE_X_INIT = width / 2 - PADDLE_WIDTH_INIT / 2;
    public static final int PADDLE_Y_INIT = (int)(height*.975);

    public static final int BALL_X_INIT = width / 2 - PADDLE_HEIGHT / 2 ;
    public static final int BALL_Y_INIT = (int)(4* height / 5);

    public static final int POWER_UP_DESCEND_SPEED = 100;


    public static final String TITLE = "Thomas's Breakout Game";
    public static final int SIZE = 600;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final Paint BACKGROUND = Color.AZURE;


    private Group root;

    public static final int BALL_DIAMETER = 12;

    public static final Paint PADDLE_COLOR = Color.PLUM;
    private ArrayList<Brick> brickList;
    private ArrayList<PowerUp> powerUpList;

    private Stage stage;
    private boolean pressedEnter;
    private boolean immortality = false;
    private int currentLevel = -1;
    private static final int HP_INIT = 3;
    private int currentHP = HP_INIT;


    /**
     * Initialize what will be displayed and how it will be updated.
     */

    @Override
    public void start (Stage stage) throws FileNotFoundException {
        this.stage = stage;
        initScenes();
        showStartScreen();

        // attach "game loop" to timeline to play it (basically just calling step() method repeatedly forever)
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY,scoreHUD));
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }// attach scene to the stage and display it

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



    public void createLevel() {
        playerLost = false;
        playerWon = false;
        BALL_SPEED_X = BALL_SPEED_X_INIT;
        BALL_SPEED_Y = BALL_SPEED_Y_INIT;
        PADDLE_WIDTH = PADDLE_WIDTH_INIT;
        PADDLE_SPEED = PADDLE_SPEED_INIT;
        brickList = new ArrayList <Brick>();
        powerUpList = new ArrayList <PowerUp>();
        ball = new Ball(BALL_X_INIT, BALL_Y_INIT,BALL_DIAMETER, BALL_DIAMETER, Color.BLACK);
        ball.setArcHeight(BALL_DIAMETER);
        ball.setArcWidth(BALL_DIAMETER);
        myPADDLE = new Paddle(PADDLE_X_INIT, PADDLE_Y_INIT, PADDLE_WIDTH, PADDLE_HEIGHT, currentHP, PADDLE_COLOR);
        scoreHUD = new Text();
        livesHUD = new Text();
        root = new Group();

        root.getChildren().add(ball);
        root.getChildren().add(myPADDLE);
        root.getChildren().add(scoreHUD);
        root.getChildren().add(livesHUD);

        Level level = new Level(currentLevel, root, brickList, levels);
        Scene scene = new Scene(root, width, height, BACKGROUND);
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        updateScore(0);
        updateLivesText();
        stage.setScene(scene);
    }

    public void showStartScreen(){
        pressedEnter = false;
        playerLost = false;
        playerWon = false;
        score = 0;
        Group startGroup = new Group();
        String message = "                                  WELCOME TO MY BREAKOUT GAME! \n\n" + "    There are 3 levels in my game. The left and right arrow keys move the paddle left \n " +
                "and right. You must hit and destroy all the bricks to beat a level. Each brick's color \n" + "designates how much hit points it has left. A green brick has 1 hp, a grey brick is 2 hp, \n" +
                "a black brick is 3 hp. Each game you play starts with 3 lives. Everytime the ball hits \n " +
                "the ground, you lose a life. When you lose all your lives, you lose. If you hit all bricks\n" + "throughout all three levels without losing all of your lives you win. \n\n\n" +
                "Cheat keys:\n" + "R: resets ball and paddle to original locations\n" + "L: adds a life\n" +
                "1-3: transports player to that level (4-9 will take the player to level 3)\n" + "C: toggles immortality (ball will bounce off the ground)\n\n\n\n" +
                "Press Enter To Start\n";
        Text startMessage = new Text();
        startGroup.getChildren().add(startMessage);
        Scene startScene = new Scene(startGroup, width, height, BACKGROUND);

        stage.setScene(startScene);
        stage.setTitle(TITLE);
        stage.show();

        writeHUD(startMessage, message, 15, (int)(width / 30), height / 20);
        startScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
    }

    public void drawLoseScreen() {
        playerLost = true;
        Group endGroup = new Group();
        Text endMessage = new Text();
        endGroup.getChildren().add(endMessage);
        Scene endScene = new Scene(endGroup, width, height, BACKGROUND);
        stage.setScene(endScene);
        writeHUD(endMessage, "       GAME OVER\n      PRESS ENTER\n    TO PLAY AGAIN", 50, (int)(width / 8), height / 2);
        endScene.setOnKeyPressed(e->handleKeyInput(e.getCode()));
    }

    public void drawWinScreen () {
        playerWon = true;
        Group winGroup = new Group();
        Text winMessage = new Text();
        winGroup.getChildren().add(winMessage);
        Scene winScene = new Scene(winGroup, width, height, BACKGROUND);
        stage.setScene(winScene);
        writeHUD(winMessage, "YOU WIN\nYOUR SCORE:\n" + score, 50, (int)(width / 8), height / 2);
        winScene.setOnKeyPressed(e->handleKeyInput(e.getCode()));

    }

    public void step (double elapsedTime,Text text) {
        if(pressedEnter && !playerWon && !playerLost) {
            if(myPADDLE.getHP() <= 0) {
                drawLoseScreen();
            }
            deleteDeadBricksCreatePowerUps();
            updateBallWallSpeed(elapsedTime);
            updateBallPaddleSpeed();
            updateBrickBallSpeed(elapsedTime);
            updateOnLostBall();
            updatePaddleX();
            lowerPowerUps(elapsedTime);
            updatePaddlePowerUp();

            ball.setX(ball.getX() + BALL_SPEED_X * elapsedTime);
            ball.setY(ball.getY() + BALL_SPEED_Y * elapsedTime);
        }
    }

    public void  updateBallWallSpeed(double elapsedTime) {
        if(ball.getX()<=0  || ball.getX() + ball.getWidth()>= width)
        {
            BALL_SPEED_X *= -1;
            ball.setX(ball.getX() + 3* BALL_SPEED_X * elapsedTime);
        }
        if(ball.getY()<=0 || (immortality && ball.getY() + ball.getWidth()>= width))
        {
            BALL_SPEED_Y *= -1;
            ball.setY(ball.getY() + 3* BALL_SPEED_Y * elapsedTime);
        }
    }

    public void  updateOnLostBall() {
        if(ball.getY()>=height) {
            myPADDLE.decreaseHP();
            currentHP--;
            updateLivesText();
            if(myPADDLE.getHP() > 0) {
                handleKeyInput(KeyCode.R);
            }
        }
    }

    public void  updatePaddleX() {
        if(myPADDLE.getX()<0) {
            myPADDLE.setX(0);
        }
        if(myPADDLE.getX()+PADDLE_WIDTH>width) {
            myPADDLE.setX(width-PADDLE_WIDTH);
        }
    }

    public void  updatePaddlePowerUp() {
        Iterator<PowerUp> powerUpIterator = powerUpList.iterator();
        while (powerUpIterator.hasNext()) {
            PowerUp powerUp = powerUpIterator.next();
            Shape intersection = Shape.intersect(myPADDLE, powerUp);
            if (intersection.getBoundsInLocal().getWidth() != -1) {
                powerUp.destroyPowerUp();

                if(powerUp.getTypeArray()[0])
                {
                    PADDLE_SPEED *= 2;

                }
                else if(powerUp.getTypeArray()[1])
                {
                    BALL_SPEED_X *= .75;
                    BALL_SPEED_Y *= .75;
                    BALL_SPEED_TOTAL *= .75;

                }
                else if(powerUp.getTypeArray()[2])
                {
                    int x_val = (int)myPADDLE.getX();
                    int currentLives = myPADDLE.getHP();
                    root.getChildren().remove(myPADDLE);
                    PADDLE_WIDTH *= 2;
                    myPADDLE = new Paddle(x_val, PADDLE_Y_INIT, PADDLE_WIDTH, PADDLE_HEIGHT, currentLives, PADDLE_COLOR);
                    root.getChildren().add(myPADDLE);
                }
                powerUpIterator.remove();
            }
        }
    }

    public void  updateBallPaddleSpeed() {
        Shape intersection = Shape.intersect(myPADDLE, ball);
        if (intersection.getBoundsInLocal().getWidth() != -1) {
//            if(ball.getX() < myPADDLE.getX() || ball.getX() > myPADDLE.getX()+ myPADDLE.getWidth())
//            {
//                BALL_SPEED_X *= -1;
//            }
            if(ball.getY() < myPADDLE.getY()){

                double ballPaddleXDiff = (myPADDLE.getX()+PADDLE_WIDTH/2) - (ball.getX() + ball.getWidth()/2);

                double normalizedBallPaddleXDiff = (ballPaddleXDiff/(PADDLE_WIDTH));
                double angle = normalizedBallPaddleXDiff * MAX_BOUNCE_ANGLE;

                BALL_SPEED_Y = (int)(-BALL_SPEED_TOTAL * Math.cos(angle));
                BALL_SPEED_X = -1 * (int)(BALL_SPEED_TOTAL * Math.sin(angle));
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


    public void  lowerPowerUps(double elapsedTime) {
        for(PowerUp powerUp:powerUpList)
        {
            powerUp.setY(powerUp.getY() + POWER_UP_DESCEND_SPEED * elapsedTime);
        }
    }

    public Iterator<Brick> brickIterator() {
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

    public Iterator<PowerUp> powerUpIterator() {
        Iterator<PowerUp> it = new Iterator<PowerUp>() {

            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return powerUpList.get(currentIndex+1) != null;
            }

            @Override
            public PowerUp next() {
                return powerUpList.get(currentIndex++);
            }

            @Override
            public void remove() {
                powerUpList.remove(currentIndex);
            }
        };
        return it;
    }

    public void  deleteDeadBricksCreatePowerUps() {

        Iterator<Brick> brickIterator = brickList.iterator();
        while (brickIterator.hasNext()) {
            Brick brick = brickIterator.next();
            if (brick.getHP() == 0) {

                double chanceOfPowerUp = Math.random();
                if(chanceOfPowerUp < .3)
                {
                    int powerUpType = (int)(Math.random() * 3);
                    PowerUp powerUp = new PowerUp((int)brick.getX(),(int)brick.getY(), powerUpType);
                    powerUpList.add(powerUp);
                    root.getChildren().add(powerUp);
                }
                brickIterator.remove();
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
        if ((code == KeyCode.LEFT || code == KeyCode.RIGHT) && pressedEnter) {
            Shape intersection = Shape.intersect(myPADDLE, ball);
            if (intersection.getBoundsInLocal().getWidth() == -1) {
                if (code == KeyCode.RIGHT && myPADDLE.getX() + PADDLE_WIDTH< width ) {
                    myPADDLE.setX(myPADDLE.getX() + PADDLE_SPEED);
                } else if (code == KeyCode.LEFT && myPADDLE.getX() > 0) {
                    myPADDLE.setX(myPADDLE.getX() - PADDLE_SPEED);
                }
            } else {

//                if ((myPADDLE.getX() + PADDLE_WIDTH / 2) > ball.getX() + ball.getWidth()/2) {
//                    myPADDLE.setX(myPADDLE.getX() + 3 * PADDLE_SPEED);
//                } else if ((myPADDLE.getX() + PADDLE_WIDTH / 2) < ball.getX() + ball.getWidth()/2) {
//                    myPADDLE.setX(myPADDLE.getX() - 3 * PADDLE_SPEED);
//                }
            }
        }
        if (code == KeyCode.ENTER) {
            pressedEnter = true;
            //System.out.println("YEET");
            currentLevel = 1;
            score = 0;
            currentHP = HP_INIT;
            createLevel();
        }

        if (code.isDigitKey() && pressedEnter) {
            int desiredLevel = Integer.parseInt(code.getChar());
            if((desiredLevel)>=0 && (desiredLevel)<=levels.length)
            {
                currentLevel = desiredLevel;
                createLevel();
            }
            else if(desiredLevel >levels.length)
            {
                currentLevel = levels.length;
                createLevel();
            }
        }

        if (code == KeyCode.R && pressedEnter) {
            ball.setX(BALL_X_INIT);
            ball.setY(BALL_Y_INIT);
            BALL_SPEED_X = BALL_SPEED_X_INIT;
            BALL_SPEED_Y = BALL_SPEED_Y_INIT;
            myPADDLE.setX(PADDLE_X_INIT);
            myPADDLE.setY(PADDLE_Y_INIT);
        }

        if (code == KeyCode.L && pressedEnter) {
            myPADDLE.increaseHP();
            currentHP++;
            updateLivesText();
        }

        if (code == KeyCode.C && pressedEnter) {
            immortality = !immortality;
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

    /**
     * Start the program.
     */
    public static void main (String[] args) {
        launch(args);
    }
}
