/*
 * Copyright (c) 2017 - 2020 Felix Schmid, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.trafficrace.listener;

import eu.mcone.gamble.api.EndReason;
import eu.mcone.gamble.api.Gamble;
import eu.mcone.gamble.api.listener.GambleListener;
import eu.mcone.gamble.api.minigame.GambleGame;
import eu.mcone.gamble.api.minigame.GambleGameResult;
import eu.mcone.gamble.api.minigame.GambleGameType;
import eu.mcone.gamble.api.player.GamblePlayer;
import eu.mcone.gamble.trafficrace.TrafficRaceGame;
import eu.mcone.gamble.trafficrace.game.TrafficState;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.player.GamePlayerState;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MoveListener extends GambleListener {

    private final Gamble gamble;
    private final TrafficRaceGame game;

    private String direction;
    private int coordinate;
    private final Map<Player, Long> lastPushed;

    int neededPlayers = 1;

    private boolean isFinished = false;

    public MoveListener(Gamble gamble, GambleGame gambleGame) {
        super(gamble, gambleGame);
        this.gamble = gamble;
        this.game = (TrafficRaceGame) gambleGame;
        this.lastPushed = new HashMap<>();

        init();
    }

    private void init() {
        Location x = game.getMinigameWorld().getBlockLocation("trafficrace_goal_x");
        Location z = game.getMinigameWorld().getBlockLocation("trafficrace_goal_z");
        if (x != null) {
            direction = "x";
            coordinate = x.getBlockX();
        }
        if (z != null) {
            direction = "z";
            coordinate = z.getBlockZ();
        }
    }

    private void finish(GamblePlayer player) {
        if (game.getPlayersInGoal().size() < neededPlayers) {
            player.getPlayer().setGameMode(GameMode.SPECTATOR);
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_STICKS, 1f, 1f);
            game.getPlayersInGoal().add(player);
            ((GamePlugin) gamble).getMessenger().broadcast("§6" + player.getPlayer().getName() + " §7hat das Spiel als §f" + game.getPlayersInGoal().size() + " §7beendet!");
        }
        if (game.getPlayersInGoal().size() == neededPlayers && !isFinished) {
            isFinished = true;
            game.stopStateChanger();
            GambleGameResult[] results = new GambleGameResult[neededPlayers];
            Iterator<GamblePlayer> players = game.getPlayersInGoal().iterator();
            int placement = 1;
            while (players.hasNext()) {
                GamblePlayer gp = players.next();
                results[placement - 1] = new GambleGameResult(gp, placement, 6);
                placement++;
            }
            Bukkit.getScheduler().scheduleSyncDelayedTask(game, () -> gamble.finishGambleGame(GambleGameType.TRAFFIC_RACE, EndReason.ENDED, results), 5 * 20);

        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();


        Location movedFrom = e.getFrom();
        Location movedTo = e.getTo();
        if (game.getTrafficState() == TrafficState.STOP) {
            if ((movedFrom.getX() != movedTo.getX()) || (movedFrom.getY() != movedTo.getY()) || (movedFrom.getZ() != movedTo.getZ())) {
                if ((lastPushed.getOrDefault(p, 0L) + 1500 < System.currentTimeMillis())) {
                    lastPushed.put(p, System.currentTimeMillis());
                    Vector vec = game.getMinigameWorld().getLocation("trafficrace_callback").getDirection().normalize();
                    vec.setY(Math.max(0.4000000059604645D, vec.getY()));
                    p.setVelocity(vec.multiply(2));

                    p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 1f, 1f);
                }
            }
        }

        if (direction.equalsIgnoreCase("x")) {
            if (p.getLocation().getBlockX() == coordinate) {
                finish(gamble.getGamblePlayer(p.getUniqueId()));
            }
        }

        if (direction.equalsIgnoreCase("z")) {
            if (p.getLocation().getZ() == coordinate) {
                finish(gamble.getGamblePlayer(p.getUniqueId()));
            }
        }
    }

}
