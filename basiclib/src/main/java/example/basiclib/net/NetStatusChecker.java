package example.basiclib.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * net checker
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
 */

public class NetStatusChecker {

    private static final String TAG = NetStatusChecker.class.getSimpleName();

    public static final int NETWORK_TYPE_INVALID = 0;
    public static final String NETWORK_NAME_INVALID = "invalid";

    public static final int NETWORK_TYPE_2G = 1;
    public static final String NETWORK_NAME_2G = "2G";

    public static final int NETWORK_TYPE_3G = 2;
    public static final String NETWORK_NAME_3G = "3G";

    public static final int NETWORK_TYPE_4G = 3;
    public static final String NETWORK_NAME_4G = "4G";

    public static final int NETWORK_TYPE_WIFI = 4;
    public static final String NETWORK_NAME_WIFI = "wifi";

    public static final int NETWORK_TYPE_MOBILE = 5;
    public static final String NETWORK_NAME_MOBILE = "mobile";


    @SuppressWarnings("all")
    public static int getNetworkType(Context context) {

        int type = NETWORK_TYPE_INVALID;

        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                switch (networkInfo.getType()) {
                    case ConnectivityManager.TYPE_WIFI: {
                        type = NETWORK_TYPE_WIFI;
                        break;
                    }
                    case ConnectivityManager.TYPE_MOBILE: {
                        String _strSubTypeName = networkInfo.getSubtypeName();
                        int networkType = networkInfo.getSubtype();                         // TD-SCDMA  networkType is 17

                        switch (networkType) {
                            //2G type
                            case TelephonyManager.NETWORK_TYPE_GPRS:
                            case TelephonyManager.NETWORK_TYPE_EDGE:
                            case TelephonyManager.NETWORK_TYPE_CDMA:
                            case TelephonyManager.NETWORK_TYPE_1xRTT:
                            case TelephonyManager.NETWORK_TYPE_IDEN: {                      //api<8 : replace by 11
                                type = NETWORK_TYPE_2G;
                                break;
                            }
                            //3G type
                            case TelephonyManager.NETWORK_TYPE_UMTS:
                            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                            case TelephonyManager.NETWORK_TYPE_HSDPA:
                            case TelephonyManager.NETWORK_TYPE_HSUPA:
                            case TelephonyManager.NETWORK_TYPE_HSPA:
                            case TelephonyManager.NETWORK_TYPE_EVDO_B:                      //api<9 : replace by 14
                            case TelephonyManager.NETWORK_TYPE_EHRPD:                       //api<11 : replace by 12
                            case TelephonyManager.NETWORK_TYPE_HSPAP: {                     //api<13 : replace by 15
                                type = NETWORK_TYPE_3G;
                                break;
                            }
                            //4G type
                            case TelephonyManager.NETWORK_TYPE_LTE: {                        //api<11 : replace by 13
                                type = NETWORK_TYPE_4G;
                                break;
                            }
                            default:
                                // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                                if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                                    type = NETWORK_TYPE_3G;
                                } else {
                                    type = NETWORK_TYPE_MOBILE;
                                }
                                break;
                        }
                        Log.d(TAG, "Network getSubtype : " + Integer.valueOf(networkType).toString());
                        break;
                    }
                }
            }
        }
        return type;
    }

    public static String getNetworkTypeName(Context context) {
        String name = NETWORK_NAME_INVALID;
        switch (getNetworkType(context)) {
            case NETWORK_TYPE_INVALID:
                name = NETWORK_NAME_INVALID;
                break;
            case NETWORK_TYPE_2G:
                name = NETWORK_NAME_2G;
                break;
            case NETWORK_TYPE_3G:
                name = NETWORK_NAME_3G;
                break;
            case NETWORK_TYPE_4G:
                name = NETWORK_NAME_4G;
                break;
            case NETWORK_TYPE_WIFI:
                name = NETWORK_NAME_WIFI;
                break;
            case NETWORK_TYPE_MOBILE:
                name = NETWORK_NAME_MOBILE;
                break;
        }
        return name;
    }

    public static boolean networkAvailable(Context context) {
        return !(NETWORK_TYPE_INVALID == getNetworkType(context));
    }

    public static boolean isWifi(Context context) {
        return NETWORK_TYPE_WIFI == getNetworkType(context);
    }

    public static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

            }
        }};
        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
