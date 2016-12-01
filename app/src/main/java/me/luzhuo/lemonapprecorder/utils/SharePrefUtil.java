package me.luzhuo.lemonapprecorder.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * =================================================
 * 
 * 作者:卢卓
 * 
 * 版本:1.0
 * 
 * 创建日期:2015-9-12 下午3:51:51
 * 
 * 描述:操作SharePreferences数据存取工具类
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class SharePrefUtil {
	private static String tag = SharePrefUtil.class.getSimpleName();
	private final static String SP_NAME = "config";
	private static SharedPreferences sp;
	
	/**
	 * 默认保存到sd卡
	 */
	public static final String ICONSAVAEXTERNAL = "savaExternal"; // 应用图标的保存位置,是否保存到外部存储设备

	/**
	 * 保存布尔值
	 * @param context 上下文
	 * @param key 键
	 * @param value 值
	 */
	public static void saveBoolean(Context context, String key, boolean value) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, 0);
		    sp.edit().putBoolean(key, value).commit();
	}
	
	/**
	 * 获取布尔值
	 * @param context 上下文
	 * @param key 键
	 * @param defValue 默认值
	 * @return 值
	 */
	public static boolean getBoolean(Context context, String key,
			boolean defValue) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, 0);
		return sp.getBoolean(key, defValue);
	}
}
