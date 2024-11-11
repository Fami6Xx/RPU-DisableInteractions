package me.fami6xx.famidisableinteractions.commands;

import me.fami6xx.famidisableinteractions.Fami_DisableInteractions;
import me.fami6xx.rpuniverse.core.misc.utils.FamiUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DIBypassCommand implements CommandExecutor {

    private final Fami_DisableInteractions plugin;

    public DIBypassCommand(Fami_DisableInteractions plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("disableinteractions.bypass")) {
            player.sendMessage(FamiUtils.formatWithPrefix("&cYou don't have permission to use this command."));
            return true;
        }

        boolean isBypassing = plugin.toggleBypass(player.getUniqueId());

        if (isBypassing) {
            player.sendMessage(FamiUtils.formatWithPrefix("&aBypass disabled. You will be affected by disabled interactions."));
        } else {
            player.sendMessage(FamiUtils.formatWithPrefix("&aBypass enabled. You will no longer be affected by disabled interactions."));
        }

        return true;
    }
}
