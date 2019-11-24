package com.wfm.socket;

import java.io.*;
import java.net.Socket;

public class ServiceTask implements Runnable {
    Socket socket;
    InputStream in = null;
    OutputStream out = null;
    Class<?> clazz = null;
    Object obj  = null;


    public ServiceTask(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            PrintStream pw = new PrintStream(out);
            String line = br.readLine();

            System.out.println("Received String = " + line);
            String[] params = line.split(" ");
            String className = params[0];
            String methodName = params[1];
            String[] parameters = params[2].split(",");

            try {
                clazz = Class.forName(className);
                obj = clazz.newInstance();
            }catch (Exception e){
                pw.println(className + " 类不存在 ！");
                pw.flush();
            }

            if(TaskImpl.class.isInstance(obj)){
                if("getData".equals(methodName)){
                    Object invokeResult = clazz.getMethod(methodName,String.class,String.class).invoke(obj,parameters[0],parameters[1]);
                    pw.println(invokeResult);
                    pw.flush();
                }else {
                    pw.println(methodName + " 方法不存在 ！");
                    pw.flush();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
