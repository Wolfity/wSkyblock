package me.wolf.wskyblock.scoreboards;

import me.wolf.wskyblock.SkyblockPlugin;
import me.wolf.wskyblock.player.SkyblockPlayer;
import me.wolf.wskyblock.utils.Utils;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.Arrays;
import java.util.List;

public class SkyblockScoreboard {

    final List<String> sbTitle = Arrays.asList("&e&lS&f&lk&e&ly&f&lb&e&ll&f&lo&e&lc&f&lk", "&f&lS&e&lk&f&ly&e&lb&f&ll&e&lo&f&lc&e&lk");
    private final SkyblockPlugin plugin;
    int timer = 2;
    public SkyblockScoreboard(final SkyblockPlugin plugin) {
        this.plugin = plugin;
    }

    public void skyblockScoreboard(final SkyblockPlayer player) {
        final ScoreboardManager scoreboardManager = plugin.getServer().getScoreboardManager();
        Scoreboard scoreboard = scoreboardManager.getNewScoreboard();

        final Objective objective = scoreboard.registerNewObjective("sb", "sb");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        new BukkitRunnable() { // different titles (color changing)
            @Override
            public void run() {
                if (timer == 2) {
                    timer--;
                    objective.setDisplayName(Utils.colorize(sbTitle.get(0)));
                } else {
                    objective.setDisplayName(Utils.colorize(sbTitle.get(1)));
                    timer = 2;
                }
            }
        }.runTaskTimer(plugin, 0, 20L);


        final Team header = scoreboard.registerNewTeam("header");
        header.addEntry(Utils.colorize("&7The best Skyblock ever"));
        header.setPrefix("");
        header.setSuffix("");
        objective.getScore(Utils.colorize("&7The best Skyblock ever")).setScore(9);

        final Team empty3 = scoreboard.registerNewTeam("empty3");
        empty3.addEntry(" ");
        empty3.setPrefix("");
        empty3.setSuffix("");
        objective.getScore(" ").setScore(8);

        final Team coins = scoreboard.registerNewTeam("Coins");
        coins.addEntry(Utils.colorize("&eCoins: "));
        coins.setPrefix("");
        coins.setSuffix(Utils.colorize(String.valueOf(player.getCoins())));
        objective.getScore(Utils.colorize("&eCoins: ")).setScore(7);

        final Team empty2 = scoreboard.registerNewTeam("empty2");
        empty2.addEntry(" ");
        empty2.setPrefix("");
        empty2.setSuffix("");
        objective.getScore(" ").setScore(6);

        final Team lumber = scoreboard.registerNewTeam("lumber");
        lumber.addEntry(Utils.colorize(plugin.getSkillManager().getSkillByNamePlayer(player, "Lumberjack").getScoreboardDisplay() + ": "));
        lumber.setPrefix("");
        lumber.setSuffix(Utils.colorize(String.valueOf(plugin.getSkillManager().getSkillByNamePlayer(player, "Lumberjack").getLevel())));
        objective.getScore(Utils.colorize(plugin.getSkillManager().getSkillByNamePlayer(player, "Lumberjack").getScoreboardDisplay() + ": ")).setScore(5);

        final Team monster = scoreboard.registerNewTeam("monster");
        monster.addEntry(Utils.colorize(plugin.getSkillManager().getSkillByNamePlayer(player, "Monster Killer").getScoreboardDisplay() + ": "));
        monster.setPrefix("");
        monster.setSuffix(Utils.colorize(String.valueOf(plugin.getSkillManager().getSkillByNamePlayer(player, "Monster Killer").getLevel())));
        objective.getScore(Utils.colorize(plugin.getSkillManager().getSkillByNamePlayer(player, "Monster Killer").getScoreboardDisplay() + ": ")).setScore(4);

        final Team miner = scoreboard.registerNewTeam("miner");
        miner.addEntry(Utils.colorize(plugin.getSkillManager().getSkillByNamePlayer(player, "miner").getScoreboardDisplay() + ": "));
        miner.setPrefix("");
        miner.setSuffix(Utils.colorize(String.valueOf(plugin.getSkillManager().getSkillByNamePlayer(player, "Miner").getLevel())));
        objective.getScore(Utils.colorize(plugin.getSkillManager().getSkillByNamePlayer(player, "miner").getScoreboardDisplay() + ": ")).setScore(3);

        final Team empty1 = scoreboard.registerNewTeam("empty1");
        empty1.addEntry(" ");
        empty1.setPrefix("");
        empty1.setSuffix("");
        objective.getScore(" ").setScore(2);

        final Team footer = scoreboard.registerNewTeam("footer");
        footer.addEntry(Utils.colorize("&7Footer :)"));
        footer.setPrefix("");
        footer.setSuffix("");
        objective.getScore(Utils.colorize("&7Footer :)")).setScore(1);

        player.getBukkitPlayer().setScoreboard(scoreboard);

    }

}
