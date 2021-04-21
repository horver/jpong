package bme.pong.listeners;

import bme.pong.entities.ScoringSide;

public interface OnScoreListener {
    void onScore(ScoringSide side);
}
