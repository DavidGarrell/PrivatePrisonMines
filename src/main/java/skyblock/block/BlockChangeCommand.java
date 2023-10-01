package skyblock.block;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BlockChangeCommand implements CommandExecutor {


    Location loc1;
    Location loc2;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use this command.");
            return true;
        }
        Player player = (Player) sender;

        if (args[0].equalsIgnoreCase("loc1")) {
            this.loc1 = player.getLocation();
            player.sendMessage("§fLocation loc1 set to " + player.getLocation());
        } else if (args[0].equalsIgnoreCase("loc2")) {
            this.loc2 = player.getLocation();
            player.sendMessage("§fLocation loc2 set to " + player.getLocation());
        } else if (args[0].equalsIgnoreCase("set")) {
            BlockChanger.setSectionCuboidAsynchronously(loc1, loc2, new ItemStack(Material.STONE), false)
                    .thenRunAsync(() -> {
                        Bukkit.broadcastMessage("COMPLETED!");
                    });
        } else if (args[0].equalsIgnoreCase("clear")) {
            BlockChanger.setSectionCuboidAsynchronously(loc1, loc2, new ItemStack(Material.AIR), false)
                    .thenRunAsync(() -> {
                        Bukkit.broadcastMessage("COMPLETED!");
                    });
        } else {
            sender.sendMessage("Usage: /blocks <arg1> <arg2> <arg3>");
            return true;
        }
        return true;
    }
}
