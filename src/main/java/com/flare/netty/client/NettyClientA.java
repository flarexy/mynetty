package com.flare.netty.client;

import com.flare.netty.codec.PacketDecoder;
import com.flare.netty.codec.PacketEncoder;
import com.flare.netty.codec.Spliter;
import com.flare.netty.handler.inbound.LoginResponseHandler;
import com.flare.netty.handler.inbound.MessageResponseHandler;
import com.flare.netty.packet.request.LoginRequestPacket;
import com.flare.netty.packet.request.MessageRequestPacket;
import com.flare.netty.util.SessionUtil;
import io.netty.bootstrap.Bootstrap;
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
public class NettyClientA {

    // 最大重连次数
    final static int MAX_RETRY = 50;
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8000;

    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();
        bootstrap
                .group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        // 调用自定义处理器
                        channel.pipeline().addLast(new Spliter());
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
        Scanner sc = new Scanner(System.in);
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        new Thread(()->{
            while (!Thread.interrupted()){
                if (!SessionUtil.hasLogin(channel)) {
                    System.out.print("输入用户名登录:");
                    String username = sc.nextLine();
                    loginRequestPacket.setUserName(username);
                    // 密码使用默认的
                    loginRequestPacket.setPassword("pwd");
                    // 发送登录数据包
                    channel.writeAndFlush(loginRequestPacket);
                    waitForLoginResponse();
                } else {
                    String toUserId = sc.next();
                    String message = sc.next();
                    channel.writeAndFlush(new MessageRequestPacket(toUserId, message));
                }
            }
        }).start();
    }
    private static void waitForLoginResponse() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
    }
}
