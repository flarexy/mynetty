package com.flare.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * @ClassName OutBoundHandlerC
 * @Description OutBoundHandlerC
 * @Author xieyi
 * @Data 2019/1/17
 */
public class OutBoundHandlerC extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("OutBoundHandlerC：" + msg);
        super.write(ctx, msg, promise);
    }
}
