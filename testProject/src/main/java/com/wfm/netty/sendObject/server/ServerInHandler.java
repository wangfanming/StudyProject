package com.wfm.netty.sendObject.server;

import com.wfm.netty.sendObject.entity.Person;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerInHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Person person = (Person) msg;
        System.out.println("服务端接收到消息： " + person);
        ctx.channel().writeAndFlush(person);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
