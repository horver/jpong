package bme.pong.networking.gameevents;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ConnectionRequestEvent implements IGameEvent, Serializable {
    public String guestName;

    public ConnectionRequestEvent(String name) {
        guestName = name;
    }

    public void writeObject(ObjectOutputStream oos) throws IOException {
        oos.writeUTF(guestName);
    }
    public void readObject(ObjectInputStream ois) throws IOException {
        guestName = ois.readUTF();
    }
}
