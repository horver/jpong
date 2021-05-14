package bme.pong.networking;

import javafx.geometry.Point2D;

import java.io.Serializable;

public class NetworkMessage implements Serializable {
    private static final long serialVersionUID = 2034944416852362783L;
    private String playerName;
    private String otherName;

    private double playerPositionY;
    private double otherPositionY;

    private double ballX;
    private double ballY;

    public void setPlayerPositionY(Point2D position) {
        playerPositionY = position.getY();
    }

    public double getPlayerPositionY() {
        return playerPositionY;
    }

    public void setOtherPositionY(Point2D position) {
        otherPositionY = position.getY();
    }

    public double getOtherPositionY() {
        return otherPositionY;
    }

    public void setBallPosition(Point2D position) {
        ballX = position.getX();
        ballY = position.getY();
    }

    public Point2D getBallPosition() {
        return new Point2D(ballX, ballY);
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }
}
