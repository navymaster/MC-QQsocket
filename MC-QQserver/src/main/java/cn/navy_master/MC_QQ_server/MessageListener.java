package cn.navy_master.MC_QQ_server;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MessageListener implements Listener {
    @EventHandler
    public void handle(PlayerJoinEvent e){
        String msg=e.getJoinMessage();
        if (msg != null) {
            ServerThreadManager.add_to_pool(msg.substring(2));
        }
    }
    @EventHandler
    public void handle(PlayerDeathEvent e){
        String msg=e.getDeathMessage();
        ServerThreadManager.add_to_pool(msg);
    }
    @EventHandler
    public void handle(PlayerQuitEvent e){
        String msg=e.getQuitMessage();
        if (msg != null) {
            ServerThreadManager.add_to_pool(msg.substring(2));
        }
    }
}
