/*
 * Copyright (c) 2017 - 2020 Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.race.handler;

import eu.mcone.coresystem.api.bukkit.broadcast.SimpleBroadcast;
import eu.mcone.gamble.api.minigame.GamePhase;
import eu.mcone.gamble.race.Race;
import org.bukkit.Bukkit;

public class GameHandler extends eu.mcone.gamble.api.minigame.GameHandler {

    @Override
    public void gamePhaseSwitched(GamePhase phase) {
        switch (phase) {
            case LOBBY:
                Race.getInstance().getMessenger().broadcastSimple(new SimpleBroadcast(
                        "§7==================================================" +
                                "\n" +
                                "\n§6Erreiche die Ziellinie als erster Spieler" +
                                "\n" +
                                "\n§7=================================================="
                ));

                Bukkit.getOnlinePlayers().forEach(x -> x.teleport(Race.getInstance().getMinigameWorld().getLocation("race_spawn")));
                break;
            case INGAME:
                Race.getInstance().getGateHelper().remove();
                break;
            case END:
                Race.getInstance().getGateHelper().recreate();
                break;
        }
    }
}
