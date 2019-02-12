package org.dq.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbsIntegerEncoderTest {
    @Test
    public void testEncoded() {
        ByteBuf byteBuf = Unpooled.buffer();
        for (var i = 0; i < 10; i++) {
            byteBuf.writeInt(i);
        }
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new AbsIntegerEncoder());
        assertTrue(embeddedChannel.writeOutbound(byteBuf.retain()));
        assertTrue(embeddedChannel.finish());
        for (var i = 0; i < 10; i++) {
            Object o = embeddedChannel.readOutbound();
            assertEquals(i,o);
        }
    }
}
