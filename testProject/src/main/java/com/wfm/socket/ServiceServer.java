package com.wfm.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServiceServer {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress("localhost", 8899));

        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(new ServiceTask(socket)).start();
        }
    }
}
