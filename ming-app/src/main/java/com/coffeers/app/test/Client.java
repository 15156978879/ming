package com.coffeers.app.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Administrator on 2017/9/19 0019.
 */
public class Client {

    public static void main(String[] args){
        String msg = "啊啊啊啊啊啊啊啊啊啊啊啊啊啊";
        try {
            //创建一个socket跟本机8080端口连接
            Socket socket = new Socket("127.0.0.1",8080);
            //创建PrintWriter和BufferedReader进行数据读写
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //发送数据
            pw.println(msg);
            System.out.print("客户端发送数据：：："+msg);
            pw.flush();
            //接收数据
            String line = br.readLine();
            System.out.print("客户端接收数据：：："+line);
            //关闭资源
            pw.close();
            br.close();
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
