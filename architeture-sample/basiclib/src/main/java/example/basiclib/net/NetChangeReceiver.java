package example.basiclib.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * 网络变化广播
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
 */

public class NetChangeReceiver extends BroadcastReceiver {

    private static final int TYPE_NONE = -2;

    // default type is WiFi，if you open app in WIFI environment, it will not show toast;
    private static int lastType = ConnectivityManager.TYPE_WIFI;
    private static String CHECKOUT_TO_WIGI = "已切换为WIFI网络";
    private static String CHECKOUT_TO_MOBILE = "已切换为数据流量";
    private static String CHECKOUT_TO_INVALID = "当前网络不可用";

    @SuppressWarnings("all")
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isAvailable()) {
            int type = netInfo.getType();
            if (type == ConnectivityManager.TYPE_WIFI && type != lastType) {                        //WiFi网络
                Toast.makeText(context, CHECKOUT_TO_WIGI, Toast.LENGTH_SHORT).show();
            } else if (type == ConnectivityManager.TYPE_MOBILE && type != lastType) {               //移动网络
                Toast.makeText(context, CHECKOUT_TO_MOBILE, Toast.LENGTH_SHORT).show();
            }
            lastType = type;
        } else {
            Toast.makeText(context, CHECKOUT_TO_INVALID, Toast.LENGTH_SHORT).show();
            lastType = TYPE_NONE;
        }
    }
}
