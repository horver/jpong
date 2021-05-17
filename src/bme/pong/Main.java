package bme.pong;

import bme.pong.networking.EventBus;
import bme.pong.storages.PropertyStorage;
import bme.pong.threading.ThreadMgr;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    private static Stage stage;
    public static final String configPath = "game_conf.ini";
    public static final PropertyStorage propertyStorage = new PropertyStorage(Main.configPath);
    public static final EventBus eventBus = new EventBus();
    public static final ThreadMgr threadMgr = new ThreadMgr();

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        switchScene("mainmenu.fxml");
        primaryStage.setTitle("PONG");
        primaryStage.show();
    }

    @Override
    public void stop() {
        Main.threadMgr.nukeAll();
        System.exit(0);
    }

    public static File showOpenFileDialog(FileChooser fileChooser) {
        return fileChooser.showOpenDialog(stage);
    }

    public static void switchScene(String fxml) {
        Parent root;
        try {
            URL resource = Main.class.getResource("/res/scenes/" + fxml);
            if (resource == null) {
                throw new RuntimeException("Resource not found.");
            }
            root = FXMLLoader.load(resource);
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            return;
        }
        stage.setScene(new Scene(root, 640, 480));
    }

    public static void connectionErrorHandler() {
        new Alert(Alert.AlertType.ERROR, "Connection error!").show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
