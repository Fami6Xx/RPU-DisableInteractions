package me.fami6xx.famidisableinteractions;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Objects;

public class BlockData {
    private String worldName;
    private int x;
    private int y;
    private int z;

    public BlockData(String worldName, int x, int y, int z) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockData(Location location) {
        this.worldName = location.getWorld().getName();
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
    }

    public Location toLocation() {
        return new Location(Bukkit.getWorld(worldName), x, y, z);
    }

    @Override
    public String toString() {
        return worldName + ":" + x + ":" + y + ":" + z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlockData blockData = (BlockData) o;

        if (x != blockData.x) return false;
        if (y != blockData.y) return false;
        if (z != blockData.z) return false;
        return Objects.equals(worldName, blockData.worldName);
    }

    @Override
    public int hashCode() {
        int result = worldName != null ? worldName.hashCode() : 0;
        result = 31 * result + x;
        result = 31 * result + y;
        result = 31 * result + z;
        return result;
    }
}
