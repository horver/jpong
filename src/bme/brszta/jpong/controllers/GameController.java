package bme.brszta.jpong.controllers;

import bme.brszta.jpong.gameobjects.Pad;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

public class GameController {
    private AnimationTimer animationTimer;
    @FXML
    private Canvas canvas;
    private long prevTime;
    private Pad player1;
    private GraphicsContext context;

    public GameController() {

        player1 = new Pad();
        prevTime = System.nanoTime();

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                updater(l);
            }
        };
    }

    @FXML
    void initialize() {
        context = canvas.getGraphicsContext2D();
        // To handle keyboard input
        canvas.setFocusTraversable(true);
        animationTimer.start();
    }

    private void updater(long l) {
        long deltaT = (l - prevTime) / 1000_000;    // Convert to ms
        prevTime = l;

        player1.update(deltaT);

        drawer();
    }

    private void drawer() {
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        player1.draw(context);
    }

    @FXML
    void keyEventHandler(KeyEvent event) {
        player1.keyHandler(event);
    }
}
