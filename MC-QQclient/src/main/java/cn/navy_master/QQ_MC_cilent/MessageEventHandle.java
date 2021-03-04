package cn.navy_master.QQ_MC_cilent;

import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageEventHandle extends SimpleListenerHost {
    @EventHandler
    public void handle_message(GroupMessageEvent e){
        if(ClientThread.INSTANCE==null||e.getGroup().getId()==ClientThread.INSTANCE.groupId) {
            String reg = "^\\.([a-z]+)(.+)?";
            Pattern p = Pattern.compile(reg);
            String msg;
            msg = e.getMessage().contentToString();
            Matcher m = p.matcher(msg);
            if (m.find()) {
                if(m.group(1).equals("connect") &&( ClientThread.INSTANCE == null || e.getSender().getId() == ClientThread.INSTANCE.getOwner() ) ){
                    reg="^\\.([a-z]+) (.+):([0-9]+)";
                    p = Pattern.compile(reg);
                    m = p.matcher(msg);
                    if(m.find()){
                        ClientThread.INSTANCE=new ClientThread(m.group(2),Integer.parseInt(m.group(3)),e.getBot().getId(),e.getGroup().getId());
                        ClientThread.INSTANCE.setOwner(e.getSender().getId());
                        ClientThread.INSTANCE.start();
                    }
                }
                if(m.group(1).equals("bc")){
                    msg = m.group(2);
                    String send = "msg (" + e.getSender().getId() + ")" + "<" + e.getSenderName() + "> :" + msg;
                    add_to_send_stack(send);
                }
                if(m.group(1).equals("players")){
                    String send = "cmd list";
                    add_to_send_stack(send);
                }
                if(m.group(1).equals("exe")){
                    String send = "run "+e.getSender().getId()+" "+m.group(2).substring(1);
                    add_to_send_stack(send);
                }
                if(m.group(1).equals("shut")&&ClientThread.INSTANCE!=null&&e.getSender().getId()==ClientThread.INSTANCE.getOwner()){
                    ClientThread.INSTANCE.close();
                    ClientThread.INSTANCE.interrupt();
                    ClientThread.INSTANCE=null;
                }
            }
        }
    }
    public synchronized void add_to_send_stack(String s){
        ClientThread.waiting_for_send.push(s);
    }
}
