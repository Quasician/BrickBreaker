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
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 * A basic example JavaFX program for the first lab.
 *
 * @author Robert C. Duvall
 */
public class Main extends Application {
    public static final String TITLE = "Thomas's Breakout Game";
    public static final int SIZE = 600;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final Paint BACKGROUND = Color.AZURE;
    public static final Paint HIGHLIGHT = Color.OLIVEDRAB;
    public static final String BALL_IMAGE = "ball.gif";
    public static int BALL_SPEED_X = 100;
    public static int BALL_SPEED_Y = 100;
    public static final int BALL_RADIUS = 6;
    public static final Paint PADDLE_COLOR = Color.PLUM;
    public static final int PADDLE_WIDTH = 75;
    public static final int PADDLE_HEIGHT = 50/3;
    public static final int PADDLE_SPEED = 5;
    public static final double PADDLE_CORNER_THRESHOLD = BALL_RADIUS/1.3;
    public static final Paint GROWER_COLOR = Color.BISQUE;
    public static final double GROWER_RATE = 1.1;
    public static final int GROWER_SIZE = 50;

    // some things needed to remember during game
    private Scene myScene;
    // private ImageView myBouncer;
    private Rectangle myPADDLE;
    private Rectangle myGrower;
    private Circle ball;
    private Boolean playerLost;
    private int score;

    /**
     * Initialize what will be displayed and how it will be updated.
     */

