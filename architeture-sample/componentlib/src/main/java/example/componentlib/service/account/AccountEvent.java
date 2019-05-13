package example.componentlib.service.account;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import androidx.annotation.IntDef;

/**
 * account event
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

public class AccountEvent {

    public static final int LOGIN_TYPE = 0x01;
    public static final int LOGOUT_TYPE = 0x02;

    public int type;

    public AccountEvent(@AccountEventType int type) {
        this.type = type;
    }

    @IntDef({LOGIN_TYPE, LOGOUT_TYPE})
    @Target({ElementType.PARAMETER, ElementType.FIELD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AccountEventType {

    }
}

