package com.wfm.netty.sendorder.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.Date;

public class EchoServerInHandler1 extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("EchoServerInHandler1 -> server 读取数据 ... ...");
//        //读取数据
//        ByteBuf buf = (ByteBuf) msg;
//
//        byte[] req = new byte[buf.readableBytes()];
//        buf.readBytes(req);
//        String body = new String(req, "UTF-8");
//
//        //向客户端写数据
//        String currentTime = new Date(System.currentTimeMillis()).toString();
//        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());

        //通知下一个InboundHandler
        ctx.fireChannelRead(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("EchoServerInHandler1 -> server 数据读取完毕 ...");
        ctx.flush(); //刷新后才将数据发送到SocketChannel
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
