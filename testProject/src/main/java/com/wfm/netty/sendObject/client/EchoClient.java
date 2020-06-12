package com.wfm.netty.sendObject.client;

import com.wfm.netty.sendObject.commons.PersonDecoder;
import com.wfm.netty.sendObject.commons.PersonEncoder;
import com.wfm.netty.sendObject.entity.Person;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.net.InetSocketAddress;


public class EchoClient {
    private final String host;
    private final int port;

    private Channel channel = null;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
        channel = start();
    }

    public Channel start() {
        EventLoopGroup nioEventLoopGroup = null;
        Channel channel = null;
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
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                            pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
                            pipeline.addLast(new PersonDecoder()); // InboundHandler
                            pipeline.addLast(new PersonEncoder());  // OutboundHandler
                            pipeline.addLast(new EchoClientHandler());  // InboundHandler
                        }
                    });


            channel = bootstrap.connect().sync().channel();

//            //调用Bootstrap.connect()来连接服务器
//            ChannelFuture futrue = bootstrap.connect().sync();
//            //最后关闭EventLoopGroup来释放资源
//            futrue.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return channel;
    }


    public void sendMessage(Person person) throws InterruptedException {
        System.out.println("打开管道开始发送消息 ...");
        if (this.channel != null) {
            this.channel.writeAndFlush(person).sync();
        } else {
            System.out.println("管道未建立成功，消息发送失败 ...");
        }
    }


    public static void main(String[] args) throws Exception {
        EchoClient client = new EchoClient("localhost", 20000);


        for (int i = 0; i < 10; i++) {
            Person person = new Person("zhangsan", 21);
            person.setAge(person.getAge() + i);
            client.sendMessage(person);
        }

    }
}
