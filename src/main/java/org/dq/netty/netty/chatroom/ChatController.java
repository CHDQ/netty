package org.dq.netty.netty.chatroom;

import lombok.extern.slf4j.Slf4j;
import org.dq.netty.netty.chatroom.frame.WSMapping;
import org.springframework.stereotype.Component;

@WSMapping("chat")
@Component
public class ChatController {
    @WSMapping("broadcastMsg")
    public void broadcastMsg(String msg) {
        ChatServer.publishMsg(msg);//直接广播消息,路由实现不同逻辑
    }

    @WSMapping("power")
    public void power(String msg) {
        ChatServer.publishMsg(msg + ".chdq");//直接广播消息,路由实现不同逻辑
    }
}
