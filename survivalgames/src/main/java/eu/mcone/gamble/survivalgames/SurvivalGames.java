/*
 * Copyright (c) 2017 - 2020 Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.survivalgames;

import eu.mcone.gamble.api.minigame.GambleGame;
import eu.mcone.gamble.survivalgames.handler.GameHandler;
import lombok.Getter;
import org.bukkit.ChatColor;

public class SurvivalGames extends GambleGame {

    @Getter
    public static SurvivalGames instance;

    public SurvivalGames() {
        super("survival-games", ChatColor.GREEN, "survivalgames.prefix");
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
