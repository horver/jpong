package bme.pong.controllers;

import bme.pong.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MainMenuController {

    @FXML
    void actionNewGame(ActionEvent event) {
        Main.switchScene("modeselector.fxml");
    }

    @FXML
    void actionExit(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

}
