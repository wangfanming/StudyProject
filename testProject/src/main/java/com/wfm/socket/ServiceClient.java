package com.wfm.socket;

import java.io.*;
import java.net.Socket;

public class ServiceClient {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost",8899);
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();

        PrintStream pw = new PrintStream(outputStream);
        pw.println("com.wfm.socket.TaskImpl getData 1,2");

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String result = br.readLine();
        System.out.println(result);

        socket.close();
    }
}
