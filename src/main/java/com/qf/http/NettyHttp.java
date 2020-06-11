package com.qf.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;

/**
 * @author ChenJie
 * @date 2020-06-10 17:32:18
 * 功能说明
 */
public class NettyHttp {
    public static void main(String[] args) {
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
                        ChannelPipeline pipeline = channel.pipeline();
                        /*pipeline.addLast(new HttpRequestDecoder());
                        pipeline.addLast(new HttpResponseEncoder());*/

                        // netty服务端HTTP请求的编码解码处理器   - 跟上面的编码与解码效果一样
                        pipeline.addLast(new HttpServerCodec());

                        // http请求的聚合器  通常放到HttpServerCodec编解码器之后，表示对HttpRequest、HttpContent和LastHttpContent结果聚合
                        // 放在这个之后的ChannelHandler只会收到一个FullHttpRequest
                        pipeline.addLast(new HttpObjectAggregator(1024*1024*10));

                        // 自定义的消息处理器 - 处理HTTP请求
                        pipeline.addLast(new HttpChannelHandler());
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
