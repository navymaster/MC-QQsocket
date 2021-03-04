package cn.navy_master.MC_QQ_server;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class ServerMain extends JavaPlugin {
    public static ReceivedCommandExecutor executor;
    public static ConfigurationSection opList;
    public static int port=2077;
    @Override
    public void onEnable() {
        saveDefaultConfig();
        if(getConfig().contains("opQQ")){
            opList= getConfig().getConfigurationSection("opQQ");
        }else{
            opList= getConfig().createSection("opQQ");
        }
        if(getConfig().contains("port")){
            port=getConfig().getInt("port");
        }else{
            getConfig().set("port",2077);
        }
        PluginCommands qs=new PluginCommands();
        Bukkit.getPluginManager().registerEvents(new MessageListener(),this);
        Objects.requireNonNull(Bukkit.getPluginCommand("bc")).setExecutor(qs);
        Objects.requireNonNull(Bukkit.getPluginCommand("setQQ")).setExecutor(qs);
        ServerThreadManager s = new ServerThreadManager(this);
        s.start();
        executor=new ReceivedCommandExecutor();
        executor.runTaskTimer(this,20,1);
    }

    @Override
    public void onDisable() {
        saveConfig();
    }
}
