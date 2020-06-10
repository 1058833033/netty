package com.qf.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author ChenJie
 * @date 2020-06-09 10:42:58
 * 功能说明
 */
public class MySocketClient {
    public static void main(String[] args) throws IOException {
        /*Socket socket = new Socket("127.0.0.1",8080);

        // 建立连接
        System.out.println("建立连接成功");

        // 给服务器发消息
        String str = "服务器你好啊";
        OutputStream out = socket.getOutputStream();
        out.write(str.getBytes("utf-8"));
        out.flush();

        // 获取服务器的响应
        InputStream in = socket.getInputStream();
        byte[] bytes = new byte[1024 * 10];
        int len = in.read(bytes);
        System.out.println(new String(bytes,0,len));

        socket.close();*/

        Socket socket = new Socket("127.0.0.1",8080);
        // 建立连接
        System.out.println("建立连接成功");

        // 开启子线程接收消息
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream in = null;
                try {
                    in = socket.getInputStream();
                    byte[] bytes = new byte[1024 * 10];
                    int len = in.read(bytes);
                    System.out.println("客户端收到消息：" + new String(bytes,0,len));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        while (true){
            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入消息：");
            String content = scanner.next();

            // 给服务器发送消息
            OutputStream out = socket.getOutputStream();
            out.write(content.getBytes("utf-8"));
            out.flush();
        }

    }
}
