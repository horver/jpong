package bme.brszta.jpong;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    private static Stage stage;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        switchScene("mainmenu.fxml");
        primaryStage.setTitle("JPong");
        primaryStage.show();
    }

    public static void switchScene(String fxml) {
        Parent root;
        try {
            URL resource = Main.class.getResource("/res/" + fxml);
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

    public static void main(String[] args) {
        launch(args);
    }
}
