package com.wfm.rpc.sample.endpoint;

public interface HelloService {
    String hello(String name);

    String hello(Person person);
}
