package com.wfm.rpc.server;

/**
 * @Auther: wangfanming
 * @Date: 2019/7/23 14:48
 * @Description:
 */
import com.wfm.rpc.context.RpcRequest;
import com.wfm.rpc.context.RpcRequestWrapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class RpcServerDispatchHandler extends ChannelInboundHandlerAdapter
{
    private RpcServerRequestHandler rpcServerRequestHandler;

    public RpcServerDispatchHandler(
            RpcServerRequestHandler rpcServerRequestHandler)
    {
        this.rpcServerRequestHandler = rpcServerRequestHandler;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception
    {
        RpcRequest rpcRequest = (RpcRequest)msg;
        RpcRequestWrapper rpcRequestWrapper = new RpcRequestWrapper(rpcRequest, ctx.channel());

        rpcServerRequestHandler.addRequest(rpcRequestWrapper);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception
    {

    }
}
