package bme.brszta.jpong.gameobjects;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyEvent;

public class Player extends Pad {

    private MoveAction moveAction = MoveAction.IDLE;

    public Player(int x, int y) {
        super(x, y);
    }

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
}
