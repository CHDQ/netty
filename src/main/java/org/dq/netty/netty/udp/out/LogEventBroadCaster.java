package org.dq.netty.netty.udp.out;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;

public class LogEventBroadCaster {
    private final EventLoopGroup group;
    private final Bootstrap bootstrap;
    private final File file;

    public LogEventBroadCaster(InetSocketAddress socketAddress, File file) {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioDatagramChannel.class).option(ChannelOption.SO_BROADCAST, true).handler(new LogEventEncoder(socketAddress));
        this.file = file;
    }

    public void run() throws InterruptedException, IOException {
        Channel channel = bootstrap.bind(0).sync().channel();
        long pointer = 0;
        while (true) {
            long length = file.length();
            if (length < pointer) {
                pointer = length;
            } else if (length > pointer) {
                RandomAccessFile accessFile = new RandomAccessFile(file, "r");
                accessFile.seek(pointer);//设置读取文件的位置
                String line;
                while ((line = accessFile.readLine()) != null) {
                    channel.writeAndFlush(new LogEvent(null, -1, file.getAbsolutePath(), line));//逐行写入日志
                }
                pointer = accessFile.getFilePointer();
                accessFile.close();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                    break;
                }
            }

        }
    }

    public void stop() {
        group.shutdownGracefully();
    }

    public static void main(String[] args) {
        LogEventBroadCaster logEventBroadCaster = new LogEventBroadCaster(new InetSocketAddress("255.255.255.255", 8080), new File(LogEventBroadCaster.class.getClassLoader().getResource("").getPath() + "/log4j.properties"));
        try {
            logEventBroadCaster.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            logEventBroadCaster.stop();
        }
    }
}
