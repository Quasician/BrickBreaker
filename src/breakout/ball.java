package breakout;

import javafx.scene.paint.Paint;

public class ball extends gameObject{
    public ball(int x, int y, int width, int height, Paint color) {
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
