package org.dq.netty.netty.chatroom;

import lombok.extern.slf4j.Slf4j;
import org.dq.netty.netty.chatroom.frame.WSMapping;
import org.springframework.stereotype.Component;

@WSMapping("chat")
@Component
@Slf4j
public class ChatController {
    @WSMapping("broadcastMsg")
    public void broadcastMsg(String msg) {
        log.info("access broadcastMsg msg:"+msg);
        ChatServer.publishMsg(msg);//直接广播消息,路由实现不同逻辑
    }

    @WSMapping("power")
    public void power(String msg) {
        log.info("access power msg:"+msg);
        ChatServer.publishMsg(msg + ".chdq");//直接广播消息,路由实现不同逻辑
    }
}
