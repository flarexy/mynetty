package com.flare.netty.handler.inbound;

import com.flare.netty.packet.response.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

/**
 * @ClassName MessageResponseHandler
 * @Description 消息响应
 * @Author xieyi
 * @Data 2019/1/17
 */
public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageResponsePacket messageResponsePacket) throws Exception {
        // 处理消息
        System.out.println(new Date() + ": 收到服务端的消息: " + messageResponsePacket.getMessage());
    }

}
