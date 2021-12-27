package me.wolf.wskyblock.listeners;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.player.SkyblockPlayer;
import me.wolf.wskyblock.skills.Skill;
import me.wolf.wskyblock.skills.SkillReward;
import me.wolf.wskyblock.skills.skilltypes.LumberJack;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomEnchantListeners implements Listener {

    private final SkyblockPlugin plugin;

    public CustomEnchantListeners(final SkyblockPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onWalk(PlayerMoveEvent event) {
        if (event.getPlayer().getInventory().getBoots() == null) return;
        final ItemStack boots = event.getPlayer().getInventory().getBoots();
        if (boots == null) return;

        final PersistentDataContainer pdc = boots.getItemMeta().getPersistentDataContainer();
        if (pdc.has(new NamespacedKey(plugin, "speed"), PersistentDataType.STRING)) {
            assert boots.getItemMeta() != null && boots.getItemMeta().getLore() != null;
            final String data = pdc.get(new NamespacedKey(plugin, "speed"), PersistentDataType.STRING);
            int level = Integer.parseInt(data.replaceAll("[\\D]", ""));
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, level - 1));
        }
    }

    @EventHandler
    public void onTelekinesis(BlockBreakEvent event) {
        final ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if (item.getItemMeta() == null) return;

        final PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        if (pdc.has(new NamespacedKey(plugin, "telekinesis"), PersistentDataType.STRING)) {
            event.getPlayer().getInventory().addItem(new ItemStack(event.getBlock().getType()));
            event.setDropItems(false);

        }
    }

    @EventHandler
    public void onTimber(BlockBreakEvent event) {
        final ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if (item.getItemMeta() == null) return;

        final SkyblockPlayer player = plugin.getPlayerManager().getSkyblockPlayer(event.getPlayer().getUniqueId());
        final Skill skill = plugin.getSkillManager().getSkillByNamePlayer(player, "lumberjack");
        final SkillReward<Material> lumberJackRewards = ((LumberJack) skill).getSkillRewards();
        final PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();

        if (event.getBlock().getType().name().contains("WOOD") || event.getBlock().getType().name().contains("LOG")) {
            if (pdc.has(new NamespacedKey(plugin, "timber"), PersistentDataType.STRING)) {
                final String data = pdc.get(new NamespacedKey(plugin, "timber"), PersistentDataType.STRING); // checking if the PDC contains timber
                final int level = Integer.parseInt(data.replaceAll("[\\D]", ""));
                int limit = 4;
                // every level 4 x level blocks will break (lvl 2 = 4x2 = 8 blocks)
                for (final Block block : getConnectedBlock(event.getBlock()).stream().limit((long) limit * (long) level).collect(Collectors.toList())) {
                    event.getPlayer().getInventory().addItem(new ItemStack(block.getType()));
                    // adding skill experience for every "broken" block
                    if (lumberJackRewards.getRewardsMap().containsKey(event.getBlock().getType())) {
                        plugin.getSkillManager().addExperience(player, skill, lumberJackRewards.getRewardsMap().get(event.getBlock().getType()));
                    }
                    block.setType(Material.AIR);

                }
            }
        }
    }


    private void getConnectedBlocks(final Block block, final Set<Block> results, final List<Block> todo) {
        for (final BlockFace f : BlockFace.values()) {
            final Block b = block.getRelative(f);
            if (b.getType() == block.getType()) {
                if (!results.contains(b)) {
                    if (results.add(b)) {
                        todo.add(b);
                    }
                }
            }
        }
    }

    private Set<Block> getConnectedBlock(Block block) {
        final Set<Block> set = new HashSet<>();
        final LinkedList<Block> list = new LinkedList<>();

        list.add(block);

        while ((block = list.poll()) != null) {
            getConnectedBlocks(block, set, list);
        }
        return set;
    }
}
