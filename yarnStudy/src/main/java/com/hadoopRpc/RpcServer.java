package com.hadoopRpc;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;


public class RpcServer {
    public static void main(String[] args) throws IOException {
        RPC.Server server = new RPC.Builder(new Configuration())
                .setProtocol(ClientProtocol.class).setInstance(new ClientProtocolImpl())
                .setBindAddress("192.168.99.152").setPort(8998).setNumHandlers(5).build();

        server.start();
    }
}
