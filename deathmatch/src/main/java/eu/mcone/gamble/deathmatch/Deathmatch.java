/*
 * Copyright (c) 2017 - 2020 Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.deathmatch;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import eu.mcone.gamble.api.minigame.GambleGame;
import eu.mcone.gamble.api.player.GamblePlayer;
import eu.mcone.gamble.deathmatch.listeners.GenerelPlayerListener;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedList;

public class Deathmatch extends GambleGame {

    @Getter
    public static Deathmatch instance;
    @Getter
    private final CoreWorld minigameWorld;
    @Getter
    private final ArrayList<Player> alivedPlayers;
    @Getter
    private final LinkedList<GamblePlayer> playersInGoal;


    public Deathmatch() {
        super("Deathmatch", ChatColor.DARK_RED, "deathmatch.prefix");
        setGameHandler(new eu.mcone.gamble.Deathmatch.handler.GameHandler());

        minigameWorld = CoreSystem.getInstance().getWorldManager().getWorld("onehit");

        registerListener(new GenerelPlayerListener());

        alivedPlayers = new ArrayList<>();
        playersInGoal = new LinkedList<>();

    }

    @Override
    public void initiate() {
        instance = this;
    }

    @Override
    public void abandon() {

    }
}
