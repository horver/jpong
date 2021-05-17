package bme.pong.networking.events;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ConnectionEstablishedEvent implements IGameEvent, Serializable {
    private String guestName;
    private String hostName;
    private int targetGoal;

    public ConnectionEstablishedEvent(String guestPlayerName, String hostPlayerName, int targetGoal) {
        guestName = guestPlayerName;
        hostName = hostPlayerName;
        this.targetGoal = targetGoal;
    }

    public String getName() {
        return "Connection established. Players (host - guest): " + hostName + " - " + guestName;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getTargetGoal() {
        return targetGoal;
    }

    public void setTargetGoal(int targetGoal) {
        this.targetGoal = targetGoal;
    }

    @Override
    public void writeObject(ObjectOutputStream outputStream) throws IOException {
        outputStream.writeUTF(guestName);
        outputStream.writeUTF(hostName);
    }

    @Override
    public void readObject(ObjectInputStream inputStream) throws IOException {
        guestName = inputStream.readUTF();
        hostName = inputStream.readUTF();
    }
}
