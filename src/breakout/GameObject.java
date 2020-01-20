package breakout;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;


public class GameObject extends Rectangle {
    protected int hp;
    protected int x_init;
    protected int y_init;
    protected int width;
    protected int height;
    protected Paint color;

    public GameObject(int x, int y, int width, int height) {
        super(x, y, width, height);
    }
}
