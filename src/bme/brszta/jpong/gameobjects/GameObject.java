package bme.brszta.jpong.gameobjects;

import javafx.geometry.Point2D;

public class GameObject {
    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    private Point2D position;

}
