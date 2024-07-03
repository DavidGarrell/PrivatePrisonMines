package skyblock.api;

import com.fastasyncworldedit.bukkit.regions.WorldGuardFeature;
import com.sk89q.worldedit.math.BlockVector3;
import de.backpack.apfloat.Apfloat;
import de.backpack.apfloat.ApfloatMath;
import de.backpack.listener.EconomyAPI;
import de.prestigesystem.api.PlayerTalents;
import de.prestigesystem.api.Talent;
import de.prestigesystem.api.UserManager;
import de.prestigesystem.events.TalentInitialzier;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.MemoryNPCDataStore;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.trait.HologramTrait;
import net.citizensnpcs.trait.LookClose;
import net.citizensnpcs.trait.SkinTrait;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.units.qual.A;
import skyblock.main.Main;

import java.io.IOException;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

public class Island {

    private int ISLAND_SIZE = 35;
    private int ISLAND_HIGH = 80;
    private int ISLAND_LEVEL = 1;

    private int ILSAND_STARTXP_NEED = 500;
    private int ILSAND_XP_PERLEVEL = 100;


    private int ISLAND_WORLD_LEVEL = 0;

    private String ISLAND_WORLD_STRING = "a";
    public static final int ISLAND_LEVEL_MAX = 900;

    public static final int ISLAND_START_SIZE = 35;

    public static final int ISLAND_START_HIGH = 80;

    public static float SUPERBLOCK_CHANCE = 1;

    public Material base_Block = Material.COBBLESTONE;
    public Material lucky_Block = Material.RAW_GOLD_BLOCK;

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

    private IslandMaterials islandMaterials = new IslandMaterials();

    private HashMap<Material, Float> mine_Materials = new HashMap<>();

    private HashMap<Material, Integer> mine_Materials_level = new HashMap<>();

    private float LUCKY_BLOCK_CHANCE = 0.001f;

    private IslandGenerator islandGenerator;

    private Material customFrameMaterial = Material.BEDROCK;

    private NPC npc;
    NPCRegistry registery = Main.instance.registery;

    public Island(Player owner, Plugin plugin) {
        this.islandOwner = owner;
        Island.plugin = plugin;
    }

