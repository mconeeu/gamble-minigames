/*
 * Copyright (c) 2017 - 2020 Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.gungame.handler;

import eu.mcone.gamble.api.minigame.GamePhase;
import eu.mcone.gamble.gungame.GunGame;
import eu.mcone.gamble.gungame.listeners.GeneralPlayerListener;
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
                GunGame.getInstance().getMessenger().broadcast("§7==================================================");
                GunGame.getInstance().getMessenger().broadcast("");
                GunGame.getInstance().getMessenger().broadcast("§6Versuche Spieler zu töten");
                GunGame.getInstance().getMessenger().broadcast("§6aber versuche nicht selbst");
                GunGame.getInstance().getMessenger().broadcast("§6erschlagen zu werden");
                GunGame.getInstance().getMessenger().broadcast("§6Du erhällst durch jeden Kill");
                GunGame.getInstance().getMessenger().broadcast("§6ein neues Item!");
                GunGame.getInstance().getMessenger().broadcast("");
                GunGame.getInstance().getMessenger().broadcast("§7==================================================");
                Bukkit.getOnlinePlayers().forEach(x -> {
                    x.setGameMode(GameMode.ADVENTURE);
                });
                break;
            case INGAME:
                Bukkit.getOnlinePlayers().forEach(x -> {
                    x.getInventory().clear();
                    x.getPlayer().teleport(getRandomSpawn());
                    x.getPlayer().setGameMode(GameMode.ADVENTURE);
                    GunGame.getInstance().getAlivedPlayers().add(x.getPlayer());
                    GeneralPlayerListener.setItemsLevel(x, GunGame.getInstance().getGungameLevels().get(0));
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
