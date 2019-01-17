package com.flare.netty.client;

import com.flare.netty.codec.PacketDecoder;
import com.flare.netty.codec.PacketEncoder;
import com.flare.netty.handler.inbound.LoginResponseHandler;
import com.flare.netty.handler.inbound.MessageResponseHandler;
import com.flare.netty.packet.PacketCodeC;
import com.flare.netty.packet.request.MessageRequestPacket;
import com.flare.netty.util.LoginUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName NettyClient
 * @Description netty客户端
 * @Author xieyi
 * @Data 2019/1/14
 */
public class NettyClient {

    // 最大重连次数
    final static int MAX_RETRY = 50;
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8000;

    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();
        bootstrap
                // 1.指定线程模型
                .group(group)
                // 2.指定 IO 类型为 NIO
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                // 3.IO 处理逻辑
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        // 调用自定义处理器
//                        channel.pipeline().addLast(new ClientHandler());
                        channel.pipeline().addLast(new PacketDecoder());
                        channel.pipeline().addLast(new LoginResponseHandler());
                        channel.pipeline().addLast(new MessageResponseHandler());
                        channel.pipeline().addLast(new PacketEncoder());

                    }
                });
        // 4.建立连接
        connect(bootstrap,HOST,PORT,MAX_RETRY);
    }

    /**
     * 建立连接
     * @param bootstrap
     * @param host
     * @param port
     */
    private static void connect(Bootstrap bootstrap, String host, int port,int retry){
        bootstrap.connect(host,port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("连接成功");
                // 启动控制台线程
                Channel channel = ((ChannelFuture) future).channel();
                startConsoleThread(channel);
            } else if (retry == 0){
                System.err.println("重试次数已用完，放弃连接！");
            } else {
                // 第几次重连
                int order = (MAX_RETRY - retry) + 1;
                System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");
                // 本次重连的间隔
                int delay = 1 << order;
                // 重新连接(定时重连)
                bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit.SECONDS);
            }
        });
    }

    private static void startConsoleThread(Channel channel) {
        new Thread(()->{
            while (!Thread.interrupted()){
                if (LoginUtil.hasLogin(channel)) {
                    System.out.println("输出消息发送至服务器：");
                    Scanner sc = new Scanner(System.in);
                    String line = sc.nextLine();

                    MessageRequestPacket messageRequestPacket = new MessageRequestPacket();
                    messageRequestPacket.setMessage(line);

                    ByteBuf byteBuf = PacketCodeC.getInstance().encode(channel.alloc(),messageRequestPacket);
                    channel.writeAndFlush(byteBuf);
                }
            }
        }).start();

    }
}
