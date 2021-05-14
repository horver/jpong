package bme.pong.networking.gameevents;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class PlayerKeyDownEvent implements IGameEvent, Serializable {
    public int keyCode;

    public PlayerKeyDownEvent(int key) {
        keyCode = key;
    }

    public String getName() { return "Player key down event with keycode: " + Integer.toString(keyCode); }

    public void writeObject(ObjectOutputStream oos) throws IOException {
        oos.writeInt(keyCode);
    }
    public void readObject(ObjectInputStream ois) throws IOException {
        keyCode = ois.readInt();
    }
}
