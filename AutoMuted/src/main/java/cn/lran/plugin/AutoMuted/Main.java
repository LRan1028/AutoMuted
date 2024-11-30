package cn.lran.plugin.AutoMuted;

import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;

import java.util.HashMap;
import java.util.Map;

public class Main extends PluginBase implements Listener {
    public static Map<String, Integer> ChatCount;
    public static Map<String, Long> MutedEndTime;
    public static Main plugin;

    @Override
    public void onLoad(){
        this.getLogger().info("插件加载");
        this.getLogger().info("插件作者：LRan");
        //插件加载
    }
    @Override
    public void onEnable(){
        this.getLogger().info("插件启用");
        plugin=this;
        ChatCount = new HashMap<>();
        MutedEndTime = new HashMap<>();
        this.getServer().getPluginManager().registerEvents(new EventListener(this), this);

    }

    @Override
    public void onDisable(){
        this.getLogger().info("插件卸载");
        //插件卸载
    }
}
