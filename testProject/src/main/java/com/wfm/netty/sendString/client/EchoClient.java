package com.wfm.netty.sendString.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;


public class EchoClient {
    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup nioEventLoopGroup = null;
        try {
            //创建Bootstrap对象来引导启动客户端
            Bootstrap bootstrap = new Bootstrap();

            //创建EventLoopGroup对象，并设置到Bootstrap中，EventLoopGroup可以理解为一个线程池，用于处理连接、接收数据、发送数据
            nioEventLoopGroup = new NioEventLoopGroup();

            bootstrap.group(nioEventLoopGroup).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        //添加一个ChannelHandler,客户端成功连接服务器后就会被执行
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            //调用Bootstrap.connect()来连接服务器
            ChannelFuture futrue = bootstrap.connect().sync();
            //最后关闭EventLoopGroup来释放资源
            futrue.channel().closeFuture().sync();
        } finally {
            nioEventLoopGroup.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        new EchoClient("localhost", 20000).start();
    }
}
