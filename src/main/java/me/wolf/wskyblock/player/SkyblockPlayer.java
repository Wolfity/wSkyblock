package me.wolf.wskyblock.player;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.skills.Skill;
import me.wolf.wskyblock.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class SkyblockPlayer {

    private final UUID uuid;
    private int coins;
    private Set<Skill> skills = new HashSet<>();

    public SkyblockPlayer(final SkyblockPlugin plugin, final UUID uuid) {
        this.uuid = uuid;
        this.coins = 0;
        plugin.getSkillManager().createSkills(this);

    }

    public void sendMessage(final String msg) {
        getBukkitPlayer().sendMessage(Utils.colorize(msg));
    }

    public Inventory getInventory() {
        return getBukkitPlayer().getInventory();
    }

    public Player getBukkitPlayer() {
        return Bukkit.getPlayer(getUuid());
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(getUuid());
    }

    public String getName() {
        return getBukkitPlayer().getName();
    }

    public UUID getUuid() {
        return uuid;
    }

    public Set<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void removeCoins(int amount) {
        this.coins -= amount;
    }

    public void addCoins(int amount) {
        this.coins += amount;
    }

    public Location getLocation() {
        return getBukkitPlayer().getLocation();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkyblockPlayer that = (SkyblockPlayer) o;
        return uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}