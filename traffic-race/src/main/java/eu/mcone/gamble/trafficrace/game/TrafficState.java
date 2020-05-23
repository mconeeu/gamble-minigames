/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.trafficrace.game;

import lombok.Getter;

public enum TrafficState {

    GO(2, 4),
    ATTENTION(1, 2),
    STOP(1, 2);

    @Getter
    private final int minLength, maxLength;

    TrafficState(int minLength, int maxLength) {
        this.minLength = minLength;
        this.maxLength = maxLength;
    }
}
