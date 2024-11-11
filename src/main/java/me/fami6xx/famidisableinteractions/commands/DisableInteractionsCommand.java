package me.fami6xx.famidisableinteractions.commands;

import me.fami6xx.famidisableinteractions.BlockManager;
import me.fami6xx.famidisableinteractions.menu.DisableInteractionsMenu;
import me.fami6xx.rpuniverse.RPUniverse;
import me.fami6xx.rpuniverse.core.menuapi.utils.PlayerMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.entity.Player;

public class DisableInteractionsCommand implements CommandExecutor {

    private final Plugin plugin;
    private final BlockManager blockManager;

    public DisableInteractionsCommand(Plugin plugin, BlockManager blockManager) {
        this.plugin = plugin;
        this.blockManager = blockManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("disableinteractions.manage")) {
            player.sendMessage("You don't have permission to use this command.");
            return true;
        }

        // Open the menu
        PlayerMenu playerMenu = RPUniverse.getInstance().getMenuManager().getPlayerMenu(player);
        DisableInteractionsMenu menu = new DisableInteractionsMenu(playerMenu, blockManager, plugin);
        menu.open();

        return true;
    }
}
