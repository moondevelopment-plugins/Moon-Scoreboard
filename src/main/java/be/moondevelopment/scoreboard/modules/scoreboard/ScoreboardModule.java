package be.moondevelopment.scoreboard.modules.scoreboard;
/*
 * @created 27/03/2023 - 19:43
 * @project Moon-Scoreboard
 * @author MoonDevelopment
 */

import be.moondevelopment.scoreboard.Scoreboard;
import be.moondevelopment.scoreboard.framework.scoreboard.Assemble;
import be.moondevelopment.scoreboard.framework.scoreboard.AssembleStyle;
import be.moondevelopment.scoreboard.framework.utils.ColorUtil;
import be.moondevelopment.scoreboard.framework.utils.ConfigUtil;
import be.moondevelopment.scoreboard.modules.scoreboard.scoreboard.ScoreboardAdapter;
import lombok.Getter;
import me.qiooip.lazarus.timer.TimerManager;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.Bukkit;

public class ScoreboardModule {

    private static Assemble assemble;

    public static void onEnable(Scoreboard plugin) {
        if (ConfigUtil.getConfig().getBoolean("SCOREBOARD.ENABLED")) {
            assemble = new Assemble(plugin, new ScoreboardAdapter());
            assemble.setTicks(2L);
            assemble.setAssembleStyle(AssembleStyle.VIPER);

            Bukkit.getConsoleSender().sendMessage(ColorUtil.CC("&aSuccesfully enabled Scoreboard"));

            TimerManager.getInstance().getCombatTagTimer().setFormat(StringUtils.FormatType.MILLIS_TO_SECONDS);
        } else {
            Bukkit.getConsoleSender().sendMessage(ColorUtil.CC("&cScoreboard is disabled in the config.yml"));
        }
    }

    public static void onDisable(Scoreboard plugin) {
        if (assemble != null) {
            assemble.cleanup();
        }
    }
}
