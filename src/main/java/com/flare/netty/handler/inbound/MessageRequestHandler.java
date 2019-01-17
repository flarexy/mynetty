package com.flare.netty.handler.inbound;

import com.flare.netty.packet.request.MessageRequestPacket;
import com.flare.netty.packet.response.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

/**
 * @ClassName MessageRequestHandler
 * @Description 消息请求
 * @Author xieyi
 * @Data 2019/1/17
 */
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageRequestPacket messageRequestPacket) throws Exception {
        // 处理消息
        channelHandlerContext.channel().writeAndFlush(receiveMessage(messageRequestPacket));
    }

    private MessageResponsePacket receiveMessage(MessageRequestPacket messageRequestPacket){
        System.out.println(new Date() + "：收到客户端消息："+ messageRequestPacket.getMessage());
        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        messageResponsePacket.setMessage("服务端回复消息【"+ messageRequestPacket.getMessage() +"】");
        return messageResponsePacket;
    }
}
