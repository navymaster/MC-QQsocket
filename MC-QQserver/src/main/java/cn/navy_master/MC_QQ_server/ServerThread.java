package cn.navy_master.MC_QQ_server;

import org.bukkit.Bukkit;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Stack;

public class ServerThread extends Thread {
    Socket client;
    private long last_heart_beat;
    boolean connected=false;
    ServerThread(Socket client){
        this.client=client;
    }
    public Stack<String> waiting_for_send =new Stack<>();
    @Override
    public void run() {
        connected=true;
        System.out.println("已链接");

        InputStreamReader inSR = null;
        OutputStreamWriter outSW = null;
        try {
            //初始化输入输出流使其适配UTF-8格式以支持中文
            inSR = new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(inSR);

            outSW = new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8);
            BufferedWriter bw = new BufferedWriter(outSW);

            String str;
            while(connected) {
                if(br.ready()){//如果收到信息，转发到服务器
                    str = br.readLine();
                    str = str.trim();
                    if(str.startsWith("msg "))
                        Bukkit.broadcastMessage(str.substring(4));
                    if(str.startsWith("cmd ")){
                        ServerThreadManager.runCommand(str.substring(4));
                    }
                    if(str.startsWith("run "))
                    {
                        String s=str.substring(4);
                        String requester = null;
                        for(int i=0;i<s.length();i++)
                        {
                            if(s.charAt(i)==' '){
                                requester=s.substring(0,i);
                                s=s.substring(i+1);
                                break;
                            }
                        }
                        Bukkit.getLogger().info("QQ号为"+requester+"的用户请求执行指令：" + s);
                        if(requester!=null)
                        for(String name: ServerMain.opList.getKeys(false)){
                            if(Objects.equals(ServerMain.opList.getLong(name), Long.parseLong(requester))){
                                ServerMain.executor.push(s);
                            }
                        }
                    }
                }
                if(!waiting_for_send.isEmpty()){//如果等待发送的列表不为空，将其发出
                    Stack<String> ss=clearStack();
                    for(String s: ss)
                    {
                        Bukkit.getLogger().info("发送消息:\n"+s);
                        bw.write("msg "+s+" \r\n");
                    }
                    bw.flush();//发送完后记得flush
                }
                if(System.currentTimeMillis()- last_heart_beat >10000){
                    bw.write("hrt \r\n");
                    bw.flush();
                    last_heart_beat =System.currentTimeMillis();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inSR.close();
                outSW.close();
                client.close();//关闭流和socket
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Bukkit.getLogger().info("已断开");
        this.stop();
    }

    private synchronized Stack<String> clearStack(){
        Stack<String> cl=(Stack<String>) waiting_for_send.clone();
        waiting_for_send.clear();
        return cl;
    }
}
