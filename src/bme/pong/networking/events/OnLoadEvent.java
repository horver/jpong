package bme.pong.networking.events;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class OnLoadEvent implements IGameEvent, Serializable {
    private int playerScore;
    private int otherScore;

    public OnLoadEvent(int playerScore, int otherScore) {
        this.playerScore = playerScore;
        this.otherScore = otherScore;
    }

    @Override
    public String getName() {
        return "On load event";
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public int getOtherScore() {
        return otherScore;
    }

    public void setOtherScore(int otherScore) {
        this.otherScore = otherScore;
    }

    @Override
    public void writeObject(ObjectOutputStream outputStream) throws IOException {
        outputStream.writeInt(playerScore);
        outputStream.writeInt(otherScore);
    }

    @Override
    public void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        this.playerScore = inputStream.readInt();
        this.otherScore = inputStream.readInt();
    }
}
