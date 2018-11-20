package kryo;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author fandong
 * @create 2018/11/8
 * netty集成版本
 */
public class KryoSerializer {

    private static final KryoFactory KRYO_FACTORY = new KryoFactory();

    public static void serialize(Object o, ByteBuf out) throws IOException {
        Kryo kryo = KRYO_FACTORY.getThreadLocalKryo();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos, 10240);
        kryo.writeClassAndObject(output, o);
        output.flush();
        output.close();

        byte[] b = baos.toByteArray();
        System.out.println(b.length);
        baos.flush();
        baos.close();
        out.writeBytes(b);
        System.out.println("serial");
    }

    public static Object deserialize(ByteBuf in){
        System.out.println("deserial");
        Input input = new Input(new ByteBufInputStream(in));
        Kryo kryo = KRYO_FACTORY.getThreadLocalKryo();
        return kryo.readClassAndObject(input);
    }
}
