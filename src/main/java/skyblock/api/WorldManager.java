package skyblock.api;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

public class WorldManager {

    public static World createEmptyWorld(String worldName) {
        // Check if the world already exists

        System.out.println("check if World exist");

        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            return world;
        } else {
            System.out.println("creating new World");
            // Create a new world creator with the specified name
            WorldCreator worldgen = new WorldCreator(worldName);
            worldgen.generator(new VoidWorldGenerator());
            worldgen.createWorld();


            System.out.println("new World has been created");
            return worldgen.createWorld();
        }
    }
}
