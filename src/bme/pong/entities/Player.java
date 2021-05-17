package bme.pong.entities;

import bme.pong.Main;
import bme.pong.networking.events.PlayerMoveActionEvent;
import javafx.event.EventType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.List;

public class Player extends Pad {

    private final List<KeyCode> keys = new ArrayList<>();

    public Player(int x, int y) {
        super(x, y);
    }

    private void sendKeyAction(MoveAction action) {
        Main.eventBus.pushOutgoing(new PlayerMoveActionEvent(action));
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
                    keys.remove(keyEvent.getCode());
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

        sendKeyAction(this.moveAction);
    }

    @Override
    public void restart() {
        moveAction = MoveAction.IDLE;
        keys.clear();
        super.restart();
    }
}
