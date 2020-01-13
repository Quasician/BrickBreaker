package breakout;

import javafx.scene.paint.Paint;

public class paddle extends gameObject{
    public paddle(int x, int y, int width, int height, int hp, Paint color) {
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
