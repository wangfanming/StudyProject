package com.hadoopRpc;

import org.apache.hadoop.ipc.ProtocolSignature;

import java.io.IOException;

public class ClientProtocolImpl implements ClientProtocol {
    public String echo(String value) throws IOException {
        return value;
    }

    public int add(int v1, int v2) throws IOException {
        return v1 + v2;
    }

    //重载的方法，用于获取自定义的协议版本号
    public long getProtocolVersion(String s, long l) throws IOException {
        return ClientProtocol.versionID;
    }

    //重载的方法，用于获取协议签名
    public ProtocolSignature getProtocolSignature(String s, long l, int i) throws IOException {
        return new ProtocolSignature(ClientProtocol.versionID, null);
    }
}
