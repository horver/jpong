package bme.pong.controllers;

import bme.pong.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MainMenuController {

    @FXML
    void actionNewGame(ActionEvent event) {
        Main.switchScene("modeselector.fxml");
    }

    @FXML
    void actionExit(ActionEvent event) {
        System.exit(0);
    }

}
