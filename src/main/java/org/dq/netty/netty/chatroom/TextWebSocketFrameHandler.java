package org.dq.netty.netty.chatroom;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private final ChannelGroup channels;

    public TextWebSocketFrameHandler(ChannelGroup channels) {
        this.channels = channels;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        channels.writeAndFlush(msg.retain());//增加引用计数，并将接收到的消息，写入到所有连接的客户端
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {//握手成功
            ctx.pipeline().remove(HttpRequestHandler.class);
            channels.writeAndFlush(new TextWebSocketFrame("Client " + ctx.channel() + " joined"));
            channels.add(ctx.channel());
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
