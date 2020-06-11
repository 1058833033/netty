package com.qf.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * @author ChenJie
 * @date 2020-06-10 10:27:20
 * 功能说明
 */
@ChannelHandler.Sharable
public class ServerChannelHandler extends SimpleChannelInboundHandler<ByteBuf>{
    ArrayList<Channel> channels = new ArrayList<>();

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("有一个客户端连接了服务器！");
        channels.add(ctx.channel());
    }

    // 消息处理的方法  有消息发送过来自动调用
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
        System.out.println("接收到客户端的消息：" + byteBuf.toString(Charset.forName("UTF-8")));

        // 将消息发给其他客户端   但是不能直接发送，这个方法一运行完byteBuf就会被回收，需要新的对象进行发送
        for (Channel channel : channels) {
            if (channel != ctx.channel()){
                /*ByteBuf buf = Unpooled.buffer(byteBuf.capacity());
                buf.writeBytes(byteBuf);
                channel.writeAndFlush(buf);*/

                ByteBuf buf = Unpooled.copiedBuffer(byteBuf);
                channel.writeAndFlush(buf);
            }
        }
    }
}

/*@ChannelHandler.Sharable
public class ServerChannelHandler extends SimpleChannelInboundHandler<String>{
    ArrayList<Channel> channels = new ArrayList<>();

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("有一个客户端连接了服务器！");
        channels.add(ctx.channel());
    }

    // 消息处理的方法  有消息发送过来自动调用
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("接收到客户端的消息：" + msg);

        // 将消息发给其他客户端   但是不能直接发送，这个方法一运行完byteBuf就会被回收，需要新的对象进行发送
        for (Channel channel : channels) {
            if (channel != ctx.channel()){
                channel.writeAndFlush(msg);
            }
        }
    }
}*/

