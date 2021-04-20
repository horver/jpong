package bme.brszta.jpong.gameobjects;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

public class Pad extends GameObject {
    static final int WIDTH = 16;
    static final int HEIGHT = 64;
    public static final float SPEED = 2;
    private Point2D position = new Point2D(0, 240);

    enum MoveAction {
        IDLE,
        MOVE_UP,
        MOVE_DOWN
    }

    private MoveAction moveAction = MoveAction.IDLE;

    public void keyHandler(KeyEvent keyEvent) {
        if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED) {
            switch (keyEvent.getCode()) {
                case UP:
                case W:
                    moveAction = MoveAction.MOVE_UP;
                    break;
                case DOWN:
                case S:
                    moveAction = MoveAction.MOVE_DOWN;
                    break;
            }
        }

        if (keyEvent.getEventType() == KeyEvent.KEY_RELEASED) {
            switch (keyEvent.getCode()) {
                case UP:
                case W:
                case DOWN:
                case S:
                    moveAction = MoveAction.IDLE;
                    break;
            }
        }
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

    public void draw(GraphicsContext context) {
        context.setFill(Color.WHITE);
        context.fillRect(position.getX(), position.getY(), WIDTH, HEIGHT);
    }
}
