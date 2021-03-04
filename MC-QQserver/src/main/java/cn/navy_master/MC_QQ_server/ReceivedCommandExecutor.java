package cn.navy_master.MC_QQ_server;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Stack;

public class ReceivedCommandExecutor extends BukkitRunnable {
    Stack<String> cmd=new Stack<>();
    @Override
    public void run() {
        if(!cmd.empty()){
            Stack<String> copy=clear();
            for(String s:copy)
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),s);
        }
    }
    private Stack<String> clear(){
        Stack<String> copy=cmd;
        cmd=new Stack<>();
        return copy;
    }
    public synchronized void push(String s){
        cmd.push(s);
    }
}
