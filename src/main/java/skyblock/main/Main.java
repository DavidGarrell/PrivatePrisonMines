package skyblock.main;

import de.backpack.listener.EconomyAPI;
import dev.sergiferry.playernpc.api.NPCLib;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.MemoryNPCDataStore;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import skyblock.api.CustomFrameMaterials;
import skyblock.api.IslandManager;
import skyblock.api.IslandMaterials;
import skyblock.api.WorldManager;
import skyblock.battlepass.api.BattlePassLevel;
import skyblock.menus.*;
import skyblock.playtimerewards.commands.PlayTimeRewardsCommands;
import skyblock.commands.IslandCommands;
import skyblock.events.IslandNPC;
import skyblock.events.PlayerJoin;
import skyblock.listeners.Listeners;
import skyblock.playtimerewards.rewards.RewardStore;
import skyblock.store.PlayerStore;

import java.io.File;
import java.util.*;

public final class Main extends JavaPlugin {

    public static Main instance;
    public static World emptyWorld;

    public static IslandManager islandManager;
    public static IslandMaterials islandMaterials = new IslandMaterials();

    public static EconomyAPI economyAPI;
    public static String prefix = "§bMines | ";

    private static File dataFolder;
    private static FileConfiguration configuration;

    public PlayerStore playerStore;

    public BattlePassLevel passLevel;

    public RewardStore rewardStore;

    public CustomFrameMaterials customFrameMaterials;
    public NPCRegistry registery;

    @Override
    public void onEnable() {
        //NPCLib.getInstance().registerPlugin(this);

        registery = CitizensAPI.createAnonymousNPCRegistry(new MemoryNPCDataStore());
        dataFolder = new File(getDataFolder().getPath());
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        emptyWorld = WorldManager.createEmptyWorld("Mines");
        instance = this;
        islandManager = new IslandManager();
        economyAPI = de.backpack.main.Main.economyAPI;
        playerStore = new PlayerStore();
        this.passLevel = new BattlePassLevel();
        rewardStore = new RewardStore();
        customFrameMaterials = new CustomFrameMaterials();


        Objects.requireNonNull(getCommand("mine")).setExecutor(new IslandCommands(this));
        Objects.requireNonNull(getCommand("pmine")).setExecutor(new IslandCommands(this));
        Objects.requireNonNull(getCommand("rewards")).setExecutor(new PlayTimeRewardsCommands(this));
        Objects.requireNonNull(getCommand("gifts")).setExecutor(new PlayTimeRewardsCommands(this));

        Bukkit.getServer().getPluginManager().registerEvents(new IslandLevelsMenu(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new Listeners(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new IslandPrestigeMenu(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new IslandMenu(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new IslandNPC(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new MineBlocksMenu(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new CustomFrameMenu(), this);


    }


    @Override
    public void onDisable() {


    }

}
