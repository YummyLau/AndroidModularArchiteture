package example.basiclib.util;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jaeger.library.StatusBarUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 状态栏 统一处理入口
 * 第三方库持续更新，作为与应用层交互类
 * 沉浸式策略：
 * 1. 支持>=4.4以上设备着色，若着色颜色接近白色，则需要高亮状态栏图文
 * 2. 支持fitsystemui下的状态栏着色，见{@link StatusbarUtils#setStatusbarColor}
 * 3. 支持头部imageview的沉浸，见{@link StatusbarUtils#setTranslucentForImageView}
 * 4. 支持透明状态栏，区别于3,>=5.0以上导航栏透明
 * 5. 支持多个fragment沉浸，fragment头部添加{}
 * StatusBarView 可定义bar_color属性制定颜色，默认是使用{@link}
 * statusBarView策略：
 * a) 4.4以下设备自动隐藏；
 * b) >=4.4需要高亮场景，自动隐藏（沉浸式取消）
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
 */

public class StatusbarUtils {


    //能处理状态栏的版本
    public static boolean matchHandleStatusbarVersion() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    //能高亮状态栏文本颜色的机型
    public static boolean matchLightStatusbarTextDevice() {
        if (matchHandleStatusbarVersion()) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                return RomUtils.isFlyme() || RomUtils.isMiui();
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    //状态栏颜色接近是否接近白色
    public static boolean matchNearWhiteColor(@ColorInt int color) {
        int[] colors = parseColorInt(color);
        if (colors != null &&
                colors.length == 4 &&
                colors[0] >= 255 * 0.7 &&
                colors[1] >= 255 * 0.7 &&
                colors[2] >= 255 * 0.7 &&
                colors[3] >= 255 * 0.7) {
            return true;
        } else {
            return false;
        }
    }

    //魅族 flyme4.0以上
    public static boolean lightFlymeStatusbarText(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    //小米 miui6 以上
    public static boolean lightMIUIStatusbarText(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    //高亮状态栏
    public static void lightStatusbarText(Activity activity) {
        //6.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int visibility = activity.getWindow().getDecorView().getSystemUiVisibility();
            visibility |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            activity.getWindow().getDecorView().setSystemUiVisibility(visibility);
        }

        if (lightFlymeStatusbarText(activity.getWindow(), true)) {
            return;
        }

        if (lightMIUIStatusbarText(activity.getWindow(), true)) {
            return;
        }
    }

    //用于判断fragment是否需要引用填充的statusBarView
    public static boolean matchColorStatucbarDevice(@ColorInt int color) {
        //>=4.4
        if (matchHandleStatusbarVersion()) {
            //颜色接近白色，所以需要高亮
            if (matchNearWhiteColor(color)) {
                //设备是否支持高亮
                return matchLightStatusbarTextDevice();
            } else {
                return true;
            }
        } else {
            return false;
        }
    }


    /**
     * 状态栏着色，兼容颜色变化，不会影响到导航栏，支持扩展透明值
     *
     * @param activity
     * @param color
     */
    public static void setStatusbarColor(Activity activity, @ColorInt int color) {
        if (matchHandleStatusbarVersion()) {
            if (matchNearWhiteColor(color)) {
                if (matchLightStatusbarTextDevice()) {
                    StatusBarUtil.setColor(activity, color, 0);
                    StatusbarUtils.lightStatusbarText(activity);
                }else {
                    //因字体不能修改背景不能设成白色的设置为黑色
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Window window = activity.getWindow();
                        window.setStatusBarColor(ContextCompat.getColor(activity,android.R.color.black));
                    }
                }
            } else {
                StatusBarUtil.setColor(activity, color, 0);
            }
        }
    }


    /**
     * 原理是让状态栏透明并允许content布局layout到状态栏
     * 不会影响到导航栏
     * 同{@link StatusbarUtils#setTranslucentForImageView 基本一致 }
     *
     * @param activity
     * @param activity               color
     * @param needOffsetView         需要偏移的view，添加一个状态栏高度的marginTop增量
     * @param needLightStatusbarText 是否适配状态栏字体
     */
    public static void setStatusbarWithFragment(Activity activity, View needOffsetView, boolean needLightStatusbarText) {
        if (matchHandleStatusbarVersion()) {
            if (needLightStatusbarText) {
                if (matchLightStatusbarTextDevice()) {
                    StatusBarUtil.setTranslucentForImageViewInFragment(activity, needOffsetView);
                    StatusbarUtils.lightStatusbarText(activity);
                    return;
                } else {
                    //因字体不能修改背景不能设成白色的设置为黑色
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Window window = activity.getWindow();
                        window.setStatusBarColor(ContextCompat.getColor(activity,android.R.color.black));
                    }
                    return;
                }
            }
            StatusBarUtil.setTranslucentForImageViewInFragment(activity, needOffsetView);
        }
    }

    /**
     * 原理是让状态栏透明并允许content布局layout到状态栏
     * 不会影响到导航栏
     *
     * @param activity
     * @param statusBarAlpha
     * @param needOffsetView
     * @param needLightStatusbarText 是否适配状态栏字体
     */
    public static void setTranslucentForImageView(Activity activity, @IntRange(from = 0, to = 255) int statusBarAlpha, View needOffsetView,
                                                  boolean needLightStatusbarText) {
        if (matchHandleStatusbarVersion()) {
            if (needLightStatusbarText) {
                if (matchLightStatusbarTextDevice()) {
                    StatusBarUtil.setTranslucentForImageView(activity, statusBarAlpha, needOffsetView);
                    StatusbarUtils.lightStatusbarText(activity);
                    return;
                } else {
                    //因字体不能修改背景不能设成白色的设置为黑色
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Window window = activity.getWindow();
                        window.setStatusBarColor(ContextCompat.getColor(activity,android.R.color.black));
                    }
                    return;
                }
            }
            StatusBarUtil.setTranslucentForImageView(activity, statusBarAlpha, needOffsetView);
        }
    }

    /**
     * 状态栏透明
     * 影响到导航栏
     *
     * @param activity
     */
    public static void setStatusbarTranslucent(Activity activity) {
        StatusBarUtil.setTranslucent(activity, 0);
    }

    /**
     * 分解colorInt
     *
     * @param color
     * @return
     */
    private static int[] parseColorInt(@ColorInt int color) {
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return new int[]{alpha, red, green, blue};
    }
}
