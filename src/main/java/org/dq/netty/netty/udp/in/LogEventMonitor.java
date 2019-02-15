package org.dq.netty.netty.udp.in;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;

public class LogEventMonitor {
    private EventLoopGroup group;
    private final Bootstrap bootstrap;

    public LogEventMonitor(InetSocketAddress address) {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioDatagramChannel.class).option(ChannelOption.SO_BROADCAST, true).handler(new ChannelInitializer<>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline().addLast(new LogEventDecoder()).addLast(new LogEventHandler());
            }
        }).localAddress(address);
    }

    public Channel bind() {
        ChannelFuture future = bootstrap.bind().syncUninterruptibly();
        return future.channel();
    }

    public void stop() {
        group.shutdownGracefully();
    }

    public static void main(String[] args) {
        LogEventMonitor monitor = new LogEventMonitor(new InetSocketAddress(8080));
        try {
            Channel channel = monitor.bind();
            System.out.println("LogEventMonitor is running");
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            monitor.stop();
        }
    }
}
