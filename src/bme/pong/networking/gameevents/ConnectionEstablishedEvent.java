package bme.pong.networking.gameevents;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ConnectionEstablishedEvent implements IGameEvent, Serializable {
    public String guestName;
    public String hostName;

    public ConnectionEstablishedEvent(String guestPlayerName, String hostPlayerName) {
        guestName = guestPlayerName;
        hostName = hostPlayerName;
    }

    public void writeObject(ObjectOutputStream oos) throws IOException {
        oos.writeUTF(guestName);
        oos.writeUTF(hostName);
    }
    public void readObject(ObjectInputStream ois) throws IOException {
        guestName = ois.readUTF();
        hostName = ois.readUTF();
    }
}
