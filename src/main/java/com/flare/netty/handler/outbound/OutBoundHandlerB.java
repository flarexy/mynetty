package com.flare.netty.handler.outbound;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * @ClassName OutBoundHandlerB
 * @Description OutBoundHandlerB
 * @Author xieyi
 * @Data 2019/1/17
 */
public class OutBoundHandlerB extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("OutBoundHandlerB：" + msg);
        super.write(ctx, msg, promise);
    }
}
