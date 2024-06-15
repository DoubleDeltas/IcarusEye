package com.doubledeltas.icaruseye.util;

import com.doubledeltas.icaruseye.IcarusEye;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class MessageUtil {
    public static String MSG_PREFIX = "§8[ §fIcarus' §bEye §8]§f ";

    /**
     * 로그 메시지를 보냅니다.
     *
     * @param level 로그 레벨
     * @param msg   메시지
     */
    public static void log(Level level, String msg) {
        IcarusEye.getInstance().getLogger().log(level, msg);
    }

    /**
     * {@link Level#INFO} 레벨의 로그 메시지를 보냅니다.
     *
     * @param msg 메시지
     */
    public static void log(String msg) {
        log(Level.INFO, msg);
    }

    /**
     * 플레이어 또는 콘솔에게 메시지를 보냅니다.
     *
     * @param subject 수신자
     * @param msg     메시지
     */
    public static void send(CommandSender subject, String msg) {
        subject.sendMessage(MSG_PREFIX + msg);
    }

    /**
     * 플레이어의 action bar에 메시지를 보냅니다.
     */
    public static void sendActionBar(Player player, String message) {
        try {
            AudienceProvider audienceProvider = IcarusEye.getInstance().getAudienceProvider();
            Audience audience = audienceProvider.player(player.getUniqueId());

            audience.sendActionBar(Component.text(message));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
