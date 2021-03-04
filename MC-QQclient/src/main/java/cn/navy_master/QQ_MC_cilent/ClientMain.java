package cn.navy_master.QQ_MC_cilent;

import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import org.jetbrains.annotations.NotNull;

public class ClientMain extends JavaPlugin {
     public static final ClientMain INSTANCE = new ClientMain();

     private ClientMain(){
        super(new JvmPluginDescriptionBuilder("cn.navy_master.QQ-to-MC-tube","0.0.1")
                .author("navy_master")
                .info("用于与配套的mc服务器插件进行本地通信，使qq群可以和mc服务器互发消息")
        .build());
     }
    @Override
    public void onDisable() {
         ClientThread.INSTANCE.close();
        getLogger().info("QQ-to-MC-tube quiting...");
    }

    @Override
    public void onEnable() {
         GlobalEventChannel.INSTANCE.registerListenerHost(new MessageEventHandle());
    }

    @Override
    public void onLoad(@NotNull PluginComponentStorage $this$onLoad) {
        getLogger().info("QQ-to-MC-tube loading...");
    }
    public void log(String s){
         getLogger().info(s);
    }
}
