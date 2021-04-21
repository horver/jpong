package bme.pong.entities;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

public abstract class GameObject {
    protected Point2D position;
    protected Point2D startPos;

    public GameObject(int x, int y) {
        position = new Point2D(x, y);
        startPos = position;
    }

    public void update(long deltaT) {
        // TODO: Add Sockets
    }

    public void restart() {
        position = startPos;
    }

    public abstract Rectangle2D getBoundingBox();

    public abstract void draw(GraphicsContext context);

}
