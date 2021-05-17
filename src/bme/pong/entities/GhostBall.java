package bme.pong.entities;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GhostBall extends GameObject {
    public static final int SIZE = 16;
    public static final double SPEED = 0.4;

    private Point2D velocity;

    public GhostBall(int x, int y) {
        super(x, y);
    }

    public void setDirection(Point2D vec) {
        velocity = vec;
    }

    @Override
    public void restart() {
        System.out.println("Ghost reset");
        super.restart();
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return null;
    }

    public void update(long deltaT) {
        position = position.add(velocity.multiply(deltaT * SPEED).normalize());
    }

    @Override
    public void draw(GraphicsContext context) {
        context.setFill(Color.WHITE);
        context.fillRect(position.getX(), position.getY(), SIZE, SIZE);
    }
}
