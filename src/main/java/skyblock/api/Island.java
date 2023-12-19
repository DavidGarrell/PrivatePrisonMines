package skyblock.api;

import com.fastasyncworldedit.bukkit.regions.WorldGuardFeature;
import com.sk89q.worldedit.math.BlockVector3;
import de.backpack.listener.EconomyAPI;
import de.prestigesystem.api.PlayerTalents;
import de.prestigesystem.api.Talent;
import de.prestigesystem.api.UserManager;
import de.prestigesystem.events.TalentInitialzier;
import dev.sergiferry.playernpc.api.NPC;
import dev.sergiferry.playernpc.api.NPCLib;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import skyblock.main.Main;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.List;

public class Island {

    private int ISLAND_SIZE = 35;
    private int ISLAND_HIGH = 40;
    private int ISLAND_LEVEL = 1;

    private int ILSAND_STARTXP_NEED = 100;
    private int ILSAND_XP_PERLEVEL = 100;


    private int ISLAND_WORLD_LEVEL = 0;

    private String ISLAND_WORLD_STRING = "a";
    public static final int ISLAND_LEVEL_MAX = 900;

    public static final int ISLAND_START_SIZE = 35;

    public static final int ISLAND_START_HIGH = 40;

    public static Plugin plugin;

    private boolean isOnIsland;
    private Location islandLocation;

    private Location islandPlayerSpawnPoint;

    public List<UUID> islandBannedPlayerMap = new ArrayList<>();

    public List<UUID> islandTrust = new ArrayList<>();

    public List<UUID> member = new ArrayList<>();

    public List<Island> trustedIslands = new ArrayList<>();

    private boolean islandClose = false;
    private Player islandOwner;
    private BossBar actionBar;

    private String prefix = Main.prefix;
    private IslandManager islandManager = Main.islandManager;
    NPC.Personal npc;

    public Island(Player owner, Plugin plugin) {
        this.islandOwner = owner;
        Island.plugin = plugin;
    }

    public void createIsland(Player player) {
        World world = player.getWorld();
        UUID uuid = player.getUniqueId();

        IslandGenerator islandGenerator = new IslandGenerator();

        this.islandLocation = getNextGridLocation(islandManager.lastIslandLocation);
        islandGenerator.loadIslandSchematic(player, "/island.schem", islandLocation);

        islandGenerator.clearIsland(getIslandLocation(), ISLAND_SIZE+1, ISLAND_SIZE+1, ISLAND_START_HIGH, player);
        islandGenerator.generateIsland(islandLocation, ISLAND_START_SIZE, ISLAND_START_SIZE, ISLAND_START_HIGH, 0, player);
        islandGenerator.generateBedrockFrame(islandLocation, ISLAND_START_SIZE + 4, ISLAND_START_SIZE + 4, ISLAND_START_HIGH, player);
        islandGenerator.clearIslandSchem(getIslandLocation(), ISLAND_SIZE+4, ISLAND_SIZE+4, ISLAND_HIGH+1, player);
        mineAutoResetByPercent(player);
        autoUpgrade(player);
        islandManager.lastIslandLocation = islandLocation;
        initActionBar();

        //World Guard Region

        placeNPC(player);
    }



    private Location getNextGridLocation(final Location lastIsland) {
        int x = lastIsland.getBlockX();
        int z = lastIsland.getBlockZ();
        int d = 500;

        Location nextIsland = lastIsland.clone();

        if (x < z) {
            if (-1 * x < z) {
                nextIsland.setX(nextIsland.getX() + d);
                return nextIsland;
            }
            nextIsland.setZ(nextIsland.getZ() + d);
            return nextIsland;
        }
        if (x > z) {
            if (-1 * x >= z) {
                nextIsland.setX(nextIsland.getX() - d);
                return nextIsland;
            }
            nextIsland.setZ(nextIsland.getZ() - d);
            return nextIsland;
        }
        if (x <= 0) {
            nextIsland.setZ(nextIsland.getZ() + d);
            return nextIsland;
        }
        nextIsland.setZ(nextIsland.getZ() - d);
        return nextIsland;
    }

