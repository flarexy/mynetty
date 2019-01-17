package com.flare.netty.handler.inbound;

import com.flare.netty.packet.PacketCodeC;
import com.flare.netty.packet.request.LoginRequestPacket;
import com.flare.netty.packet.response.LoginResponsePacket;
import com.flare.netty.util.LoginUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;
import java.util.UUID;

/**
 * @ClassName LoginResponseHandler
 * @Description 登录响应
 * @Author xieyi
 * @Data 2019/1/17
 */
public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {
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

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LoginResponsePacket loginResponsePacket) throws Exception {
        if (loginResponsePacket.getSuccess()){
            // 绑定登录成功标识
            LoginUtil.markAsLogin(channelHandlerContext.channel());
            System.out.println(new Date() + "：登录成功");
        }else {
            System.out.println(new Date() + "：登录失败，原因：" + loginResponsePacket.getMsg());
        }
    }
}
