package com.wfm.netty.sendObject.client;

import com.wfm.netty.sendObject.entity.Person;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class EchoClientHandler extends SimpleChannelInboundHandler<Object> {

    //客户端连接服务器后被调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端连接服务器，开始发送数据 ... ...");
        super.channelActive(ctx);
    }

    //从服务器接收到数据后调用
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channel 读取 Server 数据 ...");
        //服务端返回消息后

        System.out.println("服务端数据为 ：" + (Person)msg);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("客户端发生异常 ...");
        ctx.close();
    }


}
