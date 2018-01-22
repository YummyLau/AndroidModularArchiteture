package example.basiclib.support;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import example.basiclib.R;
import example.basiclib.util.FileUtils;


/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
 */
public class CrashManager implements UncaughtExceptionHandler {

    public static final String TAG = CrashManager.class.getSimpleName();

    public static final long SLEEP_TIME = 3000;

    private static CrashManager instance;

    private UncaughtExceptionHandler mDefaultHandler;
    private Context mContext;
    private Map<String, String> mInfoMap;
    private DateFormat mDateFormat;


    private CrashManager() {
        mInfoMap = new HashMap<>();
        mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    }

    public static CrashManager getInstance() {
        if (instance == null) {
            instance = new CrashManager();
        }
        return instance;
    }

    public void init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }


    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                Log.e(TAG, "error : InterruptedException");
            }
            // 退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    Toast.makeText(mContext, mContext.getString(R.string.basic_crash_toast), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }
        }.start();
        //收集设备参数信息   
        collectDeviceInfo(mContext);
        //保存日志文件   
        saveCrashInfo2File(ex);
        return true;
    }

    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                mInfoMap.put("VersionName", versionName);
                mInfoMap.put("VersionCode", versionCode);
            }
            long timestamp = System.currentTimeMillis();
            String time = mDateFormat.format(new Date());
            mInfoMap.put("CrashTime", time + "-" + timestamp);
        } catch (NameNotFoundException e) {
            Log.e(TAG, "an error occurred when collect package info");
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                mInfoMap.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occurred when collect crash info");
            }
        }
    }


    private String saveCrashInfo2File(Throwable ex) {

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : mInfoMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append("=").append(value).append("\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        Log.e(TAG, result);
        sendLog(result);
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            String time = mDateFormat.format(new Date());
            String fileName = "crash-" + time + "-" + timestamp + ".txt";

            String crashCachePath = FileUtils.getCrashLogDir();
            File crashCacheDir = new File(crashCachePath);
            if (!crashCacheDir.exists()) {
                crashCacheDir.mkdirs();
            }

            FileOutputStream fos = new FileOutputStream(crashCachePath + "/" + fileName);
            fos.write(sb.toString().getBytes());
            fos.close();

            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occurred while writing file...");
        }
        return null;
    }

    private void sendLog(String ex) {
        //you can send log to server
    }
}  