package com.wfm.rpc.server;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RPC 请求注解（标注在 服务实现类 上）
 */
@Target({ElementType.TYPE})  //注解用在接口上
@Retention(RetentionPolicy.RUNTIME) //VM 将在运行期也保留注解，因此可以通过反射机制读取注解的信息
@Component
public @interface RpcService {
    Class<?> value();
}
