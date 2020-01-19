package breakout;

import javafx.scene.paint.Paint;

public class Ball extends GameObject{

    public Ball(int x, int y, int width, int height, Paint color) {
        super(x, y, width, height);
        super.setFill(color);
    }

    public void decreaseHP()
    {
        hp--;
    }

    public int getHP()
    {
        return hp;
    }

}
