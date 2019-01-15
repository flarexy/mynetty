package com.flare.netty.entity;

import lombok.Data;

/**
 * @ClassName LoginRequestPacket
 * @Description 登录数据包
 * @Author xieyi
 * @Data 2019/1/15
 */
@Data
public class LoginRequestPacket extends Packet{

    private Integer userId;

    private String username;

    private String password;


    @Override
    public Byte getCommand() {
        return Command.LOGIN_REQUEST;
    }
}
