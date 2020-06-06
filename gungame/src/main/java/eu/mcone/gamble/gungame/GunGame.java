/*
 * Copyright (c) 2017 - 2020 Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.gungame;

import eu.mcone.gamble.api.minigame.GambleGame;
import eu.mcone.gamble.gungame.handler.GameHandler;
import lombok.Getter;
import org.bukkit.ChatColor;

public class GunGame extends GambleGame {

    @Getter
    public static GunGame instance;

    public GunGame() {
        super("GunGame", ChatColor.BLUE, "gungame.prefix");
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
