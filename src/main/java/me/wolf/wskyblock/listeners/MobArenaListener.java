package me.wolf.wskyblock.listeners;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.mobarena.MobArena;
import me.wolf.wskyblock.player.SkyblockPlayer;
import me.wolf.wskyblock.skills.Skill;
import me.wolf.wskyblock.utils.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class MobArenaListener implements Listener {

    private final SkyblockPlugin plugin;

    public MobArenaListener(final SkyblockPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMobKill(final EntityDeathEvent event) {
        if(event.getEntity().getKiller() == null) return;

        final SkyblockPlayer killer = plugin.getPlayerManager().getSkyblockPlayer(event.getEntity().getKiller().getUniqueId());
        final String name = event.getEntity().getName();
        for (MobArena mobArena : plugin.getMobArenaManager().getMobArenas()) {
            if (plugin.getMobArenaManager().getMobRewards().containsKey(Utils.colorize(name))) {
                if (mobArena.getCuboid().isIn(event.getEntity().getLocation())) {
                    event.getDrops().clear(); // clear mob drops
                    mobArena.decreaseEntityCount(); // remove an entity from, the arena
                    final Skill monsterKiller = plugin.getSkillManager().getSkillByNamePlayer(killer, "monster killer");
                    // grab the monster killer skill & update the XP
                    plugin.getSkillManager().addExperience(killer, monsterKiller, plugin.getMobArenaManager().getMobRewards().get(Utils.colorize(name)));
                    killer.sendMessage("&cYou killed a &e " + name + "&c!");
                }
            }
        }
    }

}
