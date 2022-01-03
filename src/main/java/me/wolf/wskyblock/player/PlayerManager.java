package me.wolf.wskyblock.player;

import me.wolf.wskyblock.SkyblockPlugin;

import java.util.*;

public class PlayerManager {
    private final SkyblockPlugin plugin;
    private final Map<UUID, SkyblockPlayer> skyblockPlayers = new HashMap<>();
    private final Set<SkyblockPlayer> inFilterMode = new HashSet<>();

    public PlayerManager(final SkyblockPlugin plugin) {
        this.plugin = plugin;
    }

    public void removeSkyblockPlayer(final UUID uuid) {
        this.skyblockPlayers.remove(uuid);
    }

    public void addSkyblockPlayer(final UUID uuid) {
        this.skyblockPlayers.put(uuid, new SkyblockPlayer(plugin, uuid));
    }

    public SkyblockPlayer getSkyblockPlayer(final UUID uuid) {
        return skyblockPlayers.get(uuid);
    }

    public Set<SkyblockPlayer> getInFilterMode() {
        return inFilterMode;
    }

    public Map<UUID, SkyblockPlayer> getSkyblockPlayers() {
        return skyblockPlayers;
    }
}
