package me.wolf.wskyblock.mobs.types;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.files.YamlConfig;
import me.wolf.wskyblock.mobs.IArenaMob;
import me.wolf.wskyblock.utils.Utils;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.monster.EntitySkeleton;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

public class MobArenaSkeleton extends EntitySkeleton implements IArenaMob {
    public MobArenaSkeleton(final World world, final Location location, final SkyblockPlugin plugin) {
        super(EntityTypes.aB, world);
        this.setPosition(location.getX(), location.getY(), location.getZ());
        this.setCustomNameVisible(true);
        world.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
        setupEntity(plugin.getFileManager().getMobsConfig());
    }


    @Override
    public void setupEntity(YamlConfig cfg) {
        final String name = cfg.getConfig().getString("mobs.skeleton.name");
        final double damage = cfg.getConfig().getDouble("mobs.skeleton.damage");
        final double speed = cfg.getConfig().getDouble("mobs.skeleton.speed");
        final double health = cfg.getConfig().getDouble("mobs.skeleton.health");
        final String helmet = cfg.getConfig().getString("mobs.skeleton.helmet");
        final String chestplate = cfg.getConfig().getString("mobs.skeleton.chestplate");
        final String leggings = cfg.getConfig().getString("mobs.skeleton.leggings");
        final String boots = cfg.getConfig().getString("mobs.skeleton.boots");

        this.setCustomName(new ChatComponentText(Utils.colorize(name)));
        this.craftAttributes.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(speed);
        this.craftAttributes.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
        this.craftAttributes.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(damage);


        if(!helmet.equalsIgnoreCase("none")) {
            ((LivingEntity)this.getBukkitEntity()).getEquipment().setHelmet(new ItemStack(Material.valueOf(helmet)));
        }
        if(!chestplate.equalsIgnoreCase("none")) {
            ((LivingEntity)this.getBukkitEntity()).getEquipment().setChestplate(new ItemStack(Material.valueOf(chestplate)));
        }
        if(!leggings.equalsIgnoreCase("none")) {
            ((LivingEntity)this.getBukkitEntity()).getEquipment().setLeggings(new ItemStack(Material.valueOf(leggings)));
        }
        if(!boots.equalsIgnoreCase("none")) {
            ((LivingEntity)this.getBukkitEntity()).getEquipment().setLeggings(new ItemStack(Material.valueOf(boots)));
        }
    }
}
