package kryo;

import com.esotericsoftware.kryo.Kryo;

/**
 * @author fandong
 * @create 2018/11/8
 */
public class KryoFactory {

    private final ThreadLocal<Kryo> holder = new ThreadLocal<Kryo>(){
        @Override
        protected Kryo initialValue() {
            return new Kryo();
        }
    };

    public Kryo getThreadLocalKryo(){
        return holder.get();
    }

}
