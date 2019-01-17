package com.flare.netty.client;

import com.flare.netty.packet.PacketCodeC;
import com.flare.netty.packet.request.LoginRequestPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.UUID;

/**
 * @ClassName ClientHandler
 * @Description 客户端处理器
 * @Author xieyi
 * @Data 2019/1/15
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        System.out.println("客户端开始登录");
        // 创建登陆对象
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserId(UUID.randomUUID().toString());
        loginRequestPacket.setUsername("flare");
        loginRequestPacket.setPassword("pwd");

        // 编码
        ByteBuf byteBuf = PacketCodeC.getInstance().encode(ctx.alloc(),loginRequestPacket);

        //写数据
        ctx.channel().writeAndFlush(byteBuf);
    }

    /*@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf requestByteBuf = (ByteBuf) msg;
        // 解码
        Packet packet = PacketCodeC.getInstance().decode(requestByteBuf);
        if (packet instanceof LoginResponsePacket){
            LoginResponsePacket loginResponsePacket = (LoginResponsePacket) packet;
            if (loginResponsePacket.getSuccess()){
                // 绑定登录成功标识
                LoginUtil.markAsLogin(ctx.channel());
                System.out.println(new Date() + "：登录成功");
            }else {
                System.out.println(new Date() + "：登录失败，原因：" + loginResponsePacket.getMsg());
            }
        } else if (packet instanceof MessageResponsePacket){
            MessageResponsePacket messageResponsePacket = (MessageResponsePacket) packet;
            System.out.println(new Date() + ": 收到服务端的消息: " + messageResponsePacket.getMessage());
        }
    }*/
}
