package eu.mcone.gamble.tntrun.listener;

import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gamble.tntrun.Tntrun;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.player.GamePlayerState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;

public class GeneralPlayerListener implements Listener {

    public static ArrayList<Player> spectators = new ArrayList<>();
    public static ArrayList<CorePlayer> alive = new ArrayList<>();

    public static boolean isMinigamer(CorePlayer player) {
        return !player.isVanished();
    }

    @EventHandler
    public static void on(PlayerDeathEvent event) {
        Player player = event.getEntity();
        spectators.add(player);
        GameAPI.getInstance().getGamePlayer(player).setState(GamePlayerState.SPECTATING);
    }

    @EventHandler
    public void on(ExplosionPrimeEvent event) {
        if (event.getEntity().getType().equals(EntityType.PRIMED_TNT)) {
            event.setCancelled(true);
            event.getEntity().remove();
        }
    }

    @EventHandler
    public void on(PlayerMoveEvent event) {
        Location pL = event.getPlayer().getLocation();
        Location location = new Location(pL.getWorld(), pL.getX(), pL.getY() - 1, pL.getZ());
        Block block = location.getBlock();

        if (block.getType().equals(Material.TNT)) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(Tntrun.getInstance(), () -> {
                block.setType(Material.AIR);
                TNTPrimed primed = location.getWorld().spawn(location, TNTPrimed.class);
                primed.setFuseTicks(20);
            }, 20);
        }

    }
}
