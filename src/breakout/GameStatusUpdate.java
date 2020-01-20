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

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author Thomas Chemmanoor
 * The purpose of the GameStatusUpdate class is essentially being the supervisor of the entire game.
 * This class creates the game and updates GameObject object values as the game progresses.
 * The only situation that the class would fail would be if someting happens in the game that GameStatusUpdate class has not accounted for.
 * This class depends on the package breakout and the class application.
 * To use this class simply initialize the state of your desired game in the start method and call the launch method within the main method.
 */
public class GameStatusUpdate extends Application {

    private LevelInfo[] levels;

    private boolean playerLost;
    private boolean playerWon;
    private int score;
    private final static int width = 600;
    private final static int height = 600;

    private Ball ball;
    private Paddle myPADDLE;
    private Text scoreHUD;
    private Text livesHUD;

    public final static double MAX_BOUNCE_ANGLE = Math.PI*.7;
    public final static int BALL_SPEED_X_INIT = 0;
    public final static int BALL_SPEED_Y_INIT = 200;
    private int BALL_SPEED_X = BALL_SPEED_X_INIT;
    private int BALL_SPEED_Y = BALL_SPEED_Y_INIT;
    public static int BALL_SPEED_TOTAL = 200;
    public static final int BALL_DIAMETER = 12;
    public static final int BALL_X_INIT = width / 2 - BALL_DIAMETER;
    public static final int BALL_Y_INIT = 4* height / 5;

    public static final int PADDLE_WIDTH_INIT = 75;
    private int PADDLE_WIDTH = PADDLE_WIDTH_INIT;
    private int PADDLE_WIDTH_WITH_POWERUP = PADDLE_WIDTH_INIT*2;
    public static final int PADDLE_HEIGHT = 50/3;
    public static final int PADDLE_SPEED_INIT = 5;
    private int PADDLE_SPEED = PADDLE_SPEED_INIT;
    public static final Paint PADDLE_COLOR = Color.PLUM;
    public static final int PADDLE_X_INIT = width / 2 - PADDLE_WIDTH_INIT / 2;
    public static final int PADDLE_Y_INIT = (int)(height*.975);


    public static final int POWER_UP_DESCEND_SPEED = 100;


    public static final String TITLE = "Thomas's Breakout Game";
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final Paint BACKGROUND = Color.AZURE;

    private Group root;

    private ArrayList<Brick> brickList;
    private ArrayList<PowerUp> powerUpList;

    private Stage stage;
    private boolean pressedEnter;
    private boolean immortality = false;
    private int currentLevel = -1;
    private static final int HP_INIT = 3;
    private int currentHP = HP_INIT;
    private int maxScore;
    private String splashScreenMessage = "                                  WELCOME TO MY BREAKOUT GAME! \n\n" +
            "    There are 3 levels in my game. The left and right arrow keys move the paddle left \n " +
            "and right. You must hit and destroy all the bricks to beat a level. Each brick's color \n" +
            "designates how much hit points it has left. A green brick is 1 hp, a grey brick is 2 hp, \n" +
            "a black brick is 3 hp. Each game you play starts with 3 lives. Everytime the ball hits \n " +
            "the ground, you lose a life. When you lose all your lives, you lose. If you hit all bricks\n" +
            "throughout all three levels without losing all of your lives you win. \n\n\n" +
            "Power Ups:\nBlue: Increases Paddle Size \nYellow: Increases Paddle speed\nRed: Slows down ball speed\n\n\n" +
            "Cheat keys:\n" + "R: resets ball and paddle to original locations\n" + "L: adds a life\n" +
            "1-3: transports player to that level (4-9 will take the player to level 3)\n" +
            "C: toggles immortality (ball will bounce off the ground)\n\n\n\n" + "Press Enter To Start\n";


