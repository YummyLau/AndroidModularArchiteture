package example.basiclib.net.interceptor;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import example.basiclib.Constants;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;

/**
 * add dynamic parame
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
 */
public abstract class AbsParameterInterceptor implements Interceptor {

    private Context context;

    public AbsParameterInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request oriRequest = chain.request();
        Request.Builder requestBuilder = oriRequest.newBuilder();

        Request request = requestBuilder.build();
        Response response = chain.proceed(request);

        //add dynamic head
        ArrayMap<String, String> propertyMap = getParameters(context);
        for (int i = 0, size = propertyMap.size(); i < size; i++) {
            requestBuilder.header(propertyMap.keyAt(i), propertyMap.valueAt(i));
        }

        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();

        if (HttpHeaders.hasBody(response) && !bodyEncoded(response.headers())) {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();
            Charset charset = Constants.UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(Constants.UTF8);
                } catch (UnsupportedCharsetException e) {
                    return response;
                }
            }
            if (!isPlaintext(buffer)) {
                return response;
            }

            if (contentLength != 0) {
                String result = buffer.clone().readString(charset);
                if (needRefreshParameter(context, result)) {
                    getNewParameter(context);
                    ArrayMap<String, String> newCookieMap = getParameters(context);
                    requestBuilder = oriRequest.newBuilder();
                    for (int i = 0, size = newCookieMap.size(); i < size; i++) {
                        requestBuilder.header(newCookieMap.keyAt(i), newCookieMap.valueAt(i));
                    }
                    requestBuilder.method(oriRequest.method(), oriRequest.body());
                    request = requestBuilder.build();
                    response = chain.proceed(request);
                }
            }
        }
        return response;
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    /**
     * 是否是明码文本
     *
     * @param buffer
     * @return
     */
    private boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false;
        }
    }

    public abstract boolean needRefreshParameter(Context context, String result);

    public abstract void getNewParameter(Context context);

    public abstract ArrayMap<String, String> getParameters(Context context);
}
