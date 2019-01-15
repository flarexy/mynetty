package com.flare.netty.packet;

import lombok.Data;

/**
 * @ClassName Packet
 * @Description 指令数据包
 * @Author xieyi
 * @Data 2019/1/15
 */
@Data
public abstract class Packet {

    /**
     * 协议版本
     */
    private Byte version = 1;

    /**
     * 指令
     * @return
     */
    public abstract Byte getCommand();
}
