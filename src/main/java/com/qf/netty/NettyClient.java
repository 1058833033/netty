package com.qf.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * @author ChenJie
 * @date 2020-06-10 10:00:41
 * 功能说明
 */
public class NettyClient {
    public static void main(String[] args) {
        // 创建引导对象
        Bootstrap bootstrap = new Bootstrap();

        // 配置线程模型
        bootstrap
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                // 设置服务端消息处理器
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
                        System.out.println("接收到服务端的消息：" + byteBuf.toString(Charset.forName("UTF-8")));
                    }
                });
        // 连接服务器
        ChannelFuture future = bootstrap.connect("127.0.0.1", 8080);
        try {
            future.sync();
            System.out.println("连接服务器成功！");
            Scanner scanner = new Scanner(System.in);
            while (true){
                System.out.println("请输入发送的消息：");
                String content = scanner.next();

                // 发送消息到服务端
                Channel channel = future.channel();// 获取连接的对象
                byte[] bytes = content.getBytes("utf-8");
                ByteBuf byteBuf = Unpooled.buffer(bytes.length);
                byteBuf.writeBytes(bytes);
                channel.writeAndFlush(byteBuf);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
