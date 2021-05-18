package bme.pong.controllers;

import bme.pong.Main;
import bme.pong.networking.NetworkHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

public class ModeSelectorController {

    @FXML
    private TextField txtPlayerName;

    @FXML
    private ToggleGroup mode;

    @FXML
    private RadioButton selectJoin;
    private boolean isJoinSelected = true;

    @FXML
    private RadioButton selectHost;

    @FXML
    private TextField txtPort;

    @FXML
    private Label lblIp;

    @FXML
    private TextField txtAddress;

    @FXML
    private Button btnStartGame;

    @FXML
    private TextField txtGoal;


    @FXML
    void initialize() {
        txtPlayerName.setText(Main.propertyStorage.getPlayerName());
        txtAddress.setText(Main.propertyStorage.getHostAddress());
        txtPort.setText(String.valueOf(Main.propertyStorage.getHostPort()));
        txtGoal.setText(String.valueOf(Main.propertyStorage.getTargetGoal()));

        if (Main.propertyStorage.isClient()) {
            selectJoin.setSelected(true);
        } else {
            selectHost.setSelected(true);
        }
        isJoinSelected = selectJoin.isSelected();

        updateSelectors();

        mode.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            isJoinSelected = selectJoin.isSelected();
            updateSelectors();
        });
    }

    private void updateSelectors() {
        txtAddress.setVisible(isJoinSelected);
        lblIp.setVisible(isJoinSelected);
        validateInput(null);
    }

    @FXML
    void validateInput(KeyEvent event) {
        btnStartGame.setVisible(!(txtPlayerName.getText().isBlank() || (isJoinSelected && txtAddress.getText().isBlank())));
    }

    @FXML
    void StartGame(ActionEvent event) {
        validateInput(null);
        if (!btnStartGame.isVisible()) {
            return;
        }
        Main.propertyStorage.setPlayerName(txtPlayerName.getText());

        if (isJoinSelected) {
            Main.propertyStorage.setHostAddress(txtAddress.getText());
            btnStartGame.setText("Connecting...");
        }
        Main.propertyStorage.setClient(isJoinSelected);

        try {
            Main.propertyStorage.setHostPort(Integer.parseInt(txtPort.getText()));
        } catch (NumberFormatException e) {
            Main.propertyStorage.setHostPort(12345);
        }

        try {
            Main.propertyStorage.setTargetGoal(Integer.parseInt(txtGoal.getText()));
        } catch (NumberFormatException e) {
            Main.propertyStorage.setTargetGoal(10);
        }

        Main.propertyStorage.save(Main.configPath);

        NetworkHandler networkHandler = new NetworkHandler(Main.eventBus, Main.propertyStorage, Main.threadMgr);
        NetworkHandler.NetworkRole role = Main.propertyStorage.isClient() ? NetworkHandler.NetworkRole.GUEST :
                NetworkHandler.NetworkRole.HOST;
        networkHandler.setNetworkRole(role);
        Main.threadMgr.startThread(networkHandler, "NetworkHandler", (thread, throwable) -> {
            Platform.runLater(ModeSelectorController::handleNetworkError);
        });

        while (!Main.isNetworkingReady.get()) {
            if (Main.hasNetworkingFailed.get()) {
                //handleNetworkError();
                return;
            }
        }
        Main.switchScene("game.fxml");
    }

    private static void handleNetworkError() {
        new Alert(Alert.AlertType.ERROR, "Connection error!").show();
        Main.switchScene("mainmenu.fxml");
        Main.threadMgr.nukeAll();
        Main.hasNetworkingFailed.set(false);
        Main.isNetworkingReady.set(false);
    }
}
