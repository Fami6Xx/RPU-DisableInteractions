package me.fami6xx.famidisableinteractions.listeners;

import me.fami6xx.famidisableinteractions.BlockManager;
import me.fami6xx.rpuniverse.core.misc.utils.FamiUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractionListener implements Listener {

    private final BlockManager blockManager;

    public InteractionListener(BlockManager blockManager) {
        this.blockManager = blockManager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.hasBlock()) {
            Material blockType = event.getClickedBlock().getType();
            if (blockManager.isBlockDisabled(blockType)) {
                if (!player.hasPermission("disableinteractions.override")) {
                    event.setCancelled(true);
                    player.sendMessage(FamiUtils.formatWithPrefix("&cS timto blockem nemuzes interagovat."));
                }
            }
        }
    }
}
