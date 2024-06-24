package cn.lran.plugin.AutoMuted;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import cn.nukkit.utils.TextFormat;

import java.util.Arrays;

import static cn.lran.plugin.AutoMuted.Main.ChatCount;
import static cn.lran.plugin.AutoMuted.Main.MutedEndTime;

public class EventListener implements Listener {

    public EventListener(Main plugin) {
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        handleChat(event.getPlayer(), event.getMessage(), event);
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage().toLowerCase();
        if (message.startsWith("/msg") || message.startsWith("/w") || message.startsWith("/tell") || message.startsWith("/me")) {
            handleChat(event.getPlayer(), event.getMessage(), event);
        }
    }

    private void handleChat(Player player, String message, Cancellable event) {
        String playerName = player.getName();
        if (MutedEndTime.containsKey(playerName) && System.currentTimeMillis() < MutedEndTime.get(playerName)) {
            player.sendMessage(TextFormat.RED + "你已被禁言，请等待 " + (MutedEndTime.get(playerName) - System.currentTimeMillis()) / 1000 + " 秒后再试。");
            event.setCancelled(true);
            return;
        }

        Long lastMessageTime = MutedEndTime.getOrDefault(playerName, System.currentTimeMillis());
        if (System.currentTimeMillis() - lastMessageTime > 5000) {
            ChatCount.put(playerName, 0);
        }

        ChatCount.put(playerName, ChatCount.getOrDefault(playerName, 0) + 1);
        MutedEndTime.put(playerName, System.currentTimeMillis());

        if (ChatCount.get(playerName) >= 6) {
            MutedEndTime.put(playerName, System.currentTimeMillis() + 10000);
            player.sendMessage(TextFormat.RED + "因为你在五秒内发言过多，已被禁言 10 秒。");
            ChatCount.remove(playerName);
        }
    }
}