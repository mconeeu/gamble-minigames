package eu.mcone.gamble.gungame.listener;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.gamble.api.Gamble;
import eu.mcone.gamble.api.minigame.EndReason;
import eu.mcone.gamble.gungame.GunGame;
import eu.mcone.gamble.gungame.game.GungameLevel;
import eu.mcone.gamble.gungame.handler.GameHandler;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.player.GamePlayerState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class GeneralPlayerListener implements Listener {

    @EventHandler
    public void on(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        GunGame.getInstance().getAlivedPlayers().remove(p);
    }


    @EventHandler
    public void onEntityDeathEvent(PlayerDeathEvent e) {
        Player victim = e.getEntity();
        e.setDeathMessage(null);
        victim.spigot().respawn();

        if (GunGame.getInstance().getAlivedPlayers().contains(victim)) {
            Player killer = e.getEntity().getKiller() != null ? e.getEntity().getKiller() : Gamble.getInstance().getDamageLogger().getKiller(victim);

            if (killer != null) {
                CoreSystem.getInstance().getMessenger().send(killer, "§aDu hast §6" + victim.getName() + " §agetötet!");
                CoreSystem.getInstance().getMessenger().send(victim, "§cDu wurdest von §6" + killer.getName() + " §cgetötet!");

                GungameLevel levelKiller = GunGame.getInstance().getGungameLevels().get(killer.getLevel() + 1);
                setItemsLevel(killer, levelKiller);
                killer.setLevel(levelKiller.getLevel());

                victim.setLevel(victim.getLevel() > 0 ? (victim.getLevel() - 1) : 0);
            }
        }
    }

    @EventHandler
    public void on(PlayerRespawnEvent e) {
        if (GameAPI.getInstance().getGamePlayer(e.getPlayer()).getState().equals(GamePlayerState.PLAYING)) {
            Bukkit.getScheduler().runTask(GunGame.getInstance(), () -> setItemsLevel(
                    e.getPlayer(),
                    GunGame.getInstance().getGungameLevels().get(e.getPlayer().getLevel())
            ));
        }
    }

    @EventHandler
    public void on(EntityDamageEvent e) {
        if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Material to = event.getTo().getBlock().getType();
        if (to.equals(Material.WATER) || to.equals(Material.STATIONARY_WATER)) {
            event.getPlayer().setHealth(0);
        }
    }

    public static void setItemsLevel(Player target, GungameLevel level) {
        System.out.println("set "+target.getName()+" level "+level.getLevel());

        Map<Integer, ItemStack> test = level.calculateItems();
        target.getInventory().clear();
        for (Map.Entry<Integer, ItemStack> item : test.entrySet()) {
            target.getInventory().setItem(item.getKey(), item.getValue());
        }
    }

}
