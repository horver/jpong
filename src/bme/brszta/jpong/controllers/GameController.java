package bme.brszta.jpong.controllers;

import bme.brszta.jpong.ScoringSide;
import bme.brszta.jpong.gameobjects.Ball;
import bme.brszta.jpong.gameobjects.Pad;
import bme.brszta.jpong.gameobjects.Player;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class GameController {
    private AnimationTimer animationTimer;

    @FXML
    private Canvas canvas;

    private long prevTime;
    private Player player;
    private Pad other;
    private Ball ball;
    private GraphicsContext context;
    private int score1 = 0;
    private int score2 = 0;

    @FXML
    private Text scoreField;

    public GameController() {
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

        player = new Player(0, (int) canvas.getHeight() / 2 - Pad.HEIGHT / 2);
        other = new Pad((int) (canvas.getWidth() - Pad.WIDTH), (int) canvas.getHeight() / 2 - Pad.HEIGHT / 2);
        ball = new Ball((int) canvas.getWidth() / 2 - Ball.SIZE / 2, (int) canvas.getHeight() / 2 - Ball.SIZE / 2);
        ball.setOnScoreListener(side -> {
            if (side == ScoringSide.PLAYER_LEFT) {
                score2++;
            } else {
                score1++;
            }
            scoreField.setText(score1 + " : " + score2);
        });
        animationTimer.start();
    }

    private void updater(long l) {
        long deltaT = (l - prevTime) / 1000_000;    // Convert to ms
        prevTime = l;

        player.update(deltaT);
        other.update(deltaT);
        ball.update(deltaT, player.getBoundingBox(), other.getBoundingBox());

        drawer();
    }

    private void drawer() {
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        player.draw(context);
        other.draw(context);
        ball.draw(context);
    }

    @FXML
    void keyEventHandler(KeyEvent event) {
        player.keyHandler(event);
    }
}
