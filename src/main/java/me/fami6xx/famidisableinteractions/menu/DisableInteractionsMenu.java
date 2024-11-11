package me.fami6xx.famidisableinteractions.menu;

import me.fami6xx.famidisableinteractions.BlockManager;
import me.fami6xx.rpuniverse.core.menuapi.types.EasyPaginatedMenu;
import me.fami6xx.rpuniverse.core.menuapi.utils.MenuTag;
import me.fami6xx.rpuniverse.core.menuapi.utils.PlayerMenu;
import me.fami6xx.rpuniverse.core.misc.utils.FamiUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class DisableInteractionsMenu extends EasyPaginatedMenu implements Listener {

    private final BlockManager blockManager;
    private final Plugin plugin;
    private List<Material> allMaterials;
    private List<Material> displayMaterials;
    private String searchQuery = "";
    private boolean awaitingSearchInput = false;

    public DisableInteractionsMenu(PlayerMenu menu, BlockManager blockManager, Plugin plugin) {
        super(menu);
        this.blockManager = blockManager;
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        initializeMaterials();
    }

    private void initializeMaterials() {
        this.allMaterials = getInteractableBlockMaterials();
        updateDisplayMaterials();
    }

    private void updateDisplayMaterials() {
        if (searchQuery.isEmpty()) {
            this.displayMaterials = new ArrayList<>(allMaterials);
        } else {
            this.displayMaterials = new ArrayList<>();
            for (Material material : allMaterials) {
                if (material.name().toLowerCase().contains(searchQuery.toLowerCase())) {
                    displayMaterials.add(material);
                }
            }
        }
    }

    @Override
    public ItemStack getItemFromIndex(int index) {
        if (index >= displayMaterials.size()) {
            return null;
        }
        Material material = displayMaterials.get(index);
        boolean isDisabled = blockManager.isBlockDisabled(material);

        String displayName = (isDisabled ? "&c" : "&a") + material.name();
        String[] lore = new String[]{
                isDisabled ? "&7Click to enable interactions." : "&7Click to disable interactions."
        };

        return FamiUtils.makeItem(material, displayName, lore);
    }

    @Override
    public int getCollectionSize() {
        return displayMaterials.size();
    }

    @Override
    public void handlePaginatedMenu(InventoryClickEvent e) {
        ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null) return;

        Material clickedType = clickedItem.getType();
        String displayName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());

        // Check if the clicked item is one of the control items
        if (clickedType == Material.COMPASS && displayName.equalsIgnoreCase("Search")) {
            // Search button clicked
            playerMenu.getPlayer().closeInventory();
            playerMenu.getPlayer().sendMessage(FamiUtils.formatWithPrefix("&aPlease enter your search query in chat."));
            awaitingSearchInput = true;
        } else if (clickedType == Material.BARRIER && displayName.equalsIgnoreCase("Reset Search")) {
            // Reset search
            searchQuery = "";
            updateDisplayMaterials();
            this.open();
        } else if (clickedType == Material.BARRIER && displayName.equalsIgnoreCase("Close")) {
            playerMenu.getPlayer().closeInventory();
        } else if (clickedType == Material.STONE_BUTTON && (displayName.equalsIgnoreCase("Next Page") || displayName.equalsIgnoreCase("Previous Page"))) {
        } else {
            // Handle block item clicks
            if (!clickedItem.hasItemMeta() || !clickedItem.getItemMeta().hasDisplayName()) {
                return; // Ignore items without a proper display name
            }

            // Ensure the clicked item is a block material
            if (!clickedType.isBlock()) {
                return;
            }

            Material material = clickedItem.getType();

            if (blockManager.isBlockDisabled(material)) {
                // Enable interactions with this block type
                blockManager.removeDisabledBlock(material);
                playerMenu.getPlayer().sendMessage(FamiUtils.formatWithPrefix("&aEnabled interactions with " + material.name() + "."));
            } else {
                // Disable interactions with this block type
                blockManager.addDisabledBlock(material);
                playerMenu.getPlayer().sendMessage(FamiUtils.formatWithPrefix("&cDisabled interactions with " + material.name() + "."));
            }

            // Refresh the menu
            this.open();
        }
    }

    @Override
    public void addAdditionalItems() {
        // Add Search item
        inventory.setItem(45, FamiUtils.makeItem(Material.COMPASS, "&bSearch", "&7Click to search for a block type."));
        // Add Reset Search item
        inventory.setItem(53, FamiUtils.makeItem(Material.BARRIER, "&cReset Search", "&7Click to reset the search."));
        // Add Close item
        inventory.setItem(49, FamiUtils.makeItem(Material.BARRIER, "&cClose", "&7Click to close the menu."));
    }

    @Override
    public String getMenuName() {
        return FamiUtils.formatWithPrefix("&cDisable Interactions");
    }

    @Override
    public List<MenuTag> getMenuTags() {
        return new ArrayList<>();
    }

    private List<Material> getInteractableBlockMaterials() {
        Set<Material> materials = new HashSet<>();
        for (Material material : Material.values()) {
            if (material.isBlock() && material.isInteractable()) {
                materials.add(material);
            }
        }

        List<Material> materialList = new ArrayList<>(materials);
        materialList.sort(Comparator.comparing(Enum::name));
        return materialList;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.getPlayer().equals(playerMenu.getPlayer()) && awaitingSearchInput) {
            event.setCancelled(true);
            String message = event.getMessage();
            awaitingSearchInput = false;
            setSearchQuery(message);
            Bukkit.getScheduler().runTask(plugin, this::open);
        }
    }

    public void setSearchQuery(String query) {
        this.searchQuery = query;
        updateDisplayMaterials();
    }
}