package skyblock.api;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class IslandMaterials {

    public HashMap<Integer, Material> islandMaterials = new HashMap<>();
    public HashMap<Integer, Material> worldMaterials = new HashMap<>();

    public IslandMaterials(){

        islandMaterials.put(200, Material.RAW_GOLD_BLOCK);
        islandMaterials.put(0, Material.COBBLESTONE);
        islandMaterials.put(1, Material.MOSSY_COBBLESTONE);
        islandMaterials.put(2, Material.PACKED_MUD);
        islandMaterials.put(3, Material.MUD_BRICKS);
        islandMaterials.put(4, Material.STONE);
        islandMaterials.put(5, Material.STONE_BRICKS);
        islandMaterials.put(6, Material.MOSSY_STONE_BRICKS);
        islandMaterials.put(7, Material.CRACKED_STONE_BRICKS);
        islandMaterials.put(8, Material.GRANITE);
        islandMaterials.put(9, Material.ANDESITE);
        islandMaterials.put(10, Material.DIORITE);
        islandMaterials.put(11, Material.POLISHED_GRANITE);
        islandMaterials.put(12, Material.POLISHED_ANDESITE);
        islandMaterials.put(13, Material.POLISHED_DIORITE);
        islandMaterials.put(14, Material.COBBLED_DEEPSLATE);
        islandMaterials.put(15, Material.DEEPSLATE);
        islandMaterials.put(16, Material.DEEPSLATE_BRICKS);
        islandMaterials.put(17, Material.COAL_ORE);
        islandMaterials.put(18, Material.IRON_ORE);
        islandMaterials.put(19, Material.COPPER_ORE);
        islandMaterials.put(20, Material.LAPIS_ORE);
        islandMaterials.put(21, Material.REDSTONE_ORE);
        islandMaterials.put(22, Material.GOLD_ORE);
        islandMaterials.put(23, Material.EMERALD_ORE);
        islandMaterials.put(24, Material.DIAMOND_ORE);
        islandMaterials.put(25, Material.WHITE_TERRACOTTA);
        islandMaterials.put(26, Material.ORANGE_TERRACOTTA);
        islandMaterials.put(27, Material.MAGENTA_TERRACOTTA);
        islandMaterials.put(28, Material.LIGHT_BLUE_TERRACOTTA);
        islandMaterials.put(29, Material.YELLOW_TERRACOTTA);
        islandMaterials.put(30, Material.LIME_TERRACOTTA);
        islandMaterials.put(31, Material.PINK_TERRACOTTA);
        islandMaterials.put(32, Material.GRAY_TERRACOTTA);
        islandMaterials.put(33, Material.LIGHT_GRAY_TERRACOTTA);
        islandMaterials.put(34, Material.CYAN_TERRACOTTA);
        islandMaterials.put(35, Material.PURPLE_TERRACOTTA);
        islandMaterials.put(36, Material.BLUE_TERRACOTTA);
        islandMaterials.put(37, Material.BROWN_TERRACOTTA);
        islandMaterials.put(38, Material.GREEN_TERRACOTTA);
        islandMaterials.put(39, Material.RED_TERRACOTTA);
        islandMaterials.put(40, Material.BLACK_TERRACOTTA);
        islandMaterials.put(41, Material.WHITE_GLAZED_TERRACOTTA);
        islandMaterials.put(42, Material.ORANGE_GLAZED_TERRACOTTA);
        islandMaterials.put(43, Material.MAGENTA_GLAZED_TERRACOTTA);
        islandMaterials.put(44, Material.LIGHT_BLUE_GLAZED_TERRACOTTA);
        islandMaterials.put(45, Material.YELLOW_GLAZED_TERRACOTTA);
        islandMaterials.put(46, Material.LIME_GLAZED_TERRACOTTA);
        islandMaterials.put(47, Material.PINK_GLAZED_TERRACOTTA);
        islandMaterials.put(48, Material.GRAY_GLAZED_TERRACOTTA);
        islandMaterials.put(49, Material.LIGHT_GRAY_GLAZED_TERRACOTTA);
        islandMaterials.put(50, Material.CYAN_GLAZED_TERRACOTTA);
        islandMaterials.put(51, Material.PURPLE_GLAZED_TERRACOTTA);
        islandMaterials.put(52, Material.BLUE_GLAZED_TERRACOTTA);
        islandMaterials.put(53, Material.BROWN_GLAZED_TERRACOTTA);
        islandMaterials.put(54, Material.GREEN_GLAZED_TERRACOTTA);
        islandMaterials.put(55, Material.RED_GLAZED_TERRACOTTA);
        islandMaterials.put(56, Material.BLACK_GLAZED_TERRACOTTA);
        islandMaterials.put(57, Material.DEAD_TUBE_CORAL_BLOCK);
        islandMaterials.put(58, Material.DEAD_BRAIN_CORAL_BLOCK);
        islandMaterials.put(59, Material.DEAD_BUBBLE_CORAL_BLOCK);
        islandMaterials.put(60, Material.DEAD_FIRE_CORAL_BLOCK);
        islandMaterials.put(61, Material.TUBE_CORAL_BLOCK);
        islandMaterials.put(62, Material.BRAIN_CORAL_BLOCK);
        islandMaterials.put(63, Material.FIRE_CORAL_BLOCK);
        islandMaterials.put(64, Material.HORN_CORAL_BLOCK);
        islandMaterials.put(65, Material.PRISMARINE);
        islandMaterials.put(66, Material.PRISMARINE_BRICKS);
        islandMaterials.put(67, Material.DARK_PRISMARINE);
        islandMaterials.put(68, Material.NETHERRACK);
        islandMaterials.put(69, Material.NETHER_BRICKS);
        islandMaterials.put(70, Material.BASALT);
        islandMaterials.put(71, Material.POLISHED_BASALT);
        islandMaterials.put(72, Material.SMOOTH_BASALT);
        islandMaterials.put(73, Material.QUARTZ_BLOCK);
        islandMaterials.put(74, Material.QUARTZ_BRICKS);
        islandMaterials.put(75, Material.SMOOTH_BASALT);
        islandMaterials.put(76, Material.OBSIDIAN);
        islandMaterials.put(77, Material.END_STONE);
        islandMaterials.put(78, Material.END_STONE_BRICKS);
        islandMaterials.put(79, Material.PURPUR_BLOCK);
        islandMaterials.put(80, Material.PURPUR_PILLAR);
        islandMaterials.put(81, Material.CRACKED_STONE_BRICKS);
        islandMaterials.put(82, Material.COPPER_BLOCK);
        islandMaterials.put(83, Material.REDSTONE_BLOCK);
        islandMaterials.put(84, Material.LAPIS_BLOCK);
        islandMaterials.put(85, Material.IRON_BLOCK);
        islandMaterials.put(86, Material.GOLD_BLOCK);
        islandMaterials.put(85, Material.EMERALD_BLOCK);
        islandMaterials.put(88, Material.DIAMOND_BLOCK);
        islandMaterials.put(89, Material.NETHERITE_BLOCK);


    }


}
