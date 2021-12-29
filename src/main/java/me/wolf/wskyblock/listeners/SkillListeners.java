package me.wolf.wskyblock.listeners;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.player.SkyblockPlayer;
import me.wolf.wskyblock.skills.Skill;
import me.wolf.wskyblock.skills.SkillReward;
import me.wolf.wskyblock.skills.skilltypes.LumberJack;
import me.wolf.wskyblock.skills.skilltypes.Miner;
import me.wolf.wskyblock.skills.skilltypes.MonsterKiller;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class SkillListeners implements Listener {

    private final SkyblockPlugin plugin;

    public SkillListeners(final SkyblockPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLumberJack(BlockBreakEvent event) {
        final SkyblockPlayer player = plugin.getPlayerManager().getSkyblockPlayer(event.getPlayer().getUniqueId());
        final Skill lumberJack = plugin.getSkillManager().getSkillByNamePlayer(player, "lumberjack");
        // if a block that was added to the reward config was mined, add experience
        final SkillReward<Material> lumberJackRewards = ((LumberJack) lumberJack).getSkillRewards();
        if (lumberJackRewards.getRewardsMap().containsKey(event.getBlock().getType())) {
            plugin.getSkillManager().addExperience(player, lumberJack, lumberJackRewards.getRewardsMap().get(event.getBlock().getType()));
            if (lumberJack.getLucky()) {
                player.sendMessage("&aYou got lucky and received an extra log!");
                event.getPlayer().getInventory().addItem(new ItemStack(event.getBlock().getType()));
            }
        }

    }

    @EventHandler
    public void onMiner(BlockBreakEvent event) {
        final SkyblockPlayer player = plugin.getPlayerManager().getSkyblockPlayer(event.getPlayer().getUniqueId());
        final Skill miner = plugin.getSkillManager().getSkillByNamePlayer(player, "miner");
        // if a block that was added to the reward config was mined, add experience
        final SkillReward<Material> minerRewards = ((Miner) miner).getSkillRewards();
        if (minerRewards.getRewardsMap().containsKey(event.getBlock().getType())) {
            plugin.getSkillManager().addExperience(player, miner, minerRewards.getRewardsMap().get(event.getBlock().getType()));
            if (miner.getLucky()) {
                player.sendMessage("&aYou got lucky and received an extra log!");
                event.getPlayer().getInventory().addItem(new ItemStack(event.getBlock().getType()));
            }
        }
    }

    @EventHandler
    public void onMonsterKiller(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Monster)) return;
        if (event.getEntity().getKiller() == null) return;

        final SkyblockPlayer player = plugin.getPlayerManager().getSkyblockPlayer(event.getEntity().getKiller().getUniqueId());
        final Skill monsterkiller = plugin.getSkillManager().getSkillByNamePlayer(player, "monster killer");
        // if a monster that was in the skill rewards config was killed, reward the player

        final SkillReward<EntityType> monsterKillerRewards = ((MonsterKiller) monsterkiller).getSkillRewards();

        if (monsterKillerRewards.getRewardsMap().containsKey(event.getEntity().getType())) {
            plugin.getSkillManager().addExperience(player, monsterkiller, monsterKillerRewards.getRewardsMap().get(event.getEntity().getType()));
            if (monsterkiller.getLucky()) {
                final int random = new Random().nextInt(100);
                player.sendMessage("&aYou got lucky and received " + random + " coins!");
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    plugin.getSqLiteManager().setCoins(player.getUuid(), player.getCoins() + random);
                    plugin.getSqLiteManager().saveData(player.getUuid());
                });
            }
        }
    }

    @EventHandler
    public void onMonsterDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Monster)) return; // checking if the entity that took damage isn't a monster and returning
        if (!(event.getDamager() instanceof Player)) return; // checking if the damager isn't a player and returning

        final SkyblockPlayer player = plugin.getPlayerManager().getSkyblockPlayer(event.getDamager().getUniqueId());
        final Skill monsterkiller = plugin.getSkillManager().getSkillByNamePlayer(player, "monster killer");
        if (monsterkiller.getLucky()) {
            player.sendMessage("&aYou got lucky and did double damage!");
            event.setDamage(event.getDamage() * 2);
        }
    }


}
