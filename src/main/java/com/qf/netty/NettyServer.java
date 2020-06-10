package com.qf.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author ChenJie
 * @date 2020-06-10 09:36:53
 * 功能说明
 */
public class NettyServer {
    public static void main(String[] args){
        // 创建主从线程池
        EventLoopGroup master = new NioEventLoopGroup();
        EventLoopGroup slave = new NioEventLoopGroup();

        // 创建服务器引导对象serverBootstrap
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        // 配置引导对象
        // 设置netty的线程模型   单线程、多线程（线程池）、主从线程模型（主线程池，从线程池）
        serverBootstrap
                .group(master,slave)
                .channel(NioServerSocketChannel.class)

                // 事件处理器 重要
                .childHandler(new ChannelInitializer() {

                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        // 事件处理器链
                        ChannelPipeline pipeline = channel.pipeline();
                        // 可以进行多次添加   一般只用addLast好区分顺序
                        pipeline.addLast(new ServerChannelHandler());
                        /*pipeline.addLast(new ServerChannelHandler());
                        pipeline.addLast(new ServerChannelHandler());
                        pipeline.addLast(new ServerChannelHandler());*/
                    }
                });

        // 绑定端口
        ChannelFuture future = serverBootstrap.bind(8080);// 绑定这个方法是异步的动作，这里还未绑定可能就打印了结果
        try {
            // 同步阻塞  完成了再往下
            future.sync();
            System.out.println("端口绑定完成，服务器启动");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
