package bme.pong.entities;

import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.List;

public class Player extends Pad {

    public static final double SPEED = 0.7;
    private MoveAction moveAction = MoveAction.IDLE;
    private final List<KeyCode> keys = new ArrayList<>();

    public Player(int x, int y) {
        super(x, y);
    }

    public void keyHandler(KeyEvent keyEvent) {
        EventType<KeyEvent> type = keyEvent.getEventType();
        switch (keyEvent.getCode()) {
            case UP:
            case W:
            case DOWN:
            case S:
                if (type == KeyEvent.KEY_PRESSED) {
                    if (!keys.contains(keyEvent.getCode())) {
                        keys.add(keyEvent.getCode());
                    }
                }
                if (type == KeyEvent.KEY_RELEASED) {
                    if (keys.contains(keyEvent.getCode())) {
                        keys.remove(keyEvent.getCode());
                    }
                }
                break;
        }

        if (keys.contains(KeyCode.W) || keys.contains(KeyCode.UP)) {
            moveAction = MoveAction.MOVE_UP;
        }

        if (keys.contains(KeyCode.S) || keys.contains(KeyCode.DOWN)) {
            moveAction = MoveAction.MOVE_DOWN;
        }

        if (keys.isEmpty() || keys.size() > 2) {
            moveAction = MoveAction.IDLE;
            keys.clear();
        }
    }

    @Override
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
        super.update(deltaT);
    }

    @Override
    public void restart() {
        moveAction = MoveAction.IDLE;
        keys.clear();
        super.restart();
    }
}
