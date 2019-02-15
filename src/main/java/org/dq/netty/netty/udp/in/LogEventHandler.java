package org.dq.netty.netty.udp.in;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.dq.netty.netty.udp.out.LogEvent;

public class LogEventHandler extends SimpleChannelInboundHandler<LogEvent> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogEvent msg) throws Exception {
        System.out.println("[" + msg.getLogFile() + "] : " + msg.getMsg());
    }
}
