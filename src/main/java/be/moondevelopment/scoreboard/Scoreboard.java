package be.moondevelopment.scoreboard;
/*
 * @created 27/03/2023 - 19:40
 * @project Moon-Scoreboard
 * @author MoonDevelopment
 */

import be.moondevelopment.scoreboard.framework.utils.ColorUtil;
import be.moondevelopment.scoreboard.framework.utils.ConfigUtil;
import be.moondevelopment.scoreboard.modules.scoreboard.ScoreboardModule;
import be.moondevelopment.scoreboard.ownership.OwnerListener;
import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.dependency.SoftDependency;
import org.bukkit.plugin.java.annotation.dependency.SoftDependsOn;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import org.bukkit.plugin.java.annotation.plugin.author.Authors;

@Plugin(
        name = "Moon-Scoreboard",
        version = "1.0"
)
@Description(
        "A Scoreboard plugin based on Lazarus | Made by MoonDevelopment"
)
@Authors({
        @Author("MoonDevelopment")
})
@SoftDependsOn({
        @SoftDependency("Lazarus"),
        @SoftDependency("Moon-KothBoost"),
        @SoftDependency("Moon-GankDamage")
})

public class Scoreboard extends JavaPlugin {

    @Getter
    private static Scoreboard instance;

    @Getter
    private boolean kothBoostEnabled = false;

    @Override
    public void onEnable() {
        instance = this;

        long start = System.currentTimeMillis();

        ConfigUtil.init();
        Bukkit.getPluginManager().registerEvents(new OwnerListener(), this);
        Bukkit.getConsoleSender().sendMessage(ColorUtil.CC("&7===&8=============================================&7==="));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.CC("&8- &3Name: &bMoon-Scoreboard"));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.CC("&8- &3Version: &b" + this.getDescription().getVersion()));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.CC("&8- &3Authors: &b" + this.getDescription().getAuthors()));
        Bukkit.getConsoleSender().sendMessage("");

        // Lazarus
        Bukkit.getConsoleSender().sendMessage(ColorUtil.CC("&6Checking Lazarus:"));
        if (!Bukkit.getPluginManager().isPluginEnabled("Lazarus")) {
            Bukkit.getConsoleSender().sendMessage(ColorUtil.CC("  &cLazarus is not installed on this server!"));
            Bukkit.getConsoleSender().sendMessage(ColorUtil.CC(""));
            Bukkit.getConsoleSender().sendMessage(ColorUtil.CC("&8- &cDisabling plugin..."));
            Bukkit.getConsoleSender().sendMessage(ColorUtil.CC("&7===&8=============================================&7==="));
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        } else {
            Bukkit.getConsoleSender().sendMessage(ColorUtil.CC("  &aSuccesfully detected &eLazarus"));
            Bukkit.getConsoleSender().sendMessage(ColorUtil.CC(""));
            Lazarus.getInstance().getScoreboardManager().disable();
        }

        // Moon Plugins
        Bukkit.getConsoleSender().sendMessage(ColorUtil.CC("&6Checking Moon Plugins:"));
        if (
                Bukkit.getPluginManager().isPluginEnabled("Moon-KothBoost")
        ) {
            if (Bukkit.getPluginManager().isPluginEnabled("Moon-KothBoost")) {
                Bukkit.getConsoleSender().sendMessage(ColorUtil.CC("  &aSuccesfully detected &eMoon-KothBoost"));
                kothBoostEnabled = true;
            }
            Bukkit.getConsoleSender().sendMessage(ColorUtil.CC(""));
        } else {
            Bukkit.getConsoleSender().sendMessage(ColorUtil.CC("  &cNo Moon plugins detected!"));
            Bukkit.getConsoleSender().sendMessage(ColorUtil.CC(""));
        }

        ScoreboardModule.onEnable(this);

        Bukkit.getConsoleSender().sendMessage(ColorUtil.CC("&8- &aSuccesfully enabled &eMoon-Scoreboard&a plugin in &2" + (System.currentTimeMillis() - start) + "ms&a."));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.CC("&7===&8=============================================&7==="));

    }

    @Override
    public void onDisable() {

        Bukkit.getConsoleSender().sendMessage(ColorUtil.CC("&7===&8=============================================&7==="));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.CC("- &cDisabling &bMoon-Scoreboard &3" + this.getDescription().getVersion()));
        Bukkit.getConsoleSender().sendMessage("");
        ScoreboardModule.onDisable(this);
        Bukkit.getServicesManager().unregisterAll(this);
        Bukkit.getConsoleSender().sendMessage(ColorUtil.CC("&7===&8=============================================&7==="));
    }

}