package com.wfm.netty.sendorder.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    //客户端连接服务器后被调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端连接服务器，开始发送数据 ... ...");
        byte[] req = "QUERY Time Order".getBytes();
        ByteBuf firstMessage = Unpooled.buffer(req.length);
        firstMessage.writeBytes(req);

        ctx.writeAndFlush(firstMessage);
    }

    //从服务器接收到数据后调用
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println("channel 读取 Server 数据 ...");
        //服务端返回消息后
        byte[] buf = new byte[msg.readableBytes()];
        msg.readBytes(buf);
        String body = new String(buf, "UTF-8");
        System.out.println("服务端数据为 ：" + body);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("客户端发生异常 ...");
        ctx.close();
    }


}
