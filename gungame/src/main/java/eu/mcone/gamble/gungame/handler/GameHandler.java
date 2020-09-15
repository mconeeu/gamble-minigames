/*
 * Copyright (c) 2017 - 2020 Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.gungame.handler;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.world.CoreLocation;
import eu.mcone.gamble.api.Gamble;
import eu.mcone.gamble.api.minigame.GamePhase;
import eu.mcone.gamble.api.player.GamblePlayer;
import eu.mcone.gamble.gungame.GunGame;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;

import java.util.*;

public class GameHandler extends eu.mcone.gamble.api.minigame.GameHandler {

    private static final Map<Location, Long> SPAWN_LOCATIONS = new HashMap<>();

    static {
        for (Map.Entry<String, CoreLocation> location : CoreSystem.getInstance().getWorldManager().getWorld(GunGame.getInstance().getMinigameworld().bukkit()).getLocations().entrySet()) {
            if (location.getKey().startsWith("Gungame-")) {
                SPAWN_LOCATIONS.put(location.getValue().bukkit(), (System.currentTimeMillis() / 1000) - 5);
            }
        }
    }

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
                });
                GunGame.getInstance().getMessenger().broadcast("§fDas Spiel beginnt in 4 Sekunden!");
                Bukkit.getScheduler().runTaskLater(GunGame.getInstance(), () -> {
                    GunGame.getInstance().getMessenger().broadcast("§fDas Spiel beginnt in 3 Sekunden!");
                    Bukkit.getScheduler().runTaskLater(GunGame.getInstance(), () -> {
                        GunGame.getInstance().getMessenger().broadcast("§fDas Spiel beginnt in 2 Sekunden!");
                        Bukkit.getScheduler().runTaskLater(GunGame.getInstance(), () -> {
                            GunGame.getInstance().getMessenger().broadcast("§fDas Spiel beginnt in einer Sekunden!");
                            Bukkit.getScheduler().runTaskLater(GunGame.getInstance(), () -> {
                                GunGame.getInstance().getMessenger().broadcast("§fDas Spiel hat begonnen!");
                                for (GamblePlayer all : Gamble.getInstance().getGamblePlayers()) {
                                    all.getPlayer().teleport(getRandomSpawn());
                                    all.getPlayer().setGameMode(GameMode.ADVENTURE);
                                }
                            }, 20);
                        }, 20);
                    }, 20);
                }, 20);

                break;
            case END:
                break;
        }
    }

    public Location getRandomSpawn() {
        List<Location> locations = new ArrayList<>();

        for (Map.Entry<Location, Long> location : SPAWN_LOCATIONS.entrySet()) {
            if (((System.currentTimeMillis() / 1000) - location.getValue()) > 3) {
                locations.add(location.getKey());
            }
        }

        Location location = locations.get(new Random().nextInt(locations.size() - 1));
        SPAWN_LOCATIONS.put(location, System.currentTimeMillis() / 1000);

        return location;
    }

}
