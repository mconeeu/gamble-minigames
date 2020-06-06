/*
 * Copyright (c) 2017 - 2020 Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamble.trafficrace.game;

import eu.mcone.gamble.api.minigame.EndReason;
import eu.mcone.gamble.trafficrace.TrafficRaceGame;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GateHelper {

    private final TrafficRaceGame game;
    private final Map<Location, Material> materials;

    public GateHelper(TrafficRaceGame game) {
        this.game = game;
        materials = new HashMap<>();
    }

    public void remove() {
        for (Block b : getBlocksBetween()) {
            materials.put(b.getLocation(), b.getType());
            b.setType(Material.AIR);
            System.out.println(b.getLocation().toString());
        }
    }

    public void recreate() {
        for (Map.Entry<Location, Material> entry : materials.entrySet()) {
            entry.getKey().getBlock().setType(entry.getValue());
        }
    }

    private List<Block> getBlocksBetween() {
        List<Block> blocks = new ArrayList<>();
        Location start = game.getMinigameWorld().getBlockLocation("trafficrace_gate1");
        Location end = game.getMinigameWorld().getBlockLocation("trafficrace_gate2");

        if (start == null || end == null) {
            TrafficRaceGame.getInstance().getGameHandler().finishGame(EndReason.EXCEPTION);
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
