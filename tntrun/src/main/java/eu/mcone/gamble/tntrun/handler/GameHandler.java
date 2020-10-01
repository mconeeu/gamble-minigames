/*
 * Copyright (c) 2017 - 2020 Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.tntrun.handler;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.broadcast.SimpleBroadcast;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gamble.tntrun.Tntrun;
import eu.mcone.gamble.tntrun.listener.GeneralPlayerListener;
import eu.mcone.gamble.api.minigame.GamePhase;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;

public class GameHandler extends eu.mcone.gamble.api.minigame.GameHandler {

    public static boolean hasStarted = false;

    @Override
    public void gamePhaseSwitched(GamePhase gamePhase) {

        switch (gamePhase) {
            case LOBBY:
                Tntrun.getInstance().getMessenger().broadcastSimple(new SimpleBroadcast(
                        "§7==================================================" +
                                "\n" +
                                "\n§6Versuche nicht zu" +
                                "\n§6Sterben den der Boden" +
                                "\n§6fällt unter dir weg!!" +
                                "\n§6Wenn du der Letze lebende" +
                                "\n§6Spieler bist hast du gewonnen!" +
                                "\n" +
                                "\n§7=================================================="
                ));

                Bukkit.getOnlinePlayers().forEach(x -> {
                    CorePlayer cp = CoreSystem.getInstance().getCorePlayer(x);
                    if (GeneralPlayerListener.isMinigamer(cp)) {
                        GeneralPlayerListener.alive.add(cp);
                    } else {
                        GeneralPlayerListener.spectators.add(x);
                    }
                });
                break;
            case INGAME:
                Bukkit.getOnlinePlayers().forEach(x -> {
                    x.getPlayer().teleport(Tntrun.getInstance().getSpawnLocation());
                    x.getPlayer().teleport(Tntrun.getInstance().getSpawnLocation());
                    if (GeneralPlayerListener.alive.contains(CoreSystem.getInstance().getCorePlayer(x))) {
                        x.getInventory().clear();
                        x.getPlayer().setGameMode(GameMode.ADVENTURE);
                    }
                });
                hasStarted = true;
                break;
            case END:
                break;
        }
    }
}
