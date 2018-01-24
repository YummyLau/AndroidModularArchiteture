package example.basiclib.net;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/13.
 */

public class ObjectUtils {

    public static boolean equals(Object o1, Object o2) {
        if (o1 == null) {
            return o2 == null;
        }
        if (o2 == null) {
            return false;
        }
        return o1.equals(o2);
    }
}
