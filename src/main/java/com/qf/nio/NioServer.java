package com.qf.nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author ChenJie
 * @date 2020-06-09 21:21:11
 * 功能说明
 */
public class NioServer {

    private static List<SocketChannel> socketChannels = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        // 创建NIO服务对象，用来监听端口，客户端连接
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 绑定端口
        serverSocketChannel.bind(new InetSocketAddress(8080));

        // 设置channel通道模式
        //设置为非阻塞模式，所有注册到多路复用器的channel必须为非阻塞模式
        serverSocketChannel.configureBlocking(false);

        // 创建多路复用器
        Selector selector = Selector.open();

        // 将serverSocketChannel注册到多路复用器selector上
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT); //第二个参数设置为"等待客户端"的链接事件

        // 轮询多路复用器
        while (true){
            // 调用多路复用器，看是否有准备好的事件
            int select = selector.select();
            if (select > 0){
                // 多路服务器上有事件发生，获取多路复用器上的事件  - selectionKeys 包装(事件类型，channel)
                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                // 循环处理事件
                // 循环的时候操作集合，new一个新的集合，删除的时候从旧的集合删除
                for (SelectionKey selectionKey : new HashSet<>(selectionKeys)) {

                    // 处理完一个事件必须从集合中移除
                    selectionKeys.remove(selectionKey);

                    // 依次处理事件
                    if (selectionKey.isAcceptable()){

                        // 说明当前有客户端连接了服务器
                        System.out.println("有一个客户端连接了服务器！");
                        ServerSocketChannel channel = (ServerSocketChannel)selectionKey.channel();

                        // 客户端连接
                        SocketChannel socket = channel.accept();

                        // 连接对象设置为非阻塞模式
                        socket.configureBlocking(false);

                        // 注册链接对象到多路复用器上，设置事件为读   什么时候客户端发消息不知道，轮询到就处理
                        socket.register(selector, SelectionKey.OP_READ);

                        // 统一管理socketChannel对象
                        socketChannels.add(socket);
                    }else if (selectionKey.isReadable()){
                        // 有客户端给我发送了消息
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                        // 获取客户端发来的消息
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 10);
                        socketChannel.read(byteBuffer);
                        
                        // 循环所有socketChannel对象
                        for (SocketChannel channel : socketChannels) {
                            if (channel != socketChannel){
                                byteBuffer.flip();
                                channel.write(byteBuffer);
                            }
                        }
                    }
                }
            }
        }

    }
}
