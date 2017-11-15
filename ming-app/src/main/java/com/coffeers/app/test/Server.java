package com.coffeers.app.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Administrator on 2017/9/19 0019.
 */
public class Server {

    public static void main(String[] args){
        try {
            //创建一个ServerSocket监听8080端口
            ServerSocket server = new ServerSocket(8080);
            //等待监听
            Socket socket = server.accept();
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = br.readLine();
            System.out.print("服务端接收数据：：："+line);
            //创建PrintWriter,用于发送数据
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            pw.print("服务端发送数据：：："+line);
            pw.flush();
            //关闭资源
            pw.close();
            br.close();
            socket.close();
            server.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
