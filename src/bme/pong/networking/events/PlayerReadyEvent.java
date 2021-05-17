package bme.pong.networking.events;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class PlayerReadyEvent implements IGameEvent, Serializable {

    public String getName() {
        return "Game started";
    }

    @Override
    public void writeObject(ObjectOutputStream outputStream) {
    }

    @Override
    public void readObject(ObjectInputStream inputStream) {
    }
}
