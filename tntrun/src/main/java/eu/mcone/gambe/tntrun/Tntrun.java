/*
 * Copyright (c) 2017 - 2020 Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gambe.tntrun;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.world.CoreLocation;
import eu.mcone.gambe.tntrun.handler.GameHandler;
import eu.mcone.gamble.api.Gamble;
import eu.mcone.gamble.api.minigame.GambleGame;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.Map;

public class Tntrun extends GambleGame {

    @Getter
    private Location spawnLocation;
    @Getter
    public static Tntrun instance;

    public Tntrun() {
        super("tntrun", ChatColor.RED, "tntrun.prefix");
        setGameHandler(new GameHandler());
        spawnLocation = null;
    }

    @Override
    public void initiate() {
        instance = this;
        for (Map.Entry<String, CoreLocation> location : CoreSystem.getInstance().getWorldManager().getWorld(Gamble.getInstance().getMinigameWorld().bukkit()).getLocations().entrySet()) {
            if (location.getKey().startsWith("Tntrun")) {
                spawnLocation = location.getValue().bukkit();
            }
        } //loc set Gungame-1

    }

    @Override
    public void abandon() {

    }

}
