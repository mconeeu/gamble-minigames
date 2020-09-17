/*
 * Copyright (c) 2017 - 2020 Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.gungame;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import eu.mcone.gamble.api.minigame.GambleGame;
import eu.mcone.gamble.api.player.GamblePlayer;
import eu.mcone.gamble.gungame.commands.SetupCommand;
import eu.mcone.gamble.gungame.game.GungameLevel;
import eu.mcone.gamble.gungame.handler.GameHandler;
import eu.mcone.gamble.gungame.listeners.GeneralPlayerListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class GunGame extends GambleGame {

    @Getter
    private static GunGame instance;
    @Getter
    private final CoreWorld minigameworld;
    @Getter
    private final ArrayList<Player> alivedPlayers;
    @Getter
    private final LinkedList<GamblePlayer> playersInGoal;
    @Getter
    private Map<Integer, GungameLevel> gungameLevels = new HashMap<>();

    public GunGame() {
        super("GunGame", ChatColor.BLUE, "gungame.prefix");
        setGameHandler(new GameHandler());
        minigameworld = CoreSystem.getInstance().getWorldManager().getWorld("GunGame");
        alivedPlayers = new ArrayList<>();
        playersInGoal = new LinkedList<>();

        for (GungameLevel level : CoreSystem.getInstance().getMongoDB().getCollection("gamble_gungame_levels", GungameLevel.class).find()) {
            gungameLevels.put(level.getLevel(), level);
        }

        registerCommands(
                new SetupCommand()
        );
        registerListener(
                new GeneralPlayerListener()
        );


    }

    @Override
    public void initiate() {
        instance = this;
    }

    @Override
    public void abandon() {

    }
}
