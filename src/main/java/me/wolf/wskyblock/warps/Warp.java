package me.wolf.wskyblock.warps;

import org.bukkit.Location;

public class Warp {

    private final Location location;
    private final String name;

    public Warp(final Location location, final String name) {
        this.location = location;
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }


}
