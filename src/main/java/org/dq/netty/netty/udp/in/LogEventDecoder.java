package org.dq.netty.netty.udp.in;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;
import org.dq.netty.netty.udp.out.LogEvent;

import java.util.List;

public class LogEventDecoder extends MessageToMessageDecoder<DatagramPacket> {
    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket datagramPacket, List<Object> out) throws Exception {
        ByteBuf content = datagramPacket.content();
        int seperatorIndex = content.indexOf(0, content.readableBytes(), LogEvent.SEPARATOR);
        String fileName = content.slice(0, seperatorIndex).toString(CharsetUtil.UTF_8);
        String logMsg = content.slice(seperatorIndex + 1, content.readableBytes()).toString(CharsetUtil.UTF_8);
        out.add(new LogEvent(datagramPacket.sender(), content.readableBytes(), fileName, logMsg));
    }
}
