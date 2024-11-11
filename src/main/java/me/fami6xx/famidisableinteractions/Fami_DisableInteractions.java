package me.fami6xx.famidisableinteractions;

import me.fami6xx.famidisableinteractions.commands.DisableInteractionsCommand;
import me.fami6xx.famidisableinteractions.listeners.InteractionListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Fami_DisableInteractions extends JavaPlugin {

    private BlockManager blockManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        blockManager = new BlockManager(this);
        getServer().getPluginManager().registerEvents(new InteractionListener(blockManager), this);
        getCommand("disableinteractions").setExecutor(new DisableInteractionsCommand(this, blockManager));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        blockManager.saveDisabledBlocks();
    }

    public BlockManager getBlockManager() {
        return blockManager;
    }
}
