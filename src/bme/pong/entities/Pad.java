package bme.pong.entities;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Pad extends GameObject {

    public static final int WIDTH = 16;
    public static final int HEIGHT = 64;
    public static final double SPEED = 0.7;
    protected MoveAction moveAction = MoveAction.IDLE;

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

    public void setMoveAction(MoveAction ma) {
        this.moveAction = ma;
    }

    public void update(long deltaT) {
        switch (moveAction) {
            case MOVE_UP:
                position = position.subtract(0, deltaT * SPEED);
                if (position.getY() < 0) {
                    position = new Point2D(position.getX(), 0);
                }
                break;
            case MOVE_DOWN:
                position = position.add(0, deltaT * SPEED);
                if (position.getY() > 480 - HEIGHT) {
                    position = new Point2D(position.getX(), 480 - HEIGHT);
                }
                break;
        }
    }

    @Override
    public void restart() {
        moveAction = MoveAction.IDLE;
        super.restart();
    }
}
