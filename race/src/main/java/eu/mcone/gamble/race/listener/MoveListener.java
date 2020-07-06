package eu.mcone.gamble.race.listener;

import eu.mcone.gamble.api.Gamble;
import eu.mcone.gamble.api.minigame.EndReason;
import eu.mcone.gamble.api.minigame.GameResult;
import eu.mcone.gamble.api.player.GamblePlayer;
import eu.mcone.gamble.race.Race;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Iterator;

public class MoveListener implements Listener {

    private String direction;
    private int coordinate;
    int neededPlayers = 1;
    private boolean isFinished = false;

    public MoveListener() {
        init();
    }

    private void init() {
        Location x = Race.getInstance().getMinigameWorld().getBlockLocation("race_goal_x");
        Location z = Race.getInstance().getMinigameWorld().getBlockLocation("race_goal_z");
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
        if (Race.getInstance().getPlayersInGoal().size() < neededPlayers) {
            player.getPlayer().setGameMode(GameMode.SPECTATOR);
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_STICKS, 1f, 1f);
            Race.getInstance().getPlayersInGoal().add(player);
            Race.getInstance().getMessenger().broadcast("§6" + player.getPlayer().getName() + " §7hat das Spiel als §f" + Race.getInstance().getPlayersInGoal().size() + " §7beendet!");
        }

        if (Race.getInstance().getPlayersInGoal().size() == neededPlayers && !isFinished) {
            isFinished = true;
            GameResult[] results = new GameResult[neededPlayers];
            Iterator<GamblePlayer> players = Race.getInstance().getPlayersInGoal().iterator();
            int placement = 1;
            while (players.hasNext()) {
                GamblePlayer gp = players.next();
                results[placement - 1] = new GameResult(gp, placement, 6);
                placement++;
            }

            Race.getInstance().getGameHandler().finishGame(EndReason.ENDED, results);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (direction.equalsIgnoreCase("x")) {
            if (p.getLocation().getBlockX() == coordinate) {
                finish(Gamble.getInstance().getGamblePlayer(p.getUniqueId()));
            }
        }

        if (direction.equalsIgnoreCase("z")) {
            if (p.getLocation().getZ() == coordinate) {
                finish(Gamble.getInstance().getGamblePlayer(p.getUniqueId()));
            }
        }
    }


}
