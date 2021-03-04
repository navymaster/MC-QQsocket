package cn.navy_master.MC_QQ_server;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ServerThreadManager implements Runnable {
    public static List<ServerThread> pool=new ArrayList<>();
    public static synchronized void add_to_pool(String s){
        //Bukkit.getLogger().info("当前连接池大小："+pool.size());
        pool.removeIf(st -> !st.isAlive());
        for(ServerThread st:pool){
            if(st.isAlive()){
                st.waiting_for_send.push(s);
            }
        }
    }
    public void start(){
        Thread t=new Thread(this,"qq-socket-mc");
        t.start();
    }
    ServerThreadManager(JavaPlugin p){
        INSTANCE=p;
    }
    JavaPlugin INSTANCE;
    ServerSocket QQStream;
    private void init(){
        try {
            QQStream= new ServerSocket(ServerMain.port);
            Bukkit.getLogger().info("Server for mirai QQ bot has started at port "+ ServerMain.port);
        }catch (IOException e){
            INSTANCE.onDisable();
            Bukkit.getLogger().info("端口打开失败，插件已关闭");
        }
    }
    @Override
    public void run(){
        init();
        while(INSTANCE.isEnabled()){
            Socket tar;
            try{
                tar=QQStream.accept();
                ServerThread ST=new ServerThread(tar);
                ST.start();
                pool.add(ST);
                //连接建立成功，进行服务
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            QQStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void runCommand(String cmd){
        //Bukkit.getLogger().info("指令为"+cmd);
        if(cmd.startsWith("list")){
            Collection<?extends Player> cp= Bukkit.getServer().getOnlinePlayers();
            StringBuilder msg= new StringBuilder("服务器现在有" + cp.size() + "人,分别是：");
            for(Player p:cp)
            {
                msg.append(p.getName());
            }
            ServerThreadManager.add_to_pool(msg.toString());
        }
    }
}
