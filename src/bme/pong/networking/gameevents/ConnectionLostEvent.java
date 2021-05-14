package bme.pong.networking.gameevents;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ConnectionLostEvent implements IGameEvent, Serializable {
    public void writeObject(ObjectOutputStream oos) {}
    public void readObject(ObjectInputStream ois)  {}
}
