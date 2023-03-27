package be.moondevelopment.scoreboard.modules.scoreboard.scoreboard;
/*
 * @created 27/03/2023 - 19:50
 * @project Moon-Scoreboard
 * @author MoonDevelopment
 */

import be.moondevelopment.kothboost.modules.kothboost.api.KothBoostAPI;
import be.moondevelopment.scoreboard.Scoreboard;
import be.moondevelopment.scoreboard.framework.scoreboard.AssembleAdapter;
import be.moondevelopment.scoreboard.framework.utils.ColorUtil;
import be.moondevelopment.scoreboard.framework.utils.ConfigUtil;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.factions.Faction;
import me.qiooip.lazarus.factions.FactionsManager;
import me.qiooip.lazarus.factions.claim.ClaimManager;
import me.qiooip.lazarus.factions.enums.ChatType;
import me.qiooip.lazarus.factions.type.PlayerFaction;
import me.qiooip.lazarus.games.dtc.DtcData;
import me.qiooip.lazarus.games.koth.RunningKoth;
import me.qiooip.lazarus.handlers.staff.RebootHandler;
import me.qiooip.lazarus.timer.TimerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScoreboardAdapter implements AssembleAdapter {
    @Override
    public String getTitle(Player player) {
        return ColorUtil.CC(ConfigUtil.getConfig().getString("SCOREBOARD.TITLE"));
    }

    @Override
    public List<String> getLines(Player player) {
        final List<String> toReturn = new ArrayList<>();
        String line = ConfigUtil.getConfig().getString("SCOREBOARD.SCOREBOARD_LINE");
        Faction factionAt = ClaimManager.getInstance().getFactionAt(player);
        RebootHandler reboot = Lazarus.getInstance().getRebootHandler();
        List<RunningKoth> koths = Lazarus.getInstance().getKothManager().getRunningKoths();
        RunningKoth koth = null;
        PlayerFaction faction = FactionsManager.getInstance().getPlayerFaction(player.getUniqueId());
        Iterator<RunningKoth> kothit;
        if (Lazarus.getInstance().getStaffModeManager().isInStaffMode(player)) {
            // DEFAULT
            for (String s : ConfigUtil.getConfig().getStringList("SCOREBOARD.LINES.STAFF.DEFAULT")) {
                String chat = null;
                String vanish = null;
                if (Lazarus.getInstance().getVanishManager().isVanished(player)) {
                    vanish = "&aEnabled";
                } else {
                    vanish = "&cDisabled";
                }
                if (Lazarus.getInstance().getStaffChatHandler().isStaffChatEnabled(player)) {
                    chat = "&cStaff";
                } else if (faction != null && faction.getMember(player).getChatType().equals(ChatType.FACTION)) {
                    chat = "&aFaction";
                } else {
                    chat = "&fGlobal";
                }
                toReturn.add(ColorUtil.CC(s
                        .replace("%line%", line)
                        .replace("%player%", player.getName())
                        .replace("%online%", String.valueOf(Bukkit.getOnlinePlayers().size()))
                        .replace("%chat%", chat)
                        .replace("%vanish%", vanish)
                ));
            }

            // KOTH
            if (koths != null) {
                kothit = koths.iterator();

                while (kothit.hasNext()) {
                    koth = kothit.next();
                    for (String s : ConfigUtil.getConfig().getStringList("SCOREBOARD.LINES.STAFF.KOTH")) {
                        if (koth.getCapzone().hasNoPlayers()) {
                            toReturn.add(ColorUtil.CC(s
                                    .replace("%line%", line)
                                    .replace("%koth_name%", koth.getKothData().getName())
                                    .replace("%koth_capper%", "&cN/A")
                                    .replace("%koth_time%", koth.getCapzone().getTimeLeft())
                                    .replace("%koth_boost%", "&cN/A")

                            ));
                        } else if (koth.getCapzone().getCapper().getName() != null) {
                            toReturn.add(ColorUtil.CC(s
                                    .replace("%line%", line)
                                    .replace("%koth_name%", koth.getKothData().getName())
                                    .replace("%koth_capper%", koth.getCapzone().getCapper().getName())
                                    .replace("%koth_time%", koth.getCapzone().getTimeLeft())
                                    .replace("%koth_boost%",
                                            Scoreboard.getInstance().isKothBoostEnabled() ? "&a" + KothBoostAPI.getStringKothBoostByPlayer(player) : "&cN/A")

                            ));
                        }
                    }
                }
            }

            // DTC

            if (Lazarus.getInstance().getDtcManager().isActive()) {
                DtcData data = Lazarus.getInstance().getDtcManager().getDtcData();
                for (String s : ConfigUtil.getConfig().getStringList("SCOREBOARD.LINES.STAFF.DTC")) {
                    toReturn.add(ColorUtil.CC(s
                            .replace("%line%", line)
                            .replace("%dtc_breaks%", String.valueOf(data.getBreaksLeft()))
                            .replace("%dtc_location%", data.getLocation().getBlockX() + ", " + data.getLocation().getBlockZ())
                    ));

                }
            }

        } else if (!isActiveTimer(player) && !koths.iterator().hasNext() && !Lazarus.getInstance().getDtcManager().isActive()) {
            for (String s : ConfigUtil.getConfig().getStringList("SCOREBOARD.LINES.PLAYER.NO_TIMER")) {
                toReturn.add(ColorUtil.CC(s
                        .replace("%line%", line)
                        .replace("%claim%", factionAt.getDisplayName(player))
                        .replace("%kills%", String.valueOf(Lazarus.getInstance().getUserdataManager().getUserdata(player).getKills()))
                        .replace("%deaths%", String.valueOf(Lazarus.getInstance().getUserdataManager().getUserdata(player).getDeaths()))
                        .replace("%balance%", String.valueOf(Lazarus.getInstance().getUserdataManager().getUserdata(player).getBalance()))
                        .replace("%online%", String.valueOf(Bukkit.getOnlinePlayers().size()))
                        .replace("%player%", player.getName())
                ));

            }
        } else {
            toReturn.add(ColorUtil.CC("%line%").replace("%line%", line));
            if (isActiveTimer(player)) {
                final List<String> timers = new ArrayList<>();
                if (reboot.isRebooting()) {
                    timers.add(ConfigUtil.getConfig().getString("SCOREBOARD.LINES.PLAYER.TIMER_ACTIVE.TIMERS.REBOOT").replace("%time%", reboot.getScoreboardEntry()));
                }

                if (TimerManager.getInstance().getSotwTimer().isActive()) {
                    timers.add(ConfigUtil.getConfig().getString("SCOREBOARD.LINES.PLAYER.TIMER_ACTIVE.TIMERS.SOTW").replace("%time%", TimerManager.getInstance().getSotwTimer().getDynamicTimeLeft()));
                }

                if (TimerManager.getInstance().getEotwTimer().isActive()) {
                    timers.add(ConfigUtil.getConfig().getString("SCOREBOARD.LINES.PLAYER.TIMER_ACTIVE.TIMERS.EOTW").replace("%time%", TimerManager.getInstance().getEotwTimer().getDynamicTimeLeft()));
                }


                if (TimerManager.getInstance().getPvpProtTimer().isActive(player)) {
                    timers.add(ConfigUtil.getConfig().getString("SCOREBOARD.LINES.PLAYER.TIMER_ACTIVE.TIMERS.PVPTIMER").replace("%time%", TimerManager.getInstance().getPvpProtTimer().getTimeLeft(player)));
                }

                if (TimerManager.getInstance().getEnderPearlTimer().isActive(player)) {
                    timers.add(ConfigUtil.getConfig().getString("SCOREBOARD.LINES.PLAYER.TIMER_ACTIVE.TIMERS.ENDERPEARL").replace("%time%", TimerManager.getInstance().getEnderPearlTimer().getTimeLeft(player) + "s"));
                }

                if (TimerManager.getInstance().getCombatTagTimer().isActive(player)) {
                    timers.add(ConfigUtil.getConfig().getString("SCOREBOARD.LINES.PLAYER.TIMER_ACTIVE.TIMERS.COMBAT").replace("%time%", TimerManager.getInstance().getCombatTagTimer().getTimeLeft(player) + "s"));
                }

                if (TimerManager.getInstance().getTeleportTimer().isActive(player)) {
                    timers.add(ConfigUtil.getConfig().getString("SCOREBOARD.LINES.PLAYER.TIMER_ACTIVE.TIMERS.SPAWN").replace("%time%", TimerManager.getInstance().getTeleportTimer().getTimeLeft(player) + "s"));
                }

                if (TimerManager.getInstance().getHomeTimer().isActive(player)) {
                    timers.add(ConfigUtil.getConfig().getString("SCOREBOARD.LINES.PLAYER.TIMER_ACTIVE.TIMERS.HOME").replace("%time%", TimerManager.getInstance().getHomeTimer().getTimeLeft(player) + "s"));
                }

                if (TimerManager.getInstance().getLogoutTimer().isActive(player)) {
                    timers.add(ConfigUtil.getConfig().getString("SCOREBOARD.LINES.PLAYER.TIMER_ACTIVE.TIMERS.LOGOUT").replace("%time%", TimerManager.getInstance().getLogoutTimer().getTimeLeft(player) + "s"));
                }

                if (TimerManager.getInstance().getStuckTimer().isActive(player)) {
                    timers.add(ConfigUtil.getConfig().getString("SCOREBOARD.LINES.PLAYER.TIMER_ACTIVE.TIMERS.STUCK").replace("%time%", TimerManager.getInstance().getStuckTimer().getTimeLeft(player) + "s"));
                }

                for (String s : ConfigUtil.getConfig().getStringList("SCOREBOARD.LINES.PLAYER.TIMER_ACTIVE.DEFAULT")) {
                    toReturn.add(ColorUtil.CC(s));
                }
                timers.forEach(s1 -> toReturn.add(ColorUtil.CC(s1)));
            }

            if (koths.iterator().hasNext()) {
                kothit = koths.iterator();

                while (kothit.hasNext()) {
                    if (isActiveTimer(player)) toReturn.add(ColorUtil.CC("%line%").replace("%line%", line));
                    koth = kothit.next();
                    for (String s : ConfigUtil.getConfig().getStringList("SCOREBOARD.LINES.PLAYER.TIMER_ACTIVE.KOTH")) {
                        if (koth.getCapzone().hasNoPlayers()) {
                            toReturn.add(ColorUtil.CC(s
                                    .replace("%koth_name%", koth.getKothData().getName())
                                    .replace("%koth_time%", koth.getCapzone().getTimeLeft())
                                    .replace("%koth_boost%", Scoreboard.getInstance().isKothBoostEnabled() ? "&a" + KothBoostAPI.getStringKothBoostByPlayer(player) : "&cN/A")

                            ));
                        } else if (koth.getCapzone().getCapper().getName() != null) {
                            toReturn.add(ColorUtil.CC(s
                                    .replace("%koth_name%", koth.getKothData().getName())
                                    .replace("%koth_time%", koth.getCapzone().getTimeLeft())
                                    .replace("%koth_boost%",
                                            Scoreboard.getInstance().isKothBoostEnabled() ? "&a" + KothBoostAPI.getStringKothBoostByPlayer(player) + "&7 | &c" + KothBoostAPI.getStringKothBoostByPlayer(koth.getCapzone().getCapper().getPlayer()) : "&cN/A")

                            ));
                        }
                    }
                }
            }

            if (Lazarus.getInstance().getDtcManager().isActive()) {
                if (isActiveTimer(player) || koths.iterator().hasNext()) toReturn.add(ColorUtil.CC("%line%").replace("%line%", line));
                DtcData data = Lazarus.getInstance().getDtcManager().getDtcData();
                for (String s : ConfigUtil.getConfig().getStringList("SCOREBOARD.LINES.PLAYER.TIMER_ACTIVE.DTC")) {
                    toReturn.add(ColorUtil.CC(s
                            .replace("%dtc_breaks%", String.valueOf(data.getBreaksLeft()))
                            .replace("%dtc_location%", data.getLocation().getBlockX() + ", " + data.getLocation().getBlockZ())
                    ));

                }
            }
            toReturn.add(ColorUtil.CC("%line%").replace("%line%", line));
        }


        return toReturn;
    }

    private boolean isActiveTimer(Player player) {
        return TimerManager.getInstance().getEnderPearlTimer().isActive(player) || TimerManager.getInstance().getCombatTagTimer().isActive(player) || TimerManager.getInstance().getLogoutTimer().isActive(player) || TimerManager.getInstance().getHomeTimer().isActive(player) || TimerManager.getInstance().getTeleportTimer().isActive(player) || TimerManager.getInstance().getPvpProtTimer().isActive(player) || TimerManager.getInstance().getSotwTimer().isActive() || TimerManager.getInstance().getEotwTimer().isActive() || Lazarus.getInstance().getRebootHandler().isRebooting() || TimerManager.getInstance().getStuckTimer().isActive(player);
    }

}
