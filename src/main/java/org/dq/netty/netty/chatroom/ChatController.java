package org.dq.netty.netty.chatroom;

import org.dq.netty.netty.chatroom.frame.WSMapping;

@WSMapping("chat")
public class ChatController {
    @WSMapping("broadcastMsg")
    public void broadcastMsg(String msg) {
        ChatServer.publishMsg(msg);//直接广播消息
    }
}
