package me.fami6xx.famidisableinteractions.listeners;

import me.fami6xx.famidisableinteractions.BlockManager;
import me.fami6xx.famidisableinteractions.Fami_DisableInteractions;
import me.fami6xx.rpuniverse.core.misc.utils.FamiUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractionListener implements Listener {

    private final BlockManager blockManager;
    private final Fami_DisableInteractions plugin;

    public InteractionListener(BlockManager blockManager, Fami_DisableInteractions plugin) {
        this.blockManager = blockManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.hasBlock()) {
            Material blockType = event.getClickedBlock().getType();
            if (blockManager.isBlockDisabled(blockType)) {
                if (!player.hasPermission("disableinteractions.override")) {
                    event.setCancelled(true);
                    player.sendMessage(FamiUtils.formatWithPrefix("&cThis block is disabled."));
                }else if(plugin.isBypassing(player.getUniqueId())){
                    event.setCancelled(true);
                    player.sendMessage(FamiUtils.formatWithPrefix("&cThis block is disabled."));
                }
            }
        }
    }
}
