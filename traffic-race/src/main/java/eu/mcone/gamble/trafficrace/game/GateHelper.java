package eu.mcone.gamble.trafficrace.game;

import eu.mcone.gamble.api.EndReason;
import eu.mcone.gamble.api.minigame.GambleGameType;
import eu.mcone.gamble.trafficrace.TrafficRaceGame;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Datei erstellt von: Felix Schmid in Projekt: eu.mcone-gamble-minigames
 */
public class GateHelper {

    private TrafficRaceGame game;
    private Map<Location, Material> materials;

    public GateHelper(TrafficRaceGame game) {
        this.game = game;
        materials = new HashMap<Location, Material>();
    }

    public void remove() {
        for(Block b : getBlocksBetween()) {
            materials.put(b.getLocation(), b.getType());
            b.setType(Material.AIR);
            System.out.println(b.getLocation().toString());
        }
    }

    public void recreate() {
        for(Map.Entry<Location, Material> entry : materials.entrySet()) {
            entry.getKey().getBlock().setType(entry.getValue());
        }
    }

    private List<Block> getBlocksBetween() {
        List<Block> blocks = new ArrayList<>();
        Location start = game.getMinigameWorld().getBlockLocation("trafficrace_gate1");
        Location end = game.getMinigameWorld().getBlockLocation("trafficrace_gate2");

        if (start == null || end == null) {
            game.getGamble().finishGambleGame(GambleGameType.TRAFFIC_RACE, EndReason.EXCEPTION);
            return blocks;
        }

        int topBlockX = (Math.max(start.getBlockX(), end.getBlockX()));
        int bottomBlockX = (Math.min(start.getBlockX(), end.getBlockX()));

        int topBlockY = (Math.max(start.getBlockY(), end.getBlockY()));
        int bottomBlockY = (Math.min(start.getBlockY(), end.getBlockY()));

        int topBlockZ = (Math.max(start.getBlockZ(), end.getBlockZ()));
        int bottomBlockZ = (Math.min(start.getBlockZ(), end.getBlockZ()));

        for (int x = bottomBlockX; x <= topBlockX; x++) {
            for (int z = bottomBlockZ; z <= topBlockZ; z++) {
                for (int y = bottomBlockY; y <= topBlockY; y++) {
                    System.out.println("Block found");
                    Block block = start.getWorld().getBlockAt(x, y, z);

                    blocks.add(block);
                }
            }
        }
        return blocks;
    }

}
