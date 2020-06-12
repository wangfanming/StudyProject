package com.wfm.netty.sendObject.commons;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class PersonDecoder extends ByteToMessageDecoder {  //ChannelInboundHandlerAdapter
    /**
     * 将字节数组解码成消息对象
     *
     * @param ctx
     * @param in
     * @param out
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        byte[] bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);

        Object object = ByteObjConverter.byteToObject(bytes);
        out.add(object);
    }
}
