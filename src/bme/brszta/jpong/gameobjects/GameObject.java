package bme.brszta.jpong.gameobjects;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

abstract class GameObject {
    protected Point2D position;

    public GameObject(int x, int y) {
        this.position = new Point2D(x, y);
    }

    public void update(long deltaT) {
        // TODO: Add Sockets
    }

    public abstract Rectangle2D getBoundingBox();

    public abstract void draw(GraphicsContext context);

}
