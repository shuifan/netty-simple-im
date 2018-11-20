package time.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import kryo.KryoSerializer;
import time.Message;

/**
 * @author fandong
 * @create 2018/11/7
 */
public class Encoder extends MessageToByteEncoder<Message> {

    @Override
    public void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
        System.out.println("TimeEncoder");
        KryoSerializer.serialize(message, byteBuf);
    }
}