    public void createIsland(Player player) {
        World world = player.getWorld();
        UUID uuid = player.getUniqueId();
        islandOwner = player;

        mine_Materials.put(Material.COBBLESTONE, 1f-LUCKY_BLOCK_CHANCE);
        mine_Materials.put(Material.RAW_GOLD_BLOCK, LUCKY_BLOCK_CHANCE);

        islandGenerator = new IslandGenerator();

        this.islandLocation = getNextGridLocation(islandManager.lastIslandLocation);
        islandGenerator.loadIslandSchematic(player, "/island.schem", islandLocation);

        islandGenerator.clearIsland(getIslandLocation(), ISLAND_SIZE+1, ISLAND_SIZE+1, ISLAND_START_HIGH, player);
        islandGenerator.generateIsland(this, islandLocation, ISLAND_START_SIZE, ISLAND_START_SIZE, ISLAND_START_HIGH, 0, player);
        islandGenerator.generateBedrockFrame(islandLocation, ISLAND_START_SIZE + 4, ISLAND_START_SIZE + 4, ISLAND_START_HIGH, player);
        islandGenerator.clearIslandSchem(getIslandLocation(), ISLAND_SIZE+4, ISLAND_SIZE+4, ISLAND_HIGH+1, player);
        islandGenerator.generateCustomFrame(getIslandLocation(), ISLAND_SIZE + 4, ISLAND_SIZE + 4, ISLAND_HIGH, player, customFrameMaterial);

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

        islandGenerator.emeraldRush(getIslandLocation(), ISLAND_SIZE, ISLAND_SIZE, ISLAND_HIGH, ISLAND_LEVEL, player);
    }
    public void islandUpgrade(Player player) {

        if(getISLAND_LEVEL()<ISLAND_LEVEL_MAX) {
            this.ISLAND_LEVEL+=1;

            int next_block = ISLAND_LEVEL/10;
            int next_reward_level = 10-ISLAND_LEVEL%10;

            if (this.ISLAND_LEVEL % 10 == 0) {
                String color = RewardStrings.getRandomColor()+ "§l";
                player.sendMessage(color + RewardStrings.getRandomMessage() + "§fYou have reached mine level " + color + ISLAND_LEVEL);
                IslandMaterials islandMaterials = new IslandMaterials();
                player.sendMessage("§6§lUNLOCK §fnew Mine Block: §f" + islandMaterials.islandMaterials.get(next_block));

                islandGenerator.clearIsland(getIslandLocation(), ISLAND_SIZE, ISLAND_SIZE, ISLAND_HIGH, player);
                islandGenerator.clearIslandFrame(getIslandLocation(), ISLAND_SIZE + 4, ISLAND_SIZE + 4, ISLAND_HIGH, player);

                this.ISLAND_SIZE = ISLAND_START_SIZE + ISLAND_LEVEL/15 * 2;
                this.ISLAND_HIGH = ISLAND_START_HIGH + ISLAND_LEVEL/10;

                islandGenerator.clearIslandSchem(getIslandLocation(), ISLAND_SIZE+4, ISLAND_SIZE+4, ISLAND_HIGH+1, player);
                islandGenerator.clearIsland(getIslandLocation(), ISLAND_SIZE+1, ISLAND_SIZE+1, ISLAND_HIGH+1, player);
                islandGenerator.generateIsland(this, getIslandLocation(), ISLAND_SIZE, ISLAND_SIZE, ISLAND_HIGH, ISLAND_LEVEL, player);
                islandGenerator.generateBedrockFrame(getIslandLocation(), ISLAND_SIZE + 4, ISLAND_SIZE + 4, ISLAND_HIGH, player);
                islandGenerator.generateCustomFrame(getIslandLocation(), ISLAND_SIZE + 4, ISLAND_SIZE + 4, ISLAND_HIGH, player, customFrameMaterial);

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
        islandGenerator.generateIsland(this, getIslandLocation(), ISLAND_SIZE, ISLAND_SIZE, ISLAND_HIGH, ISLAND_LEVEL, player);
        islandGenerator.generateBedrockFrame(getIslandLocation(), ISLAND_SIZE + 4, ISLAND_SIZE + 4, ISLAND_HIGH, player);
        islandGenerator.generateCustomFrame(getIslandLocation(), ISLAND_SIZE + 4, ISLAND_SIZE + 4, ISLAND_HIGH, player, customFrameMaterial);

        moveNPC();
        setISLAND_WORLD_STRING();
    }

    public void setISLAND_LEVEL(int ISLAND_LEVEL, Player player) {

        int ISLAND_LEVEL_BEFORE = this.ISLAND_LEVEL;
        this.ISLAND_LEVEL = ISLAND_LEVEL;

        if (this.ISLAND_LEVEL % 10 == 0) {

            islandGenerator.clearIsland(getIslandLocation(), ISLAND_SIZE, ISLAND_SIZE, ISLAND_HIGH, player);
            islandGenerator.clearIslandFrame(getIslandLocation(), ISLAND_SIZE + 4, ISLAND_SIZE + 4, ISLAND_HIGH, player);

            this.ISLAND_SIZE = ISLAND_START_SIZE + ISLAND_LEVEL/15 * 2;
            this.ISLAND_HIGH = ISLAND_START_HIGH;

            if(ISLAND_LEVEL<ISLAND_LEVEL_BEFORE) {
                islandGenerator.loadIslandSchematic(player, "/island.schem", islandLocation);
            }
            islandGenerator.clearIslandSchem(getIslandLocation(), ISLAND_SIZE+4, ISLAND_SIZE+4, ISLAND_HIGH+1, player);
            islandGenerator.clearIsland(getIslandLocation(), ISLAND_SIZE+1, ISLAND_SIZE+1, ISLAND_HIGH+1, player);
            islandGenerator.generateIsland(this, getIslandLocation(), ISLAND_SIZE, ISLAND_SIZE, ISLAND_HIGH, ISLAND_LEVEL, player);
            islandGenerator.generateBedrockFrame(getIslandLocation(), ISLAND_SIZE + 4, ISLAND_SIZE + 4, ISLAND_HIGH, player);
            islandGenerator.generateCustomFrame(getIslandLocation(), ISLAND_SIZE + 4, ISLAND_SIZE + 4, ISLAND_HIGH, player, customFrameMaterial);

            moveNPC();

        }
    }

    public Location getIslandPlayerSpawnPoint() {

        Location playerSpawnLoc = islandLocation.clone();

        playerSpawnLoc.add(0.5, 0, -ISLAND_SIZE/2-3.5);

        return playerSpawnLoc;
    }

    public void placeNPC(Player player){
        npc = registery.createNPC(EntityType.PLAYER, "Cobblemate");
        npc.setName("§e§lCobblemate");
        npc.getOrAddTrait(LookClose.class).lookClose(true);
        npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.HAND, new ItemStack(Material.DIAMOND_PICKAXE, 1));
        npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.OFF_HAND, new ItemStack(Material.CHEST, 1));
        npc.getOrAddTrait(SkinTrait.class).setTexture(
                "ewogICJ0aW1lc3RhbXAiIDogMTcxMTEyNTM1OTI1NCwKICAicHJvZmlsZUlkIiA6ICIxODA1Y2E2MmM0ZDI0M2NiOWQxYmY4YmM5N2E1YjgyNCIsCiAgInByb2ZpbGVOYW1lIiA6ICJSdWxsZWQiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTI5YmQ4NjQ0OWFlZWQ1YWMwYjE1Mzk0MDg5NmExODI4Yzc2NTIxODg0M2I4NjM3NGNkYmU5OTAwODkxNzE4ZiIKICAgIH0KICB9Cn0=",
                "ZqScyeMkN1vL53po/RVTqep9gFNeFMsImospoQvTJSfpLfPHi6KjwIrmle1cJelagdTZDx0ugprvyRyWxBtiSOAq7xBMxraGRH0TMhQxQk/LWd4i+msp+adnAs9S9toCuKV5Tj7C/+Q3eVpWc/pK6YiOSyM4p/4TYdnj/HSykn4cI/xYf4U5HsIxQ3Kn4lVxHuzo+g2BYRAqAUSY9rv5tvtrE7ioqDAibSp6L4yjFJkS60NRc67+dJv5DxtI3HtODUqI4Nx0NFfk1ANko1RAHVskyfpQlFjfINH0AtVD2BFyGLW2z03DHZKQjFkZ3C6g2NtPMcuGoejxXq98qLGKP1cyhoZza8bJaLm8p/TTmGCH0MOcUdbmsQQgYAClMf+1MrFSF5dmVMXeyyw/HJ7PMBlPUROULcJTu8SMP63w5c0umE7jxULx05v3sBOg6OjX29RCI57G3siSrqxJCl6mrDpRvSI9aj1ItRBPAs1lJMD6rnM9i1J9DxvRNi/zgx+TGPdP/vC5iKHkPyDr2a6GoC8+hcmFt69nAErdzcB5svUDHUmN5oogmNu0sa5uZOM9CVnOt8xaQwIOfCAhglolDv+rqog51fZm2/lHIu3OHRUZjpcfcJgvuAqvf8XxfPLCppomGO7BS7gYuD6mD35qvTXmgpp/2qMnJRjIV1aO9B8=");
        npc.spawn(getIslandNPCSpawnPoint());
    }

