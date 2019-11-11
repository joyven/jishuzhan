package com.openmind.io.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ${name}
 *
 * @author zhoujunwen
 * @date 2019-11-05
 * @time 16:25
 * @desc
 */
public class ThreadPoolBIODemo {
    public static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress("0.0.0.0", 8888), 50);
        Socket socket;

        while ((socket = serverSocket.accept()) != null) {
            final Socket clientSocket = socket;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        InputStream is = clientSocket.getInputStream();
                        byte[]  data = new byte[1024];
                        is.read(data);

                        OutputStream out = clientSocket.getOutputStream();
                        out.write(data);
                        clientSocket.close();
                    } catch (Exception e) {
                        ;
                    }
                }
            });
        }
    }
}
