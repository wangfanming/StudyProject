package com.wfm.rpc.client;

import com.wfm.rpc.common.RpcDecoder;
import com.wfm.rpc.common.RpcEncoder;
import com.wfm.rpc.common.RpcRequest;
import com.wfm.rpc.common.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 框架的RPC客户端 （用于发送 RPC 请求）
 */
public class RpcClient extends SimpleChannelInboundHandler<RpcResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcClient.class);

    private String host;
    private int port;

    public RpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private RpcResponse response;

    private final Object obj = new Object();


    /**
     * 连接服务端，发送消息
     *
     * @param request
     * @return
     */
    public RpcResponse send(RpcRequest request) throws Exception {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //向pipeline中添加编码、解码、业务处理的handler
                            socketChannel.pipeline()
                                    .addLast(new RpcDecoder(RpcResponse.class))
                                    .addLast(new RpcEncoder(RpcRequest.class))
                                    .addLast(RpcClient.this);
                        }
                    }).option(ChannelOption.SO_KEEPALIVE, true);
            // 连接服务器
            ChannelFuture future = bootstrap.connect(host, port).sync();

            //将request对象写入outbound处理后发送（即RpcEncoder编码器）
            future.channel().writeAndFlush(request).sync();

            // 用线程等待的方式决定是否关闭连接
            // 其意义是：先在此阻塞，等待获取到服务端返回后，被唤醒，从而关闭网络连接
            synchronized (obj) {
                obj.wait();
            }

            if (response != null) {
                future.channel().closeFuture().sync();
            }

            return response;
        } finally {
            group.shutdownGracefully();
        }

    }

    /**
     * 读取服务端的返回结果
     *
     * @param ctx
     * @param response
     * @throws Exception
     */
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
        this.response = response;

        synchronized (obj) {
            obj.notify();
        }
    }

    /**
     * 异常处理
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("client caught exception", cause);
        ctx.close();
    }
}
