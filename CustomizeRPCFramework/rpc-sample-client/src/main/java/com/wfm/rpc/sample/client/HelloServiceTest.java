package com.wfm.rpc.sample.client;

import com.wfm.rpc.client.RpcProxy;
import com.wfm.rpc.sample.endpoint.HelloService;
import com.wfm.rpc.sample.endpoint.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring.xml")
public class HelloServiceTest {

    @Autowired
    private RpcProxy rpcProxy;

    @Test
    public void helloTest1() {
        //调用代理的create方法，代理HelloService接口
        HelloService helloService = rpcProxy.create(HelloService.class);

        //调用代理的方法.jdk 会把整个调用通过反射 invoke 完成，在invoke方法中会接收到方法名，参数值
        String result = helloService.hello("World");
        System.out.println("服务端返回结果：");
        System.out.println(result);
    }

    @Test
    public void helloTest2() {
        HelloService helloService = rpcProxy.create(HelloService.class);
        String result = helloService.hello(new Person("zhang san", "john"));
        System.out.println("服务端返回结果：");
        System.out.println(result);
    }
}
