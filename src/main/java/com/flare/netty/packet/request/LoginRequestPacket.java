package com.flare.netty.packet.request;

import com.flare.netty.packet.command.Command;
import com.flare.netty.packet.Packet;
import lombok.Data;

/**
 * @ClassName LoginRequestPacket
 * @Description 登录数据包
 * @Author xieyi
 * @Data 2019/1/15
 */
@Data
public class LoginRequestPacket extends Packet {

    private String userId;

    private String username;

    private String password;


    @Override
    public Byte getCommand() {
        return Command.LOGIN_REQUEST;
    }
}
