package com.wfm.netFramework.netty;

/**
 * @Auther: wangfanming
 * @Date: 2019/7/23 14:40
 * @Description:
 */

import com.wfm.kryoserializer.serializer.KryoSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class NettyKryoEncoder extends MessageToByteEncoder<Object>
{
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out)
            throws Exception
    {
        KryoSerializer.serialize(msg, out);
    }
}
