package eu.mcone.gamble.gungame.listeners;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.gamble.api.minigame.EndReason;
import eu.mcone.gamble.gungame.GunGame;
import eu.mcone.gamble.gungame.game.GungameLevel;
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
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class GeneralPlayerListener implements Listener {

    public static Map<Player, Player> damagers = new HashMap<>();

    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent event) {
        EntityDamageEvent.DamageCause damageCause = event.getEntity().getLastDamageCause().getCause();
        if (event.getEntity() instanceof Player && event.getEntity().getKiller() instanceof Player) {
            Player victim = (Player) event.getEntity();
            Player killer = victim.getKiller();
            handleDeath(victim, killer);
        } else if (event.getEntity() instanceof Player && damageCause == EntityDamageEvent.DamageCause.VOID) {
            Player killer = damagers.get(event.getEntity());
            handleDeath((Player) event.getEntity(), killer);
        }
    }

    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            damagers.put((Player) event.getEntity(), (Player) event.getDamager());
        }
    }


    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        event.getEntity().spigot().respawn();
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


    @EventHandler
    public void on(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Player) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getShooter() instanceof Player) {
                Player shooter = (Player) arrow.getShooter();
                GunGame.getInstance().getGameHandler().finishGame(EndReason.ENDED);
            }
        }
    }

    public void setItemsLevel(Player target, GungameLevel level) {
        Map<Integer, ItemStack> test = level.calculateItems();
        target.getInventory().clear();
        for (Map.Entry<Integer, ItemStack> item : test.entrySet()) {
            target.getInventory().setItem(item.getKey(), item.getValue());
        }
    }

    public void handleDeath(Player victim, Player killer) {
        CoreSystem.getInstance().getMessenger().send(killer, "§aDu hast §6" + victim.getName() + " §agetötet!");
        CoreSystem.getInstance().getMessenger().send(killer, "§cDu wurdest von §6" + victim.getName() + " §cgetötet!");
        GungameLevel levelKiller = GunGame.getInstance().getGungameLevels().get(killer.getLevel() + 1);
        GungameLevel levelVictim = GunGame.getInstance().getGungameLevels().get(victim.getLevel() - 1);
        setItemsLevel(killer, levelKiller);
        setItemsLevel(victim, levelVictim);
        killer.setLevel(levelKiller.getLevel());
        victim.setLevel(levelVictim.getLevel());
        damagers.remove(victim);
    }
}
