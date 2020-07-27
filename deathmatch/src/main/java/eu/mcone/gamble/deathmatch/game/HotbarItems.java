package eu.mcone.gamble.deathmatch.game;

import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import net.minecraft.server.v1_8_R3.ItemSaddle;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class HotbarItems {

    public static final ItemStack STONE_SWORD = new ItemBuilder(Material.STONE_SWORD)
            .displayName("§3Schwert")
            .create();

    public static final ItemStack ROD = new ItemBuilder(Material.FISHING_ROD)
            .displayName("§3Angel")
            .create();

    public static final ItemStack GOLD_APPLE = new ItemBuilder(Material.GOLDEN_APPLE)
            .displayName("§3Gold-Apfel")
            .create();
}
