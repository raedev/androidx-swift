package androidx.swift.content;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.app.ActivityCompat;
import androidx.swift.AppSwift;

/**
 * 资源文件管理
 * @author rae
 * @since 2020/10/17
 */
public final class AppResources {

    private static Context context() {
        return AppSwift.getContext();
    }

    /**
     * 获取字符串
     * @param resId 字符串Id
     */
    public static String getString(@StringRes int resId) {
        return context().getString(resId);
    }

    /**
     * 获取字符串
     * @param resId 字符串Id
     * @param args 参数信息
     */
    public static String getString(@StringRes int resId, Object... args) {
        return context().getString(resId, args);
    }

    /**
     * 获取图片资源
     * @param resId 图片资源Id
     */
    public static Drawable getDrawable(@DrawableRes int resId) {
        return ActivityCompat.getDrawable(context(), resId);
    }

    /**
     * 获取图片资源
     * @param name 图片资源名称
     */
    @Nullable
    public static Drawable getDrawable(String name) {
        int resId = context().getResources().getIdentifier(name, "drawable", context().getPackageName());
        return resId > 0 ? ActivityCompat.getDrawable(context(), resId) : null;
    }

    /**
     * 获取颜色
     * @param resId 颜色Id
     */
    public static int getColor(@ColorRes int resId) {
        return ActivityCompat.getColor(context(), resId);
    }

}
