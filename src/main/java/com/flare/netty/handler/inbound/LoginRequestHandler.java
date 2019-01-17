package com.flare.netty.handler.inbound;

import com.flare.netty.packet.request.LoginRequestPacket;
import com.flare.netty.packet.response.LoginResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

/**
 * @ClassName LoginResponseHandler
 * @Description 登录请求
 * @Author xieyi
 * @Data 2019/1/17
 */
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LoginRequestPacket loginRequestPacket) throws Exception {
        // 写数据
        channelHandlerContext.channel().writeAndFlush(login(loginRequestPacket));
    }



    private LoginResponsePacket login(LoginRequestPacket loginRequestPacket) {
        // 登录逻辑
        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setVersion(loginRequestPacket.getVersion());
        // 登陆校验
        if (valid(loginRequestPacket)) {
            // 成功
            loginResponsePacket.setSuccess(true);
            System.out.println(new Date() + ": 校验成功!");
        } else {
            // 失败
            loginResponsePacket.setSuccess(false);
            loginResponsePacket.setMsg("账号密码错误");
            System.out.println(new Date() + ": 校验失败!");
        }
        return loginResponsePacket;
    }
    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }
}