    public void emeraldRush(Player player){
        IslandGenerator islandGenerator = new IslandGenerator();

        islandGenerator.emeraldRush(getIslandLocation(), ISLAND_SIZE, ISLAND_SIZE, ISLAND_HIGH, ISLAND_LEVEL, player);
    }
    public void islandUpgrade(Player player) {

        if(getISLAND_LEVEL()<ISLAND_LEVEL_MAX) {
            this.ISLAND_LEVEL+=1;

            int next_block = ISLAND_LEVEL/10;
            int next_reward_level = 10-ISLAND_LEVEL%10;

            if (this.ISLAND_LEVEL % 10 == 0) {
                IslandGenerator islandGenerator = new IslandGenerator();
                String color = RewardStrings.getRandomColor()+ "§l";
                player.sendMessage(color + RewardStrings.getRandomMessage() + "§fYou have reached mine level " + color + ISLAND_LEVEL);
                IslandMaterials islandMaterials = new IslandMaterials();
                player.sendMessage("§6§lUNLOCK §fnew Mine Block: §f" + islandMaterials.islandMaterials.get(next_block));

                islandGenerator.clearIsland(getIslandLocation(), ISLAND_SIZE, ISLAND_SIZE, ISLAND_HIGH, player);
                islandGenerator.clearIslandFrame(getIslandLocation(), ISLAND_SIZE + 4, ISLAND_SIZE + 4, ISLAND_HIGH, player);

                this.ISLAND_SIZE = ISLAND_START_SIZE + ISLAND_LEVEL/10 * 2;
                this.ISLAND_HIGH = ISLAND_START_HIGH + ISLAND_LEVEL/10;

                islandGenerator.clearIslandSchem(getIslandLocation(), ISLAND_SIZE+4, ISLAND_SIZE+4, ISLAND_HIGH+1, player);
                islandGenerator.clearIsland(getIslandLocation(), ISLAND_SIZE+1, ISLAND_SIZE+1, ISLAND_HIGH+1, player);
                islandGenerator.generateIsland(getIslandLocation(), ISLAND_SIZE, ISLAND_SIZE, ISLAND_HIGH, ISLAND_LEVEL, player);
                islandGenerator.generateBedrockFrame(getIslandLocation(), ISLAND_SIZE + 4, ISLAND_SIZE + 4, ISLAND_HIGH, player);

                moveNPC();
            }
            else {
                String color = RewardStrings.getRandomColor()+ "§l";
                player.sendMessage(color + RewardStrings.getRandomMessage() + "§fYou have reached mine level " + color + ISLAND_LEVEL + ". §fYou need " + color + next_reward_level + " §fmore mine level to get a new mine block");
            }
        }

    }

    public Location getIslandLocation() {
        return islandLocation;
    }

    public void setIslandLocation(Location islandLocation) {
        this.islandLocation = islandLocation;
    }

    public boolean isIslandClose() {
        return islandClose;
    }

    public void setIslandClose(boolean islandClose) {
        this.islandClose = islandClose;
    }

    public int getISLAND_SIZE() {
        return ISLAND_SIZE;
    }

    public void setISLAND_SIZE(int ISLAND_SIZE) {
        this.ISLAND_SIZE = ISLAND_SIZE;
    }

    public int getISLAND_HIGH() {
        return ISLAND_HIGH;
    }

    public int getISLAND_LEVEL() {
        return ISLAND_LEVEL;
    }

    public int getISLAND_WORLD_LEVEL() {
        return ISLAND_WORLD_LEVEL;
    }

