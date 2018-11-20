package time;

import java.util.Date;

/**
 * @author fandong
 * @create 2018/11/7
 */
public class UnixTime {

    private final long value;

    public UnixTime() {
        this(System.currentTimeMillis() / 1000 + 2208988800L);
    }

    public UnixTime(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    @Override
    public String toString() {
        return new Date((value - 2208988800L) * 1000).toString();
    }
}
