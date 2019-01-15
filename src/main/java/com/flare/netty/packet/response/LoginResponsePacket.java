package com.flare.netty.packet.response;

import com.flare.netty.packet.Packet;
import com.flare.netty.packet.command.Command;
import lombok.Data;

/**
 * @ClassName LoginResponsePacket
 * @Description 登录响应
 * @Author xieyi
 * @Data 2019/1/15
 */
@Data
public class LoginResponsePacket extends Packet {

    private Boolean success;

    private String msg;

    @Override
    public Byte getCommand() {
        return Command.LOGIN_RESPONSE;
    }
}
