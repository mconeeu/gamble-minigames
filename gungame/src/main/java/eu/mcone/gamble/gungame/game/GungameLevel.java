package eu.mcone.gamble.gungame.game;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class GungameLevel {

    private int level;
    private Map<String, ItemStack> items;

    private transient Map<Integer, ItemStack> itemsMap;

    public GungameLevel(int level, Map<Integer, ItemStack> items) {
        this.level = level;
        this.items = new HashMap<>();

        for (Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
            this.items.put(String.valueOf(entry.getKey()), entry.getValue());
        }
    }

    public Map<Integer, ItemStack> calculateItems() {
        if (itemsMap == null) {
            itemsMap = new HashMap<>();
            for (Map.Entry<String, ItemStack> entry : items.entrySet()) {
                itemsMap.put(Integer.parseInt(entry.getKey()), entry.getValue());
            }
        }

        return itemsMap;
    }
}
