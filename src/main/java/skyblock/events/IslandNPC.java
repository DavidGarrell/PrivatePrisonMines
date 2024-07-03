package skyblock.events;

import net.citizensnpcs.api.event.NPCClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import skyblock.api.IslandManager;
import skyblock.main.Main;
import skyblock.menus.IslandMenu;

public class IslandNPC implements Listener {



    @EventHandler

    public void onClickNPC(NPCClickEvent event){
        Player player = event.getClicker();

        NPC npc = event.getNPC();

        IslandManager islandManager = Main.islandManager;


        if(npc.equals(islandManager.island.get(player.getUniqueId()).getNpc())){
            IslandMenu islandMenu = new IslandMenu();
            islandMenu.openMenu(player);
        }

    }
}
