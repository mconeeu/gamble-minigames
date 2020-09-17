package eu.mcone.gamble.gungame.commands;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.command.CorePlayerCommand;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gamble.api.minigame.GambleGame;
import eu.mcone.gamble.gungame.GunGame;
import eu.mcone.gamble.gungame.game.GungameLevel;
import eu.mcone.gamble.gungame.handler.GameHandler;
import net.minecraft.server.v1_8_R3.WorldManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SetupCommand extends CorePlayerCommand {

    public SetupCommand() {
        super("setup");
    }

    @Override
    public boolean onPlayerCommand(Player player, String[] args) {
        if (player.hasPermission("group.developer")) {
            if (!(args.length > 0)) {
                CoreSystem.getInstance().getMessenger().sendTransl(player, "system.command.wronguse");
            } else if (args.length == 2 && args[0].equalsIgnoreCase("createlevel")) {
                try {
                    int level = Integer.valueOf(args[1]);
                    Inventory inv = player.getInventory();
                    Map<Integer, ItemStack> items = new HashMap<>();
                    for (int i = 0; i < inv.getSize(); i++) {
                        ItemStack item = inv.getItem(i);
                        items.put(i, item);
                    }
                    CoreSystem.getInstance().getMongoDB().getCollection("gamble_gungame_levels", GungameLevel.class).insertOne(new GungameLevel(level, items));
                    CoreSystem.getInstance().getMessenger().sendTransl(player, "setup.gamble.gungame.commands.setup.level.success", level);
                } catch (NumberFormatException ex) {
                    CoreSystem.getInstance().getMessenger().sendTransl(player, "system.command.notnumber");
                }

            } else if (args.length > 1 && args[0].equalsIgnoreCase("display")) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    stringBuilder.append(args[i]).append(" ");
                }
                if (player.getItemInHand() != null) {
                    String displayname = stringBuilder.toString().replaceAll("&", "§");
                    ItemStack playerHand = player.getItemInHand().clone();
                    ItemStack item = ItemBuilder.wrap(playerHand).displayName(displayname).create();
                    player.setItemInHand(item);
                    CoreSystem.getInstance().getMessenger().sendTransl(player, "setup.gamble.gungame.commands.setup.display.success", displayname);
                } else {
                    CoreSystem.getInstance().getMessenger().sendTransl(player, "system.command.noiteminhand");
                }
            } else if (args.length == 2 && args[0].equalsIgnoreCase("unbreakable")) {
                if (player.getItemInHand() != null) {
                    ItemStack playerHand = player.getItemInHand().clone();
                    if (args[1].equalsIgnoreCase("true")) {
                        ItemStack item = ItemBuilder.wrap(playerHand).unbreakable(true).create();
                        player.setItemInHand(item);
                        CoreSystem.getInstance().getMessenger().sendTransl(player, "setup.gamble.gungame.commands.setup.unbreakable.unbreakable");
                    } else if (args[0].equalsIgnoreCase("false")) {
                        ItemStack item = ItemBuilder.wrap(playerHand).unbreakable(true).create();
                        player.setItemInHand(item);
                        CoreSystem.getInstance().getMessenger().sendTransl(player, "setup.gamble.gungame.commands.setup.unbreakable.breakable");
                    }
                } else {
                    CoreSystem.getInstance().getMessenger().sendTransl(player, "system.command.noiteminhand");
                }
            } else {
                CoreSystem.getInstance().getMessenger().sendTransl(player, "system.command.wronguse");
            }
        } else {
            CoreSystem.getInstance().getMessenger().sendTransl(player, "system.command.noperm");
        }
        return false;
    }

}
