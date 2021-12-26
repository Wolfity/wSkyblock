package me.wolf.wskyblock.player;

import me.wolf.wskyblock.SkyblockPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {
    private final SkyblockPlugin plugin;
    private final Map<UUID, SkyblockPlayer> skyblockPlayers = new HashMap<>();

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


    public Map<UUID, SkyblockPlayer> getSkyblockPlayers() {
        return skyblockPlayers;
    }
}
