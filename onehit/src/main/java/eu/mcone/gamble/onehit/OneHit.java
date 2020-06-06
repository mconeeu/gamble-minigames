/*
 * Copyright (c) 2017 - 2020 Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.onehit;

import eu.mcone.gamble.api.minigame.GambleGame;
import eu.mcone.gamble.onehit.handler.GameHandler;
import lombok.Getter;
import org.bukkit.ChatColor;

public class OneHit extends GambleGame {

    @Getter
    public static OneHit instance;

    public OneHit() {
        super("OneHit", ChatColor.DARK_RED, "onehit.prefix");
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
