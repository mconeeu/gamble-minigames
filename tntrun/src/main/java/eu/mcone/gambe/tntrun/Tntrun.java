/*
 * Copyright (c) 2017 - 2020 Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gambe.tntrun;

import eu.mcone.gambe.tntrun.handler.GameHandler;
import eu.mcone.gamble.api.minigame.GambleGame;
import lombok.Getter;
import org.bukkit.ChatColor;

public class Tntrun extends GambleGame {

    @Getter
    public static Tntrun instance;

    public Tntrun() {
        super("tntrun", ChatColor.RED, "tntrun.prefix");
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
