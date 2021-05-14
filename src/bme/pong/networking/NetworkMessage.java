package bme.pong.networking;

import java.io.Serializable;

public class NetworkMessage implements Serializable {
    private static final long serialVersionUID = 2034944416852362783L;
    private String playerName;
    private String otherName;

    private double playerPositionY;
    private double otherPositionY;

    private double ballX;
    private double ballY;

    @Override
    public String toString() {
        return "NetworkMessage{" +
                "playerName='" + playerName + '\'' +
                ", otherName='" + otherName + '\'' +
                ", playerPositionY=" + playerPositionY +
                ", otherPositionY=" + otherPositionY +
                ", ballX=" + ballX +
                ", ballY=" + ballY +
                '}';
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

    public double getPlayerPositionY() {
        return playerPositionY;
    }

    public void setPlayerPositionY(double playerPositionY) {
        this.playerPositionY = playerPositionY;
    }

    public double getOtherPositionY() {
        return otherPositionY;
    }

    public void setOtherPositionY(double otherPositionY) {
        this.otherPositionY = otherPositionY;
    }

    public double getBallX() {
        return ballX;
    }

    public void setBallX(double ballX) {
        this.ballX = ballX;
    }

    public double getBallY() {
        return ballY;
    }

    public void setBallY(double ballY) {
        this.ballY = ballY;
    }

    public void update(NetworkMessage message) {
        System.out.println(this.playerName);
        playerName = message.getPlayerName();
        System.out.println(this.playerName);
        otherName = message.getOtherName();

        playerPositionY = message.getPlayerPositionY();
        otherPositionY = message.getOtherPositionY();

        ballX = message.getBallX();
        ballY = message.getBallY();
        System.out.println("Update message");
    }
}
