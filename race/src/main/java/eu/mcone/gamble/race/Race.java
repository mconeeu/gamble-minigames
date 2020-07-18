package eu.mcone.gamble.race;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import eu.mcone.gamble.api.minigame.GambleGame;
import eu.mcone.gamble.api.player.GamblePlayer;
import eu.mcone.gamble.race.game.GateHelper;
import eu.mcone.gamble.race.handler.GameHandler;
import eu.mcone.gamble.race.listener.MoveListener;
import lombok.Getter;
import org.bukkit.ChatColor;

import java.util.LinkedList;

public class Race extends GambleGame {
    @Getter
    public static Race instance;
    @Getter
    private CoreWorld minigameWorld;
    @Getter
    private GateHelper gateHelper;
    @Getter
    private LinkedList<GamblePlayer> playersInGoal;

    public Race() {
        super("Race", ChatColor.WHITE, "race.prefix");

        sendConsoleMessage("Initialising traffic race minigame...");

        setGameHandler(new GameHandler());
        registerListener(new MoveListener());
    }

    @Override
    public void initiate() {
        instance = this;
        minigameWorld = CoreSystem.getInstance().getWorldManager().getWorld("race");
        gateHelper = new GateHelper(this);
        playersInGoal = new LinkedList<>();

    }

    @Override
    public void abandon() {

    }
}
