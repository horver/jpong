package bme.pong.entities;

import bme.pong.Main;
import bme.pong.networking.gameevents.PlayerKeyDownEvent;
import bme.pong.networking.gameevents.PlayerKeyUpEvent;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyEvent;

public class Player extends Pad {

    public static final double SPEED = 0.7;
    private MoveAction moveAction = MoveAction.IDLE;
    private int keyCounter = 0;

    public Player(int x, int y) {
        super(x, y);
    }

    private void sendKeyAction(MoveAction action, EventType<KeyEvent> keyEvtType) {
        if (keyEvtType == KeyEvent.KEY_PRESSED) {
            Main.eventBus.pushOutgoing(new PlayerKeyDownEvent(action));
        }
        else if (keyEvtType == KeyEvent.KEY_RELEASED) {
            Main.eventBus.pushOutgoing(new PlayerKeyUpEvent(action));
        }
    }

    public void keyHandler(KeyEvent keyEvent) {
        if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED) {
            switch (keyEvent.getCode()) {
                case UP:
                case W:
                    moveAction = MoveAction.MOVE_UP;
                    sendKeyAction(moveAction, keyEvent.getEventType());
                    keyCounter++;
                    break;
                case DOWN:
                case S:
                    moveAction = MoveAction.MOVE_DOWN;
                    sendKeyAction(moveAction, keyEvent.getEventType());
                    keyCounter++;
                    break;
            }
        }

        if (keyEvent.getEventType() == KeyEvent.KEY_RELEASED) {
            switch (keyEvent.getCode()) {
                case UP:
                case W:
                    // Actually it doesn't matter which move action is sent,
                    // because KEY_RELEASED events are used to halt any movement
                    sendKeyAction(MoveAction.MOVE_UP, keyEvent.getEventType());
                case DOWN:
                case S:
                    sendKeyAction(MoveAction.MOVE_DOWN, keyEvent.getEventType());
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
        //System.out.println(keyCounter);
        super.update(deltaT);
    }

    @Override
    public void restart() {
        moveAction = MoveAction.IDLE;
        keyCounter = 0;
        super.restart();
    }
}
