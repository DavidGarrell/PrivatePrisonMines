package skyblock.api;

import com.google.common.collect.ImmutableMap;
import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extension.factory.PatternFactory;
import com.sk89q.worldedit.extension.input.ParserContext;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.extent.inventory.BlockBag;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.function.pattern.RandomPattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.io.Closer;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import skyblock.block.BlockChanger;
import skyblock.main.Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

public class IslandGenerator {


    private String resetMessage = "mine has been reset";
    IslandMaterials islandMaterials = Main.islandMaterials;

    public IslandGenerator() {

    }

    public static WorldEditPlugin getWorldEdit() {
        return (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
    }

    public void generateIsland(Island island, Location location, int ISLAND_SIZE_X, int ISLAND_SIZE_Z, int ISLAND_SIZE_Y, int level, Player player) {

        World world = location.getWorld();

        int startX = location.getBlockX() - ISLAND_SIZE_X / 2;
        int startY = location.getBlockY() - ISLAND_SIZE_Y - 1;
        int startZ = location.getBlockZ() - ISLAND_SIZE_Z / 2;

        EditSession session = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(player.getWorld()), -1);
        BlockVector3 vector1 = BlockVector3.at(startX, startY + 3, startZ);
        BlockVector3 vector2 = BlockVector3.at(startX + ISLAND_SIZE_X - 1, startY + ISLAND_SIZE_Y - 1, startZ + ISLAND_SIZE_Z - 1);
        CuboidRegion region = new CuboidRegion(new BukkitWorld(player.getWorld()), vector1, vector2);
        session.setFastMode(true);

        RandomPattern pat = new RandomPattern();

        for(Map.Entry<Material, Float> material : island.getMine_Materials().entrySet()) {
            BlockType cobble = BlockTypes.parse(String.valueOf(material.getKey()));
            BlockState blockState = cobble.getDefaultState();
            BaseBlock block = blockState.toBaseBlock();
            Pattern p = block;
            session.disableBuffering();
            session.setBlocks((Region) region, p);
            pat.add(p, material.getValue());
        }

        /*

        level/=10;

        BlockType cobble = BlockTypes.parse(String.valueOf(islandMaterials.islandMaterials.get(level)));
        BlockState blockState = cobble.getDefaultState();
        BaseBlock block = blockState.toBaseBlock();
        Pattern p = block;
        session.disableBuffering();
        session.setBlocks((Region) region, p);

        RandomPattern pat = new RandomPattern();

         */


        /**
         *     SUPER BLOCK
         */


        /*
        BlockType super_block = BlockTypes.parse(String.valueOf(Material.RAW_GOLD_BLOCK));
        BlockState sblockState = super_block.getDefaultState();
        BaseBlock sblock = sblockState.toBaseBlock();
        Pattern s = sblock;
        pat.add(s, 0.001);

         */

        /**
         *
         */

        /*
        if (level == 0) {
            pat.add(p, 1);
        }
        if (level == 1) {

            BlockType blockType2 = BlockTypes.parse(String.valueOf(islandMaterials.islandMaterials.get(level - 1)));
            BlockState blockState2 = blockType2.getDefaultState();
            BaseBlock block2 = blockState2.toBaseBlock();
            Pattern p2 = block2;

            pat.add(p, 0.5);
            pat.add(p2, 0.5);
        }
        if (level == 2) {

            BlockType blockType2 = BlockTypes.parse(String.valueOf(islandMaterials.islandMaterials.get(level - 1)));
            BlockState blockState2 = blockType2.getDefaultState();
            BaseBlock block2 = blockState2.toBaseBlock();
            Pattern p2 = block2;

            BlockType blockType3 = BlockTypes.parse(String.valueOf(islandMaterials.islandMaterials.get(level - 2)));
            BlockState blockState3 = blockType3.getDefaultState();
            BaseBlock block3 = blockState3.toBaseBlock();
            Pattern p3 = block3;

            pat.add(p, 0.34);
            pat.add(p2, 0.33);
            pat.add(p3, 0.33);
        }
        if (level > 2) {
            BlockType blockType2 = BlockTypes.parse(String.valueOf(islandMaterials.islandMaterials.get(level - 1)));
            BlockState blockState2 = blockType2.getDefaultState();
            BaseBlock block2 = blockState2.toBaseBlock();
            Pattern p2 = block2;

            BlockType blockType3 = BlockTypes.parse(String.valueOf(islandMaterials.islandMaterials.get(level - 2)));
            BlockState blockState3 = blockType3.getDefaultState();
            BaseBlock block3 = blockState3.toBaseBlock();
            Pattern p3 = block3;

            BlockType blockType4 = BlockTypes.parse(String.valueOf(islandMaterials.islandMaterials.get(level - 3)));
            BlockState blockState4 = blockType4.getDefaultState();
            BaseBlock block4 = blockState4.toBaseBlock();
            Pattern p4 = block4;

            pat.add(p, 0.25);
            pat.add(p2, 0.25);
            pat.add(p3, 0.25);
            pat.add(p4, 0.25);
        }

         */
        session.setReorderMode(EditSession.ReorderMode.FAST);
        session.setBlocks((Region) region, pat);
        session.flushSession();
        if (player.getLocation().getBlockY() < 149) {
            player.teleport(location);
        }

    }

