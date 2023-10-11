package skyblock.battlepass.commands;

import de.backpack.apfloat.Apfloat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import skyblock.api.Island;
import skyblock.battlepass.api.BattlePassLevel;
import skyblock.battlepass.api.BattlePassReward;
import skyblock.battlepass.menus.BattlePassMainMenu;
import skyblock.battlepass.rewards.RewardsEnum;
import skyblock.main.Main;

import java.util.UUID;

public class BattlePassCommands implements CommandExecutor {


    private Main plugin;

    public BattlePassCommands(Main plugin) {
        this.plugin = plugin;

    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID uuid = player.getUniqueId();
            if (label.equalsIgnoreCase("pass") || label.equalsIgnoreCase("battlepass")) {

                if (args.length == 0) {

                    BattlePassMainMenu battlePassMainMenu = new BattlePassMainMenu();
                    battlePassMainMenu.openMenu(player);

                } else if (args[0].equalsIgnoreCase("addreward")) {
                    if(sender.hasPermission("battlepass.addreward") || sender.hasPermission("battlepass.admin")) {

                        int level = Integer.parseInt(args[1]);
                        ItemStack stack = player.getItemInHand();
                        ItemMeta meta = player.getItemInHand().getItemMeta();
                        RewardsEnum type = RewardsEnum.valueOf(args[2]);


                        BattlePassLevel battlePassLevel = Main.instance.passLevel;

                        if(type.equals(RewardsEnum.Item)){
                            battlePassLevel.setRewards(level, new BattlePassReward(type, stack, meta));
                        }
                        if(args.length==4) {
                            String value = args[3];
                            Apfloat avalue = new Apfloat(value);
                            if (type.equals(RewardsEnum.Tokens)) {
                                battlePassLevel.setRewards(level, new BattlePassReward(type, avalue));
                            }
                            if (type.equals(RewardsEnum.Money)) {
                                battlePassLevel.setRewards(level, new BattlePassReward(type, avalue));
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
