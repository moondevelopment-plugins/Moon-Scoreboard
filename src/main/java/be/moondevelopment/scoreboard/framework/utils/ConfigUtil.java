package be.moondevelopment.scoreboard.framework.utils;
/*
 * @created 22/02/2023
 * @project Scoreboard
 * @author MoonDevelopment
 */

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigUtil {

    @Getter
    private static FileConfiguration config;

    public static void init() {
        config = new YamlConfigUtil("config.yml");
    }


}