    /**
     * The start method is run by the launch method within the main method at the botom of this file.
     * This method sets a global variable to equal the stage that was passed in and it calls the functions needed to create the base game.
     * Specifically it calls methods that initialize the levels from the text files and create the splash screen.
     * It finally starts the gameloop through the use of the Timeline animation and continuously calling the step function.
     *
     * The only assumption being made here is that this method is inside a class that extends application.
     * @param stage
     * @throws IOException
     */
    @Override
    public void start (Stage stage) throws IOException {
        this.stage = stage;
        initScenes();
        showStartScreen();

        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> {
            try {
                step(SECOND_DELAY,scoreHUD);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    /**
     * initScenes goes through all text files within the resource folder and adds each level's info into a levelInfo (an object that contains all pertinent level elements) array
     * It does this so that the game can create levels simply by loading up a levelInfo object into the correct function.
     * @throws FileNotFoundException
     */
    private void initScenes() throws FileNotFoundException {
        int fileNumber = new File("./resources/").listFiles().length;
        levels = new LevelInfo[fileNumber];

        File directory = new File("./resources/");
        File[] files = directory.listFiles();
        if (files != null) {
            for (int x = 0; x <files.length; x++) {
                levels[x]= getLevelFromText(files, x);
            }
        } else {
            System.out.println("THERE ARE NO FILES IN RESOURCE TO MAKE GAME LEVELS. ADD MORE FILES IN THE CORRECT FORMAT TO PLAY THE GAME");
        }
    }

    /**
     * getlevelFromText loads the info from the level text files into ringInfo (object that contains all of a ring of brick's info) and levelInfo objects
     * @param files
     * @param file
     * @return I am returning a levelInfo object that contains all of a level's pertinent information
     * @throws FileNotFoundException
     */
    private LevelInfo getLevelFromText(File[] files, int file) throws FileNotFoundException {
        Scanner sc = new Scanner(files[file]);
        int firstNumInLine = sc.nextInt();
        while (firstNumInLine == 0) {
            sc.nextLine();
            firstNumInLine = sc.nextInt();
        }
        LevelInfo levelText = new LevelInfo(firstNumInLine);
        for(int i = 0; i< levelText.getRingNumber();i++) {
            RingInfo ringText = new RingInfo(sc.nextInt(),sc.nextInt(),sc.nextInt(),sc.nextInt(),sc.nextInt(),sc.nextInt(),sc.nextInt(), sc.nextInt());
            levelText.getRingArray()[i] = ringText;
        }
        return levelText;
    }

    /**
     * createLevel adds the ball, paddle, and both the score and lives counter to the level.
     * It also calls the methods that actually load up the level from the levelInfo array.
     * Finally it also sets the event handler to work for this scene when the user presses a key.
     */
    private void createLevel() {
        initLevel();

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

    /**
     * initLevel sets all values to their initial values
     * Every time a level is created this method is called.
     */
    private void initLevel() {
        playerLost = false;
        playerWon = false;
        BALL_SPEED_X = BALL_SPEED_X_INIT;
        BALL_SPEED_Y = BALL_SPEED_Y_INIT;
        PADDLE_WIDTH = PADDLE_WIDTH_INIT;
        PADDLE_SPEED = PADDLE_SPEED_INIT;
        brickList = new ArrayList <Brick>();
        powerUpList = new ArrayList <PowerUp>();
        ball = new Ball(BALL_X_INIT, BALL_Y_INIT, BALL_DIAMETER, BALL_DIAMETER, Color.BLACK);
        ball.setArcHeight(BALL_DIAMETER);
        ball.setArcWidth(BALL_DIAMETER);
        myPADDLE = new Paddle(PADDLE_X_INIT, PADDLE_Y_INIT, PADDLE_WIDTH, PADDLE_HEIGHT, currentHP, PADDLE_COLOR);
        scoreHUD = new Text();
        livesHUD = new Text();
        root = new Group();
    }

    /**
     * showStartScreen creates and shows the player the splash screen.
     * It also sets the event handler to work for this scene when the user presses a key.
     */
    private void showStartScreen() {
        pressedEnter = false;
        playerLost = false;
        playerWon = false;
        score = 0;
        Group startGroup = new Group();

        Text startMessage = new Text();
        startGroup.getChildren().add(startMessage);
        Scene startScene = new Scene(startGroup, width, height, BACKGROUND);
        stage.setScene(startScene);
        stage.setTitle(TITLE);
        stage.show();
        writeHUD(startMessage, splashScreenMessage, 15, (int)(width / 30), height / 20);
        startScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
    }

    /**
     * drawLoseScreen creates and shows the player the lose screen.
     * It also sets the event handler to work for this scene when the user presses a key.
     */
    private void drawLoseScreen() {
        playerLost = true;
        Group endGroup = new Group();
        Text endMessage = new Text();
        endGroup.getChildren().add(endMessage);
        Scene endScene = new Scene(endGroup, width, height, BACKGROUND);
        stage.setScene(endScene);
        writeHUD(endMessage, "       GAME OVER\n      PRESS ENTER\n    TO PLAY AGAIN", 50, (int)(width / 8), height / 2);
        endScene.setOnKeyPressed(e->handleKeyInput(e.getCode()));
    }

    /**
     * drawWinScreen creates and shows the player the win screen.
     * Shows the player their score and the highest score in the game's history.
     * It also sets the event handler to work for this scene when the user presses a key.
     */
    private void drawWinScreen() throws IOException {
        playerWon = true;
        Group winGroup = new Group();
        Text winMessage = new Text();
        winGroup.getChildren().add(winMessage);
        Scene winScene = new Scene(winGroup, width, height, BACKGROUND);
        stage.setScene(winScene);
        updateMaxScore();
        writeHUD(winMessage, "       YOU WIN!\n       YOUR SCORE:\n" + "       " +score + "\n     HIGHEST SCORE:\n" + "       " + maxScore, 40, (int)(width / 8), height / 2);
        winScene.setOnKeyPressed(e->handleKeyInput(e.getCode()));
    }

    /**
     * updateMaxScore changes the maxScore in the game and the maxScore file within the game's module if the player beats the high score.
     * @throws IOException
     */
    private void updateMaxScore() throws IOException {
        File scoreFile = new File("./maxScore/maxScore.txt");
        Scanner sc = new Scanner(scoreFile);
        maxScore = sc.nextInt();
        if(score>maxScore) {
            PrintWriter clearWriter = new PrintWriter("./maxScore/maxScore.txt");
            clearWriter.print(score+"");
            clearWriter.close();
            maxScore = score;
        }
    }

    /**
     * step updates the speed of the ball on collisions with bricks, walls, the paddle, and the ground.
     * step also deletes dead bricks, updates speedy paddle x coordinates, lowers the power ups, updates the game when the paddle gets a power up, and checks to see if the player has finished the level.
     * @param elapsedTime
     * @param text
     * @throws IOException
     */
    private void step (double elapsedTime,Text text) throws IOException  {
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
            checkLevelStatus();

            ball.setX(ball.getX() + BALL_SPEED_X * elapsedTime);
            ball.setY(ball.getY() + BALL_SPEED_Y * elapsedTime);
        }
    }

    /**
     * updateBallWallSpeed updates the ball's speed in both directions when it hits a wall.
     * @param elapsedTime
     */
    private void  updateBallWallSpeed(double elapsedTime) {
        if(ball.getX()<=0  || ball.getX() + ball.getWidth()>= width) {
            BALL_SPEED_X *= -1;
            ball.setX(ball.getX() + 3* BALL_SPEED_X * elapsedTime);
        }
        if(ball.getY()<=0 || (immortality && ball.getY() + ball.getWidth()>= width)) {
            BALL_SPEED_Y *= -1;
            ball.setY(ball.getY() + 3* BALL_SPEED_Y * elapsedTime);
        }
    }

    /**
     * updateOnLostBall updates the ball's speed in both directions when it hits the ground.
     */
    private void  updateOnLostBall() {
        if(ball.getY()>=height) {
            myPADDLE.decreaseHP();
            currentHP--;
            updateLivesText();
            if(myPADDLE.getHP() > 0) {
                handleKeyInput(KeyCode.R);
            }
        }
    }

    /**
     * updatePaddleX updates the paddle's x coordinate if it goes into restricted areas.
     */
    private void  updatePaddleX() {
        if(myPADDLE.getX()<0) {
            myPADDLE.setX(0);
        }
        if(myPADDLE.getX()+PADDLE_WIDTH>width) {
            myPADDLE.setX(width-PADDLE_WIDTH);
        }
    }

    /**
     * updatePaddlePowerUp destroys power up if paddle touches a power up.
     * If destroyed, the power up affects the game by calling the updateGameOnPowerUpActivation method
     * Finally, it is removed from the power up arraylist.
     */
    private void  updatePaddlePowerUp() {
        Iterator<PowerUp> powerUpIterator = powerUpList.iterator();
        while (powerUpIterator.hasNext()) {
            PowerUp powerUp = powerUpIterator.next();
            Shape intersection = Shape.intersect(myPADDLE, powerUp);
            if (intersection.getBoundsInLocal().getWidth() != -1) {
                powerUp.destroyPowerUp();
                updateGameOnPowerUpActivation(powerUp);
                powerUpIterator.remove();
            }
        }
    }

    /**
     * updateGameOnPowerUpActivation updates game state when power up is collected.
     * Either increases the paddle speed, slows down the speed of the ball, or makes the paddle bigger
     * @param powerUp
     */
    private void  updateGameOnPowerUpActivation(PowerUp powerUp) {
        if(powerUp.getTypeArray()[0]) {
            PADDLE_SPEED *= 2;
        }
        else if(powerUp.getTypeArray()[1]) {
            BALL_SPEED_X *= .75;
            BALL_SPEED_Y *= .75;
            BALL_SPEED_TOTAL *= .75;

        }
        else if(powerUp.getTypeArray()[2]) {
            int x_val = (int)myPADDLE.getX();
            int currentLives = myPADDLE.getHP();
            root.getChildren().remove(myPADDLE);
            PADDLE_WIDTH *= 2;
            myPADDLE = new Paddle(x_val, PADDLE_Y_INIT, PADDLE_WIDTH_WITH_POWERUP, PADDLE_HEIGHT, currentLives, PADDLE_COLOR);
            root.getChildren().add(myPADDLE);
        }
    }

    /**
     * updateBallPaddleSpeed updates the ball's speed in both directions when it hits the paddle.
     */
    private void  updateBallPaddleSpeed() {
        Shape intersection = Shape.intersect(myPADDLE, ball);
        if (intersection.getBoundsInLocal().getWidth() != -1) {
            if(ball.getY() < myPADDLE.getY()){

                double ballPaddleXDiff = (myPADDLE.getX()+PADDLE_WIDTH/2) - (ball.getX() + ball.getWidth()/2);

                double normalizedBallPaddleXDiff = (ballPaddleXDiff/(PADDLE_WIDTH));
                double angle = normalizedBallPaddleXDiff * MAX_BOUNCE_ANGLE;

                BALL_SPEED_Y = (int)(-BALL_SPEED_TOTAL * Math.cos(angle));
                BALL_SPEED_X = -1 * (int)(BALL_SPEED_TOTAL * Math.sin(angle));
            }
        }
    }

    /**
     * updateBrickBallSpeed updates the ball's speed in both directions when it hits a brick.
     * @param elapsedTime
     */
    private void  updateBrickBallSpeed(double elapsedTime) {
        for (Brick i : brickList) {
            Shape intersection = Shape.intersect(i, ball);
            if (intersection.getBoundsInLocal().getWidth() != -1 && i.getHP() > 0) {
                if(ball.getY() + ball.getHeight()/2>= i.getY()+i.getHeight() || ball.getY() + ball.getHeight()/2<= i.getY()) {
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

    /**
     * lowerPowerUps updates a power up's y coordinate when it exists (makes it fall down).
     * @param elapsedTime
     */
    private void  lowerPowerUps(double elapsedTime) {
        for(PowerUp powerUp:powerUpList) {
            powerUp.setY(powerUp.getY() + POWER_UP_DESCEND_SPEED * elapsedTime);
        }
    }

    /** returns a brickIterator - was needed since I am removing bricks from a list while I am traversing that same list.
     * @return brickIterator
     */
    private Iterator<Brick> brickIterator() {
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

    /** returns a powerUpIterator - was needed since I am removing power ups from a list while I am traversing that same list.
     * @return powerUpIterator
     */
    private Iterator<PowerUp> powerUpIterator() {
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

    /**
     * deleteDeadBricksCreatePowerUps removes dead bricks from the game and creates power ups
     */
    private void  deleteDeadBricksCreatePowerUps() {
        Iterator<Brick> brickIterator = brickList.iterator();
        while (brickIterator.hasNext()) {
            Brick brick = brickIterator.next();
            if (brick.getHP() == 0) {
                double chanceOfPowerUp = Math.random();
                if(chanceOfPowerUp < .3) {
                    int powerUpType = (int)(Math.random() * 3);
                    PowerUp powerUp = new PowerUp((int)brick.getX(),(int)brick.getY(), powerUpType);
                    powerUpList.add(powerUp);
                    root.getChildren().add(powerUp);
                }
                brickIterator.remove();
            }
        }
    }

    /**
     * checkLevelStatus checks to see if the player has destroyed all the bricks in the level.
     * If they have, the method either creates the next level or it shows the win screen.
     * @throws IOException
     */
    private void checkLevelStatus() throws IOException {
        if(brickList.size() == 0){
            if(currentLevel == levels.length) {
                drawWinScreen();
            }else {
                currentLevel++;
                createLevel();
            }
        }
    }


    /**
     * handleKeyInput is an event handler that takes care of all key press events
     * @param code
     */
    private void handleKeyInput(KeyCode code) {
        handleLeftAndRightKeyPress(code);
        handleEnterKeyPress(code);
        handleDigitKeyPress(code);
        handleRKeyPress(code);
        handleLKeyPress(code);

        if (code == KeyCode.C && pressedEnter) {
            immortality = !immortality;
        }
    }

    private void handleLKeyPress(KeyCode code) {
        if (code == KeyCode.L && pressedEnter) {
            myPADDLE.increaseHP();
            currentHP++;
            updateLivesText();
        }
    }

    private void handleRKeyPress(KeyCode code) {
        if (code == KeyCode.R && pressedEnter) {
            ball.setX(BALL_X_INIT);
            ball.setY(BALL_Y_INIT);
            BALL_SPEED_X = BALL_SPEED_X_INIT;
            BALL_SPEED_Y = BALL_SPEED_Y_INIT;
            myPADDLE.setX(PADDLE_X_INIT);
            myPADDLE.setY(PADDLE_Y_INIT);
        }
    }

    private void handleDigitKeyPress(KeyCode code) {
        if (code.isDigitKey() && pressedEnter) {
            int desiredLevel = Integer.parseInt(code.getChar());
            if((desiredLevel)>=0 && (desiredLevel)<=levels.length) {
                currentLevel = desiredLevel;
                createLevel();
            }
            else if(desiredLevel >levels.length) {
                currentLevel = levels.length;
                createLevel();
            }
        }
    }

    private void handleEnterKeyPress(KeyCode code) {
        if (code == KeyCode.ENTER) {
            pressedEnter = true;
            currentLevel = 1;
            score = 0;
            currentHP = HP_INIT;
            createLevel();
        }
    }

    private void handleLeftAndRightKeyPress(KeyCode code) {
        if ((code == KeyCode.LEFT || code == KeyCode.RIGHT) && pressedEnter) {
            Shape intersection = Shape.intersect(myPADDLE, ball);
            if (intersection.getBoundsInLocal().getWidth() == -1) {
                if (code == KeyCode.RIGHT && myPADDLE.getX() + PADDLE_WIDTH< width ) {
                    myPADDLE.setX(myPADDLE.getX() + PADDLE_SPEED);
                } else if (code == KeyCode.LEFT && myPADDLE.getX() > 0) {
                    myPADDLE.setX(myPADDLE.getX() - PADDLE_SPEED);
                }
            }
        }
    }

    /**
     * writeHUD writes a string into a text object with a font at a certain x, y coordinate
     * @param text
     * @param s
     * @param fontSize
     * @param x
     * @param y
     */
    private void writeHUD(Text text, String s, int fontSize, int x, int y)
    {
        text.setFont(new Font(fontSize));
        text.setText(s);
        text.setX(x);
        text.setY(y);
    }


    private void updateScore(int value) {
        score += value;
        writeHUD(scoreHUD,"Score: "+score,30,0,height/20);
    }

    private void updateLivesText() {
        writeHUD(livesHUD,"Lives: "+ myPADDLE.getHP(),30,0,height/10);
    }

    /**
     * Starts the program.
     */
    public static void main (String[] args) {
        launch(args);
    }
}
