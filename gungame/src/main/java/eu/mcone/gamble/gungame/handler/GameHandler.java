/*
 * Copyright (c) 2017 - 2020 Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.gungame.handler;

import eu.mcone.coresystem.api.bukkit.broadcast.SimpleBroadcast;
import eu.mcone.gamble.api.minigame.GamePhase;
import eu.mcone.gamble.gungame.GunGame;
import eu.mcone.gamble.gungame.listener.GeneralPlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameHandler extends eu.mcone.gamble.api.minigame.GameHandler {

    public static boolean hasStarted = false;

    @Override
    public void gamePhaseSwitched(GamePhase gamePhase) {
        switch (gamePhase) {
            case LOBBY:
                GunGame.getInstance().getMessenger().broadcastSimple(new SimpleBroadcast(
                        "§7==================================================" +
                                "\n" +
                                "\n§6Versuche Spieler zu töten" +
                                "\n§6aber versuche nicht selbst" +
                                "\n§6erschlagen zu werden" +
                                "\n§6Du erhällst durch jeden Kill" +
                                "\n§6ein neues Item!" +
                                "\n" +
                                "\n§7=================================================="
                ));

                Bukkit.getOnlinePlayers().forEach(p -> p.setGameMode(GameMode.ADVENTURE));
                break;
            case INGAME:
                Bukkit.getOnlinePlayers().forEach(p -> {
                    p.getInventory().clear();
                    p.getPlayer().teleport(getRandomSpawn());
                    p.getPlayer().setGameMode(GameMode.ADVENTURE);
                    GunGame.getInstance().getAlivedPlayers().add(p.getPlayer());
                    GeneralPlayerListener.setItemsLevel(p, GunGame.getInstance().getGungameLevels().get(0));
                });
                hasStarted = true;
                break;
            case END:
                break;
        }
    }

    public Location getRandomSpawn() {
        List<Location> locations = new ArrayList<>();

        for (Map.Entry<Location, Long> location : GunGame.getInstance().getSpawnLocations().entrySet()) {
            if (((System.currentTimeMillis() / 1000) - location.getValue()) > 3) {
                locations.add(location.getKey());
            }
        }

        Location location = locations.get(new Random().nextInt(locations.size() - 1));
        GunGame.getInstance().getSpawnLocations().put(location, System.currentTimeMillis() / 1000);

        return location;
    }

}
