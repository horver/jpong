package bme.pong.networking.events;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ConnectionRequestEvent implements IGameEvent, Serializable {
    public String guestName;

    public ConnectionRequestEvent(String name) {
        guestName = name;
    }

    public String getName() {
        return "Connection request event, guest name: " + guestName;
    }

    @Override
    public void writeObject(ObjectOutputStream outputStream) throws IOException {
        outputStream.writeUTF(guestName);
    }

    @Override
    public void readObject(ObjectInputStream inputStream) throws IOException {
        guestName = inputStream.readUTF();
    }
}
