package org.dq.netty.netty.chatroom;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.CharsetUtil;

public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private final ChannelGroup channels;

    public TextWebSocketFrameHandler(ChannelGroup channels) {
        this.channels = channels;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String str = msg.text() + ".dq";//修改请求过来的数据
        ByteBuf buf = Unpooled.copiedBuffer(str, CharsetUtil.UTF_8);
        channels.writeAndFlush(new TextWebSocketFrame(buf));//增加引用计数，并将接收到的消息，写入到所有连接的客户端
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {//握手成功
            ctx.pipeline().remove(HttpRequestHandler.class);
            channels.writeAndFlush(new TextWebSocketFrame("Client " + ctx.channel() + " joined"));
            channels.add(ctx.channel());
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}