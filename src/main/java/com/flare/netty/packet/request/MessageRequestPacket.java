package com.flare.netty.packet.request;

import com.flare.netty.packet.Packet;
import com.flare.netty.packet.command.Command;
import lombok.Data;

/**
 * @ClassName MessageRequestPacket
 * @Description 消息数据包
 * @Author xieyi
 * @Data 2019/1/16
 */
@Data
public class MessageRequestPacket extends Packet {

    private String Message;

    @Override
    public Byte getCommand() {
        return Command.MESSAGE_REQUEST;
    }
}
