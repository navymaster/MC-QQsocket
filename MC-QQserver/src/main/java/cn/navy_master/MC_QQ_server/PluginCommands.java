package cn.navy_master.MC_QQ_server;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.annotation.processing.SupportedAnnotationTypes;

public class PluginCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(command.getName().equals("bc")) {
            if (strings.length == 0) {
                commandSender.sendMessage("你什么也没说");
            } else {
                push_wait_stack("<" + commandSender.getName() + "> :" + strings[0]);
                commandSender.getServer().broadcastMessage("<" + commandSender.getName() + "> :" + strings[0]);
            }
        }else if(command.getName().equals("setQQ")){
            if(commandSender.isOp()){
                ServerMain.opList.set(commandSender.getName(),Long.parseLong(strings[0]));
            }
        }
        return true;
    }
    public void push_wait_stack(String s){
        ServerThreadManager.add_to_pool(s);
    }
}
