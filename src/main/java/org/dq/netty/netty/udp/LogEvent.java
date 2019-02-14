package org.dq.netty.netty.udp;

import lombok.Data;

import java.net.InetSocketAddress;

@Data
public class LogEvent {
    private static final byte SEPARATOR = ':';
    private final InetSocketAddress source;
    private final String logFile;
    private final String msg;
    private final long received;

    public LogEvent(InetSocketAddress source, long received, String logFile, String msg) {
        this.source = source;
        this.logFile = logFile;
        this.msg = msg;
        this.received = received;
    }

    public LogEvent(String logFile, String msg) {
        this(null, -1, logFile, msg);
    }

}
