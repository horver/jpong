package bme.pong.networking.events;

import javafx.geometry.Point2D;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class BallDirectionChangeEvent implements IGameEvent, Serializable {
    public double X, Y;

    public BallDirectionChangeEvent(Point2D v) {
        X = v.getX();
        Y = v.getY();
    }

    public String getName() {
        return "Ball direction change event";
    }

    public Point2D getPoint() {
        return new Point2D(X, Y);
    }

    @Override
    public void writeObject(ObjectOutputStream outputStream) throws IOException {
        // oos.writeObject(velocity) apparently doesn't work
        outputStream.writeDouble(X);
        outputStream.writeDouble(Y);
    }

    @Override
    public void readObject(ObjectInputStream inputStream) throws IOException {
        X = inputStream.readDouble();
        Y = inputStream.readDouble();
    }
}
