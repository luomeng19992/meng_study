package com.breakout.meng.common.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class NettyClient {
    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        ChannelFuture channelFuture = new Bootstrap().group(eventExecutors)// 分配至一组事件处理器
                .channel(NioSocketChannel.class) //绑定通道
                .handler(new ChannelInitializer() { // 给通道增加io操作后的处理器
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                       // channel.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));//增加一个日主处理的handler
                        channel.pipeline().addLast(new StringEncoder());
                    }
                }).connect(new InetSocketAddress("127.0.0.1", 8888));

        //localhost.addListener() 异步处理
        ChannelFuture future = channelFuture.sync();//同步获取结果

        Channel channel = future.channel();

        new Thread(()->{
            Scanner scanner=new Scanner(System.in);
            while(true){
                String s = scanner.nextLine();
                if ("q".equals(s)) {
                    channel.close();
                    break;
                }
                channel.writeAndFlush(s);
            }
        }).start();

        ChannelFuture channelFuture1 = channel.closeFuture();// 同步处理
        channelFuture1.sync();
        eventExecutors.shutdownGracefully();// 停止eventLoopGroup线程组
        System.out.println("关闭后的处理");
        //异步处理
//        channelFuture1.addListener((ChannelFutureListener) channelFuture2 -> {
//            eventExecutors.shutdownGracefully(); // 停止eventLoopGroup线程组
//        });
    }
}
