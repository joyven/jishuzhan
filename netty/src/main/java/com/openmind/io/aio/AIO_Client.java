package com.openmind.io.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * ${name}
 *
 * @author zhoujunwen
 * @date 2019-11-11
 * @time 09:31
 * @desc
 */
public class AIO_Client {
    static Charset charset = Charset.forName("UTF-8");

    public static void main(String[] args) throws InterruptedException {
        int port = 7890;
        String host  = "127.0.0.1";

        // 启动客户端
        new Thread(new AIOClient(port, host)).start();
        TimeUnit.MINUTES.sleep(100);

    }

    static class AIOClient implements Runnable {

        int port;
        String host;
        AsynchronousChannelGroup group;
        AsynchronousSocketChannel channel;
        InetSocketAddress address;


        public AIOClient(int port, String host) {
            this.port = port;
            this.host = host;

            // 初始化
            init();
        }

        private void init() {
            try {
                // 创建处理线程组
                group = AsynchronousChannelGroup.withCachedThreadPool(Executors.newCachedThreadPool(), 5);
                // 创建客户端channel
                channel = AsynchronousSocketChannel.open(group);
                address = new InetSocketAddress(host, port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            // 接收请求，并传入收到请求后的处理器
            // connect 方法的第一二个参数是目标地址，第二个参数是附件对象，第三个参数是连接处理器
            // 连接处理器的泛型的第一个参数为空(即Void)，第二个参数为附件
            channel.connect(address, channel, new ConnectHandler());

        }
    }

    /**
     * 连接处理器
     */
    static class ConnectHandler implements CompletionHandler<Void, AsynchronousSocketChannel> {

        @Override
        public void completed(Void result, AsynchronousSocketChannel attachment) {
            try {
                System.out.println("connect server: " + attachment.getRemoteAddress().toString());

                // 定义数据读取缓存
                ByteBuffer buffer = ByteBuffer.allocate(1024);

                // 读取数据，并传入到数据到达时的处理器
                attachment.read(buffer, buffer, new ReadHandler(attachment));

                // 新开线程，发送数据
                new WriteThread(attachment).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void failed(Throwable exc, AsynchronousSocketChannel attachment) {

        }
    }

    /**
     * 读处理器
     */
    static class ReadHandler implements CompletionHandler<Integer, ByteBuffer> {
        AsynchronousSocketChannel channel;

        public ReadHandler(AsynchronousSocketChannel channel) {
            this.channel = channel;
        }

        @Override
        public void completed(Integer result, ByteBuffer attachment) {
            attachment.flip();
            String readMsg = charset.decode(attachment).toString();
            System.out.println("client receive msg: " + readMsg);
            attachment.compact();

            // 继续接收数据，构成循坏
            channel.read(attachment, attachment, this);
        }

        @Override
        public void failed(Throwable exc, ByteBuffer attachment) {

        }
    }

    /**
     * 写处理器
     */
    static class WriteHandler implements CompletionHandler<Integer, ByteBuffer> {
        AsynchronousSocketChannel channel;
        Scanner scanner;

        public WriteHandler(AsynchronousSocketChannel channel, Scanner scanner) {
            this.channel = channel;
            this.scanner = scanner;
        }

        @Override
        public void completed(Integer result, ByteBuffer attachment) {
            attachment.compact();

            System.out.print("client input data: ");
            String msg = scanner.nextLine();

            System.out.println("clinet will send msg:" + msg);

            attachment.put(charset.encode(msg));
            attachment.flip();

            // 继续写入数据，构成循环
            channel.write(attachment, attachment, this);
        }

        @Override
        public void failed(Throwable exc, ByteBuffer attachment) {

        }
    }


    /**
     * 写处理独立创建线程
     */
    static class WriteThread extends Thread {
        private AsynchronousSocketChannel channel;

        public WriteThread(AsynchronousSocketChannel channel) {
            this.channel = channel;
        }

        @Override
        public void run() {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            Scanner scanner = new Scanner(System.in);
            System.out.print("client input data:");
            String msg = scanner.nextLine();

            System.out.println("client send msg:" + msg);

            buffer.put(charset.encode(msg));
            buffer.flip();

            channel.write(buffer, buffer, new WriteHandler(channel, scanner));
        }
    }
}
