package com.flare.netty.handler.inbound;

import com.flare.netty.packet.response.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @ClassName MessageResponseHandler
 * @Description 消息响应
 * @Author xieyi
 * @Data 2019/1/17
 */
public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageResponsePacket messageResponsePacket) throws Exception {
        String fromUserId = messageResponsePacket.getFromUserId();
        String fromUserName = messageResponsePacket.getFromUserName();
        // 处理消息
        System.out.println(fromUserId + "：" + fromUserName + " -> " + messageResponsePacket.getMessage());
    }

}
