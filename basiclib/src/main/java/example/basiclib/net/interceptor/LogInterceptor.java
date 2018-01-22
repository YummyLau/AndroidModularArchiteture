package example.basiclib.net.interceptor;

import android.util.Log;

import java.io.IOException;

import example.basiclib.Constants;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;

/**
 * log interceptor
 * output log that show in logcat to show http action
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
 */
public class LogInterceptor implements Interceptor {

    private static final String TAG = LogInterceptor.class.getSimpleName();

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Response response;
        try {
            long t1 = System.nanoTime();
            response = chain.proceed(request);
            long t2 = System.nanoTime();
            BufferedSource source = response.body().source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();
            String logStr = "\n-------------------- HTTP--------------------"
                    .concat("\nmethod -> ").concat(request.method())
                    .concat("\nurl -> ").concat(request.url() + "")
                    .concat("\ncode -> ").concat(response.code() + "")
                    .concat("\ntime -> ").concat((t2 - t1) / 1e6d + " ms")
                    .concat("\nrequest headers -> ").concat(request.headers() + "")
                    .concat("\nrequest body -> ").concat(request.body() == null ? "" : bodyToString(request.body()))
                    .concat("\nresponse -> ").concat(buffer.clone().readString(Constants.UTF8));
            Log.i(TAG, logStr);
        } catch (IOException e) {
            throw e;
        }
        return response;
    }

    private static String bodyToString(final RequestBody requestBody) {
        try {
            final Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "to string fail from LogInterceptor!";
        }
    }
}
