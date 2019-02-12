package org.dq.netty.netty.bootstrap;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.dq.netty.netty.echo.client.EchoClientHandler;

import java.net.InetSocketAddress;

/**
 * 在channel中创建bootstrap，复用EventLoop
 */
public class CreateServerFromChannel {
    private final int port;

    public CreateServerFromChannel(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        new CreateServerFromChannel(8089).start();
    }

    public void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            final AttributeKey<Integer> key = AttributeKey.newInstance("ID");//使用属性值
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(group).channel(NioServerSocketChannel.class).localAddress(new InetSocketAddress(port)).childHandler(new SimpleChannelInboundHandler<ByteBuf>() {
                ChannelFuture connectFuture;

                @Override
                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                    Bootstrap bootstrap = new Bootstrap();
                    bootstrap.channel(NioSocketChannel.class).handler(new SimpleChannelInboundHandler<ByteBuf>() {
                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                            System.out.println("Receive data");
                        }
                    });
                    bootstrap.handler(new EchoClientHandler());
                    bootstrap.group(ctx.channel().eventLoop());//在建立连接之后，创建一个客户端，并且使用channel所在的EventLoop，完成新的客户端的生命周期，避免了上下文切换（cpu切换线程带来的开销），避免了额外的线程开销
                    connectFuture = bootstrap.connect(new InetSocketAddress("127.0.0.1", 8080));
                }

                @Override
                protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                    System.out.println(ctx.channel().attr(key).get());//获取属性值
                    if (connectFuture.isDone()) {
                        System.out.println("data");
                    }
                }
            });
            serverBootstrap.attr(key, 1234);//设置属性值，后面在channel中调用
            ChannelFuture future = serverBootstrap.bind().sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully().sync();//释放线程资源
        }
    }
}
