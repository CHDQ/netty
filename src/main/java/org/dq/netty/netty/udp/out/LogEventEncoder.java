package org.dq.netty.netty.udp.out;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.List;

public class LogEventEncoder extends MessageToMessageEncoder<LogEvent> {
    private final InetSocketAddress socketAddress;

    public LogEventEncoder(InetSocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, LogEvent logEvent, List<Object> out) throws Exception {
        byte[] logFile = logEvent.getLogFile().getBytes(CharsetUtil.UTF_8);
        byte[] msg = logEvent.getMsg().getBytes(CharsetUtil.UTF_8);
        ByteBuf buf = ctx.alloc().buffer(logFile.length + msg.length + 1);//获取byteBufHolder,通过ByteBuf池分配ByteBuf的空间
        buf.writeBytes(logFile);
        buf.writeByte(LogEvent.SEPARATOR);
        buf.writeBytes(msg);
        out.add(new DatagramPacket(buf, socketAddress));
    }
}
