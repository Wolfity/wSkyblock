package me.wolf.wskyblock.enchants;

import me.wolf.wskyblock.utils.Utils;

public class SBEnchantment {

    private final SupportEnchantItem supportEnchantItem;
    private final String name, display;
    private final int maxLevel, cost;
    private final double levelIncreaseCost;
    private int currentLevel;

    public SBEnchantment(final SupportEnchantItem supportEnchantItem, final String name, final String display, final int maxLevel, final double levelIncreaseCost, final int cost) {
        this.supportEnchantItem = supportEnchantItem;
        this.name = name;
        this.display = display;
        this.currentLevel = 1;
        this.maxLevel = maxLevel;
        this.levelIncreaseCost = levelIncreaseCost;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public double getLevelIncreaseCost() {
        return levelIncreaseCost;
    }

    public int getCost() {
        return cost;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public String getDisplay() {
        return Utils.colorize(display);
    }

    public SupportEnchantItem getSupportEnchantItem() {
        return supportEnchantItem;
    }

    public String format() {
        return Utils.colorize(getName() + " " + currentLevel);
    }
}
