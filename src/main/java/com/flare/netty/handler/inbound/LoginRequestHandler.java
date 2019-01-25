package com.flare.netty.handler.inbound;

import com.flare.netty.packet.request.LoginRequestPacket;
import com.flare.netty.packet.response.LoginResponsePacket;
import com.flare.netty.session.Session;
import com.flare.netty.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;
import java.util.UUID;

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
        channelHandlerContext.channel().writeAndFlush(login(channelHandlerContext,loginRequestPacket));
    }



    private LoginResponsePacket login(ChannelHandlerContext ctx,LoginRequestPacket loginRequestPacket) {
        // 登录逻辑
        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setVersion(loginRequestPacket.getVersion());
        loginResponsePacket.setUserName(loginRequestPacket.getUserName());
        // 登陆校验
        if (valid(loginRequestPacket)) {
            // 成功
            loginResponsePacket.setSuccess(true);
            String userId = randomUserId();
            loginResponsePacket.setUserId(userId);
            System.out.println("[" + loginRequestPacket.getUserName() + "]登录成功");
            SessionUtil.bindSession(new Session(userId, loginRequestPacket.getUserName()), ctx.channel());
        } else {
            // 失败
            loginResponsePacket.setSuccess(false);
            loginResponsePacket.setMsg("账号密码错误");
            System.out.println(new Date() + ": 登录失败!");
        }
        return loginResponsePacket;
    }
    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }

    private static String randomUserId() {
        return UUID.randomUUID().toString().split("-")[0];
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        SessionUtil.unBindSession(ctx.channel());
    }
}
