package site.kkuil.netty;

import site.kkuil.netty.utils.ByteBufferUtil;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class TestCharSet {
    public static void main(String[] args) {
        String str = "Hello World!";
        byte[] bytes = str.getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.put(bytes);
        ByteBufferUtil.debugAll(buffer);

        ByteBuffer buffer1 = StandardCharsets.UTF_8.encode(str);
        ByteBufferUtil.debugAll(buffer1);
    }
}
