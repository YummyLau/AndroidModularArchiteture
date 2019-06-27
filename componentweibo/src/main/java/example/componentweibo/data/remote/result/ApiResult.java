package example.componentweibo.data.remote.result;

import com.google.gson.annotations.SerializedName;

/**
 * 微博接口错误代码说明
 * <a href="http://open.weibo.com/wiki/Error_code">Error code</a>
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/14.
 */

public class ApiResult {

    @SerializedName("request")
    public String request;

    @SerializedName("error_code")
    public int errorCode;

    @SerializedName("error")
    public String errorMsg;
}
