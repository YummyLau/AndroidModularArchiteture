package example.basiclib.net;


import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * http param
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

public class HttpParam {

    private final static int READ_TIME_OUT = 30;
    private final static int WRITE_TIME_OUT = 30;
    private final static int CONNECT_TIME_OUT = 15;

    public int connectTimeOut;
    public int readTimeOut;
    public int writeTimeOut;
    public String baseUrl;
    public boolean cache;
    public boolean debug;
    public Converter.Factory converterFactory;
    public CallAdapter.Factory callAdapterFactory;

    private HttpParam(Builder builder) {
        connectTimeOut = builder.connectTimeOut;
        readTimeOut = builder.readTimeOut;
        writeTimeOut = builder.writeTimeOut;
        baseUrl = builder.baseUrl;
        cache = builder.cache;
        debug = builder.debug;
        converterFactory = builder.converterFactory;
        callAdapterFactory = builder.callAdapterFactory;
    }

    public static class Builder {

        int connectTimeOut = CONNECT_TIME_OUT;
        int readTimeOut = READ_TIME_OUT;
        int writeTimeOut = WRITE_TIME_OUT;
        String baseUrl;
        boolean cache = false;          //default no user cache
        boolean debug = true;           //default open debug mode to show log
        Converter.Factory converterFactory;
        CallAdapter.Factory callAdapterFactory = RxJava2CallAdapterFactory.create();

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder connectTimeOut(int connectTimeOut) {
            this.connectTimeOut = connectTimeOut;
            return this;
        }

        public Builder readTimeOut(int readTimeOut) {
            this.readTimeOut = readTimeOut;
            return this;
        }

        public Builder writeTimeOut(int writeTimeOut) {
            this.writeTimeOut = writeTimeOut;
            return this;
        }

        public Builder cache(boolean userCache) {
            this.cache = userCache;
            return this;
        }

        public Builder debug(boolean isDebugMode) {
            this.debug = isDebugMode;
            return this;
        }

        public Builder converterFactory(Converter.Factory converterFactory) {
            this.converterFactory = converterFactory;
            return this;
        }

        public Builder callAdatperFactory(CallAdapter.Factory callAdapterFactory) {
            this.callAdapterFactory = callAdapterFactory;
            return this;
        }

        public HttpParam build() {
            return new HttpParam(this);
        }
    }
}
