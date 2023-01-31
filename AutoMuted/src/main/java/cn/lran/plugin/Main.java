package cn.lran.plugin;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;

import java.util.HashMap;
import java.util.Map;

public class Main extends PluginBase implements Listener {
    private Map<String, Integer> playerChatCount;
    private Map<String, Long> playerMuteEndTime;


    @Override
    public void onLoad(){
        this.getLogger().info("插件加载");
        //插件加载
    }
    @Override
    public void onEnable(){
        this.getLogger().info("插件启用");
        this.playerChatCount = new HashMap<>();
        this.playerMuteEndTime = new HashMap<>();

        this.getServer().getPluginManager().registerEvents(this, this);
        //插件启用
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        if (playerMuteEndTime.containsKey(playerName) && System.currentTimeMillis() < playerMuteEndTime.get(playerName)) {
            player.sendMessage(TextFormat.RED + "你已被禁言，请等待 " + (playerMuteEndTime.get(playerName) - System.currentTimeMillis()) / 1000 + " 秒后再试。");
            event.setCancelled();
            return;
        }

        Long lastMessageTime = playerMuteEndTime.getOrDefault(playerName, System.currentTimeMillis());
        if (System.currentTimeMillis() - lastMessageTime > 5000) {
            playerChatCount.put(playerName, 0);
        }

        playerChatCount.put(playerName, playerChatCount.getOrDefault(playerName, 0) + 1);
        playerMuteEndTime.put(playerName, System.currentTimeMillis());

        if (playerChatCount.get(playerName) >= 6) {
            playerMuteEndTime.put(playerName, System.currentTimeMillis() + 10000);
            player.sendMessage(TextFormat.RED + "因为你在五秒内发言过多，已被禁言 10 秒。");
            playerChatCount.remove(playerName);
        }
    }

    @Override
    public void onDisable(){
        this.getLogger().info("插件卸载");
        //插件卸载
    }
}
