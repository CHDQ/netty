package org.dq.netty.aio;


public class TimeClient {
    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
            }
        }
        new Thread(new AsyncTimeClientHandler("127.0.0.1", port), "org.dq.netty.aio.TimeClient-001").start();
    }
}
