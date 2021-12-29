package me.wolf.wskyblock.mobs.types;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.files.YamlConfig;
import me.wolf.wskyblock.mobs.IArenaMob;
import me.wolf.wskyblock.utils.Utils;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.monster.EntitySpider;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class MobArenaSpider extends EntitySpider implements IArenaMob {

    public MobArenaSpider(final World world, final Location location, final SkyblockPlugin plugin) {
        super(EntityTypes.aI, world);
        this.setPosition(location.getX(), location.getY(), location.getZ());
        this.setCustomNameVisible(true);
        world.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
        setupEntity(plugin.getFileManager().getMobsConfig());
    }


    @Override // setting it up, giving the appropriate armor, name, damage, speed and HP
    public void setupEntity(YamlConfig cfg) {
        final String name = cfg.getConfig().getString("mobs.spider.name");
        final double damage = cfg.getConfig().getDouble("mobs.spider.damage");
        final double speed = cfg.getConfig().getDouble("mobs.spider.speed");
        final double health = cfg.getConfig().getDouble("mobs.spider.health");
        this.setCustomName(new ChatComponentText(Utils.colorize(name)));
        this.craftAttributes.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(speed);
        this.craftAttributes.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
        this.craftAttributes.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(damage);
    }

    @Override
    public boolean t() { // prevent a spider from climbing walls
        return false;
    }

}
