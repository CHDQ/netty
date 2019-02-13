package org.dq.netty.netty.chatroom;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;

import java.net.InetSocketAddress;

public class ChatServer {
    private final ChannelGroup channels = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
    private final EventLoopGroup group = new NioEventLoopGroup();
    private Channel channel;

    public ChannelFuture start(InetSocketAddress address) {
        var bootStart = new ServerBootstrap();
        bootStart.group(group).channel(NioServerSocketChannel.class).childHandler(createInitializer(channels));
        ChannelFuture future = bootStart.bind(address);
        future.syncUninterruptibly();
        channel = future.channel();//父channel
        return future;
    }

    public ChannelInitializer<Channel> createInitializer(ChannelGroup channels) {
        return new ChatServerInitializer(channels);
    }

    public void destroy() {
        if (channel != null) {
            channel.close();
        }
        channels.close();
        group.shutdownGracefully();
    }

    public static void main(String[] args) {
        var chatServer = new ChatServer();
        ChannelFuture future = chatServer.start(new InetSocketAddress(8080));
        //增加关闭钩子,在jvm关闭的时候调用
        Runtime.getRuntime().addShutdownHook(new Thread(() -> chatServer.destroy()));
        future.channel().closeFuture().syncUninterruptibly();
    }
}
