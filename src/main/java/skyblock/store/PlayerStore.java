package skyblock.store;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerStore {
    ArrayList<UUID> joined_Player = new ArrayList<>();

    public void addJoined_Player(Player player){

        if(!joined_Player.contains(player.getUniqueId())) {
            joined_Player.add(player.getUniqueId());
        }
    }

    public ArrayList<UUID> getJoined_Player() {
        return joined_Player;
    }
}
