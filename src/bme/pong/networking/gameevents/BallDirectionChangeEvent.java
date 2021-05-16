package bme.pong.networking.gameevents;

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

    public void writeObject(ObjectOutputStream oos) throws IOException {
        // oos.writeObject(velocity) apparently doesn't work
        oos.writeDouble(X);
        oos.writeDouble(Y);
    }

    public void readObject(ObjectInputStream ois) throws IOException {
        X = ois.readDouble();
        Y = ois.readDouble();
    }
}