    public void setISLAND_WORLD_LEVEL(int ISLAND_WORLD_LEVEL, Player player){
        IslandGenerator islandGenerator = new IslandGenerator();

        islandGenerator.clearIsland(getIslandLocation(), ISLAND_SIZE, ISLAND_SIZE, ISLAND_HIGH, player);
        islandGenerator.clearIslandFrame(getIslandLocation(), ISLAND_SIZE + 4, ISLAND_SIZE + 4, ISLAND_HIGH, player);


        this.ISLAND_SIZE = ISLAND_START_SIZE + ISLAND_WORLD_LEVEL * 6;
        this.ISLAND_HIGH = ISLAND_START_HIGH + ISLAND_WORLD_LEVEL * 3;

        if(ISLAND_WORLD_LEVEL<this.getISLAND_WORLD_LEVEL()) {
            islandGenerator.loadIslandSchematic(player, "/island.schem", islandLocation);
        }
        islandGenerator.clearIslandSchem(getIslandLocation(), ISLAND_SIZE+4, ISLAND_SIZE+4, ISLAND_HIGH+1, player);
        this.ISLAND_WORLD_LEVEL = ISLAND_WORLD_LEVEL;

        islandGenerator.clearIsland(getIslandLocation(), ISLAND_SIZE+1, ISLAND_SIZE+1, ISLAND_HIGH+1, player);
        islandGenerator.generateIsland(getIslandLocation(), ISLAND_SIZE, ISLAND_SIZE, ISLAND_HIGH, ISLAND_LEVEL, player);
        islandGenerator.generateBedrockFrame(getIslandLocation(), ISLAND_SIZE + 4, ISLAND_SIZE + 4, ISLAND_HIGH, player);

        moveNPC();
        setISLAND_WORLD_STRING();
    }

    public void setISLAND_LEVEL(int ISLAND_LEVEL, Player player) {

        IslandGenerator islandGenerator = new IslandGenerator();

        int ISLAND_LEVEL_BEFORE = this.ISLAND_LEVEL;
        this.ISLAND_LEVEL = ISLAND_LEVEL;

        if (this.ISLAND_LEVEL % 10 == 0) {

            islandGenerator.clearIsland(getIslandLocation(), ISLAND_SIZE, ISLAND_SIZE, ISLAND_HIGH, player);
            islandGenerator.clearIslandFrame(getIslandLocation(), ISLAND_SIZE + 4, ISLAND_SIZE + 4, ISLAND_HIGH, player);

            this.ISLAND_SIZE = ISLAND_START_SIZE + ISLAND_LEVEL/10 * 2;
            this.ISLAND_HIGH = ISLAND_START_HIGH + ISLAND_LEVEL/10;

            if(ISLAND_LEVEL<ISLAND_LEVEL_BEFORE) {
                islandGenerator.loadIslandSchematic(player, "/island.schem", islandLocation);
            }
            islandGenerator.clearIslandSchem(getIslandLocation(), ISLAND_SIZE+4, ISLAND_SIZE+4, ISLAND_HIGH+1, player);
            islandGenerator.clearIsland(getIslandLocation(), ISLAND_SIZE+1, ISLAND_SIZE+1, ISLAND_HIGH+1, player);
            islandGenerator.generateIsland(getIslandLocation(), ISLAND_SIZE, ISLAND_SIZE, ISLAND_HIGH, ISLAND_LEVEL, player);
            islandGenerator.generateBedrockFrame(getIslandLocation(), ISLAND_SIZE + 4, ISLAND_SIZE + 4, ISLAND_HIGH, player);

            moveNPC();

        }
    }

    public Location getIslandPlayerSpawnPoint() {

        Location playerSpawnLoc = islandLocation.clone();

        playerSpawnLoc.add(0.5, 0, -ISLAND_SIZE/2-3.5);

        return playerSpawnLoc;
    }

