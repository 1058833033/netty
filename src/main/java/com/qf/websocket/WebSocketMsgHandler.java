package com.qf.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * @author ChenJie
 * @date 2020-06-10 21:03:48
 * 功能说明
 */
public class WebSocketMsgHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("有一个客户端连接上来了");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("有一个客户端断开了");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame socketFrame) throws Exception {
        String msg = socketFrame.text();
        System.out.println(msg);
        msg = msg.replace("吗","");
        msg = msg.replace("？","");
        ctx.writeAndFlush(new TextWebSocketFrame(msg));
    }
}
