package com.flare.netty.server;

import com.flare.netty.codec.PacketDecoder;
import com.flare.netty.codec.PacketEncoder;
import com.flare.netty.codec.Spliter;
import com.flare.netty.handler.inbound.AuthHandler;
import com.flare.netty.handler.inbound.LoginRequestHandler;
import com.flare.netty.handler.inbound.MessageRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * @ClassName NettyServer
 * @Description 使用netty服务端
 * @Author xieyi
 * @Data 2019/1/14
 */
public class NettyServer {

    public static void main(String[] args) {
        //1.创建引导类 serverBootstrap
        //2.指定线程模型 channel  - NioServerSocketChannel.class  NIO
        //3.连接读写处理逻辑 childHandler - ChannelInitializer
        //4.绑定端口
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        serverBootstrap
                //指定线程模型
                .group(bossGroup, workerGroup)
                //指定为NIO
                .channel(NioServerSocketChannel.class)
                // 表示系统用于临时存放已完成三次握手的请求的队列的最大长度，如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
                .option(ChannelOption.SO_BACKLOG,1024)
                // 开启TCP底层心跳机制，true为开启
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                // 是否开启Nagle算法，true表示关闭，false表示开启，通俗地说，如果要求高实时性，有数据发送时就马上发送，就关闭，
                // 如果需要减少发送次数减少网络交互，就开启
                .childOption(ChannelOption.TCP_NODELAY,true)
                // 处理逻辑
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) {
                        // 调用自定义处理器
                        // inBound，处理读数据的逻辑链
//                        nioSocketChannel.pipeline().addLast(new ServerHandler());
//                        nioSocketChannel.pipeline().addLast(new FirstServerHandler());
                        // 使用特定处理handel
                        // 基于长度域拆包器 Integer.MAX_VALUE：数据包最大长度  7：长度域偏移量，4 ：长度域长度长度
//                        nioSocketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 7, 4));
                        //使用spliter继承LengthFieldBasedFrameDecoder，实现判断开头和长度
                        nioSocketChannel.pipeline().addLast(new Spliter());
                        nioSocketChannel.pipeline().addLast(new PacketDecoder());
                        nioSocketChannel.pipeline().addLast(new LoginRequestHandler());
                        // 新增加用户认证handler
                        nioSocketChannel.pipeline().addLast(new AuthHandler());
                        nioSocketChannel.pipeline().addLast(new MessageRequestHandler());
                        nioSocketChannel.pipeline().addLast(new PacketEncoder());

                    }
                });

        // 服务端启动执行
        // 正常不使用
        serverBootstrap.handler(new ChannelInitializer<NioServerSocketChannel>() {
            @Override
            protected void initChannel(NioServerSocketChannel nioServerSocketChannel) throws Exception {
                System.out.println("服务端启动中");
            }
        });
        // 绑定端口
        bind(serverBootstrap,8000);
    }

    /**
     * 绑定端口
     * @param serverBootstrap
     * @param port
     */
    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        serverBootstrap.bind(port).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("端口[" + port + "]绑定成功");
                } else {
                    // 如果当前端口绑定失败，则端口+1重新绑定
                    System.out.println("端口[" + port + "]绑定失败");
                    bind(serverBootstrap, port + 1);
                }
            }
        });
    }
}
