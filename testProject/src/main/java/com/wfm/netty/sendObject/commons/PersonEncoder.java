package com.wfm.netty.sendObject.commons;

import com.wfm.netty.sendObject.entity.Person;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PersonEncoder extends MessageToByteEncoder<Person> { // ChannelOutboundHandlerAdapter

    /**
     * 对消息对象进行编码
     * @param ctx
     * @param msg
     * @param out
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Person msg, ByteBuf out) throws Exception {
        byte[] bytes = ByteObjConverter.objectToByte(msg);
        out.writeBytes(bytes);
        ctx.flush();
    }
}
