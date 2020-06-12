package com.wfm.rpc.server;

import com.wfm.rpc.common.RpcRequest;
import com.wfm.rpc.common.RpcResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 处理具体的业务调用
 * 通过构造时传入的 “业务接口及实现” handlerMap ,来调用客户端所请求的业务方法
 * 并将业务方法返回值封装成response对象写入下一个handler（即编码handler RPCEncoder）
 */
public class RpcHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcHandler.class);

    private final Map<String, Object> handlerMap;

    public RpcHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    /**
     * 接收消息，处理消息，返回结果
     *
     * @param ctx
     * @param rpcRequest
     * @throws Exception
     */
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest rpcRequest) throws Exception {
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(rpcRequest.getRequestId());
        LOGGER.error("收到客户端消息： " + rpcRequest.getRequestId());
        try {
            //根据request 来处理具体的业务调用
            Object result = handle(rpcRequest);
            rpcResponse.setResult(result);
        } catch (Throwable t) {
            LOGGER.error("服务端出错了 ...", t);
            rpcResponse.setError(t);
        }

        //写入 outboundle (即RpcEncoder) 进行下一步处理（即编码）后发送到channel中给客户端
        ctx.writeAndFlush(rpcResponse).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 根据 request 来处理具体的业务调用
     * 调用是通过反射的方式来完成的
     *
     * @param rpcRequest
     * @return
     */
    private Object handle(RpcRequest rpcRequest) throws Throwable {
        String className = rpcRequest.getClassName();

        //拿到实现类对象
        Object serviceBean = handlerMap.get(className);

        //拿到要调用的方法名、参数类型、参数值
        String methodName = rpcRequest.getMethodName();
        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
        Object[] parameters = rpcRequest.getParameters();

        //拿到接口类
        Class<?> forName = Class.forName(className);

        //调用实现类对象的指定方法并返回结果
        Method method = forName.getMethod(methodName, parameterTypes);
        return method.invoke(serviceBean, parameters);
    }
}
