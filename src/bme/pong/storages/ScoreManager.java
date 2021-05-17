package bme.pong.storages;

import bme.pong.Main;
import bme.pong.entities.ScoringSide;
import bme.pong.listeners.OnScoreListener;
import bme.pong.listeners.OnStatisticsListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class ScoreManager implements OnScoreListener {
    private int playerScore;
    private int otherScore;
    private String playerName = Main.propertyStorage.getPlayerName();
    private String otherName = "Waiting for other player";
    private OnStatisticsListener onStatisticsListener;

    public void setOnStatisticsListener(OnStatisticsListener onStatisticsListener) {
        this.onStatisticsListener = onStatisticsListener;
    }

    public String getPlayerText() {
        return playerName + " - " + otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
        if (onStatisticsListener != null) {
            onStatisticsListener.onNameTextChange(getPlayerText());
        }
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
        if (onStatisticsListener != null) {
            onStatisticsListener.onNameTextChange(getPlayerText());
        }
    }

    public String getOtherName() {
        return otherName;
    }

    public boolean saveState() {
        Format c = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
        File file = new File(playerName + " vs " + otherName + " " + c.format(new Date()) + ".state");
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println(playerName);
            writer.println(playerScore);
            writer.println(otherName);
            writer.println(otherScore);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean loadState(File file) {
        if (file == null) {
            return false;
        }

        try (Scanner scanner = new Scanner(file)) {
            playerName = scanner.nextLine();
            playerScore = Integer.parseInt(scanner.nextLine());
            setOtherName(scanner.nextLine());
            otherScore = Integer.parseInt(scanner.nextLine());

            scoreChanged();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public void onScore(ScoringSide side) {
        if (side == ScoringSide.PLAYER_SIDE) {
            otherScore++;
        } else {
            playerScore++;
        }

        scoreChanged();
    }

    private void scoreChanged() {
        if (onStatisticsListener != null) {
            onStatisticsListener.onScoreTextChange(playerScore + " : " + otherScore);
        }
    }

    public boolean checkWin() {
        return (Main.propertyStorage.getTargetGoal() <= playerScore ||
                Main.propertyStorage.getTargetGoal() <= otherScore);
    }

    public String getWinner() {
        if (playerScore > otherScore) {
            return playerName;
        } else {
            return otherName;
        }
    }
}
