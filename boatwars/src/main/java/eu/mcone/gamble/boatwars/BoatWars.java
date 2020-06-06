/*
 * Copyright (c) 2017 - 2020 Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.boatwars;

import eu.mcone.gamble.api.minigame.GambleGame;
import eu.mcone.gamble.boatwars.handler.GameHandler;
import lombok.Getter;
import org.bukkit.ChatColor;

public class BoatWars extends GambleGame {

    @Getter
    public static BoatWars instance;

    public BoatWars() {
        super("Deathmatch", ChatColor.DARK_RED, "boatwars.prefix");
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
