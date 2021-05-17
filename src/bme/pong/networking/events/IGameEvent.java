package bme.pong.networking.events;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface IGameEvent {
    String getName(); // for debugging

    void writeObject(ObjectOutputStream outputStream) throws IOException;

    void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException;
}
