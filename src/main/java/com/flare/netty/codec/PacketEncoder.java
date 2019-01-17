package com.flare.netty.codec;

import com.flare.netty.packet.Packet;
import com.flare.netty.packet.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @ClassName PacketEncoder
 * @Description 编码
 * @Author xieyi
 * @Data 2019/1/17
 */
public class PacketEncoder extends MessageToByteEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf byteBuf) throws Exception {
        PacketCodeC.getInstance().encode(byteBuf,packet);
    }
}
