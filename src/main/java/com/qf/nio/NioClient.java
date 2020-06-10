package com.qf.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * @author ChenJie
 * @date 2020-06-09 21:21:21
 * 功能说明
 */
public class NioClient {
    public static void main(String[] args) throws IOException {
        // 创建一个socketChannel
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",8080));

        // 客户端可以使用阻塞式编程(默认就为true)
        socketChannel.configureBlocking(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 10);
                    try {
                        int len = socketChannel.read(byteBuffer);
                        // 打印服务器的数据
                        byteBuffer.flip();
                        byte[] bytes = byteBuffer.array();
                        System.out.println("获得服务器的内容为：" + new String(bytes,0,len));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


        while (true){
            System.out.println("输入群发消息：");
            Scanner scanner = new Scanner(System.in);
            String content = scanner.next();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 10);
            byteBuffer.put(content.getBytes());
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
        }

    }
}
