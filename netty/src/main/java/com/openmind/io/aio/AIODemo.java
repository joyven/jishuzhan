package com.openmind.io.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * ${name}
 *
 * @author zhoujunwen
 * @date 2019-11-05
 * @time 17:17
 * @desc
 */
public class AIODemo {
    public static void main(String[] args) throws IOException {
        final AsynchronousServerSocketChannel asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
        asynchronousServerSocketChannel.bind(new InetSocketAddress("0.0.0.0", 8888), 1000000);
        asynchronousServerSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
            @Override
            public void completed(AsynchronousSocketChannel clientChannel, Object attachment) {
                asynchronousServerSocketChannel.accept(null, this);
                final ByteBuffer buffer = ByteBuffer.wrap(new byte[1024]);
                clientChannel.read(buffer, buffer, new ClientReadHandler(clientChannel));
            }

            @Override
            public void failed(Throwable exc, Object attachment) {

            }
        });
    }

    static class ClientReadHandler implements CompletionHandler<Integer, ByteBuffer> {
        AsynchronousSocketChannel socketChannel;
        public ClientReadHandler(AsynchronousSocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        @Override
        public void completed(Integer result, ByteBuffer buffer) {
            if (result == 1024) {
                socketChannel.write(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                    @Override
                    public void completed(Integer result, ByteBuffer attachment) {
                        if (attachment.hasRemaining()) {
                            socketChannel.write(attachment, attachment, this);
                        } else {
                            try {
                                socketChannel.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void failed(Throwable exc, ByteBuffer attachment) {

                    }
                });
            }
        }

        @Override
        public void failed(Throwable exc, ByteBuffer attachment) {

        }
    }
}
