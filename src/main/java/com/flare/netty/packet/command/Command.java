package com.flare.netty.packet.command;

/**
 * @ClassName Command
 * @Description 指令
 * @Author xieyi
 * @Data 2019/1/15
 */
public interface Command {

    Byte LOGIN_REQUEST = 1;

    Byte LOGIN_RESPONSE = 2;

    Byte MESSAGE_REQUEST = 3;

    Byte MESSAGE_RESPONSE = 4;
}