    public void placeNPC(Player player){

        npc = NPCLib.getInstance().generatePersonalNPC(player, plugin, player.getUniqueId().toString(), getIslandNPCSpawnPoint());
        npc.setText("Cobblemate", "§e§lRight Click".toUpperCase());
        npc.setGazeTrackingType(NPC.GazeTrackingType.PLAYER);
        npc.setGlowing(true, ChatColor.AQUA);
        npc.setSkin(
                "ewogICJ0aW1lc3RhbXAiIDogMTY5NTI4NzgzMDY2OSwKICAicHJvZmlsZUlkIiA6ICIzYTJjNmQ2MmU2ZDg0MGMzYTY3MzA2MzA2NzNjMzQ3NCIsCiAgInByb2ZpbGVOYW1lIiA6ICJTczU2IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzViYmM1NDVjZjkyNjM1MDA0ODJlMTE1MGMyYzgwM2I3NWM0Y2U5MDYwMWJjZjg1ZTk5MjhjZjhiNGFkOTM5Y2MiCiAgICB9CiAgfQp9",
                "IL/AXPEacpsR/Ys/NSzOXYBUaiByrMrdSEY2427YCxxpVXhEJaPfEI6Nxslxfvdaxwje+KshJonyKE3KvA2MPGXla7ju3MAmSs10FpX5At5aD+yRLY+KouY3+onHs9mnYDXLd71eQE4ZRE2LF1KZUlWGdIqoa4ijeifCrCNa/7juvOk8r3x9vraHwo8ZnIGxV7FHVzmh8PpLoKk+bzKKp2VfZpF+FHXZR7AeHR2GiIzt3BAgEKvLlsAWCtj1jPznzlEAsoYz+jSmqnEp8sOFyDBMj8F6pOgVRIBoDa7qc0n5iLoiYwhRWGyDUW8Zy7YtKQrVcwSa9dTrJq6cARmuwqa0jvDME7fFAglS+ppPF2i7KvRSuHOkKsNDamky9ZwWutW/Q32vsWKpZTfpDC9LwDBzyys4oNm0wunaO40XKNb0u3rkjNp0sYoo2wv9ovyVyT8s20s8htcGbgxKSrwJYgRHCuKKFfB1W+Z2J2rFwLK/DjrQMJ8MIqE9xEv3d6dvo8y3MLMxHeN04jr8jpOtwD5tzaN0vyjXQKieApJJEMx9F5omAkIdIV1BWBCV/fD8LGqBYtgKBtbPvzmcAXpCIEGMVF5mtmTi+oaNjrupSqT6w41xJIjNdYUbitZV0PUuR0K3dECH0CbZO1WcbO1KmpS4vF5y9EfECDrlbwIylgc="
        );
        npc.create();
        npc.setItemInMainHand(new ItemStack(Material.DIAMOND_PICKAXE));
        npc.setItemInOffHand(new ItemStack(Material.CHEST));
        npc.show();
    }

    public void moveNPC(){

        npc.teleport(getIslandNPCSpawnPoint());
    }

    public Location getIslandNPCSpawnPoint() {

        Location playerSpawnLoc = islandLocation.clone();

        playerSpawnLoc.add(2.5, -1, -ISLAND_SIZE/2-1.5);

        return playerSpawnLoc;
    }


    public void setIslandPlayerSpawnPoint(Location islandPlayerSpawnPoint) {
        this.islandPlayerSpawnPoint = islandPlayerSpawnPoint;
    }

    public void resetIsland(Player player) {

        IslandGenerator islandGenerator = new IslandGenerator();

        islandGenerator.resetIsland(getIslandLocation(), ISLAND_SIZE, ISLAND_SIZE, ISLAND_HIGH, ISLAND_LEVEL, player);
    }
    public Player getIslandOwner() {
        return islandOwner;
    }

