package com.flare.netty.server;

import com.flare.netty.packet.Packet;
import com.flare.netty.packet.PacketCodeC;
import com.flare.netty.packet.request.LoginRequestPacket;
import com.flare.netty.packet.request.MessageRequestPacket;
import com.flare.netty.packet.response.LoginResponsePacket;
import com.flare.netty.packet.response.MessageResponsePacket;
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
                System.out.println(new Date() + ": 校验成功!");
            } else {
                // 失败
                loginResponsePacket.setSuccess(false);
                loginResponsePacket.setMsg("账号密码错误");
                System.out.println(new Date() + ": 校验失败!");
            }

            // 编码
            ByteBuf responseByteBuf = PacketCodeC.getInstance().encode(ctx.alloc(),loginResponsePacket);
            // 写数据
            ctx.channel().writeAndFlush(responseByteBuf);
        } else if (packet instanceof MessageRequestPacket) {
            // 处理消息
            MessageRequestPacket messageRequestPacket = (MessageRequestPacket) packet;
            System.out.println(new Date() + "：收到客户端消息："+ messageRequestPacket.getMessage());

            MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
            messageResponsePacket.setMessage("服务端回复消息【"+ messageRequestPacket.getMessage() +"】");
            ByteBuf responseByteBuf = PacketCodeC.getInstance().encode(ctx.alloc(),messageResponsePacket);
            ctx.channel().writeAndFlush(responseByteBuf);
        }
        super.channelRead(ctx, msg);
    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }
}
