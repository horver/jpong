package bme.brszta.jpong;

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
        onStatisticsListener.onNameTextChange(getPlayerText());
    }

    @Override
    public void onScore(ScoringSide side) {
        if (side == ScoringSide.PLAYER_SIDE) {
            otherScore++;
        } else {
            playerScore++;
        }

        if (onStatisticsListener != null) {
            onStatisticsListener.onScoreTextChange(playerScore + " : " + otherScore);
        }
    }
}
