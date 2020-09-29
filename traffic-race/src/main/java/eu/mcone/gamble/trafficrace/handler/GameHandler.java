/*
 * Copyright (c) 2017 - 2020 Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.trafficrace.handler;

import eu.mcone.gamble.api.Gamble;
import eu.mcone.gamble.api.minigame.GamePhase;
import eu.mcone.gamble.trafficrace.TrafficRaceGame;
import eu.mcone.gamble.trafficrace.game.TrafficState;
import org.bukkit.Bukkit;

public class GameHandler extends eu.mcone.gamble.api.minigame.GameHandler {

    @Override
    public void gamePhaseSwitched(GamePhase phase) {
        switch (phase) {
            case LOBBY:
                TrafficRaceGame.getInstance().getMessenger().broadcast("§7==================================================");
                TrafficRaceGame.getInstance().getMessenger().broadcast("");
                TrafficRaceGame.getInstance().getMessenger().broadcast("§6Erreiche die Ziellinie als erster Spieler,");
                TrafficRaceGame.getInstance().getMessenger().broadcast("§6aber achte darauf stehen zu bleiben, wenn die");
                TrafficRaceGame.getInstance().getMessenger().broadcast("§6Ampel rot ist.");
                TrafficRaceGame.getInstance().getMessenger().broadcast("");
                TrafficRaceGame.getInstance().getMessenger().broadcast("§7==================================================");

                Bukkit.getOnlinePlayers().forEach(x -> x.teleport(Gamble.getInstance().getMinigameWorld().getLocation("trafficrace_spawn")));
                break;
            case INGAME:
                TrafficRaceGame.getInstance().getGateHelper().remove();
                TrafficRaceGame.getInstance().startStateChanger();
                TrafficRaceGame.getInstance().changeTrafficState(TrafficState.GO);
                break;
            case END:
                TrafficRaceGame.getInstance().getGateHelper().recreate();
                break;
        }
    }
}
