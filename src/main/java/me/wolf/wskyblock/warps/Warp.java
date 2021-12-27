package me.wolf.wskyblock.warps;

import org.bukkit.Location;
import org.bukkit.Material;

public class Warp {

    private final Location location;
    private final String name;
    private Material icon;
    private String display;

    public Warp(final Location location, final String name) {
        this.location = location;
        this.name = name;

    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public Material getIcon() {
        return icon;
    }

    public void setIcon(Material icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }


}