    public NPC getNpc() {
        return npc;
    }

    public void moveNPC(){
        npc.teleport(getIslandNPCSpawnPoint(), PlayerTeleportEvent.TeleportCause.COMMAND);

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
        islandGenerator.resetIsland(this, getIslandLocation(), ISLAND_SIZE, ISLAND_SIZE, ISLAND_HIGH, ISLAND_LEVEL, player);
    }
    public Player getIslandOwner() {
        return islandOwner;
    }

    public void setIslandOwner(Player islandOwner) {
        this.islandOwner = islandOwner;
    }
    private int taskid_p = -2;
    public void mineAutoResetByPercent(Player player) {
        taskid_p = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {

            if(islandGenerator.getBlocksInMine(getIslandLocation(), ISLAND_SIZE, ISLAND_SIZE, ISLAND_HIGH, ISLAND_LEVEL, player)/4 <=islandGenerator.getAirBlocksInMine(getIslandLocation(), ISLAND_SIZE, ISLAND_SIZE, ISLAND_HIGH, ISLAND_LEVEL, player)) {
                resetIsland(player);
            } else if (!player.isOnline()) {
                Bukkit.getServer().getScheduler().runTask(plugin, () -> Bukkit.getServer().getScheduler().cancelTask(taskid));
            }
        }, 0, 60);
    }
    private int taskid = -1;
    public void mineAutoResetByTime(Player player) {

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
            return (long) (ILSAND_STARTXP_NEED * Math.pow(1.024, ISLAND_LEVEL));
        } else return (long) (ILSAND_STARTXP_NEED * Math.pow(1.024, 450) * Math.pow(1.0010, ISLAND_LEVEL));
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

    public static float getSuperblockChance() {
        return SUPERBLOCK_CHANCE;
    }

    public HashMap<Material, Float> getMine_Materials() {
        return mine_Materials;
    }

    public void addMineMaterialPercent(Material material, int level) {

        float base_percent = 0.015f;
        float percent = base_percent * level;

        int mlevel = mine_Materials_level.getOrDefault(material, 0);

        if (mine_Materials.getOrDefault(material, 0f) + percent < 1f-LUCKY_BLOCK_CHANCE) {
            if (mine_Materials.containsKey(material)) {
                float value = mine_Materials.get(material);
                mine_Materials.replace(material, value, value + percent);
                mine_Materials_level.replace(material, Math.min(mlevel + level, 67));
            } else {
                mine_Materials.put(material, percent);
                mine_Materials_level.put(material, Math.min(mlevel + level, 67));
            }

        } else {
            if (mine_Materials.containsKey(material)) {
                mine_Materials.replace(material, 1f-LUCKY_BLOCK_CHANCE);
                mine_Materials_level.replace(material, Math.min(mlevel + level, 67));
            } else {
                mine_Materials.put(material, 1f-LUCKY_BLOCK_CHANCE);
                mine_Materials_level.put(material, Math.min(mlevel + level, 67));
            }
        }
    }
    public void removeMineMaterialPercent(Material material, int level) {

        float base_percent = 0.015f;
        float percent = base_percent*level;
        int mlevel = mine_Materials_level.getOrDefault(material, 0);

        if (mine_Materials.containsKey(material)) {
            float total_percent = 0;

            for (Material m : mine_Materials.keySet()) {
                total_percent += mine_Materials.get(m);
            }

            if(getTotalMineMaterialPercent()-percent>1f-LUCKY_BLOCK_CHANCE) {

                float newValue = mine_Materials.get(material) - percent;

                DecimalFormat df = new DecimalFormat("#.####");
                df.setRoundingMode(RoundingMode.HALF_UP);
                newValue = Float.parseFloat(df.format(newValue).replace(',', '.'));

                if (newValue <= 0) {
                    mine_Materials.remove(material);
                    mine_Materials_level.remove(material);
                } else {
                    mine_Materials.put(material, newValue);
                    mine_Materials_level.replace(material, mlevel - level);
                }
            }
        }
    }
    public void clearMineMaterialPercent(){
        mine_Materials.clear();
        mine_Materials.put(base_Block, 1f-LUCKY_BLOCK_CHANCE);
        mine_Materials.put(lucky_Block, LUCKY_BLOCK_CHANCE);
    }

    public float getMineMaterialPercent(Material material) {
        return mine_Materials.getOrDefault(material, 0f);
    }

    public float getTotalMineMaterialPercent(){

        float percent = 0;

        for(Material m : mine_Materials.keySet()){
            percent+=mine_Materials.get(m);
        }

        return percent;
    }

    public Apfloat calcMineBlockCost(Material material, int amount) {

        int i = 0;

        for (Map.Entry<Integer, Material> m : islandMaterials.islandMaterials.entrySet()) {
            if (m.getValue().equals(material)) {
                i = m.getKey();
            }
        }

        Apfloat baseValue = new Apfloat(50000).multiply(new Apfloat(i));
        Apfloat increasedValue = baseValue.multiply(ApfloatMath.pow(new Apfloat(2.7), i));

        int k = 0;

        if (mine_Materials_level.get(material) != null) {
            k = mine_Materials_level.get(material); // Anpassung der Berechnung für alle 1.5f

            for (int x = 0; x < k; x++) {
                increasedValue = increasedValue.multiply(new Apfloat(1.25)); // Korrekte Multiplikation für die Wertsteigerung um 30 %
            }
        }
        // Berechne die Gesamtkosten für die angegebene Anzahl von Leveln
        Apfloat totalCost = new Apfloat(0);

        if (k + amount > 67) {
            int temp = k+amount-67;
            amount -=temp; // Der Betrag wird auf das reduziert, was bis zum Limit von 66 fehlt
        }

        for (int j = 0; j < amount; j++) {
            totalCost = totalCost.add(increasedValue); // Summiere die Kosten für jeden Level
            increasedValue = increasedValue.multiply(new Apfloat(1.05)); // Multipliziere mit 1.10 für die nächste Stufe
        }

        return totalCost;
    }

    public Apfloat calcBaseBlockValue(Material material) {
        int i = 0;

        if(!material.equals(Material.RAW_GOLD_BLOCK)) {
            for (Map.Entry<Integer, Material> m : islandMaterials.islandMaterials.entrySet()) {
                if (m.getValue().equals(material)) {
                    i = m.getKey();
                }
            }

            Apfloat baseValue = new Apfloat(100);
            Apfloat increasedValue = baseValue.multiply(ApfloatMath.pow(new Apfloat(2.7), i));

            return increasedValue;
        } else return new Apfloat(0);

    }

    public boolean isMaterialBlockChanceMax(Material material){
        return mine_Materials_level.getOrDefault(material, 0) >= 67;
    }

    public Apfloat calcBaseValueAverage() {
        Apfloat totalBaseValue = new Apfloat(0);
        float totalMaterialPercent = getTotalMineMaterialPercent();
        for (Material m : mine_Materials.keySet()) {
            double materialPercent = mine_Materials.get(m);
            double temp = (materialPercent/totalMaterialPercent);
            Apfloat k = calcBaseBlockValue(m).multiply(new Apfloat(temp));

            totalBaseValue = totalBaseValue.add(k);
        }
        return totalBaseValue;
    }

    public boolean canUpgrade(Material material){
        int temp = (ISLAND_LEVEL+10)/10;
        int k = 0;
        for(Map.Entry<Integer, Material> m : islandMaterials.islandMaterials.entrySet()){
            if(m.getValue().equals(material)){
                k=m.getKey();
            }
        }
        return k-1 < temp;
    }

    public float calcDisplayBlockChance(Material material){
        float temp = 0;
        float totalMaterialPercent = getTotalMineMaterialPercent();
        for (Material m : mine_Materials.keySet()) {
            if(m.equals(material)) {
                float materialPercent = mine_Materials.get(m);
                temp = (materialPercent / totalMaterialPercent);
            }
        }
        return temp;
    }

    public Location getCenterBlockLocation(){

        return getIslandLocation();
    }

    public Material getHighestMaterial() {
        int highestNumber = Integer.MIN_VALUE;
        Material highestMaterial = Material.COBBLESTONE;

        for (Map.Entry<Integer, Material> entry : islandMaterials.islandMaterials.entrySet()) {
            Integer number = entry.getKey();
            Material material = entry.getValue();
            if (material != Material.RAW_GOLD_BLOCK) { // Sicherstellen, dass es sich nicht um RAW_GOLD_BLOCK handelt
                if (number > highestNumber) {
                    highestNumber = number;
                    highestMaterial = material;
                }
            }
        }

        if (highestMaterial != null && mine_Materials.containsKey(highestMaterial)) {
            return highestMaterial;
        } else {
            // Handle case where highest material is not present in mine_Materials
            return null; // or throw an exception, return a default material, etc.
        }
    }

    public void setCustomFrameMaterial(Material customFrameMaterial) {
        this.customFrameMaterial = customFrameMaterial;
        islandGenerator.generateCustomFrame(getIslandLocation(), ISLAND_SIZE + 4, ISLAND_SIZE + 4, ISLAND_HIGH, islandOwner, customFrameMaterial);

    }

    public Material getCustomFrameMaterial() {
        return customFrameMaterial;
    }

}
