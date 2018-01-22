package example.componentlib.net;

import java.io.IOException;


/**
 * api exception
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
 */

public class WeiboApiException extends IOException {

    public WeiboHttpStatus httpStatus;


    public WeiboApiException(WeiboHttpStatus weiboHttpStatus) {
        httpStatus = weiboHttpStatus;
    }

    @Override
    public String getMessage() {
        if (httpStatus != null) {
            return httpStatus.msg;
        }
        return super.getMessage();
    }
}
