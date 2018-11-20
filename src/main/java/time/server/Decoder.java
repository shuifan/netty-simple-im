package time.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import kryo.KryoSerializer;
import time.Message;

import java.util.List;

/**
 * @author fandong
 * @create 2018/11/7
 */
public class Decoder extends ByteToMessageDecoder {

    @Override
    public void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("TimeDecoder");
        list.add(KryoSerializer.deserialize(byteBuf));
    }

}
