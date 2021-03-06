package bme.pong.controllers;

import bme.pong.Main;
import bme.pong.entities.*;
import bme.pong.listeners.OnScoreListener;
import bme.pong.listeners.OnStatisticsListener;
import bme.pong.networking.events.*;
import bme.pong.storages.ScoreManager;
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
import java.util.logging.Logger;

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

    private final AnimationTimer animationTimer;
    private long prevTime;
    private Player player;
    private Pad opponent;
    private Ball ball;
    private GhostBall ghostBall;
    private GraphicsContext context;
    private final List<GameObject> gameObjects = new ArrayList<>();
    private boolean isPaused = false;
    private boolean isStarted = false;
    private boolean playerReady = false, opponentReady = false;
    private final ScoreManager scoreManager = new ScoreManager();
    private final Logger logger;

    public GameController() {
        prevTime = System.nanoTime();
        scoreManager.setOnStatisticsListener(this);
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                updater(l);
            }
        };
        logger = Logger.getLogger(this.getClass().getName());
    }

    @FXML
    void initialize() {
        context = canvas.getGraphicsContext2D();
        // To handle keyboard input
        canvas.setFocusTraversable(true);

        onNameTextChange(scoreManager.getPlayerText());

        // Side depends on server-client mode
        if (Main.propertyStorage.isClient()) {
            player = new Player(0, (int) canvas.getHeight() / 2 - Pad.HEIGHT / 2);
            opponent = new Pad((int) (canvas.getWidth() - Pad.WIDTH), (int) canvas.getHeight() / 2 - Pad.HEIGHT / 2);
            ghostBall = new GhostBall((int) canvas.getWidth() / 2 - Ball.SIZE / 2, (int) canvas.getHeight() / 2 - Ball.SIZE / 2);
        } else {
            player = new Player((int) (canvas.getWidth() - Pad.WIDTH), (int) canvas.getHeight() / 2 - Pad.HEIGHT / 2);
            opponent = new Pad(0, (int) canvas.getHeight() / 2 - Pad.HEIGHT / 2);
            ball = new Ball((int) canvas.getWidth() / 2 - Ball.SIZE / 2, (int) canvas.getHeight() / 2 - Ball.SIZE / 2);
        }

        if (!Main.propertyStorage.isClient()) {
            ball.setRandomDirection();
            ball.addOnScoreListener(this);
            ball.addOnScoreListener(scoreManager);
            gameObjects.add(ball);
        } else {
            gameObjects.add(ghostBall);
            logger.info("ghostBall added");
        }

        gameObjects.add(player);
        gameObjects.add(opponent);

        animationTimer.start();
    }

    private void handleGameStateChange(GameState gameState) {
        switch (gameState) {
            case PAUSED:
                isPaused = true;
                menuPause.setVisible(isPaused);
                break;
            case IN_PROGRESS:
                isPaused = false;
                menuPause.setVisible(isPaused);
                break;
            case GAME_OVER:
                resetFlags();
                String winner = scoreManager.getWinner();
                new Alert(Alert.AlertType.INFORMATION, "You're winner!" + winner).show();
                Main.threadMgr.nukeAll();
                Main.hasNetworkingFailed.set(false);
                Main.isNetworkingReady.set(false);
                Main.switchScene("mainmenu.fxml");
                break;
            case OPPONENT_QUIT:
                logger.info("Opponent ragequit");
                new Alert(Alert.AlertType.ERROR, "Opponent quit!").show();
                Main.switchScene("mainmenu.fxml");
                break;
            default:
                throw new RuntimeException("lolwut");
        }
    }

    private void resetFlags() {
        isStarted = false;
        playerReady = false;
        opponentReady = false;
    }

    private void handleConnectionEstablished(ConnectionEstablishedEvent event) {
        scoreManager.setOtherName(event.getHostName());
        scoreManager.setPlayerName(event.getGuestName());
        // Send ball initial direction vector to the guest
        if (!Main.propertyStorage.isClient()) {
            Main.eventBus.pushOutgoing(new BallDirectionChangeEvent(ball.getDirection()));
        }
    }

    private void pollEventBus() {
        IGameEvent event = Main.eventBus.popIncoming();
        if (null == event) {
            return;
        }

        if (event instanceof GameStateChangeEvent) {
            handleGameStateChange(((GameStateChangeEvent) event).newState);
        } else if (event instanceof ConnectionLostEvent) {
            logger.info("The connection to the opponent was lost");
            new Alert(Alert.AlertType.ERROR, "Connection lost!").show();
        } else if (event instanceof ConnectionEstablishedEvent) {
            handleConnectionEstablished((ConnectionEstablishedEvent) event);
        } else if (event instanceof PlayerReadyEvent) {
            opponentReady = true;
            // display a message that opponent is ready to start the game
            isStarted = playerReady;
            logger.info("Opponent is ready, waiting for you");
        } else if (event instanceof BallDirectionChangeEvent) {
            ghostBall.setDirection(((BallDirectionChangeEvent) event).getPoint());
        } else if (event instanceof OnScoreEvent) {
            resetGame();
            scoreManager.onScore(((OnScoreEvent) event).getScoringSide());
        } else if (event instanceof PlayerMoveActionEvent) {
            opponent.setMoveAction(((PlayerMoveActionEvent) event).action);
        } else if (event instanceof OnLoadEvent) {
            scoreManager.setPlayerScore(((OnLoadEvent) event).getPlayerScore());
            scoreManager.setOtherScore(((OnLoadEvent) event).getOtherScore());
            scoreManager.setPlayerName(((OnLoadEvent) event).getPlayerName());
            scoreManager.setOtherName(((OnLoadEvent) event).getOtherName());
            resetGame();
            isStarted = false;
        }
    }

    private void updater(long l) {
        long deltaT = (l - prevTime) / 1000_000;    // Convert to ms
        prevTime = l;

        if (!isPaused && isStarted) {
            player.update(deltaT);
            opponent.update(deltaT);
            if (Main.propertyStorage.isClient()) {
                ghostBall.update(deltaT);
            } else {
                ball.update(deltaT, player.getBoundingBox(), opponent.getBoundingBox());
            }
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
        resetFlags();
        txtStatus.setVisible(true);
        if (!Main.propertyStorage.isClient()) {
            Main.eventBus.pushOutgoing(new BallDirectionChangeEvent(ball.getDirection()));
        }
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
                    Main.eventBus.pushOutgoing(new PlayerReadyEvent());
                    playerReady = true;
                    // display a message that you are ready to start the game
                    isStarted = opponentReady;
                    logger.info("Ready to start the game, waiting for the opponent");
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
            Main.eventBus.pushOutgoing(
                    new OnLoadEvent(
                            scoreManager.getPlayerScore(), scoreManager.getOtherScore(),
                            scoreManager.getPlayerName(), scoreManager.getOtherName()));
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
        if (scoreManager.checkWin()) {
            Main.eventBus.pushOutgoing(new GameStateChangeEvent(GameState.GAME_OVER));
            handleGameStateChange(GameState.GAME_OVER);
        } else {
            Main.eventBus.pushOutgoing(new OnScoreEvent(side));
            resetGame();
            txtStatus.setVisible(true);
        }
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
