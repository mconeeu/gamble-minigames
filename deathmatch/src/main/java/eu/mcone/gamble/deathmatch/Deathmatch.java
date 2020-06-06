/*
 * Copyright (c) 2017 - 2020 Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.deathmatch;

import eu.mcone.gamble.api.minigame.GambleGame;
import eu.mcone.gamble.deathmatch.handler.GameHandler;
import lombok.Getter;
import org.bukkit.ChatColor;

public class Deathmatch extends GambleGame {

    @Getter
    public static Deathmatch instance;

    public Deathmatch() {
        super("Deathmatch", ChatColor.DARK_RED, "deathmatch.prefix");
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