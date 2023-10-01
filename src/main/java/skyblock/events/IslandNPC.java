package skyblock.events;

import dev.sergiferry.playernpc.api.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import skyblock.api.IslandManager;
import skyblock.main.Main;
import skyblock.menus.IslandMenu;

public class IslandNPC implements Listener {



    @EventHandler

    public void onClickNPC(NPC.Events.Interact event){
        Player player = event.getPlayer();
        NPC npc = event.getNPC();
        NPC.Interact.ClickType clickType = event.getClickType();

        IslandManager islandManager = Main.islandManager;


        if(npc.getID().equals(islandManager.island.get(player.getUniqueId()).getNpc().getID())){
            IslandMenu islandMenu = new IslandMenu();
            islandMenu.openMenu(player);
        }
    }
}
