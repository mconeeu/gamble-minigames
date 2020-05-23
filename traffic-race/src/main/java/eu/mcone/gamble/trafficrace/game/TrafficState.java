package eu.mcone.gamble.trafficrace.game;

import lombok.Getter;

/**
 * Datei erstellt von: Felix Schmid in Projekt: eu.mcone-gamble-minigames
 */
public enum TrafficState {

    GO(2, 4),
    ATTENTION(1, 2),
    STOP(1, 2);

    @Getter
    private int minLength, maxLength;

    TrafficState(int minLength, int maxLength) {
        this.minLength = minLength;
        this.maxLength = maxLength;
    }
}
