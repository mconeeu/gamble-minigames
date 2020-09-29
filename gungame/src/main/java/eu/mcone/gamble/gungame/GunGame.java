/*
 * Copyright (c) 2017 - 2020 Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.gungame;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.config.typeadapter.bson.ItemStackCodecProvider;
import eu.mcone.coresystem.api.bukkit.world.CoreLocation;
import eu.mcone.gamble.api.Gamble;
import eu.mcone.gamble.api.minigame.GambleGame;
import eu.mcone.gamble.api.player.GamblePlayer;
import eu.mcone.gamble.gungame.commands.SetupCommand;
import eu.mcone.gamble.gungame.game.GungameLevel;
import eu.mcone.gamble.gungame.handler.GameHandler;
import eu.mcone.gamble.gungame.listeners.GeneralPlayerListener;
import lombok.Getter;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class GunGame extends GambleGame {

    @Getter
    private static GunGame instance;
    @Getter
    private ArrayList<Player> alivedPlayers;
    @Getter
    private LinkedList<GamblePlayer> playersInGoal;
    @Getter
    private final Map<Integer, GungameLevel> gungameLevels = new HashMap<>();

    @Getter
    private final Map<Location, Long> spawnLocations = new HashMap<>();

    public GunGame() {
        super("GunGame", ChatColor.BLUE, "gungame.prefix");
        setGameHandler(new GameHandler());
    }

    @Override
    public void initiate() {
        instance = this;
        alivedPlayers = new ArrayList<>();
        playersInGoal = new LinkedList<>();

        for (GungameLevel level : CoreSystem.getInstance().getMongoDB().getCollection("gamble_gungame_levels", GungameLevel.class).withCodecRegistry(
                fromRegistries(getDefaultCodecRegistry(), fromProviders(new ItemStackCodecProvider(), PojoCodecProvider.builder().automatic(true).build()))
        ).find()) {
            gungameLevels.put(level.getLevel(), level);
        }
        registerCommands(
                new SetupCommand()
        );
        registerListener(
                new GeneralPlayerListener()
        );


        for (Map.Entry<String, CoreLocation> location : CoreSystem.getInstance().getWorldManager().getWorld(Gamble.getInstance().getMinigameWorld().bukkit()).getLocations().entrySet()) {
            if (location.getKey().startsWith("Gungame-")) {
                spawnLocations.put(location.getValue().bukkit(), (System.currentTimeMillis() / 1000) - 5);
            }
        } //loc set Gungame-1


    }

    @Override
    public void abandon() {

    }
}
