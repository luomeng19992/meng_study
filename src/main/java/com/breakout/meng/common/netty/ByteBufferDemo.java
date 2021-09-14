package com.breakout.meng.common.netty;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class ByteBufferDemo {
    public static void main(String[] args) throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        Selector selector=Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        serverSocketChannel.bind(new InetSocketAddress(8088));

        while(true){
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey next = iterator.next();
                iterator.remove();
                if (next.isAcceptable()) {
                    System.out.println("===================建立连接");
                    SocketChannel accept = serverSocketChannel.accept();
                    accept.configureBlocking(false);
                    accept.register(selector,SelectionKey.OP_WRITE);
                    ByteBuffer asdfhjalsdfkasdlf = Charset.defaultCharset().encode("asdfhjalsdfkasdlf");
                    while (asdfhjalsdfkasdlf.hasRemaining()){
                        accept.write(asdfhjalsdfkasdlf);
                    }
                }

            }

        }

    }

}
