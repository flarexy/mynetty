package com.flare.netty.handler.inbound;

import com.flare.netty.packet.response.LoginResponsePacket;
import com.flare.netty.session.Session;
import com.flare.netty.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

/**
 * @ClassName LoginResponseHandler
 * @Description 登录响应
 * @Author xieyi
 * @Data 2019/1/17
 */
public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LoginResponsePacket loginResponsePacket) throws Exception {

        String userId = loginResponsePacket.getUserId();
        String userName = loginResponsePacket.getUserName();
        if (loginResponsePacket.getSuccess()){
            // 绑定登录成功标识
            SessionUtil.bindSession(new Session(userId, userName), channelHandlerContext.channel());
            System.out.println("[" + userName + "]登录成功，userId 为: " + loginResponsePacket.getUserId());
        } else {
            System.out.println(new Date() + "：登录失败，原因：" + loginResponsePacket.getMsg());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端连接被关闭!");
    }
}
