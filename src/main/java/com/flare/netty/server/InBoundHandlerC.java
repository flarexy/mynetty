package com.flare.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @ClassName InBoundHandlerC
 * @Description InBoundHandlerC
 * @Author xieyi
 * @Data 2019/1/17
 */
public class InBoundHandlerC extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("测试CCC：" + msg);
        super.channelRead(ctx, msg);
    }
}
