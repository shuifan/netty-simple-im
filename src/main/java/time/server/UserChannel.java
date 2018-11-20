package time.server;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fandong
 * @create 2018/11/15
 * 存储用户和channel的映射
 */
public class UserChannel {

    private static final Map<String, Channel> MAP = new ConcurrentHashMap<String, Channel>(16);

    public static void save(String username, Channel channel){
        MAP.put(username, channel);
    }

    public static void delete(String username){
        MAP.remove(username);
    }

    public static Channel getChannelByUsername(String username){
        return MAP.get(username);
    }
}
