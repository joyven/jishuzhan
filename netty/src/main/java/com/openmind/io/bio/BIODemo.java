package com.openmind.io.bio;

import java.io.*;
import java.net.*;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * ${name}
 *
 * @author zhoujunwen
 * @date 2019-11-05
 * @time 16:14
 * @desc
 */
public class BIODemo {
    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress("0.0.0.0", 8888), 50);
        Socket socket;
        while ((socket = serverSocket.accept()) != null) {
            InputStream is = socket.getInputStream();
            byte[] data = new byte[1024];
            is.read(data);

            System.out.println(new String(data, UTF_8));
            OutputStream out = socket.getOutputStream();
            out.write(data);
            socket.close();
        }
    }
}
