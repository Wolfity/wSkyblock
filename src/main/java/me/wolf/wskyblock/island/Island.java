package me.wolf.wskyblock.island;

import me.wolf.wskyblock.player.SkyblockPlayer;
import me.wolf.wskyblock.warps.Warp;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Island {

    private final SkyblockPlayer owner;
    private List<Warp> warps;
    private boolean acceptsVisitors;
    private Location spawn;


    public Island(final SkyblockPlayer owner) {
        this.owner = owner;
        this.warps = new ArrayList<>();
        this.acceptsVisitors = true;
        this.spawn = null;
    }

    public List<Warp> getWarps() {
        return warps;
    }

    public void setWarps(final List<Warp> warps) {
        this.warps = warps;
    }

    public void addWarp(final Warp warp) {
        this.warps.add(warp);
    }

    public SkyblockPlayer getOwner() {
        return owner;
    }

    public boolean acceptsVisitors() {
        return acceptsVisitors;
    }

    public void setAcceptsVisitors(boolean acceptsVisitors) {
        this.acceptsVisitors = acceptsVisitors;
    }

    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Island island = (Island) o;
        return owner.equals(island.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner);
    }
}
