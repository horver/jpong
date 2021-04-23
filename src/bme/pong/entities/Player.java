package bme.pong.entities;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyEvent;

public class Player extends Pad {

    public static final double SPEED = 0.7;
    private MoveAction moveAction = MoveAction.IDLE;
    private int keyCounter = 0;

    public Player(int x, int y) {
        super(x, y);
    }

    public void keyHandler(KeyEvent keyEvent) {
        if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED) {
            switch (keyEvent.getCode()) {
                case UP:
                case W:
                    moveAction = MoveAction.MOVE_UP;
                    keyCounter++;
                    break;
                case DOWN:
                case S:
                    moveAction = MoveAction.MOVE_DOWN;
                    keyCounter++;
                    break;
            }
        }

        if (keyEvent.getEventType() == KeyEvent.KEY_RELEASED) {
            switch (keyEvent.getCode()) {
                case UP:
                case W:
                case DOWN:
                case S:
                    keyCounter--;
                    break;
            }
            if (keyCounter == 0) {
                moveAction = MoveAction.IDLE;
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
        System.out.println(keyCounter);
        super.update(deltaT);
    }

    @Override
    public void restart() {
        moveAction = MoveAction.IDLE;
        keyCounter = 0;
        super.restart();
    }
}
