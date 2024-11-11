package me.fami6xx.famidisableinteractions;

import me.fami6xx.famidisableinteractions.commands.DIBypassCommand;
import me.fami6xx.famidisableinteractions.commands.DisableInteractionsCommand;
import me.fami6xx.famidisableinteractions.listeners.InteractionListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class Fami_DisableInteractions extends JavaPlugin {

    private BlockManager blockManager;
    private Set<UUID> bypassPlayers;

    @Override
    public void onEnable() {
        // Plugin startup logic
        blockManager = new BlockManager(this);
        bypassPlayers = new HashSet<>();
        getServer().getPluginManager().registerEvents(new InteractionListener(blockManager, this), this);
        getCommand("disableinteractions").setExecutor(new DisableInteractionsCommand(this, blockManager));
        getCommand("dibypass").setExecutor(new DIBypassCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        blockManager.saveDisabledBlocks();
    }

    public BlockManager getBlockManager() {
        return blockManager;
    }

    public boolean isBypassing(UUID playerId) {
        return bypassPlayers.contains(playerId);
    }

    public boolean toggleBypass(UUID playerId) {
        if (bypassPlayers.contains(playerId)) {
            bypassPlayers.remove(playerId);
            return false; // Bypass disabled
        } else {
            bypassPlayers.add(playerId);
            return true; // Bypass enabled
        }
    }
}