    @Override
    public void start (Stage stage) throws InterruptedException {
        // attach scene to the stage and display it
        myScene = setupGame(SIZE, SIZE, BACKGROUND);
        stage.setScene(myScene);
        stage.setTitle(TITLE);
        stage.show();

        // attach "game loop" to timeline to play it (basically just calling step() method repeatedly forever)
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    // Create the game's "scene": what shapes will be in the game and their starting properties
    private Scene setupGame (int width, int height, Paint background) {
        // create one top level collection to organize the things in the scene
        Group root = new Group();
        BorderPane rootPane = new BorderPane();
        rootPane.setPrefSize(SIZE, SIZE*.7);
        Group rotateGroup = new Group();
        // make some shapes and set their properties
        //Image image = new Image(this.getClass().getClassLoader().getResourceAsStream(BOUNCER_IMAGE));
        //myBouncer = new ImageView(image);
        ball = new Circle(width / 2 - PADDLE_HEIGHT / 2 ,3.5* height / 5 ,BALL_RADIUS);
        // x and y represent the top left corner, so center it in window
//        myBouncer.setX(width / 2 - myBouncer.getBoundsInLocal().getWidth() / 2);
//        myBouncer.setY(height / 2 - myBouncer.getBoundsInLocal().getHeight() / 2);
        myPADDLE = new Rectangle(width / 2 - PADDLE_HEIGHT / 2, 4* height / 5, PADDLE_WIDTH, PADDLE_HEIGHT);
        myPADDLE.setFill(PADDLE_COLOR);
//        myGrower = new Rectangle(width / 2 - GROWER_SIZE / 2, height / 2 + 50, GROWER_SIZE, GROWER_SIZE);
//        myGrower.setFill(GROWER_COLOR);
        // order added to the group is the order in which they are drawn
        //root.getChildren().add(myBouncer);

        // Creating cool rectangle brick that rotates
        Rectangle rect = new Rectangle (300, 200, 50, 50);
        rect.setArcHeight(15);
        rect.setArcWidth(15);
        rect.setFill(Color.RED);

        Rectangle rect2 = new Rectangle (300, 300, 50, 50);
        rect2.setArcHeight(15);
        rect2.setArcWidth(15);
        rect2.setFill(Color.GREEN);

        rotateGroup.getChildren().addAll(rect, rect2);
        rootPane.setCenter(rotateGroup);

        RotateTransition rt2 = new RotateTransition(Duration.millis(3000), rect2);
        rt2.setByAngle(180);
        rt2.setCycleCount(Animation.INDEFINITE);
        rt2.setAutoReverse(true);

        RotateTransition rt = new RotateTransition(Duration.millis(3000), rect);
        rt.setByAngle(180);
        rt.setCycleCount(Animation.INDEFINITE);
        rt.setAutoReverse(true);

        RotateTransition grouprt = new RotateTransition(Duration.millis(3000), rotateGroup);
        grouprt.setByAngle(360);
        grouprt.setCycleCount(Animation.INDEFINITE);
        grouprt.setAutoReverse(false);

        rt2.play();
        rt.play();
        grouprt.play();



        root.getChildren().add(ball);
        root.getChildren().add(myPADDLE);
        root.getChildren().add(rootPane);
        //root.getChildren().add(rect);
        // create a place to see the shapes
        Scene scene = new Scene(root, width, height, background);
        // respond to input
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        scene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
        return scene;
    }

//    private Path createEllipsePath(double centerX, double centerY, double radiusX, double radiusY, double rotate) {
//        ArcTo arcTo = new ArcTo();
//        arcTo.setX(centerX - radiusX + 1); // to simulate a full 360 degree celcius circle.
//        arcTo.setY(centerY - radiusY);
//        arcTo.setSweepFlag(false);
//        arcTo.setLargeArcFlag(true);
//        arcTo.setRadiusX(radiusX);
//        arcTo.setRadiusY(radiusY);
//        arcTo.setXAxisRotation(rotate);
//
//        Path path = Pathbuilder.create()
//                .elements(
//                        new MoveTo(centerX - radiusX, centerY - radiusY),
//                        arcTo,
//                        new ClosePath()) // close 1 px gap.
//                .build();
//        path.setStroke(Color.DODGERBLUE);
//        path.getStrokeDashArray().setAll(5d, 5d);
//        return path;
//    }

    // Change properties of shapes in small ways to animate them over time
    // Note, there are more sophisticated ways to animate shapes, but these simple ways work fine to start
    private void step (double elapsedTime) {
        // update "actors" attributes

        if(ball.getCenterX()-ball.getRadius()<=0  || ball.getCenterX() + ball.getRadius()>= SIZE)
        {
            BALL_SPEED_X *= -1;
        }
        if(ball.getCenterY() - ball.getRadius()<=0)
        {
            BALL_SPEED_Y *= -1;
        }
        // ground boundary condition
        // ball.getCenterY() + ball.getRadius()>= SIZE

        //myPADDLE.setRotate(myPADDLE.getRotate() - 1);
        //myGrower.setRotate(myGrower.getRotate() + 1);

        // check for collisions
        // with shapes, can check precisely
        // NEW Java 10 syntax that simplifies things (but watch out it can make code harder to understand)
        // \var intersection = Shape.intersect(myPADDLE, myGrower);
        Shape intersection = Shape.intersect(myPADDLE, ball);
        if (intersection.getBoundsInLocal().getWidth() != -1) {
            if(ball.getCenterY()>= myPADDLE.getY()-PADDLE_CORNER_THRESHOLD && ball.getCenterY()<= myPADDLE.getY()+myPADDLE.getHeight()+ PADDLE_CORNER_THRESHOLD)
            {
                BALL_SPEED_X *= -1;
                //System.out.println("CHANGING X: " + BALL_SPEED_X);
            }
            if(ball.getCenterX()>= myPADDLE.getX()-PADDLE_CORNER_THRESHOLD  && ball.getCenterX()<= myPADDLE.getX()+myPADDLE.getWidth()+ PADDLE_CORNER_THRESHOLD){
                BALL_SPEED_Y *= -1;
                //System.out.println("CHANGING Y: " + BALL_SPEED_Y);
            }
        }

//        // with images can only check bounding box
//        if (myGrower.getBoundsInParent().intersects(myBouncer.getBoundsInParent())) {
//            myGrower.setFill(HIGHLIGHT);
//        }
//        else {
//            myGrower.setFill(GROWER_COLOR);
//        }
        ball.setCenterX(ball.getCenterX() + BALL_SPEED_X * elapsedTime);
        ball.setCenterY(ball.getCenterY() + BALL_SPEED_Y * elapsedTime);
    }

    // What to do each time a key is pressed
    private void handleKeyInput (KeyCode code) {
        if (code == KeyCode.RIGHT && myPADDLE.getX()<SIZE-myPADDLE.getBoundsInLocal().getWidth()) {
            myPADDLE.setX(myPADDLE.getX() + PADDLE_SPEED);
        }
        else if (code == KeyCode.LEFT && myPADDLE.getX()> 0) {
            myPADDLE.setX(myPADDLE.getX() - PADDLE_SPEED);
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

    // What to do each time a key is pressed
    private void handleMouseInput (double x, double y) {
//        if (myGrower.contains(x, y)) {
//            myGrower.setScaleX(myGrower.getScaleX() * GROWER_RATE);
//            myGrower.setScaleY(myGrower.getScaleY() * GROWER_RATE);
//        }
    }

    /**
     * Start the program.
     */
    public static void main (String[] args) {
        launch(args);
    }
}
