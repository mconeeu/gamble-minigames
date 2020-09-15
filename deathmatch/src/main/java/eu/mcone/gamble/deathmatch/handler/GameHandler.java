/*
 * Copyright (c) 2017 - 2020 Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.deathmatch.handler;

import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gamble.api.Gamble;
import eu.mcone.gamble.api.minigame.GamePhase;
import eu.mcone.gamble.api.player.GamblePlayer;
import eu.mcone.gamble.deathmatch.Deathmatch;
import eu.mcone.gamble.deathmatch.game.HotbarItems;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class GameHandler extends eu.mcone.gamble.api.minigame.GameHandler {

    @Override
    public void gamePhaseSwitched(GamePhase gamePhase) {
        switch (gamePhase) {
            case LOBBY:
                Deathmatch.getInstance().getMessenger().broadcast("§7==================================================");
                Deathmatch.getInstance().getMessenger().broadcast("");
                Deathmatch.getInstance().getMessenger().broadcast("§6Versuche Spieler zu töten");
                Deathmatch.getInstance().getMessenger().broadcast("§6aber versuche nicht selbst");
                Deathmatch.getInstance().getMessenger().broadcast("§6erschlagen zu werden");
                Deathmatch.getInstance().getMessenger().broadcast("");
                Deathmatch.getInstance().getMessenger().broadcast("§7==================================================");

                Bukkit.getOnlinePlayers().forEach(x -> x.teleport(Deathmatch.getInstance().getMinigameWorld().getLocation("deathmatch_spawn")));
                break;
            case INGAME:
                Bukkit.getOnlinePlayers().forEach(x -> {
                    x.getInventory().clear();
                    x.getInventory().setItem(0, HotbarItems.STONE_SWORD);
                    x.getInventory().setItem(1, HotbarItems.ROD);
                    x.getInventory().setItem(2, HotbarItems.GOLD_APPLE);

                    x.getInventory().setBoots(new ItemBuilder(Material.IRON_BOOTS).create());
                    x.getInventory().setLeggings(new ItemBuilder(Material.IRON_LEGGINGS).create());
                    x.getInventory().setChestplate(new ItemBuilder(Material.IRON_CHESTPLATE).create());
                    x.getInventory().setHelmet(new ItemBuilder(Material.IRON_HELMET).create());
                });
                Deathmatch.getInstance().getMessenger().broadcast("§fDas Spiel beginnt in 4 Sekunden!");
                Bukkit.getScheduler().runTaskLater(Deathmatch.getInstance(), () -> {
                    Deathmatch.getInstance().getMessenger().broadcast("§fDas Spiel beginnt in 3 Sekunden!");
                    Bukkit.getScheduler().runTaskLater(Deathmatch.getInstance(), () -> {
                        Deathmatch.getInstance().getMessenger().broadcast("§fDas Spiel beginnt in 2 Sekunden!");
                        Bukkit.getScheduler().runTaskLater(Deathmatch.getInstance(), () -> {
                            Deathmatch.getInstance().getMessenger().broadcast("§fDas Spiel beginnt in einer Sekunden!");
                            Bukkit.getScheduler().runTaskLater(Deathmatch.getInstance(), () -> {
                                for (GamblePlayer all : Gamble.getInstance().getGamblePlayers()) {
                                    Deathmatch.getInstance().getAlivedPlayers().add((Player) all);
                                    Deathmatch.getInstance().getMessenger().broadcast("§fDas Spiel beginnt...");

                                    Bukkit.getScheduler().runTaskLater(Deathmatch.getInstance(), () -> {
                                        if (Deathmatch.getInstance().getAlivedPlayers().size() != 1) {
                                            ((Player) all).getVelocity().setY(0.6).multiply(0.4);
                                            Deathmatch.getInstance().getMessenger().broadcast("§fAlle Spieler wurden in die Luft geschossen!");
                                        }
                                    }, 100);
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
