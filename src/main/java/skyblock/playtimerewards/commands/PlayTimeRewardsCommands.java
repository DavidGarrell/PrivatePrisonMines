package skyblock.playtimerewards.commands;

import de.backpack.apfloat.Apfloat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import skyblock.battlepass.api.BattlePassLevel;
import skyblock.battlepass.api.BattlePassReward;
import skyblock.battlepass.menus.BattlePassMainMenu;
import skyblock.battlepass.rewards.RewardsEnum;
import skyblock.main.Main;
import skyblock.playtimerewards.menu.PlayTimeRewardsMenu;
import skyblock.playtimerewards.rewards.RewardStore;
import skyblock.playtimerewards.rewards.Rewards;

import java.util.UUID;

public class PlayTimeRewardsCommands implements CommandExecutor {


    private Main plugin;

    public PlayTimeRewardsCommands(Main plugin) {
        this.plugin = plugin;

    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID uuid = player.getUniqueId();
            if (label.equalsIgnoreCase("rewards") || label.equalsIgnoreCase("gifts")) {

                if (args.length == 0) {

                    PlayTimeRewardsMenu playTimeRewardsMenu = new PlayTimeRewardsMenu();
                    playTimeRewardsMenu.openMenu(player);

                } else if (args[0].equalsIgnoreCase("addreward")) {
                    if(sender.hasPermission("rewards.addreward") || sender.hasPermission("rewards.admin")) {

                        int level = Integer.parseInt(args[1]);
                        ItemStack stack = player.getItemInHand();
                        ItemMeta meta = player.getItemInHand().getItemMeta();
                        RewardsEnum type = RewardsEnum.valueOf(args[2]);


                        RewardStore rewardStore = Main.instance.rewardStore;

                        if(type.equals(RewardsEnum.Item)){
                            rewardStore.setRewards(level, new Rewards(type, stack, meta));
                        }
                        if(args.length==4) {
                            String value = args[3];
                            Apfloat avalue = new Apfloat(value);
                            if (type.equals(RewardsEnum.Tokens)) {
                                rewardStore.setRewards(level, new Rewards(type, avalue));
                            }
                            if (type.equals(RewardsEnum.Money)) {
                                rewardStore.setRewards(level, new Rewards(type, avalue));
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
