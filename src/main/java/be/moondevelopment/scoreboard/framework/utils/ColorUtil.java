package be.moondevelopment.scoreboard.framework.utils;
/*
 * @created 22/02/2023
 * @project Scoreboard
 * @author MoonDevelopment
 */



import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ColorUtil {

    public static String CC(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static List<String> CC(List<String> list) {
        List<String> returnVal = new ArrayList<>(list.size());
        list.forEach(s -> returnVal.add(CC(s)));
        return returnVal;
    }

    public static String[] CC(String[] lines) {
        List<String> res = new ArrayList<>();
        if(lines == null) return null;
        for(String line : lines) {
            res.add(CC(line));
        }
        return res.toArray(new String[res.size()]);
    }

    public static List<ChatColor> COLORS = new ArrayList<>(Arrays.asList(
            ChatColor.WHITE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
            ChatColor.AQUA, ChatColor.YELLOW, ChatColor.GREEN,
            ChatColor.DARK_GRAY, ChatColor.GRAY, ChatColor.DARK_AQUA,
            ChatColor.DARK_PURPLE, ChatColor.BLUE, ChatColor.BLACK,
            ChatColor.DARK_GREEN, ChatColor.RED));

    public static int convertChatColorToWoolData(ChatColor color) {
        return color == ChatColor.DARK_RED || color == ChatColor.RED ? 14
                : color == ChatColor.DARK_GREEN ? 13
                : color == ChatColor.BLUE ? 11
                : color == ChatColor.DARK_PURPLE ? 10
                : color == ChatColor.DARK_AQUA ? 9
                : color == ChatColor.DARK_GRAY ? 7
                : COLORS.indexOf(color);
    }


}
