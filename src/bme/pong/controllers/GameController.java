package bme.pong.controllers;

import bme.pong.Main;
import bme.pong.entities.*;
import bme.pong.listeners.OnScoreListener;
import bme.pong.listeners.OnStatisticsListener;
import bme.pong.storages.ScoreManager;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GameController implements OnScoreListener, OnStatisticsListener {
    private AnimationTimer animationTimer;

    @FXML
    private Canvas canvas;

    private long prevTime;
    private Player player;
    private Pad other;
    private Ball ball;
    private GraphicsContext context;
    private final List<GameObject> gameObjects = new ArrayList<>();
    private boolean isPaused = false;
    private final ScoreManager scoreManager = new ScoreManager();

    @FXML
    private Text scoreField;

    @FXML
    private Text nameField;

    @FXML
    private VBox menuPause;

    public GameController() {
        prevTime = System.nanoTime();
        scoreManager.setOnStatisticsListener(this);
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

        onNameTextChange(scoreManager.getPlayerText());

        player = new Player(0, (int) canvas.getHeight() / 2 - Pad.HEIGHT / 2);
        other = new Pad((int) (canvas.getWidth() - Pad.WIDTH), (int) canvas.getHeight() / 2 - Pad.HEIGHT / 2);
        ball = new Ball((int) canvas.getWidth() / 2 - Ball.SIZE / 2, (int) canvas.getHeight() / 2 - Ball.SIZE / 2);

        gameObjects.add(player);
        gameObjects.add(other);
        gameObjects.add(ball);

        ball.addOnScoreListener(this);
        ball.addOnScoreListener(scoreManager);
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

    @FXML
    void actionLoadState(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load game state");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Game state", "*.state"));
        File file = Main.showOpenFileDialog(fileChooser);
        if (file != null && file.exists()) {
            if (!scoreManager.loadState(file)) {
                new Alert(Alert.AlertType.ERROR, "Failed to load!").show();
            }
            resetGame();
        }
    }

    @FXML
    void actionSaveState(ActionEvent event) {
        if (!scoreManager.saveState()) {
            new Alert(Alert.AlertType.ERROR, "Failed to save!").show();
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Saved!").show();
        }
    }

    @Override
    public void onScore(ScoringSide side) {
        resetGame();
    }

    private void resetGame() {
        gameObjects.forEach(GameObject::restart);
    }

    @Override
    public void onScoreTextChange(String newScoreText) {
        scoreField.setText(newScoreText);
    }

    @Override
    public void onNameTextChange(String newNameText) {
        nameField.setText(newNameText);
    }
}
