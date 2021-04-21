package bme.brszta.jpong.controllers;

import bme.brszta.jpong.Main;
import bme.brszta.jpong.ScoringSide;
import bme.brszta.jpong.gameobjects.Ball;
import bme.brszta.jpong.gameobjects.GameObject;
import bme.brszta.jpong.gameobjects.Pad;
import bme.brszta.jpong.gameobjects.Player;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

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
    private final List<GameObject> gameObjects = new ArrayList<>();
    private boolean isPaused = false;

    @FXML
    private Text scoreField;

    @FXML
    private Text nameField;

    @FXML
    private VBox menuPause;

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

        nameField.setText(Main.propertyStorage.getPlayerName() + " - Waiting for other player");

        player = new Player(0, (int) canvas.getHeight() / 2 - Pad.HEIGHT / 2);
        other = new Pad((int) (canvas.getWidth() - Pad.WIDTH), (int) canvas.getHeight() / 2 - Pad.HEIGHT / 2);
        ball = new Ball((int) canvas.getWidth() / 2 - Ball.SIZE / 2, (int) canvas.getHeight() / 2 - Ball.SIZE / 2);

        gameObjects.add(player);
        gameObjects.add(other);
        gameObjects.add(ball);

        ball.setOnScoreListener(side -> {
            if (side == ScoringSide.PLAYER_LEFT) {
                score2++;
            } else {
                score1++;
            }
            gameObjects.forEach(GameObject::restart);
            scoreField.setText(score1 + " : " + score2);
        });
        animationTimer.start();
    }

    private void updater(long l) {
        long deltaT = (l - prevTime) / 1000_000;    // Convert to ms
        prevTime = l;

        if (!isPaused) {
            player.update(deltaT);
            other.update(deltaT);
            ball.update(deltaT, player.getBoundingBox(), other.getBoundingBox());
        }

        drawer();
    }

    private void drawer() {
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gameObjects.forEach(gameObject -> gameObject.draw(context));
    }

    @FXML
    void keyEventHandler(KeyEvent event) {
        player.keyHandler(event);

        if (event.getEventType() == KeyEvent.KEY_RELEASED) {
            if (event.getCode() == KeyCode.ESCAPE) {
                isPaused = !isPaused;
                menuPause.setVisible(isPaused);
            }
        }
    }

    @FXML
    void actionExit(ActionEvent event) {
        Main.switchScene("mainmenu.fxml");
    }

    @FXML
    void actionResume(ActionEvent event) {
        isPaused = false;
        menuPause.setVisible(false);
    }
}
