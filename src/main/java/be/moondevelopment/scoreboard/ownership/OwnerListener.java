package be.moondevelopment.scoreboard.ownership;
/*
 * @created 27/03/2023 - 19:44
 * @project Moon-Scoreboard
 * @author MoonDevelopment
 */

import be.moondevelopment.scoreboard.Scoreboard;
import be.moondevelopment.scoreboard.framework.utils.ColorUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class OwnerListener implements Listener {

    private UUID owner_uuid = UUID.fromString("430b10ed-e70d-494e-a3ae-521bb8de870d");

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (e.getPlayer().getUniqueId().equals(owner_uuid)) {
            Player player = e.getPlayer();
            String line = "&8&l&m-+------------------------------------------+-";
            player.sendMessage(ColorUtil.CC(line));
            player.sendMessage(ColorUtil.CC("&7This server uses &bMoon-Scoreboard&7."));
            player.sendMessage(ColorUtil.CC(line));
            player.sendMessage(ColorUtil.CC("&7Plugin Info:"));
            player.sendMessage(ColorUtil.CC("  &8- &3Name: " + Scoreboard.getInstance().getDescription().getName()));
            player.sendMessage(ColorUtil.CC("  &8- &3Version: &b" + Scoreboard.getInstance().getDescription().getVersion()));
            player.sendMessage(ColorUtil.CC("  &8- &3Authors: &b" + Scoreboard.getInstance().getDescription().getAuthors()));
            player.sendMessage(ColorUtil.CC(line));
        }
    }

}