    public void clearIsland(Location location, int ISLAND_SIZE_X, int ISLAND_SIZE_Z, int ISLAND_SIZE_Y, Player player) {
        World world = location.getWorld();

        Material material = Material.COBBLESTONE;


        int startX = location.getBlockX() - ISLAND_SIZE_X / 2;
        int startY = location.getBlockY() - ISLAND_SIZE_Y - 1; // Verschiebung um die Höhe des Cubes nach unten
        int startZ = location.getBlockZ() - ISLAND_SIZE_Z / 2;


        EditSession session = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(player.getWorld()), -1);
        BlockVector3 vector1 = BlockVector3.at(startX, startY, startZ);
        BlockVector3 vector2 = BlockVector3.at(startX + ISLAND_SIZE_X, startY + ISLAND_SIZE_Y - 1, startZ + ISLAND_SIZE_Z);
        CuboidRegion region = new CuboidRegion(new BukkitWorld(player.getWorld()), vector1, vector2);
        BlockType cobble = BlockTypes.AIR;
        session.setFastMode(true);
        BlockState blockState = cobble.getDefaultState();
        BaseBlock block = blockState.toBaseBlock();
        Pattern p = block;
        session.setReorderMode(EditSession.ReorderMode.FAST);
        session.disableBuffering();
        session.setBlocks((Region) region, p);
        session.flushSession();

    }

    public void clearIslandSchem(Location location, int ISLAND_SIZE_X, int ISLAND_SIZE_Z, int ISLAND_SIZE_Y, Player player) {
        World world = location.getWorld();

        int startX = location.getBlockX() - ISLAND_SIZE_X / 2;
        int startY = location.getBlockY() - ISLAND_SIZE_Y - 1;
        int startZ = location.getBlockZ() - ISLAND_SIZE_Z / 2;

        EditSession session = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(player.getWorld()), -1);
        BlockVector3 vector1 = BlockVector3.at(startX, startY + ISLAND_SIZE_Y, startZ);
        BlockVector3 vector2 = BlockVector3.at(startX + ISLAND_SIZE_X - 1, startY + ISLAND_SIZE_Y + 3, startZ + ISLAND_SIZE_Z - 1);
        CuboidRegion region = new CuboidRegion(new BukkitWorld(player.getWorld()), vector1, vector2);
        BlockType cobble = BlockTypes.AIR;
        session.setFastMode(true);
        BlockState blockState = cobble.getDefaultState();
        BaseBlock block = blockState.toBaseBlock();
        Pattern p = block;
        session.setReorderMode(EditSession.ReorderMode.FAST);
        session.disableBuffering();
        session.setBlocks((Region) region, p);
        session.flushSession();

    }

    public void clearIslandFrame(Location location, int ISLAND_SIZE_X, int ISLAND_SIZE_Z, int ISLAND_SIZE_Y, Player player) {
        World world = location.getWorld();
        int startX = location.getBlockX() - ISLAND_SIZE_X / 2;
        int startY = location.getBlockY() - ISLAND_SIZE_Y; // Verschiebung um die Höhe des Cubes und der Bedrock-Schicht nach unten
        int startZ = location.getBlockZ() - ISLAND_SIZE_Z / 2;

        EditSession session = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(player.getWorld()), -1);
        BlockVector3 vector1 = BlockVector3.at(startX, startY, startZ);
        BlockVector3 vector2 = BlockVector3.at(startX + ISLAND_SIZE_X - 1, startY + ISLAND_SIZE_Y - 1, startZ + ISLAND_SIZE_Z - 1);
        CuboidRegion region = new CuboidRegion(new BukkitWorld(player.getWorld()), vector1, vector2);
        BlockType cobble = BlockTypes.AIR;
        session.setFastMode(true);
        BlockState blockState = cobble.getDefaultState();
        BaseBlock block = blockState.toBaseBlock();
        Pattern p = block;
        session.disableBuffering();
        session.makeCuboidWalls((Region) region, p);
        session.flushSession();

        //bottom

        EditSession session1 = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(player.getWorld()), -1);
        BlockVector3 vector3 = BlockVector3.at(startX, startY, startZ);
        BlockVector3 vector4 = BlockVector3.at(startX + ISLAND_SIZE_X - 1, startY - 1, startZ + ISLAND_SIZE_Z - 1);
        CuboidRegion region1 = new CuboidRegion(new BukkitWorld(player.getWorld()), vector3, vector4);
        BlockType cobble1 = BlockTypes.AIR;
        session1.setFastMode(true);
        BlockState blockState1 = cobble1.getDefaultState();
        BaseBlock block1 = blockState1.toBaseBlock();
        Pattern p1 = block1;
        session.setReorderMode(EditSession.ReorderMode.FAST);
        session1.disableBuffering();
        session1.setBlocks((Region) region1, p1);
        session1.flushSession();
    }

    public void generateBedrockFrame(Location location, int ISLAND_SIZE_X, int ISLAND_SIZE_Z, int ISLAND_SIZE_Y, Player player) {
        World world = location.getWorld();
        int startX = location.getBlockX() - ISLAND_SIZE_X / 2;
        int startY = location.getBlockY() - ISLAND_SIZE_Y - 1; // Verschiebung um die Höhe des Cubes und der Bedrock-Schicht nach unten
        int startZ = location.getBlockZ() - ISLAND_SIZE_Z / 2;

        //frame

        EditSession session = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(player.getWorld()), -1);
        BlockVector3 vector1 = BlockVector3.at(startX, startY, startZ);
        BlockVector3 vector2 = BlockVector3.at(startX + ISLAND_SIZE_X - 1, startY + ISLAND_SIZE_Y - 1, startZ + ISLAND_SIZE_Z - 1);
        CuboidRegion region = new CuboidRegion(new BukkitWorld(player.getWorld()), vector1, vector2);
        BlockType cobble = BlockTypes.BEDROCK;
        session.setFastMode(true);
        BlockState blockState = cobble.getDefaultState();
        BaseBlock block = blockState.toBaseBlock();
        Pattern p = block;
        session.setReorderMode(EditSession.ReorderMode.FAST);
        session.disableBuffering();
        session.makeCuboidWalls((Region) region, p);
        session.flushSession();

        //bottom

        EditSession session1 = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(player.getWorld()), -1);
        BlockVector3 vector3 = BlockVector3.at(startX, startY, startZ);
        BlockVector3 vector4 = BlockVector3.at(startX + ISLAND_SIZE_X - 1, startY, startZ + ISLAND_SIZE_Z - 1);
        CuboidRegion region1 = new CuboidRegion(new BukkitWorld(player.getWorld()), vector3, vector4);
        BlockType cobble1 = BlockTypes.BEDROCK;
        session1.setFastMode(true);
        BlockState blockState1 = cobble1.getDefaultState();
        BaseBlock block1 = blockState1.toBaseBlock();
        Pattern p1 = block;
        session.setReorderMode(EditSession.ReorderMode.FAST);
        session1.disableBuffering();
        session1.setBlocks((Region) region1, p1);
        session1.flushSession();

    }

    public void generateCustomFrame(Location location, int ISLAND_SIZE_X, int ISLAND_SIZE_Z, int ISLAND_SIZE_Y, Player player, Material material) {
        World world = location.getWorld();
        int startX = location.getBlockX() - ISLAND_SIZE_X / 2;
        int startY = location.getBlockY() - 1; // Verschiebung um die Höhe des Cubes und der Bedrock-Schicht nach unten
        int startZ = location.getBlockZ() - ISLAND_SIZE_Z / 2;

        //frame
        EditSession session = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(player.getWorld()), -1);
        BlockVector3 vector1 = BlockVector3.at(startX, startY-1, startZ);
        BlockVector3 vector2 = BlockVector3.at(startX + ISLAND_SIZE_X - 1, startY - 1, startZ + ISLAND_SIZE_Z - 1);
        CuboidRegion region = new CuboidRegion(new BukkitWorld(player.getWorld()), vector1, vector2);
        BlockType cobble = BlockTypes.parse(material.name());
        session.setFastMode(true);
        BlockState blockState = cobble.getDefaultState();
        BaseBlock block = blockState.toBaseBlock();
        Pattern p = block;
        session.setReorderMode(EditSession.ReorderMode.FAST);
        session.disableBuffering();
        session.makeCuboidWalls((Region) region, p);
        session.flushSession();

        //bottom


    }

    public void resetIsland(Island island, Location location, int ISLAND_SIZE_X, int ISLAND_SIZE_Z, int ISLAND_SIZE_Y, int level, Player player) {
        int startX = location.getBlockX() - ISLAND_SIZE_X / 2;
        int startY = location.getBlockY() - ISLAND_SIZE_Y - 1;
        int startZ = location.getBlockZ() - ISLAND_SIZE_Z / 2;

        EditSession session = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(player.getWorld()), -1);
        BlockVector3 vector1 = BlockVector3.at(startX, startY + 3, startZ);
        BlockVector3 vector2 = BlockVector3.at(startX + ISLAND_SIZE_X - 1, startY + ISLAND_SIZE_Y - 1, startZ + ISLAND_SIZE_Z - 1);
        CuboidRegion region = new CuboidRegion(new BukkitWorld(player.getWorld()), vector1, vector2);
        session.setFastMode(true);

        RandomPattern pat = new RandomPattern();

        try {

            for (Map.Entry<Material, Float> material : island.getMine_Materials().entrySet()) {
                BlockType cobble = BlockTypes.parse(String.valueOf(material.getKey()));
                BlockState blockState = cobble.getDefaultState();
                BaseBlock block = blockState.toBaseBlock();
                Pattern p = block;
                session.disableBuffering();
                session.setBlocks((Region) region, p);

                float round_chance = Math.round(material.getValue() * 1000.0f) / 1000.0f;
                pat.add(p, round_chance);
            }
        } catch (Exception e){
            System.out.println("Error by Mine reset");
            BlockType cobble = BlockTypes.parse(String.valueOf(Material.BEDROCK));
            BlockState blockState = cobble.getDefaultState();
            BaseBlock block = blockState.toBaseBlock();
            Pattern p = block;
            session.disableBuffering();
            session.setBlocks((Region) region, p);
            pat.add(p, 1);
        }

     /*   level/=10;
        BlockType cobble = BlockTypes.parse(String.valueOf(islandMaterials.islandMaterials.get(level)));
        BlockState blockState = cobble.getDefaultState();
        BaseBlock block = blockState.toBaseBlock();
        Pattern p = block;
        session.setReorderMode(EditSession.ReorderMode.FAST);
        session.disableBuffering();
        session.setBlocks((Region) region, p);

        RandomPattern pat = new RandomPattern();

        *//**
         *     SUPER BLOCK
         *//*


        BlockType super_block = BlockTypes.parse(String.valueOf(Material.RAW_GOLD_BLOCK));
        BlockState sblockState = super_block.getDefaultState();
        BaseBlock sblock = sblockState.toBaseBlock();
        Pattern s = sblock;
        pat.add(s, 0.001);

        *//**
         *
         *//*

        if (level == 0) {
            pat.add(p, 1);
        }
        if (level == 1) {

            BlockType blockType2 = BlockTypes.parse(String.valueOf(islandMaterials.islandMaterials.get(level - 1)));
            BlockState blockState2 = blockType2.getDefaultState();
            BaseBlock block2 = blockState2.toBaseBlock();
            Pattern p2 = block2;

            pat.add(p, 0.5);
            pat.add(p2, 0.5);
        }
        if (level == 2) {

            BlockType blockType2 = BlockTypes.parse(String.valueOf(islandMaterials.islandMaterials.get(level - 1)));
            BlockState blockState2 = blockType2.getDefaultState();
            BaseBlock block2 = blockState2.toBaseBlock();
            Pattern p2 = block2;

            BlockType blockType3 = BlockTypes.parse(String.valueOf(islandMaterials.islandMaterials.get(level - 2)));
            BlockState blockState3 = blockType3.getDefaultState();
            BaseBlock block3 = blockState3.toBaseBlock();
            Pattern p3 = block3;

            pat.add(p, 0.34);
            pat.add(p2, 0.33);
            pat.add(p3, 0.33);
        }
        if (level > 2) {
            BlockType blockType2 = BlockTypes.parse(String.valueOf(islandMaterials.islandMaterials.get(level - 1)));
            BlockState blockState2 = blockType2.getDefaultState();
            BaseBlock block2 = blockState2.toBaseBlock();
            Pattern p2 = block2;

            BlockType blockType3 = BlockTypes.parse(String.valueOf(islandMaterials.islandMaterials.get(level - 2)));
            BlockState blockState3 = blockType3.getDefaultState();
            BaseBlock block3 = blockState3.toBaseBlock();
            Pattern p3 = block3;

            BlockType blockType4 = BlockTypes.parse(String.valueOf(islandMaterials.islandMaterials.get(level - 3)));
            BlockState blockState4 = blockType4.getDefaultState();
            BaseBlock block4 = blockState4.toBaseBlock();
            Pattern p4 = block4;

            pat.add(p, 0.25);
            pat.add(p2, 0.25);
            pat.add(p3, 0.25);
            pat.add(p4, 0.25);
        }*/
        session.setReorderMode(EditSession.ReorderMode.FAST);
        session.setBlocks((Region) region, pat);

        if (player.getLocation().getBlockY() < 149) {
            player.teleport(location);
        }
        session.flushSession();
    }

    public int getBlocksInMine(Location location, int ISLAND_SIZE_X, int ISLAND_SIZE_Z, int ISLAND_SIZE_Y, int level, Player player) {
        World world = location.getWorld();

        int startX = location.getBlockX() - ISLAND_SIZE_X / 2;
        int startY = location.getBlockY() - ISLAND_SIZE_Y - 1;
        int startZ = location.getBlockZ() - ISLAND_SIZE_Z / 2;

        EditSession session = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(player.getWorld()), -1);
        BlockVector3 vector1 = BlockVector3.at(startX, startY + 3, startZ);
        BlockVector3 vector2 = BlockVector3.at(startX + ISLAND_SIZE_X - 1, startY + ISLAND_SIZE_Y - 1, startZ + ISLAND_SIZE_Z - 1);
        CuboidRegion region = new CuboidRegion(new BukkitWorld(player.getWorld()), vector1, vector2);

        return region.size();
    }

    public int getAirBlocksInMine(Location location, int ISLAND_SIZE_X, int ISLAND_SIZE_Z, int ISLAND_SIZE_Y, int level, Player player) {
        World world = location.getWorld();

        int startX = location.getBlockX() - ISLAND_SIZE_X / 2;
        int startY = location.getBlockY() - ISLAND_SIZE_Y - 1;
        int startZ = location.getBlockZ() - ISLAND_SIZE_Z / 2;

        int airBlocks = 0;
        for (int x = startX; x < startX + ISLAND_SIZE_X; x++) {
            for (int y = startY; y < startY + ISLAND_SIZE_Y; y++) {
                for (int z = startZ; z < startZ + ISLAND_SIZE_Z; z++) {
                    if (world.getBlockAt(x, y, z).getType() == Material.AIR) {
                        airBlocks++;
                    }
                }
            }
        }

        return airBlocks;
    }

    public Location nextGridLocation(Location lastIsland) {
        int x = lastIsland.getBlockX();
        int z = lastIsland.getBlockZ();
        int d = 200;
        if (x < z) {
            if (-1 * x < z) {
                lastIsland.setX(lastIsland.getX() + d);
                return lastIsland;
            }
            lastIsland.setZ(lastIsland.getZ() + d);
            return lastIsland;
        }
        if (x > z) {
            if (-1 * x >= z) {
                lastIsland.setX(lastIsland.getX() - d);
                return lastIsland;
            }
            lastIsland.setZ(lastIsland.getZ() - d);
            return lastIsland;
        }
        if (x <= 0) {
            lastIsland.setZ(lastIsland.getZ() + d);
            return lastIsland;
        }
        lastIsland.setZ(lastIsland.getZ() - d);
        return lastIsland;
    }

    public void loadIslandSchematic(Player player, String name, Location location) {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        File schematicFile = new File(Main.instance.getDataFolder(), "island.schem");
        if (!schematicFile.exists()) {
            player.sendMessage("Die Schematic-Datei existiert nicht.");
            return;
        }

        ClipboardFormat format = ClipboardFormats.findByFile(schematicFile);
        if (format == null) {
            player.sendMessage("Ungültiges Clipboard-Format.");
            return;
        }

        ClipboardReader reader = null;
        try {
            reader = format.getReader(new FileInputStream(schematicFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Clipboard clipboard = null;
        try {
            clipboard = reader.read();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        try {
            com.sk89q.worldedit.world.World adaptedWorld = BukkitAdapter.adapt(location.getWorld());

            EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld,
                    -1);

            Operation operation = new ClipboardHolder(clipboard).createPaste(editSession)
                    .to(BlockVector3.at(x, y, z)).ignoreAirBlocks(true).build();

            try {
                Operations.complete(operation);
                editSession.flushSession();

            } catch (WorldEditException e) {
                player.sendMessage(ChatColor.RED + "OOPS! Something went wrong, please contact an administrator");
                e.printStackTrace();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void emeraldRush(Location location, int ISLAND_SIZE_X, int ISLAND_SIZE_Z, int ISLAND_SIZE_Y, int level, Player player) {
        int startX = location.getBlockX() - ISLAND_SIZE_X / 2;
        int startY = location.getBlockY() - ISLAND_SIZE_Y - 1;
        int startZ = location.getBlockZ() - ISLAND_SIZE_Z / 2;

        IslandManager islandManager = Main.islandManager;
        Island island = islandManager.island.get(player.getUniqueId());

        EditSession session = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(player.getWorld()), -1);
        BlockVector3 vector1 = BlockVector3.at(startX, startY + 3, startZ);
        BlockVector3 vector2 = BlockVector3.at(startX + ISLAND_SIZE_X - 1, startY + ISLAND_SIZE_Y - 1, startZ + ISLAND_SIZE_Z - 1);
        CuboidRegion region = new CuboidRegion(new BukkitWorld(player.getWorld()), vector1, vector2);
        session.setFastMode(true);
        session.setReorderMode(EditSession.ReorderMode.FAST);
        BlockType cobble = BlockTypes.parse(String.valueOf(island.getHighestMaterial()));
        BlockState blockState = cobble.getDefaultState();
        BaseBlock block = blockState.toBaseBlock();
        Pattern p = block;
        session.setReorderMode(EditSession.ReorderMode.FAST);
        session.disableBuffering();
        session.setBlocks((Region) region, p);

        if (player.getLocation().getBlockY() < 149) {
            player.teleport(location);
        }
        session.flushSession();
    }
}