    public void setIslandOwner(Player islandOwner) {
        this.islandOwner = islandOwner;
    }
    private int taskid_p = -2;
    public void mineAutoResetByPercent(Player player) {

        IslandGenerator islandGenerator = new IslandGenerator();

        taskid_p = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {

            if(islandGenerator.getBlocksInMine(getIslandLocation(), ISLAND_SIZE, ISLAND_SIZE, ISLAND_HIGH, ISLAND_LEVEL, player)/2 <=islandGenerator.getAirBlocksInMine(getIslandLocation(), ISLAND_SIZE, ISLAND_SIZE, ISLAND_HIGH, ISLAND_LEVEL, player)) {
                resetIsland(player);
            } else if (!player.isOnline()) {
                Bukkit.getServer().getScheduler().runTask(plugin, () -> Bukkit.getServer().getScheduler().cancelTask(taskid));
            }
        }, 0, 60);
    }
    private int taskid = -1;
    public void mineAutoResetByTime(Player player) {
        IslandGenerator islandGenerator = new IslandGenerator();

        taskid = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (player.isOnline() && islandGenerator.getBlocksInMine(getIslandLocation(), ISLAND_SIZE, ISLAND_SIZE, ISLAND_HIGH, ISLAND_LEVEL, player)-islandGenerator.getAirBlocksInMine(getIslandLocation(), ISLAND_SIZE, ISLAND_SIZE, ISLAND_HIGH, ISLAND_LEVEL, player) < islandGenerator.getBlocksInMine(getIslandLocation(), ISLAND_SIZE, ISLAND_SIZE, ISLAND_HIGH, ISLAND_LEVEL, player)) {
                resetIsland(player);
            } else if (!player.isOnline()) {
                Bukkit.getServer().getScheduler().runTask(plugin, () -> Bukkit.getServer().getScheduler().cancelTask(taskid));

            }
        }, 0, 1500);
    }


    public void stopMineAutoResetByTime() {
        if (taskid != -1) {
            Bukkit.getServer().getScheduler().cancelTask(taskid);
            taskid = -1;
        }
    }


    private int taskidxp = -3;

    public long calcXPNeedNextLevel(){

        if(ISLAND_LEVEL<450) {
            return (long) (ILSAND_STARTXP_NEED * Math.pow(1.028, ISLAND_LEVEL));
        } else return (long) (ILSAND_STARTXP_NEED * Math.pow(1.028, 450) * Math.pow(1.0015, ISLAND_LEVEL));
    }
    public void autoUpgrade(Player player) {
        taskidxp = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            long xpneed = calcXPNeedNextLevel();


            EconomyAPI economyAPI = de.backpack.main.Main.economyAPI;
            long xp = economyAPI.getXP(player);

            if (player.isOnline()) {

                if (xp > xpneed && getISLAND_LEVEL() < ISLAND_LEVEL_MAX) {

                    TalentInitialzier talentInitialzier = de.prestigesystem.Main.talentInitialzier;

                    UserManager userManager = de.prestigesystem.Main.userManager;

                    List<Talent> talents = talentInitialzier.getTalents();

                    PlayerTalents playerTalents = userManager.getPlayerPlayerTalents().get(player);

                    int level = 0;

                    for (Talent talent : talents) {
                        if (talent.getName().equalsIgnoreCase("Rankup Multi")) {
                            level = playerTalents.getTalentLevel(talent);
                        }
                    }

                    double chance = level * 0.0015;

                    if (Math.random() < chance) {
                        setISLAND_LEVEL(getISLAND_LEVEL() + 2, player);
                        player.sendMessage(prefix + "§7Your Mine has been upgraded twice!");
                        economyAPI.setXPBalance(player, 0);
                    } else {
                        islandUpgrade(player);
                        economyAPI.setXPBalance(player, 0);
                    }
                } else {
                    double progress = (double) xp / xpneed;

                    int percentage = (int) (progress * 100);
                    int progressBarLength = 15;  // Die Länge des Fortschrittsbalkens

                    StringBuilder progressBar = new StringBuilder("§8["); // Grauer Balken

                    int greenBars = (int) (progress * progressBarLength);
                    for (int i = 0; i < progressBarLength; i++) {
                        if (i < greenBars) {
                            progressBar.append("§a|"); // Grüne Balken
                        } else {
                            progressBar.append("§7|"); // Graue Balken
                        }
                    }

                    progressBar.append("§8]"); // Abschluss des Balkens

                    String actionBarTitle = "§bMine level §f" + getISLAND_LEVEL() + " §b✦ " + progressBar + " " + String.format("§a%.2f", progress * 100) + "%";

                    if (getISLAND_LEVEL() == ISLAND_LEVEL_MAX) {
                        progress = 1;
                        actionBarTitle = "Mine level: " + getISLAND_LEVEL() + " | §e§lMAX";
                    }

                    if(economyAPI.getCashNumberBalance(player).compareTo(economyAPI.calcRankCost(player)) >= 0) {
                        actionBarTitle = "§bMine level §f" + getISLAND_LEVEL() + " §b✦ " + progressBar + " " + String.format("§a%.2f", progress * 100) + "%" + " §b✦ §6do /rankup";

                        if (getISLAND_LEVEL() == ISLAND_LEVEL_MAX) {
                            progress = 1;
                            actionBarTitle = "Mine level: " + getISLAND_LEVEL() + " | §e§lMAX"  + " §b✦ §6do /rankup";
                        }
                    }

                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(actionBarTitle));
                }
            } else {
                Bukkit.getServer().getScheduler().runTask(plugin, () -> Bukkit.getServer().getScheduler().cancelTask(taskidxp));
            }
        }, 0, 5);
    }

    public void stopAutoUpgrade() {
        if (taskidxp != -1) {
            Bukkit.getServer().getScheduler().cancelTask(taskidxp);
            taskidxp = -1;
        }
    }
    public void initActionBar() {
        actionBar = Bukkit.createBossBar("", BarColor.BLUE, BarStyle.SOLID);
    }

    public long xpneed(Player player){
        return 200+(getISLAND_LEVEL()*getISLAND_LEVEL()* 100L);
    }

    public void onPrestige(Player player){

        EconomyAPI economyAPI = de.backpack.main.Main.economyAPI;
        if(economyAPI.getBlocks(player)>=1000) {

            TalentInitialzier talentInitialzier = de.prestigesystem.Main.talentInitialzier;

            UserManager userManager = de.prestigesystem.Main.userManager;

            List<Talent> talents = talentInitialzier.getTalents();

            PlayerTalents playerTalents = userManager.getPlayerPlayerTalents().get(player);

            int level = 0;

            for(Talent talent : talents) {
                if(talent.getName().equalsIgnoreCase("Miner")){
                    level = playerTalents.getTalentLevel(talent);
                }
            }
            setISLAND_LEVEL(level, player);


            player.sendMessage(Main.prefix + "you have successfully reached prestige");
        } else {

            player.sendMessage(Main.prefix + "you can't prestige");
        }

    }

    public boolean checkIfBlockIsInMine(Location location) {

        Location islandLocation = getIslandLocation();
        int startX = islandLocation.getBlockX() - ISLAND_SIZE / 2;
        int startY = islandLocation.getBlockY() - ISLAND_HIGH;
        int startZ = islandLocation.getBlockZ() - ISLAND_SIZE / 2;

        int endX = startX + ISLAND_SIZE;
        int endY = startY + ISLAND_HIGH-1;
        int endZ = startZ + ISLAND_SIZE;

        int blockX = location.getBlockX();
        int blockY = location.getBlockY();
        int blockZ = location.getBlockZ();

        return blockX >= startX && blockX <= endX
                && blockY >= startY && blockY <= endY
                && blockZ >= startZ && blockZ <= endZ;
    }

    public NPC.Personal getNpc() {
        return npc;
    }


    public void setISLAND_WORLD_STRING(){
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        this.ISLAND_WORLD_STRING = String.valueOf(alphabet[ISLAND_WORLD_LEVEL]);
    }
    public String getISLAND_WORLD_STRING() {
        return ISLAND_WORLD_STRING;
    }

    public void addMember(Player player) {

        UUID uuid = player.getUniqueId();
        Island island = this;

        if (player.isOnline()) {
            if (islandManager.island.containsKey(uuid)) {
                if(!member.contains(uuid)) {
                    member.add(uuid);
                    islandManager.island.get(uuid).trustedIslands.add(island);
                }
            }
        }
    }

    public List getMembers(){
        return member;
    }
}
