package com.openmind.io.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;

/**
 * ${name}
 *
 * @author zhoujunwen
 * @date 2019-11-08
 * @time 13:40
 * @desc
 */
public class AIO_Demo {

    private static final int BYTE_LEN = 8;
    static Charset charset = Charset.forName("utf-8");

    public static void start() throws IOException {
        new AIO_Demo().startServer();
        while (true) {
            ;
        }
    }

    /**
     * 开启socket服务
     *
     * @throws IOException
     */
    public void startServer() throws IOException {
        final AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open();
        server.bind(new InetSocketAddress(8888));
        server.accept(server, new AcceptHandler());
    }

    /**
     * 建立连接处理器
     */
    class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {

        @Override
        public void completed(AsynchronousSocketChannel result, AsynchronousServerSocketChannel server) {
            //继续等待下一个接入的链接
            server.accept(server, this);

            new Thread(() -> {
                try {
                    //先忽略对新连接的处理相关代码
                    System.out.println("accept from:" + result.getRemoteAddress().toString());

                    // 定义数据读取缓存
                    ByteBuffer buffer = ByteBuffer.wrap(new byte[BYTE_LEN]);
                    // 读取：参数一是通道读取数据的目的地；参数二是传入回调对象的附件对象；参数三是回调对象
                    result.read(buffer, buffer, new ClientReadHandler(result));
                } catch (Exception e) {
                    ;
                }
            }).start();
        }

        @Override
        public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {
            exc.printStackTrace();
        }
    }

    /**
     * 读取客户端字节处理器
     */
    class ClientReadHandler implements CompletionHandler<Integer, ByteBuffer> {

        AsynchronousSocketChannel socketChannel;

        public ClientReadHandler(AsynchronousSocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        /**
         * @param result 本次读取成功的字节数
         * @param buffer 附件对象
         */
        @Override
        public void completed(Integer result, ByteBuffer buffer) {

//            if (result == BYTE_LEN) {
            // 读取数据完毕，准备写出
            // 参数一是承载需要写出的数据的ByteBuffer，第二个参数是传递给回调对象的附件对象；第三个参数是回调对象
            buffer.flip();
            String msg = charset.decode(buffer).toString();
            System.out.println("server receive msg: " + msg);
//                socketChannel.write(buffer, buffer, new WriteHandler(socketChannel));
//            }

//            buffer.flip();
            buffer.compact();
            //继续接收数据，构成循环
            socketChannel.read(buffer, buffer, this);
        }

        @Override
        public void failed(Throwable exc, ByteBuffer attachment) {
            exc.printStackTrace();
        }
    }

    /**
     * 写出处理器
     */
    class WriteHandler implements CompletionHandler<Integer, ByteBuffer> {

        AsynchronousSocketChannel socketChannel;

        public WriteHandler(AsynchronousSocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        @Override
        public void completed(Integer result, ByteBuffer buffer) {
            // 如果第一次没有全部继续写完，继续写
            if (buffer.hasRemaining()) {
                socketChannel.write(buffer, buffer, this);
            }
        }

        @Override
        public void failed(Throwable exc, ByteBuffer buffer) {
            exc.printStackTrace();
        }
    }
}
