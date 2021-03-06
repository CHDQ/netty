package org.dq.netty.nio;

/**
 * 多路复用选择器selector实现
 */
public class TimeServer {
    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {

            }
        }
        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
        new Thread(timeServer, "NIO-org.dq.netty.nio.MultiplexerTimeServer-001").start();
    }
}
