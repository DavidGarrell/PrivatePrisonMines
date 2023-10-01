package skyblock.listeners;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import skyblock.main.Main;

import java.math.BigInteger;

public class Listeners implements Listener {

    @EventHandler

    public void onBreak(BlockBreakEvent e){

        e.setDropItems(false);
        e.setExpToDrop(0);
        if(Main.islandManager.island.get(e.getPlayer().getUniqueId())!=null) {
            if (Main.islandManager.island.get(e.getPlayer().getUniqueId()).checkIfBlockIsInMine(e.getBlock().getLocation())) {


            } else {
                if (!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
                    e.setCancelled(true);
                }
            }
        } else {
            e.setCancelled(true);
        }

    }

    @EventHandler

    public void onPlace(BlockPlaceEvent e){

        if(e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {

        } else {

            e.setCancelled(true);
        }


    }
}
