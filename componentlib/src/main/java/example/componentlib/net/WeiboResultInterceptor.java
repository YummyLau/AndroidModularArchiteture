package example.componentlib.net;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;

import example.basiclib.Constants;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Weibo api result interceptor to compat some http result!
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

public class WeiboResultInterceptor implements Interceptor {

    private static final String TAG = WeiboResultInterceptor.class.getSimpleName();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        MediaType contentType = response.body().contentType();

        BufferedSource source = response.body().source();
        source.request(Long.MAX_VALUE);
        Buffer buffer = source.buffer();

        WeiboHttpStatus httpCode = null;
        if (contentType.type().equals("application") && contentType.subtype().equals("json")) {
            String result = buffer.clone().readString(Constants.UTF8);
            try {
                JSONObject jsonObject = new JSONObject(result);
                int errorCode = jsonObject.optInt("error_code");
                httpCode = WeiboHttpStatusGetter.CODE_SPARSE_ARRAY.get(errorCode);
                //http://open.weibo.com/wiki/Error_code 该文档不全
                if (errorCode != 0 && httpCode == null) {
                    String errorMsg = jsonObject.optString("error");
                    httpCode = new WeiboHttpStatus(errorCode, errorMsg);
                    WeiboHttpStatusGetter.CODE_SPARSE_ARRAY.append(errorCode, httpCode);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        if (httpCode != null) {
            throw new WeiboApiException(httpCode);
        }
        return response;
    }
}
