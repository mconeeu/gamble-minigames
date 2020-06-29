/*
 * Copyright (c) 2017 - 2020 Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.onehit;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import eu.mcone.gamble.api.minigame.GambleGame;
import eu.mcone.gamble.api.player.GamblePlayer;
import eu.mcone.gamble.onehit.handler.GameHandler;
import eu.mcone.gamble.onehit.listener.GeneralPlayerListener;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

public class OneHit extends GambleGame {

    @Getter
    public static OneHit instance;
    @Getter
    private final CoreWorld minigameWorld;
    @Getter
    private final ArrayList<Player> alivedPlayers;
    @Getter
    private final LinkedList<GamblePlayer> playersInGoal;


    public OneHit() {
        super("OneHit", ChatColor.DARK_RED, "onehit.prefix");
        setGameHandler(new GameHandler());

        sendConsoleMessage("Initialising onehit minigame...");
        minigameWorld = CoreSystem.getInstance().getWorldManager().getWorld("onehit");

        registerListener(new GeneralPlayerListener());

        alivedPlayers = new ArrayList<>();
        playersInGoal = new LinkedList<>();
    }

    @Override
    public void initiate() {
        instance = this;
    }

    @Override
    public void abandon() {}
}
