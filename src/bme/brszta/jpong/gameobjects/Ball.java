package bme.brszta.jpong.gameobjects;

import bme.brszta.jpong.OnScoreListener;
import bme.brszta.jpong.ScoringSide;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball extends GameObject {
    public static final int SIZE = 16;
    public static final double SPEED = 0.7;

    private Point2D velocity;
    private OnScoreListener listener;

    public Ball(int x, int y) {
        super(x, y);
        randomDirection();
    }

    private void randomDirection() {
        velocity = new Point2D(-1 + Math.random() * 2, -1 + Math.random() * 2).normalize();
    }

    @Override
    public void restart() {
        randomDirection();
        super.restart();
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), SIZE, SIZE);
    }

    public void update(long deltaT, Rectangle2D playerBoundingBox, Rectangle2D otherBoundingBox) {

        if (position.getY() < 0 || position.getY() > 480 - SIZE) {
            velocity = new Point2D(velocity.getX(), -velocity.getY());
        }

        Rectangle2D boundingBox = getBoundingBox();

        if (playerBoundingBox.intersects(boundingBox) || otherBoundingBox.intersects(boundingBox)) {
            velocity = new Point2D(-velocity.getX(), velocity.getY());
        }

        if (position.getX() < 0) {
            velocity = new Point2D(-velocity.getX(), velocity.getY());
            if (listener != null) {
                listener.onScore(ScoringSide.PLAYER_LEFT);
            }
        }

        if (position.getX() > 640 - SIZE) {
            velocity = new Point2D(-velocity.getX(), velocity.getY());
            if (listener != null) {
                listener.onScore(ScoringSide.PLAYER_RIGHT);
            }
        }

        position = position.add(velocity.multiply(deltaT * SPEED).normalize());

        super.update(deltaT);
    }

    public void setOnScoreListener(OnScoreListener listener) {
        this.listener = listener;
    }

    @Override
    public void draw(GraphicsContext context) {
        context.setFill(Color.WHITE);
        context.fillRect(position.getX(), position.getY(), SIZE, SIZE);
    }
}
