/*
 * Copyright (c) 2017 - 2020 Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.onehit.handler;

import eu.mcone.gamble.api.Gamble;
import eu.mcone.gamble.api.minigame.GamePhase;
import eu.mcone.gamble.api.player.GamblePlayer;
import eu.mcone.gamble.onehit.OneHit;
import eu.mcone.gamble.onehit.game.HotbarItems;
import eu.mcone.gameapi.api.player.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GameHandler extends eu.mcone.gamble.api.minigame.GameHandler {

    @Override
    public void gamePhaseSwitched(GamePhase phase) {
        switch (phase) {
            case LOBBY:
                OneHit.getInstance().getMessenger().broadcast("§7==================================================");
                OneHit.getInstance().getMessenger().broadcast("");
                OneHit.getInstance().getMessenger().broadcast("§6Schlage alle Spieler mit deinem Schwert");
                OneHit.getInstance().getMessenger().broadcast("§6aber versuche nicht selbst");
                OneHit.getInstance().getMessenger().broadcast("§6erschlagen zu werden");
                OneHit.getInstance().getMessenger().broadcast("");
                OneHit.getInstance().getMessenger().broadcast("§7==================================================");

                Bukkit.getOnlinePlayers().forEach(x -> x.teleport(OneHit.getInstance().getMinigameWorld().getLocation("onehit_spawn")));
                break;
            case INGAME:
                Bukkit.getOnlinePlayers().forEach(x -> {
                    x.teleport(OneHit.getInstance().getMinigameWorld().getLocation("onehit_spawn"));
                    x.getInventory().clear();
                    x.getInventory().setItem(0, HotbarItems.IRON_SWORD);
                });
                OneHit.getInstance().getMessenger().broadcast("§fDas Spiel beginnt in 4 Sekunden!");
                Bukkit.getScheduler().runTaskLater(OneHit.getInstance(), () -> {
                    OneHit.getInstance().getMessenger().broadcast("§fDas Spiel beginnt in 3 Sekunden!");
                    Bukkit.getScheduler().runTaskLater(OneHit.getInstance(), () -> {
                        OneHit.getInstance().getMessenger().broadcast("§fDas Spiel beginnt in 2 Sekunden!");
                        Bukkit.getScheduler().runTaskLater(OneHit.getInstance(), () -> {
                            OneHit.getInstance().getMessenger().broadcast("§fDas Spiel beginnt in einer Sekunden!");
                            Bukkit.getScheduler().runTaskLater(OneHit.getInstance(), () -> {
                                for (GamblePlayer all : Gamble.getInstance().getGamblePlayers()) {
                                    OneHit.getInstance().getAlivedPlayers().add((Player) all);
                                    OneHit.getInstance().getMessenger().broadcast("§fDas Spiel beginnt...");
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
}
