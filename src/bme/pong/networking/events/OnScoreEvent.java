package bme.pong.networking.events;

import bme.pong.entities.ScoringSide;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class OnScoreEvent implements IGameEvent, Serializable {
    private ScoringSide scoringSide;

    public OnScoreEvent(ScoringSide scoringSide) {
        this.scoringSide = scoringSide;
    }

    public ScoringSide getScoringSide() {
        return scoringSide;
    }

    @Override
    public String getName() {
        return "Score event";
    }

    @Override
    public void writeObject(ObjectOutputStream outputStream) throws IOException {
        outputStream.writeObject(scoringSide);
    }

    @Override
    public void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        scoringSide = (ScoringSide) inputStream.readObject();
    }
}
