package bme.brszta.jpong.gameobjects;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Pad extends GameObject {
    public static final int WIDTH = 16;
    public static final int HEIGHT = 64;
    public static final double SPEED = 2;

    public Pad(int x, int y) {
        super(x, y);
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), WIDTH, HEIGHT);
    }

    @Override
    public void draw(GraphicsContext context) {
        context.setFill(Color.WHITE);
        context.fillRect(position.getX(), position.getY(), WIDTH, HEIGHT);
    }
}
