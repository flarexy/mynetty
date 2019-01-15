package com.flare.netty.server;

import com.flare.netty.packet.request.LoginRequestPacket;
import com.flare.netty.packet.response.LoginResponsePacket;
import com.flare.netty.packet.Packet;
import com.flare.netty.packet.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

/**
 * @ClassName ServerHandler
 * @Description 服务端处理器
 * @Author xieyi
 * @Data 2019/1/15
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println(new Date() + ": 客户端开始登录……");
        ByteBuf requestByteBuf = (ByteBuf) msg;

        // 解码
        Packet packet = PacketCodeC.getInstance().decode(requestByteBuf);

        // 判断是否登录请求数据包
        if (packet instanceof LoginRequestPacket) {
            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;
            LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
            loginResponsePacket.setVersion(packet.getVersion());
            // 登陆校验
            if (valid(loginRequestPacket)) {
                // 成功
                loginResponsePacket.setSuccess(true);
                System.out.println(new Date() + ": 登录成功!");
            } else {
                // 失败
                loginResponsePacket.setSuccess(false);
                loginResponsePacket.setMsg("账号密码错误");
                System.out.println(new Date() + ": 登录失败!");
            }

            // 编码
            ByteBuf responseByteBuf = PacketCodeC.getInstance().encode(ctx.alloc(),loginResponsePacket);
            // 写数据
            ctx.channel().writeAndFlush(responseByteBuf);
        }
    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }
}
