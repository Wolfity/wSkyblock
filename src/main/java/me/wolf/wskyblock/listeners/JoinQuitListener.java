package me.wolf.wskyblock.listeners;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.player.SkyblockPlayer;
import me.wolf.wskyblock.skills.Skill;
import me.wolf.wskyblock.world.EmptyChunkGenerator;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class JoinQuitListener implements Listener {

    private final SkyblockPlugin plugin;

    public JoinQuitListener(final SkyblockPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) { // loading all the needed data for the player, and creating a player object
        final Player player = event.getPlayer();
        plugin.getPlayerManager().addSkyblockPlayer(player.getUniqueId());

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.getSqLiteManager().createPlayerData(player.getUniqueId(), player.getName());
            plugin.getSqLiteManager().createSkillData(player.getUniqueId(), player.getName());

        });
        new WorldCreator(event.getPlayer().getName()).generator(new EmptyChunkGenerator()).createWorld();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (event.getPlayer().isOnline()) {
                    plugin.getSkyblockScoreboard().skyblockScoreboard(plugin.getPlayerManager().getSkyblockPlayer(player.getUniqueId()));
                } else this.cancel();
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) { // saving the player's island/stats
        final SkyblockPlayer player = plugin.getPlayerManager().getSkyblockPlayer(event.getPlayer().getUniqueId());
        Bukkit.getWorld(event.getPlayer().getName()).save();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.getSqLiteManager().saveData(event.getPlayer().getUniqueId());
            for (final Skill skill : player.getSkills()) {
                plugin.getSqLiteManager().saveSkillData(player.getUuid(), skill, skill.toString());
            }
        });
        event.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        Bukkit.getScheduler().runTaskLater(plugin, () -> plugin.getPlayerManager().removeSkyblockPlayer(event.getPlayer().getUniqueId()), 20);
    }

}
