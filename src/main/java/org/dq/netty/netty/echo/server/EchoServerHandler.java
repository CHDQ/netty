package org.dq.netty.netty.echo.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
//        slice,duplicate,Unpooled.unmodifiableBuffer,order,readSlice,方法返回的ByteBuf，
//          实例都指向了原有的实例，所以修改了当前值，原有的ByteBuf中的值会跟着改变，可以用copy方法生成新实例
//        ByteBuf slice = ((ByteBuf) msg).slice();
//        slice.setByte(0, 'J');
        System.out.println("Server received:  " + in.toString(CharsetUtil.UTF_8));//读取完成后，ByteBuf不会被释放
        ctx.write(in);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);//此处释放ByteBuf资源
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
