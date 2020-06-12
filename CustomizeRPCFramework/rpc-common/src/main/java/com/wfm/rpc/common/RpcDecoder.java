package com.wfm.rpc.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class RpcDecoder extends ByteToMessageDecoder {

    private Class<?> genericClass;

    //构造函数传入反序列化的class
    public RpcDecoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() < 4) {
            return;
        }
        byteBuf.markReaderIndex();
        int dataLength = byteBuf.readInt();
        if (dataLength < 0) {
            ctx.close();
        }
        if (byteBuf.readableBytes() < dataLength) {
            byteBuf.resetReaderIndex();
        }

        //将ByteBuf转换成byte[]
        byte[] bytes = new byte[dataLength];
        byteBuf.readBytes(bytes);
        //将data转换成object
        Object obj = SerializationUtil.deserialize(bytes, genericClass);

        list.add(obj);
    }
}
