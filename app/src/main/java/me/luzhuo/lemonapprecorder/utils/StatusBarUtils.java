package me.luzhuo.lemonapprecorder.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * 自定义状态栏工具类
 */
public class StatusBarUtils {

    /**
     * 设置透明状态栏
     * @param activity
     */
    public static void setTranslucentStatus(Activity activity){
        if (activity == null) return;

        //取消状态栏修改颜色
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);//显示状态栏
		}
    }

    /**
     * 设置StatusBar(自定义的,假的)的高度
     */
    public static void setStatusBarViewHeight(Context context, View view){
        if(context == null || view == null) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.LayoutParams linearParams = (ViewGroup.LayoutParams) view.getLayoutParams();
            linearParams.height = StatusBarUtils.getStatusBarHeight(context);
            view.setLayoutParams(linearParams);
            view.setVisibility(View.VISIBLE);
        }else{
            view.setVisibility(View.GONE);
        }
    }

    /**
     * 获取手机状态栏高度(px)
     */
    public static int getStatusBarHeight(Context context) {
        int result = 25;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
