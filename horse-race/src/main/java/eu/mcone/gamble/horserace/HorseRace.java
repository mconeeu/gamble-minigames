/*
 * Copyright (c) 2017 - 2020 Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.horserace;

import eu.mcone.gamble.api.minigame.GambleGame;
import eu.mcone.gamble.horserace.handler.GameHandler;
import lombok.Getter;
import org.bukkit.ChatColor;

public class HorseRace extends GambleGame {

    @Getter
    public static HorseRace instance;

    public HorseRace() {
        super("HorseRace", ChatColor.DARK_RED, "horserace.prefix");
        setGameHandler(new GameHandler());
    }

    @Override
    public void initiate() {
        instance = this;
    }

    @Override
    public void abandon() {

    }
}
