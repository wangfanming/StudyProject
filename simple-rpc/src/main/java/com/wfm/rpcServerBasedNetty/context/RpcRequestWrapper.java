package com.wfm.rpcServerBasedNetty.context;

/**
 * @Auther: wangfanming
 * @Date: 2019/7/23 15:17
 * @Description:
 */
import io.netty.channel.Channel;

/**
 * wrap the RpcRequest and add a Channel field to keep the channel which this request is from
 */
public class RpcRequestWrapper
{
    private final RpcRequest rpcRequest;
    private final Channel channel;

    public RpcRequestWrapper(RpcRequest rpcRequest, Channel channel)
    {
        this.rpcRequest = rpcRequest;
        this.channel = channel;
    }

    public int getId()
    {
        return rpcRequest.getId();
    }

    public String getMethodName()
    {
        return rpcRequest.getMethodName();
    }

    public Object[] getArgs()
    {
        return rpcRequest.getArgs();
    }

    public Channel getChannel()
    {
        return channel;
    }
}
