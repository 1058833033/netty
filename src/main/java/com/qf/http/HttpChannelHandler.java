package com.qf.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

import java.nio.charset.Charset;

/**
 * @author ChenJie
 * @date 2020-06-10 17:35:01
 * 功能说明
 */
public class HttpChannelHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest request) throws Exception {
        System.out.println("接收到数据：" + request);
        System.out.println("请求方法类型：" + request.getMethod());
        System.out.println("请求的URI：" + request.getUri());
        System.out.println("请求体：" + request.content().toString(Charset.forName("utf-8")));
    }
}
