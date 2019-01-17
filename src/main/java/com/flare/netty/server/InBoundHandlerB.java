package com.flare.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @ClassName InBoundHandlerB
 * @Description InBoundHandlerB
 * @Author xieyi
 * @Data 2019/1/17
 */
public class InBoundHandlerB extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("测试BBB：" + msg);
        super.channelRead(ctx, msg);
    }
}
