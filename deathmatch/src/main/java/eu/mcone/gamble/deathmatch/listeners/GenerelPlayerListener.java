package eu.mcone.gamble.deathmatch.listeners;

import eu.mcone.gamble.api.minigame.EndReason;
import eu.mcone.gamble.api.minigame.GameResult;
import eu.mcone.gamble.api.player.GamblePlayer;
import eu.mcone.gamble.deathmatch.Deathmatch;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.util.Vector;

import java.util.Iterator;

public class GenerelPlayerListener implements Listener {

    @EventHandler
    public void on(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        Deathmatch.getInstance().getAlivedPlayers().remove(p);
    }

    @EventHandler
    public void on(EntityDamageEvent e) {
        if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void on(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();

            if (e.getDamager() instanceof Player) {
                Player k = (Player) e.getDamager();


                if (Deathmatch.getInstance().getAlivedPlayers().contains(p) && Deathmatch.getInstance().getAlivedPlayers().contains(k)) {
                        e.setCancelled(false);
                } else {
                    Deathmatch.getInstance().getMessenger().send(k, "§4Du darfst diesen Spieler noch nicht schlagen!");
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
            if (Deathmatch.getInstance().getAlivedPlayers().contains(p) && Deathmatch.getInstance().getAlivedPlayers().contains(k)) {
                Deathmatch.getInstance().getAlivedPlayers().remove(p);

                if (Deathmatch.getInstance().getAlivedPlayers().size() == 1) {
                    if (Deathmatch.getInstance().getAlivedPlayers().contains(k)) {
                        Deathmatch.getInstance().getAlivedPlayers().remove(k);

                        Deathmatch.getInstance().getPlayersInGoal().add((GamblePlayer) k);

                        GameResult[] results = new GameResult[1];
                        Iterator<GamblePlayer> players = Deathmatch.getInstance().getPlayersInGoal().iterator();
                        int placement = 1;
                        while (players.hasNext()) {
                            GamblePlayer gp = players.next();
                            results[placement - 1] = new GameResult(gp, placement, 6);
                            placement++;
                        }

                        Deathmatch.getInstance().getGameHandler().finishGame(EndReason.ENDED, results);


                        return;
                    }
                }

                Deathmatch.getInstance().getMessenger().send(k, "§7Du hast §f" + p.getName() + " §7getötet");
                Deathmatch.getInstance().getMessenger().send(p, "§7Du wurdest von §f" + k.getName() + " §7getötet!");
            }
        } else {
            Deathmatch.getInstance().getMessenger().send(p, "§cDu bist gestorben");
        }

        e.setDeathMessage("");
        p.spigot().respawn();
    }

    @EventHandler
    public void on(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        Bukkit.getOnlinePlayers().forEach(x -> x.teleport(Deathmatch.getInstance().getMinigameWorld().getLocation("deathmatch_spawn")));
    }
}
