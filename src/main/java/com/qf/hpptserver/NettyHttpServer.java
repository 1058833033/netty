package com.qf.hpptserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author ChenJie
 * @date 2020-06-10 19:01:04
 * 功能说明
 */
public class NettyHttpServer {
    public static void main(String[] args) {
        NioEventLoopGroup master = new NioEventLoopGroup();
        NioEventLoopGroup slave = new NioEventLoopGroup();


        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(master,slave)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new HttpServerCodec());
                        pipeline.addLast(new HttpObjectAggregator(1024*1024*10));
                        pipeline.addLast(new HttpRequestChannelHandler());
                    }
                });

        ChannelFuture future = serverBootstrap.bind(80);
        try {
            future.sync();
            System.out.println("HTTP服务器启动成功！");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
