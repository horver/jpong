package bme.pong.controllers;

import bme.pong.Main;
import bme.pong.entities.*;
import bme.pong.listeners.OnScoreListener;
import bme.pong.listeners.OnStatisticsListener;
import bme.pong.networking.gameevents.*;
import bme.pong.storages.ScoreManager;
import bme.pong.entities.GameState;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GameController implements OnScoreListener, OnStatisticsListener {

    @FXML
    private Canvas canvas;

    @FXML
    private Text scoreField;

    @FXML
    private Text nameField;

    @FXML
    private Text txtStatus;

    @FXML
    private VBox menuPause;

    private AnimationTimer animationTimer;
    private long prevTime;
    private Player player;
    private Pad other;
    private Ball ball;
    private GraphicsContext context;
    private final List<GameObject> gameObjects = new ArrayList<>();
    private boolean isPaused = false;
    private boolean isStarted = false;
    private final ScoreManager scoreManager = new ScoreManager();

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

    private void handleGameStateChange(GameState gs) {
        switch (gs) {
            case PAUSED:
                isPaused = true;
                menuPause.setVisible(isPaused);
                break;
            case IN_PROGRESS:
                isPaused = false;
                menuPause.setVisible(isPaused);
                break;
            case GAME_OVER:
                // Show "You won/lost" message and go back to the main menu
                break;
            case OPPONENT_QUIT:
                System.out.println("Opponent ragequit");
                // show main menu
                break;
            default: throw new RuntimeException("lolwut");

        }
    }

    private void pollEventBus() {
        IGameEvent event = Main.eventBus.popIncoming();
        if (null == event) {
            return;
        }

        if (event instanceof PlayerKeyDownEvent) {
            // Move enemy pad up or down depending on the value of ((PlayerKeyDown) event).action
        }
        else if (event instanceof PlayerKeyUpEvent) {
            // Stop enemy pad movement, regardless of direction
        }
        else if (event instanceof GameStateChangeEvent) {
            handleGameStateChange(((GameStateChangeEvent) event).newState);
        }
        else if (event instanceof ConnectionLostEvent) {
            System.out.println("The connection to the opponent was lost");
            // Go back to the main menu
        }
    }

    private void updater(long l) {
        long deltaT = (l - prevTime) / 1000_000;    // Convert to ms
        prevTime = l;

        if (!isPaused && isStarted) {
            player.update(deltaT);
            other.update(deltaT);
            ball.update(deltaT, player.getBoundingBox(), other.getBoundingBox());
        }

        pollEventBus();
        drawer();
    }

    private void drawer() {
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gameObjects.forEach(gameObject -> gameObject.draw(context));
    }

    private void resetGame() {
        gameObjects.forEach(GameObject::restart);
        txtStatus.setVisible(false);
    }

    @FXML
    void keyEventHandler(KeyEvent event) {
        player.keyHandler(event);

        if (event.getEventType() == KeyEvent.KEY_RELEASED) {
            switch (event.getCode()) {
                case ESCAPE:
                    isPaused = !isPaused;
                    menuPause.setVisible(isPaused);
                    GameState gs = isPaused ? GameState.PAUSED : GameState.IN_PROGRESS;
                    Main.eventBus.pushOutgoing(new GameStateChangeEvent(gs));
                    break;
                case SPACE:
                    if (!isStarted) {
                        txtStatus.setVisible(false);
                    }
                    isStarted = true;
                    break;
                case F7:
                    scoreManager.saveState();
                    break;
            }
        }
    }

    @FXML
    void actionExit(ActionEvent event) {
        Main.eventBus.pushOutgoing(new GameStateChangeEvent(GameState.OPPONENT_QUIT));
        Main.switchScene("mainmenu.fxml");
    }

    @FXML
    void actionResume(ActionEvent event) {
        isPaused = false;
        Main.eventBus.pushOutgoing(new GameStateChangeEvent(GameState.IN_PROGRESS));
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
            isStarted = false;
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
        //isStarted = false;
        txtStatus.setVisible(true);
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
