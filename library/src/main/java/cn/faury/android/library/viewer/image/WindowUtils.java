package cn.faury.android.library.viewer.image;

import android.content.Context;
import android.view.WindowManager;

/**
 * Created by lei.pan on 2017/2/7.
 */

public class WindowUtils {
    private static Context mContext;

    private static int window_width;

    private static int window_height;

    public WindowUtils(Context context) {
        this.mContext = context;

    }

    /**
     * 获取屏幕宽度
     * @return 屏幕宽度
     */
    public static int getWidth(Context context)
    {
        mContext = context;
        WindowManager windowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        window_width = windowManager.getDefaultDisplay().getWidth();
        return window_width;
    }

    /**
     * 获取屏幕高度
     * @return 屏幕高度
     */
    public static int getHeight(Context context)
    {
        mContext = context;
        WindowManager windowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        window_height = windowManager.getDefaultDisplay().getHeight();
        return window_height;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
