/* Copyright 2016 Luzhuo. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.luzhuo.lemonapprecorder.utils;

import android.content.Context;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/11/6 17:28
 * <p>
 * Description: 工具类
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public class LemonUtils{

	/**
	 * 根据手机的dpi将 dip单位 转为 px(像素)单位
	 * @param context 上下文
	 * @param dipValue dip值
	 * @return px像素值
	 */
	public static int dipTopx(Context context, float dipValue) {
		// 获取屏幕的dpi(密度)
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}
	
	/**
	 * 根据手机的dpi将 px(像素)单位 转成为 dip单位
	 * @param context 上下文
	 * @param pxValue 像素值
	 * @return dip值
	 */
	public static int pxTodip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	private static Toast quickToast;
	/**
	 * 快速显示的吐司
	 * @param context
	 * @param msg
	 */
	public static void showQuickToast(Context context, String msg) {
		if(quickToast == null)
			quickToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
		quickToast.setText(msg);
		quickToast.show();
	}
	
//	/**
//	 * 根据包名获取图标
//	 * @return Drawable/null
//	 */
//	public static Drawable getAppIcon(Context context,String packname){
//		PackageManager pm = context.getPackageManager();
//		//所有的安装在系统上的应用程序包信息。
//		List<PackageInfo> packInfos = pm.getInstalledPackages(0);
//		for(PackageInfo packInfo : packInfos){
//			if(packname.equals(packInfo.packageName)){
//				return packInfo.applicationInfo.loadIcon(pm);
//			}
//		}
//		return null;
//	}
	
//	public static class AppInfo {
//		public Drawable icon;
//		public String name;
//		public String packname;
//		public int uid;
//		public boolean inRom; //安装位置:true手机内存;falseSD卡
//		public boolean userApp; //用户应用:true用户应用;false系统
//	}
	
//	/**
//	 * 将Drawable保存到文件
//	 */
//	public static void saveDrawable(Drawable drawable, File file) throws Exception{
//		Bitmap bitmap=((BitmapDrawable)drawable).getBitmap(); // 先把Drawable转成Bitmap
//		saveBitmap(bitmap, file);
//	}
//
//	/**
//	 * 将Bitmap保存到文件
//	 */
//	public static void saveBitmap(Bitmap bitmap, File file) throws Exception{
//		FileOutputStream fop = new FileOutputStream(file);
//		bitmap.compress(Bitmap.CompressFormat.PNG, 100, fop); // 格式可以为jpg,png,jpg不能存储透明
//		fop.close();
//	}
	
	public static String getStringDate(long dates){
		Date date = new Date(dates);
		SimpleDateFormat sdf = new SimpleDateFormat("上次更新: yyyy-MM-dd");
		return sdf.format(date);
	}
	
//	/**
//	 * 获取版本号
//	 * @param context
//	 * @return
//	 */
//	public static String getVersion(Context context) {
//		PackageManager pm = context.getPackageManager();
//		try {
//			// 代表的就是清单文件的信息。
//			PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
//			return packageInfo.versionName;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "";
//		}
//	}
	
//	/**
//	 * 从文件获取图片
//	 * @param filePath
//	 * @return
//	 */
//	public static Bitmap getBitmapFromFile(File filePath){
//		Bitmap bitmap = BitmapFactory.decodeFile(filePath.getPath());
//		return bitmap;
//	}
	
//	/**
//	 * 拷贝文件
//	 * @param shujuyuan 数据源
//	 * @param file 目的地
//	 * @throws IOException
//	 */
//	public static void copyFile(File shujuyuan, File file) {
//		BufferedInputStream bis = null;
//		BufferedOutputStream bos = null;
//		try{
//			if(!file.getParentFile().exists()) file.getParentFile().mkdirs();
//			if(!file.exists()) file.createNewFile();
//
//			bis = new BufferedInputStream(new FileInputStream(shujuyuan));
//			bos = new BufferedOutputStream(new FileOutputStream(file));
//
//			byte[] bys = new byte[1024];
//			int len;
//			while((len=bis.read(bys))!=-1){
//				bos.write(bys, 0, len);
//				bos.flush();
//			}
//		}catch(Exception e){ e.printStackTrace();
//		}finally{
//			if(bos != null)
//				try { bos.close();
//				} catch (Exception e) { }
//			if(bis != null)
//				try { bis.close();
//				} catch (Exception e) { }
//		}
//	}
	

}