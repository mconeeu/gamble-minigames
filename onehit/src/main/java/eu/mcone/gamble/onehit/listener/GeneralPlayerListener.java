package eu.mcone.gamble.onehit.listener;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.gamble.api.minigame.EndReason;
import eu.mcone.gamble.api.minigame.GameResult;
import eu.mcone.gamble.api.player.GamblePlayer;
import eu.mcone.gamble.onehit.OneHit;
import eu.mcone.gamble.onehit.game.HotbarItems;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.util.Vector;

import java.util.Iterator;

public class GeneralPlayerListener implements Listener {

    @EventHandler
    public void on(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        OneHit.getInstance().getAlivedPlayers().remove(p);
    }

    @EventHandler
    public void on(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();

            if (e.getDamager() instanceof Player) {
                Player k = (Player) e.getDamager();


                if (OneHit.getInstance().getAlivedPlayers().contains(p) && OneHit.getInstance().getAlivedPlayers().contains(k)) {
                    if (k.getItemInHand().hasItemMeta() && k.getItemInHand().equals(HotbarItems.IRON_SWORD)) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5, 4500, false, false));
                        p.setHealth(0);
                        e.setCancelled(false);
                    }
                } else {
                    OneHit.getInstance().getMessenger().send(k, "§4Du darfst diesen Spieler noch nicht schlagen!");
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void on(PlayerDeathEvent e) {
        Player p = e.getEntity();
        Player k = p.getKiller();
        p.getInventory().clear();

        p.setVelocity(new Vector(0, 0, 0));


        if (k != null) {
            if (OneHit.getInstance().getAlivedPlayers().contains(p) && OneHit.getInstance().getAlivedPlayers().contains(k)) {
                OneHit.getInstance().getAlivedPlayers().remove(p);

                if (OneHit.getInstance().getAlivedPlayers().size() == 1) {
                    if (OneHit.getInstance().getAlivedPlayers().contains(k)) {
                        OneHit.getInstance().getAlivedPlayers().remove(k);

                        OneHit.getInstance().getPlayersInGoal().add((GamblePlayer) k);
                        OneHit.getInstance().getMessenger().broadcast("§6" + k.getPlayer().getName() + " §7hat das Spiel überlebt und hat somit gewonnen!");

                            GameResult[] results = new GameResult[1];
                            Iterator<GamblePlayer> players = OneHit.getInstance().getPlayersInGoal().iterator();
                            int placement = 1;
                            while (players.hasNext()) {
                                GamblePlayer gp = players.next();
                                results[placement - 1] = new GameResult(gp, placement, 6);
                                placement++;
                            }

                            OneHit.getInstance().getGameHandler().finishGame(EndReason.ENDED, results);


                        return;
                    }
                }

                    OneHit.getInstance().getMessenger().send(k, "§7Du hast §f" + p.getName() + " §7getötet");
                    OneHit.getInstance().getMessenger().send(p, "§7Du wurdest von §f" + k.getName() + " §7getötet!");
                }
        } else {
            OneHit.getInstance().getMessenger().send(p, "§cDu bist gestorben");
        }

        e.setDeathMessage("");
        p.spigot().respawn();
    }

    @EventHandler
    public void on(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        Bukkit.getOnlinePlayers().forEach(x -> x.teleport(OneHit.getInstance().getMinigameWorld().getLocation("onehit_spawn")));
    }
}