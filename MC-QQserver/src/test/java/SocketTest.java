import java.io.*;
import java.net.Socket;

public class SocketTest {

    public static void main(String[] args) {
        //定义套接字127.0.0.1:2077
        String url = "127.0.0.1";
        int port = 2077;

        Socket s;
        try {
            s=new Socket(url,port);//连接至目标套接字
            //下面六行的作用是让Socket操作可以支持UTF-8编码模式，保证中文正常传输
            DataInputStream dataIS = new DataInputStream(s.getInputStream());
            InputStreamReader inSR = new InputStreamReader(dataIS, "UTF-8");
            BufferedReader br = new BufferedReader(inSR);

            DataOutputStream dataOS = new DataOutputStream(s.getOutputStream());
            OutputStreamWriter outSW = new OutputStreamWriter(dataOS, "UTF-8");
            BufferedWriter bw = new BufferedWriter(outSW);
            //写入后记得flush
            bw.write("(2252479337)水师:嘿，你还好吗？\r\n");
            bw.flush();
            String str;
            while((str = br.readLine()) != null) {
                //检查是否收到新消息，注意BufferedReader的readLine方法会阻断线程直至读到东西，如果需要不会阻断的，请使用ready()
                str = str.trim();//将新消息转换
                System.out.println("服务器回复：" + str);//将读入的消息输出到控制台
                break;
            }
            outSW.close();
            inSR.close();
            dataIS.close();
            dataOS.close();
            //关闭这些中间产物
            //System.out.println(str);
            s.close();//关闭socket
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
