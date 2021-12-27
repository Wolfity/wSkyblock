package me.wolf.wskyblock.skills;

import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public abstract class Skill implements Comparable<Skill> {

    private final String name;
    private int levelCap, currentExp, level;
    private double experienceNextLevel;
    private String[] description;
    private ItemStack icon;
    private String scoreboardDisplay;

    public Skill(final String name) {
        this.name = name;
        this.experienceNextLevel = 100;
        this.levelCap = 50;
        this.currentExp = 0;
        this.level = 0;
    }

    public boolean getLucky() {
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        double chance = getLevel() * 1.15; // lvl 50 will have 75% chance to get lucky
        return chance > random.nextDouble(0, 100);
    }

    public double getExperienceNextLevel() {
        return experienceNextLevel;
    }

    public void setExperienceNextLevel(double experienceNextLevel) {
        this.experienceNextLevel = experienceNextLevel;
    }

    public void levelUp() {
        this.level += 1;
        setExperienceNextLevel(getExperienceNextLevel() * 1.5);
    }

    public String getScoreboardDisplay() {
        return scoreboardDisplay;
    }

    public void setScoreboardDisplay(String scoreboardDisplay) {
        this.scoreboardDisplay = scoreboardDisplay;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String[] getDescription() {
        return description;
    }

    public void setDescription(String[] description) {
        this.description = description;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }

    public int getCurrentExp() {
        return currentExp;
    }

    public void setCurrentExp(int currentExp) {
        this.currentExp = currentExp;
    }

    public void addExperience(int currentExp) {
        this.currentExp += currentExp;

    }

    public int getLevelCap() {
        return levelCap;
    }

    public void setLevelCap(int levelCap) {
        this.levelCap = levelCap;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Skill o) {
        return name.compareTo(o.getName());
    }
}
