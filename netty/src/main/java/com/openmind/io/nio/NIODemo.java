package com.openmind.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * ${name}
 *
 * @author zhoujunwen
 * @date 2019-11-05
 * @time 16:54
 * @desc
 */
public class NIODemo {
    public void start() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("0.0.0.0", 8888), 50);
        serverSocketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (!key.isValid()) {
                    continue;
                }

                if (key.isAcceptable()) {
                    ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                    SocketChannel clientChannel = serverChannel.accept();
                    clientChannel.configureBlocking(false);
                    clientChannel.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    ByteBuffer buffer = ByteBuffer.wrap(new byte[1024]);
                    SocketChannel clientChannel = (SocketChannel) key.channel();
                    int read = clientChannel.read(buffer);

                    if (read == -1) {
                        key.cancel();
                        clientChannel.close();
                    } else {
                        buffer.flip();
                        clientChannel.write(buffer);
                    }
                }
            }
            iterator.remove();
        }
    }
}
