package bme.pong.entities;

import bme.pong.listeners.OnScoreListener;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Ball extends GameObject {
    public static final int SIZE = 16;
    public static final double SPEED = 0.4;

    private Point2D velocity;
    private final List<OnScoreListener> listeners = new ArrayList<>();

    public Ball(int x, int y) {
        super(x, y);
    }

    public Point2D getDirection() {
        return velocity;
    }

    public void setDirection(Point2D vec) {
        velocity = vec;
    }

    public void setRandomDirection() {
        velocity = new Point2D(-1 + Math.random() * 2, -1 + Math.random() * 2).normalize();

        // Handling edge cases: zero velocity, or only mostly one directional velocity
        if (velocity == Point2D.ZERO ||
                velocity.angle(0, 1) < 15 ||
                velocity.angle(0, -1) < 15 ||
                velocity.angle(1, 0) < 15 ||
                velocity.angle(-1, 0) < 15
        ) {
            velocity = new Point2D(0.5, 0.5).normalize();
        }
    }

    @Override
    public void restart() {
        //randomDirection();
        super.restart();
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), SIZE, SIZE);
    }

    public void update(long deltaT, Rectangle2D playerBoundingBox, Rectangle2D otherBoundingBox) {

        Rectangle2D boundingBox = getBoundingBox();

        if (playerBoundingBox.intersects(boundingBox) || otherBoundingBox.intersects(boundingBox)) {
            velocity = new Point2D(-velocity.getX(), velocity.getY());
        }

        if (position.getY() < 0 || position.getY() > 480 - SIZE) {
            velocity = new Point2D(velocity.getX(), -velocity.getY());
        }

        if (position.getX() < 0 || position.getX() > 640 - SIZE) {
            velocity = new Point2D(-velocity.getX(), velocity.getY());
            ScoringSide side = position.getX() < 0 ? ScoringSide.PLAYER_SIDE : ScoringSide.OTHER_SIDE;
            for (OnScoreListener listener : listeners) {
                listener.onScore(side);
            }
        }

        position = position.add(velocity.multiply(deltaT * SPEED).normalize());
        super.update(deltaT);
    }

    public void addOnScoreListener(OnScoreListener listener) {
        if (listener == null) {
            return;
        }

        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void draw(GraphicsContext context) {
        context.setFill(Color.WHITE);
        context.fillRect(position.getX(), position.getY(), SIZE, SIZE);
    }
}
