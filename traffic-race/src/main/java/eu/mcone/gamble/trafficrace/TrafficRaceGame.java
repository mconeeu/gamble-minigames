/*
 * Copyright (c) 2017 - 2020 Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.trafficrace;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.util.CoreTitle;
import eu.mcone.gamble.api.Gamble;
import eu.mcone.gamble.api.minigame.GambleGame;
import eu.mcone.gamble.api.player.GamblePlayer;
import eu.mcone.gamble.trafficrace.game.GateHelper;
import eu.mcone.gamble.trafficrace.game.TrafficState;
import eu.mcone.gamble.trafficrace.handler.GameHandler;
import eu.mcone.gamble.trafficrace.listener.MoveListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Getter
public class TrafficRaceGame extends GambleGame {

    @Getter
    public static TrafficRaceGame instance;

    private GateHelper gateHelper;
    private TrafficState trafficState;
    private LinkedList<GamblePlayer> playersInGoal;
    private Map<TrafficState, Integer> phaseTime;

    private CoreTitle GO;
    private CoreTitle ATTENTION;
    private CoreTitle STOP;

    private int stateChangerTaskId;

    public TrafficRaceGame() {
        super("traffic-race", ChatColor.GREEN, "trafficrace.prefix");
        setGameHandler(new GameHandler());
    }

    @Override
    public void initiate() {
        instance = this;

        sendConsoleMessage("Initialising traffic race minigame...");
        trafficState = TrafficState.GO;
        gateHelper = new GateHelper(this);
        playersInGoal = new LinkedList<>();
        phaseTime = new HashMap<>();
        stateChangerTaskId = -1;

        registerListener(new MoveListener());

        GO = CoreSystem.getInstance().createTitle().title("§aLos!").subTitle("").stay(3).fadeIn(1).fadeOut(1);
        ATTENTION = CoreSystem.getInstance().createTitle().title("§eAchtung!").subTitle("").stay(3).fadeIn(1).fadeOut(1);
        STOP = CoreSystem.getInstance().createTitle().title("§4STOP!").subTitle("").stay(3).fadeIn(1).fadeOut(1);
    }

    @Override
    public void abandon() {

    }

    public void changeTrafficState(TrafficState newState) {
        Bukkit.getOnlinePlayers().forEach(p -> {
            GamblePlayer gp = Gamble.getInstance().getGamblePlayer(p.getUniqueId());
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
        }
    }
}
