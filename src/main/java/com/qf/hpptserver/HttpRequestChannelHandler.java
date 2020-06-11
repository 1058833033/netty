package com.qf.hpptserver;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * @author ChenJie
 * @date 2020-06-10 19:08:58
 * 功能说明
 */
public class HttpRequestChannelHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private String path = "D:\\";
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        // 处理请求
        HttpMethod method = request.getMethod();

        if (!method.name().equalsIgnoreCase("GET")){
            // 只处理get请求
            setError(ctx,"请发送get请求！");
            return;
        }

        // 处理uri
        String uri = request.uri();
        System.out.println("当前的访问路径：" + uri);
        String urlPath = path + uri;

        // 获得对应的路径
        File file = new File(path,uri);
        if (!file.exists()){
            setError(ctx,"当前访问的路径不存在！");
            return;
        }
        System.out.println("访问的最终路径为：" + file.getAbsolutePath());

        if(file.isDirectory()){
            // 按照路径处理
            directHandler(ctx, uri,file);
        }else{
            // 按照文件处理
        }
    }

    private void setError(ChannelHandlerContext ctx,String error){
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.BAD_REQUEST);
        // 告诉客户端响应体是什么东西
        response.headers().add("ContentType","text/html;charset=utf-8");
        try {
            // 设置响应体的内容
            response.content().writeBytes(("<html><head><meta charset=\"UTF-8\"><body>"+ error + "</head></body></html>").getBytes("utf-8"));
            ctx.writeAndFlush(response);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            ctx.close();
        }
    }

    // 处理路径
    private void directHandler(ChannelHandlerContext ctx, String uri, File file){
        String html = "<html><head><meta charset=\"UTF-8\"><body><ul>";
        File[] files = file.listFiles();
        for (File f : files) {
            html += "<li><a href='"+uri+"/"+f.getName()+"'>" + f.getName() +"</a></li>";
        }
        html += "</ul></body></html>";

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK);
        // 告诉客户端响应体是什么东西
        response.headers().add("ContentType","text/html;charset=utf-8");
        try {
            // 设置响应体的内容
            response.content().writeBytes(html.getBytes("utf-8"));
            ctx.writeAndFlush(response);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            ctx.close();
        }
    }
}
