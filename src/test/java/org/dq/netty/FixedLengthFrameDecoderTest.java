package org.dq.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.*;

public class FixedLengthFrameDecoderTest {
    @Test
    public void testFramesDecoded() {
        var byteBuf = Unpooled.buffer();
        for (var i = 0; i < 9; i++) {
            byteBuf.writeByte(i);
        }
        var input = byteBuf.duplicate();
        var embeddedChannel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));
//        assertTrue(embeddedChannel.writeInbound(input.retain()));//该断言通过
        assertTrue(embeddedChannel.writeInbound(input.readBytes(2)));//该顺序下，断言不通过，因为readInbound获取不到内容
        assertTrue(embeddedChannel.writeInbound(input.readBytes(7)));
        assertTrue(embeddedChannel.finish());
        var read = (ByteBuf) embeddedChannel.readInbound();
        assertEquals(byteBuf.readBytes(3), read);
        read.release();
        read = embeddedChannel.readInbound();
        assertEquals(byteBuf.readBytes(3), read);
        read.release();
        read = embeddedChannel.readInbound();
        assertEquals(byteBuf.readBytes(3), read);
        read.release();
        assertTrue(embeddedChannel.readInbound() == null);
        byteBuf.release();
    }
}
