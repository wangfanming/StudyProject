package com.hadoopRpc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;
import java.net.InetSocketAddress;

public class RpcClient {
    public static void main(String[] args) throws IOException {
        ClientProtocol proxy = (ClientProtocol) RPC.getProxy(ClientProtocol.class, ClientProtocol.versionID, new InetSocketAddress("192.168.99.152", 8998), new Configuration());
        int result = proxy.add(5, 6);
        System.out.println(result);
        String echoResult = proxy.echo("789");

        System.out.println(echoResult);

    }
}
