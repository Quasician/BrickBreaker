package breakout;

import javafx.scene.paint.Paint;

public class Paddle extends GameObject{
    public Paddle(int x, int y, int width, int height, int hp, Paint color) {
        super(x, y, width, height);
        this.width = width;
        this.height = height;
        super.setFill(color);
        this.hp = hp;
    }

    public void decreaseHP()
    {
        hp--;
    }

    public void increaseHP()
    {
        hp++;
    }

    public void increasePaddleSize()
    {
        width *=2;
    }

    public int getHP()
    {
        return hp;
    }

}
