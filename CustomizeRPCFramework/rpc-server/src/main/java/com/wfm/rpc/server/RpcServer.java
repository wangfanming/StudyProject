package com.wfm.rpc.server;

import com.wfm.rpc.common.RpcDecoder;
import com.wfm.rpc.common.RpcEncoder;
import com.wfm.rpc.common.RpcRequest;
import com.wfm.rpc.common.RpcResponse;
import com.wfm.rpc.registry.ServiceRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

/**
 * 框架的 RPC 服务器（用于将用户系统的业务类发布为 RPC服务）
 * 使用时，可由用户通过spring-bean的方式注入到用户的业务系统中
 * 由于本类实现了ApplicationContextAware 、InitializingBean
 * spring 构造本对象时 会调用setApplicationContext()方法，从而可以在方法中通过自定义注解获得用户的业务接口和实现
 * 还会调用afterPropertiesSet()方法，在方法中启动 netty 服务器
 */
public class RpcServer implements ApplicationContextAware, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);

    private String serverAddress;

    private ServiceRegistry serviceRegistry;

    //用于存储业务接口和实现类的实例对象（由 sparing 构造）
    private Map<String, Object> handlerMap = new HashMap<String, Object>();

    public RpcServer(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    /**
     * 服务器绑定的地址和端口由spring在构造本类时从配置文件中传入
     *
     * @param serverAddress
     * @param serviceRegistry
     */
    public RpcServer(String serverAddress, ServiceRegistry serviceRegistry) {
//        this.serverAddress = serverAddress;
        this(serverAddress);
        // 用于向 ZK 注册名称服务的工具类
        this.serviceRegistry = serviceRegistry;
    }

    /**
     * 通过注解，获取标注了 rpc 服务注解的业务类的 接口及impl对象，将他们放到 handlerMap
     *
     * @param ctx
     * @throws BeansException
     */
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        Map<String, Object> serviceBeanMap = ctx.getBeansWithAnnotation(RpcService.class);
        if (MapUtils.isNotEmpty(serviceBeanMap)) {
            for (Object serviceBean : serviceBeanMap.values()) {
                //从业务实现类的自定义注解中获取到 value ，从而获取到业务接口的全名
                String interfaceName = serviceBean.getClass().getAnnotation(RpcService.class).value().getName();
                handlerMap.put(interfaceName, serviceBean);
            }
        }
    }

    /**
     * 在此启动 netty 服务，绑定 handle流水线
     * 1、接受请求数据，进行反序列化，得到 request 对象
     * 2、根据request对象中的参数，让 RpcHandler 从 handlerMap 中找到对应的业务impl,调用指定的方法，返回结果
     * 3、将业务调用结果封装到response并序列化后发往客户端
     *
     * @throws Exception
     */
    public void afterPropertiesSet() throws Exception {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new RpcDecoder(RpcRequest.class))
                                    .addLast(new RpcEncoder(RpcResponse.class))
                                    .addLast(new RpcHandler(handlerMap));
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            String[] array = serverAddress.split(":");
            String host = array[0];
            int port = Integer.parseInt(array[1]);

            ChannelFuture future = bootstrap.bind(host, port).sync();
            LOGGER.debug("server started on port {}", port);

            if (serviceRegistry != null) {
                serviceRegistry.register(serverAddress);
            }

            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
