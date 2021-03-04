package cn.navy_master.QQ_MC_cilent;

import net.mamoe.mirai.Bot;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Stack;

public class ClientThread extends Thread {
    public static Stack<String> waiting_for_send =new Stack<>();
    public static ClientThread INSTANCE;
    public static boolean connected=false;
    Socket client;
    SocketAddress sa;
    long botId;
    long groupId;

    public long getOwner() {
        return owner;
    }

    public void setOwner(long owner) {
        this.owner = owner;
    }

    long owner;

    public ClientThread(String host, int port, long botId, long groupId){
        client= new Socket();
        sa=new InetSocketAddress(host, port);
        this.botId=botId;
        this.groupId=groupId;
    }
    @Override
    public void run() {
        DataInputStream dataIS=null;
        InputStreamReader inSR=null;
        BufferedReader br;
        DataOutputStream dataOS=null ;
        OutputStreamWriter outSW=null;
        BufferedWriter bw;
            try {
                System.out.println("？\n");
                client.connect(sa,1000);
                Bot b=Bot.getInstance(botId);
                Objects.requireNonNull(b.getGroup(groupId)).sendMessage("连接成功");
                long last_heart_beat = System.currentTimeMillis();
                connected=true;
                dataIS = new DataInputStream(client.getInputStream());
                inSR = new InputStreamReader(dataIS, StandardCharsets.UTF_8);
                br = new BufferedReader(inSR);

                dataOS = new DataOutputStream(client.getOutputStream());
                outSW = new OutputStreamWriter(dataOS, StandardCharsets.UTF_8);
                bw = new BufferedWriter(outSW);
                String str;
                while(ClientMain.INSTANCE.isEnabled()&&
                        connected) {
                    if(br.ready()){
                        str = br.readLine();
                        str = str.trim();
                        if(str.startsWith("msg "))
                        {str=str.substring(4);
                        b=Bot.getInstance(botId);
                        Objects.requireNonNull(b.getGroup(groupId)).sendMessage(str);
                        }
                    }
                    if(!waiting_for_send.empty()){
                        Stack<String> ss=clearStack();
                        for(String s: ss)
                        {
                            bw.write(s+" \r\n");
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
            }finally {
                try {
                    outSW.close();
                    inSR.close();
                    dataIS.close();
                    dataOS.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //关闭这些中间产物
                close();//关闭socket
                Bot b=Bot.getInstance(botId);
                Objects.requireNonNull(b.getGroup(groupId)).sendMessage("连接已丢失");
                INSTANCE=null;
            }
            this.stop();
    }
    public void close(){
        try {
            client.close();
            connected=false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private synchronized Stack<String> clearStack(){
        Stack<String> cl=(Stack<String>) waiting_for_send.clone();
        waiting_for_send.clear();
        return cl;
    }

}
