package com.flare.netty.packet.request;

import com.flare.netty.packet.Packet;
import com.flare.netty.packet.command.Command;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName MessageRequestPacket
 * @Description 消息数据包
 * @Author xieyi
 * @Data 2019/1/16
 */
@Data
@NoArgsConstructor
public class MessageRequestPacket extends Packet {

    private String toUserId;

    private String message;

    public MessageRequestPacket(String toUserId, String message) {
        this.toUserId = toUserId;
        this.message = message;
    }

    @Override
    public Byte getCommand() {
        return Command.MESSAGE_REQUEST;
    }
}
