package me.fami6xx.famidisableinteractions;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class BlockManager {

    private final JavaPlugin plugin;
    private final File dataFile;
    private final Gson gson = new Gson();
    private Set<String> disabledBlockTypes = new HashSet<>();

    public BlockManager(JavaPlugin plugin) {
        this.plugin = plugin;
        dataFile = new File(plugin.getDataFolder(), "disabled_block_types.json");
        loadDisabledBlocks();
    }

    public void addDisabledBlock(Material material) {
        disabledBlockTypes.add(material.name());
        saveDisabledBlocks();
    }

    public void removeDisabledBlock(Material material) {
        disabledBlockTypes.remove(material.name());
        saveDisabledBlocks();
    }

    public boolean isBlockDisabled(Material material) {
        return disabledBlockTypes.contains(material.name());
    }

    public Set<String> getDisabledBlockTypes() {
        return disabledBlockTypes;
    }

    public void saveDisabledBlocks() {
        try {
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }
            Writer writer = new FileWriter(dataFile);
            gson.toJson(disabledBlockTypes, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadDisabledBlocks() {
        if (!dataFile.exists()) {
            return;
        }
        try {
            Reader reader = new FileReader(dataFile);
            Type setType = new TypeToken<HashSet<String>>() {}.getType();
            disabledBlockTypes = gson.fromJson(reader, setType);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
