package me.wolf.wskyblock.island;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.player.SkyblockPlayer;
import me.wolf.wskyblock.warps.Warp;
import me.wolf.wskyblock.world.EmptyChunkGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class IslandManager {

    private final SkyblockPlugin plugin;
    private final Set<Island> islands = new HashSet<>();

    public IslandManager(final SkyblockPlugin plugin) {
        this.plugin = plugin;
    }

    // creation of an island, creating a new world named after the user
    public void createIsland(final SkyblockPlayer owner) {
        final Island island = new Island(owner);
        island.setSpawn(new Location(new WorldCreator(owner.getName()).generator(new EmptyChunkGenerator()).createWorld(), 0, 100, 0));
        islands.add(island);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.getSqLiteManager().setSpawn(owner.getUuid(), spawnToString(island.getSpawn()));
            plugin.getSqLiteManager().setAcceptVisitors(owner.getUuid(), island.acceptsVisitors());
            plugin.getSqLiteManager().saveData(owner.getUuid());
        });

        try {
            createSchem(island.getSpawn());
        } catch (final IOException e) {
            e.printStackTrace();
        }

        owner.getBukkitPlayer().teleport(island.getSpawn());
        owner.sendMessage("&aIsland Created!");
    }

    // loading in the island of a specific player
    public void loadIsland(final SkyblockPlayer owner, final String location, final boolean accepts) {
        final Island island = new Island(owner);
        island.setSpawn(stringToLocation(location));
        island.setAcceptsVisitors(accepts);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> island.setWarps(new ArrayList<>(plugin.getSqLiteManager().loadWarps(island))));
        islands.add(island);


    }

    // adding a warp to an island
    public void addWarp(final Island island, final Warp warp) {
        island.addWarp(warp);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.getSqLiteManager().addWarp(island, warp));
    }

    // remove a warp (by name)
    public void removeWarp(final Island island, final String warpName) {

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.getSqLiteManager().removeWarp(island, warpName));
        island.removeWarp(getWarpByName(island, warpName));
    }

    // getting an island by passing in the owner
    public Island getIslandByOwner(final SkyblockPlayer owner) {
        return islands.stream().filter(island -> island.getOwner().equals(owner)).findFirst().orElse(null);
    }

    // preparing and pasting the schemetica from the default skyblock island
    private void createSchem(final Location spawn) throws IOException {

        final File schem = new File("schematics/island.schematic");

        ClipboardFormat format = ClipboardFormats.findByFile(schem);
        try (ClipboardReader reader = format.getReader(new FileInputStream(schem))) {
            Clipboard clipboard = reader.read();

            try (EditSession editSession = WorldEdit.getInstance().newEditSession(new BukkitWorld(spawn.getWorld()))) {
                final Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(editSession)
                        .to(BlockVector3.at(spawn.getX(), spawn.getY(), spawn.getZ()))
                        .ignoreAirBlocks(false)
                        .build();
                Operations.complete(operation);

            } catch (WorldEditException e) {
                e.printStackTrace();
            }

        }
    }


    // converting a Location to a String
    private String spawnToString(final Location location) {
        return location.getWorld().getName() + " " + Math.round(location.getX()) + " " + Math.round(location.getY()) + " " + Math.round(location.getZ());
    }

    // converting a string to a location
    private Location stringToLocation(final String loc) {
        final String[] locSplit = loc.split(" ");
        return new Location(Bukkit.getWorld(locSplit[0]), Double.parseDouble(locSplit[1]), Double.parseDouble(locSplit[2]), Double.parseDouble(locSplit[3]));
    }

    public Warp getWarpByName(final Island island, final String warpName) {
        return island.getWarps().stream().filter(warp -> warp.getName().equalsIgnoreCase(warpName)).findFirst().orElse(null);
    }
}

