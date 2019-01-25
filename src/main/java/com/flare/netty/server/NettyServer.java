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

/**
 * @ClassName NettyServer
 * @Description 使用netty服务端
 * @Author xieyi
 * @Data 2019/1/14
 */
public class NettyServer {

    private static final int PORT = 8000;
    public static void main(String[] args) {
        //1.创建引导类 serverBootstrap
        //2.指定线程模型 channel  - NioServerSocketChannel.class  NIO
        //3.连接读写处理逻辑 childHandler - ChannelInitializer
        //4.绑定端口
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        serverBootstrap
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childOption(ChannelOption.TCP_NODELAY,true)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) {
                        nioSocketChannel.pipeline().addLast(new Spliter());
                        nioSocketChannel.pipeline().addLast(new PacketDecoder());
                        nioSocketChannel.pipeline().addLast(new LoginRequestHandler());
                        // 新增加用户认证handler
                        nioSocketChannel.pipeline().addLast(new AuthHandler());
                        nioSocketChannel.pipeline().addLast(new MessageRequestHandler());
                        nioSocketChannel.pipeline().addLast(new PacketEncoder());
                    }
                });
        // 绑定端口
        bind(serverBootstrap,PORT);
    }

    /**
     * 绑定端口
     * @param serverBootstrap
     * @param port
     */
    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        serverBootstrap.bind(port).addListener(future ->  {
            if (future.isSuccess()) {
                System.out.println("端口[" + port + "]绑定成功");
            } else {
                // 如果当前端口绑定失败，则端口+1重新绑定
                System.out.println("端口[" + port + "]绑定失败");
            }
        });
    }
}
