/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.trafficrace;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.util.CoreTitle;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import eu.mcone.gamble.api.EndReason;
import eu.mcone.gamble.api.Gamble;
import eu.mcone.gamble.api.helper.DefaultLobbyCallback;
import eu.mcone.gamble.api.listener.LockListener;
import eu.mcone.gamble.api.minigame.GambleGame;
import eu.mcone.gamble.api.minigame.GambleGamePhase;
import eu.mcone.gamble.api.minigame.GambleGameType;
import eu.mcone.gamble.api.player.GamblePlayer;
import eu.mcone.gamble.trafficrace.game.GateHelper;
import eu.mcone.gamble.trafficrace.game.TrafficState;
import eu.mcone.gamble.trafficrace.listener.MoveListener;
import eu.mcone.gameapi.api.GamePlugin;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Getter
public class TrafficRaceGame extends GambleGame {

    private Gamble gamble;
    private GamePlugin gamePlugin;

    private CoreWorld minigameWorld;
    private GateHelper gateHelper;
    private TrafficState trafficState;
    private LinkedList<GamblePlayer> playersInGoal;
    private Map<TrafficState, Integer> phaseTime;

    private CoreTitle GO;
    private CoreTitle ATTENTION;
    private CoreTitle STOP;

    private int stateChangerTaskId;

    @Override
    public void initiate(Gamble gamble) {
        this.gamble = gamble;
        this.gamePlugin = (GamePlugin) gamble;
        gamePlugin.sendConsoleMessage("Initialising traffic race minigame...");
        minigameWorld = CoreSystem.getInstance().getWorldManager().getWorld("minigames");
        trafficState = TrafficState.GO;
        gateHelper = new GateHelper(this);
        playersInGoal = new LinkedList<>();
        phaseTime = new HashMap<>();
        stateChangerTaskId = -1;

        GO = CoreSystem.getInstance().createTitle().title("§aLos!").subTitle("").stay(3).fadeIn(1).fadeOut(1);
        ATTENTION = CoreSystem.getInstance().createTitle().title("§eAchtung!").subTitle("").stay(3).fadeIn(1).fadeOut(1);
        STOP = CoreSystem.getInstance().createTitle().title("§4STOP!").subTitle("").stay(3).fadeIn(1).fadeOut(1);
    }

    @Override
    public void phaseSwitched(GambleGamePhase phase) {
        if (phase == GambleGamePhase.SELECTED) {
            gamble.getMinigameHelper().setLobbyCountdown(10);
            gamble.getMinigameHelper().setLobbyCountdownCallback(new DefaultLobbyCallback(new ArrayList<Player>(Bukkit.getOnlinePlayers())));
        } else if (phase == GambleGamePhase.LOBBY) {
            gamePlugin.getMessenger().broadcast("§7==================================================");
            gamePlugin.getMessenger().broadcast("");
            gamePlugin.getMessenger().broadcast("§6Erreiche die Ziellinie als erster Spieler,");
            gamePlugin.getMessenger().broadcast("§6aber achte darauf stehen zu bleiben, wenn die");
            gamePlugin.getMessenger().broadcast("§6Ampel rot ist.");
            gamePlugin.getMessenger().broadcast("");
            gamePlugin.getMessenger().broadcast("§7==================================================");


            Location spawn = minigameWorld.getLocation("trafficrace_spawn");
            if (spawn == null) {
                gamble.finishGambleGame(GambleGameType.TRAFFIC_RACE, EndReason.EXCEPTION);
                return;
            }

            gamble.registerListener(this, new MoveListener(gamble, this));
            gamble.registerListener(this, new LockListener(gamble, this));

            Bukkit.getOnlinePlayers().forEach(x -> x.teleport(spawn));
        } else if (phase == GambleGamePhase.INGAME) {
            gateHelper.remove();
            startStateChanger();
            changeTrafficState(TrafficState.GO);
        } else if (phase == GambleGamePhase.END) {
            gateHelper.recreate();
        }
    }

    public void changeTrafficState(TrafficState newState) {
        Bukkit.getOnlinePlayers().forEach(p -> {
            GamblePlayer gp = gamble.getGamblePlayer(p.getUniqueId());
            if (!playersInGoal.contains(gp)) {
                if (newState == TrafficState.GO) {
                    GO.send(p);
                } else if (newState == TrafficState.ATTENTION) {
                    ATTENTION.send(p);
                } else if (newState == TrafficState.STOP) {
                    STOP.send(p);
                }
                trafficState = newState;
                p.playSound(p.getLocation(), Sound.LEVEL_UP, 1f, 1f);
            }
        });
    }

    public void startStateChanger() {
        if (Gamble.DEBUG) gamePlugin.sendConsoleMessage("TrafficRace: Starting StateChangerTask...");

        final int[] time = {0};
        generateRandomTimes();

        stateChangerTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            if (time[0] >= phaseTime.get(trafficState)) {
                switch (trafficState) {
                    case GO:
                        changeTrafficState(TrafficState.ATTENTION);
                        break;
                    case ATTENTION:
                        changeTrafficState(TrafficState.STOP);
                        break;
                    case STOP:
                        generateRandomTimes();
                        changeTrafficState(TrafficState.GO);
                        break;
                }
                time[0] = 0;
            }
            time[0]++;
        }, 20, 20);

        if (Gamble.DEBUG) gamePlugin.sendConsoleMessage("TrafficRace: Started StateChangerTask");
    }

    private void generateRandomTimes() {
        for (TrafficState ts : TrafficState.values()) {
            phaseTime.put(ts, ThreadLocalRandom.current().nextInt(
                    ts.getMinLength(),
                    ts.getMaxLength() + 1));
        }
    }

    public void stopStateChanger() {
        if (stateChangerTaskId != -1) {
            Bukkit.getScheduler().cancelTask(stateChangerTaskId);
            if (Gamble.DEBUG) gamePlugin.sendConsoleMessage("TrafficRace: Cancelled StateChangerTask");
        }
    }

}
