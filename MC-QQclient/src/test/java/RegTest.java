import cn.navy_master.QQ_MC_cilent.ClientThread;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegTest {
    public static void main(String[] args) throws InterruptedException {
        String reg = "^\\.([a-z]+)(.+)?";
        Pattern p = Pattern.compile(reg);
        String msg;
        msg =".connect 127.0.0.1:2080";
        //MCsocket.INSTANCE.log(msg);
        Matcher m = p.matcher(msg);
        if (m.find()) {
            if(m.group(1).equals("connect")){
                reg="^\\.([a-z]+) (.+):([0-9]+)";
                p = Pattern.compile(reg);
                m = p.matcher(msg);
                if(m.find()){
                    ClientThread qc=new ClientThread(m.group(2),Integer.parseInt(m.group(3)),1262294066,656379189);
                    qc.start();
                    String send = "run "+"2252479337"+" "+"say hello";
                    //String send="cmd list";
                    ClientThread.waiting_for_send.push(send);
                    TimeUnit.MILLISECONDS.sleep(10000);
                }
            }
            if(m.group(1).equals("bc")){
                msg = m.group(2);
                String send = "msg " + msg;
                System.out.println(send);
            }
            if(m.group(1).equals("players")){
                //MCsocket.INSTANCE.log("发送列表指令");
                String send = "cmd list";
                System.out.println(send);
            }
        }

    }
}
