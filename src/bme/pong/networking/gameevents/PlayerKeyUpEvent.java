package bme.pong.networking.gameevents;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class PlayerKeyUpEvent implements IGameEvent, Serializable {
    public int keyCode;

    public PlayerKeyUpEvent(int key) {
        keyCode = key;
    }

    public void writeObject(ObjectOutputStream oos) throws IOException {
        oos.writeInt(keyCode);
    }
    public void readObject(ObjectInputStream ois) throws IOException {
        keyCode = ois.readInt();
    }
}
