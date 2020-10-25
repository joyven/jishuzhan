package com.openmind.io.bio;

import java.io.*;
import java.net.*;

/**
 * ${name}
 *
 * @author zhoujunwen
 * @date 2019-11-05
 * @time 16:19
 * @desc
 */
public class NewThreadBIODemo {
    public static void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress("0.0.0.0", 8888), 50);
        Socket socket;
        while ((socket = serverSocket.accept()) != null) {
            final Socket clientSocket = socket;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        InputStream is = clientSocket.getInputStream();
                        byte[] data = new byte[1024];
                        is.read(data);

                        OutputStream out = clientSocket.getOutputStream();
                        out.write(data);
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
