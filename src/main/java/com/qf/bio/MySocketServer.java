package com.qf.bio;


import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ChenJie
 * @date 2020-06-09 10:25:28
 * 功能说明
 */
public class MySocketServer {
    public static void main(String[] args) throws IOException {
        /*ServerSocket serverSocket = new ServerSocket(8080);
        // 等待客户端连接，阻塞式的方法，一直停在这里
        Socket socket = serverSocket.accept();
        System.out.println("收到了一个消息");

        InputStream in = socket.getInputStream();
        byte[] bytes = new byte[1024 * 10];

        // read方法是一个阻塞方法，客户端不发消息不会往下运行
        int len = in.read(bytes);

        System.out.println(new String(bytes,0,len));

        // 浏览器就收到了但是显示不出，需要协议
        OutputStream out = socket.getOutputStream();
        String str = "<html>hello word</html>";
        out.write(str.getBytes("utf-8"));
        out.flush();*/


        ServerSocket serverSocket = new ServerSocket(8080);
        List<Socket> sockets = new ArrayList<>();
        while (true){
            final Socket socket = serverSocket.accept();
            sockets.add(socket);
            System.out.println("有客户端连接上来了！");

            // 开启子线程接收消息
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (1 == 1){
                        InputStream in = null;
                        try {
                            in = socket.getInputStream();
                            byte[] bytes = new byte[1024 * 10];
                            int len = in.read(bytes);
                            String str = new String(bytes, 0, len);
                            System.out.println("服务端收到消息：" + str);

                            // 转发给其他对象
                            for (Socket s : sockets) {
                                if(s != socket){
                                    // 转发给其他人，不发给自己
                                    s.getOutputStream().write(str.getBytes("utf-8"));
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }


    }
}
