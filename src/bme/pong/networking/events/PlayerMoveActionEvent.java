package bme.pong.networking.events;

import bme.pong.entities.MoveAction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class PlayerMoveActionEvent implements IGameEvent, Serializable {
    public MoveAction action;

    public PlayerMoveActionEvent(MoveAction action) {
        this.action = action;
    }

    @Override
    public String getName() {
        return "Player Move Action Event";
    }

    @Override
    public void writeObject(ObjectOutputStream outputStream) throws IOException {
        outputStream.writeObject(action);
    }

    @Override
    public void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        action = (MoveAction) inputStream.readObject();
    }
}
