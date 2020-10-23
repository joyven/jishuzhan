package com.openmind.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 服务器原样返回客户端发送的信息
 * 客户端发送的信息以'\r'作为一个消息的结尾，一个消息的最大长度不超过 128
 * 客户端可能会一次发送多个消息，服务端需要按照收到的消息的顺序依次回复，不能乱序
 * 客户端可以在任意时刻关闭通道
 * 服务端不能主动关闭通道
 *
 * @author zhoujunwen
 * @date 2019-11-11
 * @time 18:21
 * @desc
 */
public class NIO_Demo2 {
    public void start() throws IOException, InterruptedException {
        Thread t = new Thread(new NioServer(3333), "accept-server");
        t.start();
    }

    /**
     * nio 服务端代码
     */
    static class NioServer implements Runnable {
        private static final AtomicInteger ID = new AtomicInteger();
        // 服务连接多路复用器
        private Selector selector;
        // 处理客户端消息专用多路复用器
        private Selector[] readSelectors;

        private int port;

        public NioServer(int port) {
            this.port = port;
            initServer();
        }

        /**
         * 初始化服务器
         */
        void initServer() {
            try {
                // 打开服务器通道
                SelectorProvider selectorProvider = SelectorProvider.provider();
                ServerSocketChannel serverSocketChannel = selectorProvider.openServerSocketChannel();// ServerSocketChannel.open();
                // 设置服务器通道为非阻塞模式
                serverSocketChannel.configureBlocking(false);
                // 绑定端口，backlog值队列的容量，默认为50，提供容量限制功能，避免太多客户端占用太多服务器资源
                // serverSocketChannel 有一个队列，存放没有来得及处理的客户端，服务器每次 accept，就会从队列中取一个元素
                serverSocketChannel.socket().bind(new InetSocketAddress(port), 1024);

                // 打开多路复用器
                selector = selectorProvider.openSelector(); // Selector.open();
                // 把服务器通道注册到多路复用器上，监听阻塞事件
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
                System.out.println("The server is start in port: " + port);


                readSelectors = new Selector[Runtime.getRuntime().availableProcessors()];
                for (int i = 0; i < readSelectors.length; i++) {
                    Selector sel = selectorProvider.openSelector(); // Selector.open();
                    readSelectors[i] = sel;
                    new Thread(new ClientProcessor(sel), "server-handler-client-" + 1).start();
                }


            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("The server is exception!");
                System.exit(1);
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    int sel = selector.selectNow();
                    if (sel > 0) {
                        Set<SelectionKey> selectionKeys = selector.selectedKeys();
                        Iterator<SelectionKey> iterator = selectionKeys.iterator();
                        while (iterator.hasNext()) {
                            SelectionKey key = iterator.next();
                            if (key.isValid() && key.isAcceptable()) {
                                // 获取服务通道
                                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                                // 执行阻塞方法
                                SocketChannel socketChannel = ssc.accept();
                                // 设置为非阻塞模式
                                socketChannel.configureBlocking(false);

                                // 获取处理客户端消息专用多路复用器中的一个复用器
                                Selector childSelector = readSelectors[ID.getAndIncrement() % readSelectors.length];
                                // 注册到多路复用器上，并设置为可读状态。每一个选择键或者说通道持有一个独占的buffer，用于数据的累计读取
                                socketChannel.register(childSelector, SelectionKey.OP_READ, ByteBuffer.allocate(128 * 2));


                                System.out.println("注册读线程到多路复用器");
                                iterator.remove();
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }
    }

    /**
     * 服务端--客户端消息处理器
     */
    static class ClientProcessor implements Runnable {
        private static final Charset CHARSET = Charset.forName("UTF-8");
        private Selector selector;

        public ClientProcessor(Selector selector) {
            this.selector = selector;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    int selectNow = selector.selectNow();
                    System.out.println("ClientProcessor " + selectNow);
                    if (selectNow > 0) {
                        Set<SelectionKey> selectionKeys = selector.selectedKeys();

                        for (SelectionKey each : selectionKeys) {
                            if (!each.isValid()) {
                                continue;
                            }

                            if (each.isAcceptable()) {
                                System.out.println("纳尼，怎么会有accept的连接进入到这里面" + selectNow);
                            }
                            if (each.isReadable()) {
                                SocketChannel clientChannel = (SocketChannel) each.channel();
                                ByteBuffer readBuff = (ByteBuffer) each.attachment();
                                int read = clientChannel.read(readBuff);

                                if (read == -1) {
                                    // 通道连接关闭，可以取消这个注册键，后续不在触发。
                                    each.cancel();
                                    clientChannel.close();
                                } else if (read > 0) {
                                    // 翻转buffer，从写入状态切换到读取状态
                                    readBuff.flip();
                                    int position = readBuff.position();
                                    int limit = readBuff.limit();

                                    List<ByteBuffer> buffers = new ArrayList<>();
                                    // 按照协议从流中分割出消息
                                    /**从readBuffer确认每一个字节，发现分割符则切分出一个消息**/
                                    for (int i = position; i < limit; i++) {
                                        //读取到消息结束符
                                        if (readBuff.get() == '\r' && readBuff.get() == '\n') {
                                            ByteBuffer message = ByteBuffer.allocate(i - position);
                                            // 设置position和limit位置，\r\n两个字符需要包含在里面，limit在当前position的位置+1
                                            readBuff.limit(i);
                                            // 重置position为开始位置
                                            readBuff.position(position);

                                            message.put(readBuff);


                                            // limit复位到原始的位置，接着下一次循环
                                            readBuff.limit(limit);
                                            // position位置上一次的limit左前一位
                                            readBuff.position(i + 1);


                                            message.flip();

                                            String s = CHARSET.decode(message).toString();
                                            System.out.println(s);

                                            message.position(0);
                                            buffers.add(message);
                                        }
                                    }

                                    /**从readBuffer确认每一个字节，发现分割符则切分出一个消息**/
                                    /**将所有得到的消息发送出去**/
                                    for (ByteBuffer buffer : buffers) {
                                        while (buffer.hasRemaining()) {
                                            clientChannel.write(buffer);
                                        }
                                    }
                                    /**将所有得到的消息发送出去**/
                                    // 压缩readBuffer，压缩完毕后进入写入状态。并且由于长度是256，压缩之后必然有足够的空间可以写入一条消息
                                    readBuff.compact();
                                }

                            }
                        }
                        selectionKeys.clear();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
