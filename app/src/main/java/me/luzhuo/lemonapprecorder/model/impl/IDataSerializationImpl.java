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
package me.luzhuo.lemonapprecorder.model.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import me.luzhuo.lemonapprecorder.bean.AppInfo;
import me.luzhuo.lemonapprecorder.bean.OldBean;
import me.luzhuo.lemonapprecorder.model.IConfigInfo;
import me.luzhuo.lemonapprecorder.model.IDataSerialization;
import me.luzhuo.lemonapprecorder.utils.GsonTools;
import me.luzhuo.lemonapprecorder.utils.LemonUtils;
import me.luzhuo.lemonapprecorder.utils.SharePrefUtil;

import static android.R.attr.data;
import static android.R.id.list;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/11/17 14:54
 * <p>
 * Description:
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public class IDataSerializationImpl implements IDataSerialization {

    /**
     * 存储目录(备份目录)
     */
    public static final String externalStorage = Environment.getExternalStorageDirectory().getAbsolutePath().concat("/Lemon/AppRecorder");

    /**
     * 保存分类的文件(备份目录)
     */
    public static final File classify = new File(externalStorage,"classify.lemon");

    /**
     * 保存app信息的文件(备份目录)
     */
    public static final File apps = new File(externalStorage,"apps.lemon");

    /**
     * 保存图标的文件夹(备份目录)
     */
    public static final File icons = new File(externalStorage,"icons");

    /**
     * 提示文字
     */
    public static final String[] taskName = new String[]{"正在读取数据库...", "正在写入文件...", "正在拷贝图标...", "正在读取文件...", "正在写入数据...", "正在清理数据...", "错误!请重试..."};

    private Context context;

    public IDataSerializationImpl(Context context){
        this.context = context;
    }

    @Override
    public void deleteFilesForDirectory(File Directory) {
        if(Directory.exists()){
            File[] sdFiles = Directory.listFiles();
            for (File f : sdFiles) {
                if(f.exists() && f.isFile()) f.delete();
            }
        }
    }

    @Override
    public List<String> loadClassifyForFile(File classifyFile) {
        try {
            StringBuffer buffer = new StringBuffer();
            FileInputStream fis = new FileInputStream(classifyFile);
            byte[] bs = new byte[fis.available()];
            fis.read(bs);
            fis.close();
            buffer.append(new String(bs, "utf-8"));
            String classifyData = buffer.toString();

            // 对旧数据进行处理
            List<OldBean.OldClassify> oldClassifyList = parsingOldClassify(classifyData);
            if(oldClassifyList != null){
                // 旧数据解析成功, 说明是旧格式的数据
                List<String> list = new ArrayList<>();
                for (OldBean.OldClassify oldclassify : oldClassifyList) {
                    list.add(oldclassify.classify);
                }
                return list;
            }

            List<String> list = new Gson().fromJson(classifyData, new TypeToken<List<String>>() {}.getType());

            return list;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 解析旧分类数据
     * @return 如果能解析,说明是旧格式的数据,将返回数据; 如果解析失败, 将返回null
     */
    private List<OldBean.OldClassify> parsingOldClassify(String classifyData){
        try{
            List<OldBean.OldClassify> list = new Gson().fromJson(classifyData, new TypeToken<List<OldBean.OldClassify>>() {}.getType());
            if(TextUtils.isEmpty(list.get(0).classify)) return null;
            return list;
        }catch (Exception e){
            return null;
        }
   }

    /**
     * 解析旧分类数据
     * @return 如果能解析,说明是旧格式的数据,将返回数据; 如果解析失败, 将返回null
     */
    private List<OldBean.OldAppinfo> parsingOldAppinfo(String appinfoData){
        try{
            List<OldBean.OldAppinfo> list = new Gson().fromJson(appinfoData, new TypeToken<List<OldBean.OldAppinfo>>() {}.getType());
            if(list.get(0).icon == null) return null;
            return list;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public List<AppInfo> loadAppInfosForFile(File appinfoFile) {
        try{
            // TODO: 2016/11/18 与上面的方法重复代码过多
            StringBuffer bufferapp = new StringBuffer();
            FileInputStream fisapp = new FileInputStream(appinfoFile);
            byte[] bsapp = new byte[fisapp.available()];
            fisapp.read(bsapp);
            fisapp.close();
            bufferapp.append(new String(bsapp,"utf-8"));
            String appinfoData = bufferapp.toString();

            // 对旧数据进行处理
            List<OldBean.OldAppinfo> oldAppinfoList = parsingOldAppinfo(appinfoData);
            if(oldAppinfoList != null){
                // 旧数据解析成功, 说明是旧格式的数据
                List<AppInfo> listapp = new ArrayList<>();
                for (OldBean.OldAppinfo oldAppinfo : oldAppinfoList) {
                    AppInfo appinfo = new AppInfo();
                    appinfo.iconFileName = new File(oldAppinfo.icon).getName();
                    appinfo.saveIconpath = IConfigInfo.SDCard; // 默认存在sd卡上
                    appinfo.classify = oldAppinfo.classify;
                    appinfo.appName = oldAppinfo.name;
                    appinfo.date = oldAppinfo.date;
                    appinfo.packName = oldAppinfo.packname;
                    appinfo.remarks = oldAppinfo.remarks;
                    listapp.add(appinfo);
                }
                return listapp;
            }

            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            List<AppInfo> listapp = gson.fromJson(appinfoData, new TypeToken<List<AppInfo>>() {}.getType());
            return listapp;
        }catch(Exception e){}
        return null;
    }

    /**
     * 写入分类信息到文件
     * @param classifyList
     */
    public boolean writeClassifyToFile(ArrayList classifyList, File saveFile) {
        try {
            // 把classifyList中的数据生成Json字符串,并写入文件
            String classifyJson = GsonTools.listToJson(classifyList);

            byte[] bytes = classifyJson.getBytes("utf-8");
            FileOutputStream fos = new FileOutputStream(saveFile);
            fos.write(bytes, 0, bytes.length);
            fos.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 写入应用信息到文件
     * @param appList
     */
    public boolean writeAppinfoToFile(ArrayList<AppInfo> appList, File saveFile) {
        try {
            // 把appinfoList中的数据生成Json字符串,并写入文件
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            String appinfoJson = gson.toJson(appList);

            byte[] bytes = appinfoJson.getBytes("utf-8");
            FileOutputStream fos = new FileOutputStream(saveFile);
            fos.write(bytes, 0, bytes.length);
            fos.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String saveDrawable(Drawable icon, File file) {
        try{
            Bitmap bitmap = ((BitmapDrawable)icon).getBitmap(); // 先把Drawable转成Bitmap
            return saveBitmap(bitmap, file);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public String saveBitmap(Bitmap bitmap, File file) {
        try{
            FileOutputStream fop = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fop); // 格式可以为jpg,png, jpg不能存储透明
            fop.close();
            return file.getAbsolutePath();
        }catch (Exception e){
            return null;
        }
    }

    public void copyFile(File shuJuYuan, File file) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try{
            if(!file.getParentFile().exists()) file.getParentFile().mkdirs();
            if(!file.exists()) file.createNewFile();

            bis = new BufferedInputStream(new FileInputStream(shuJuYuan));
            bos = new BufferedOutputStream(new FileOutputStream(file));

            byte[] bys = new byte[1024];
            int len;
            while((len=bis.read(bys))!=-1){
                bos.write(bys, 0, len);
                bos.flush();
            }
        }catch(Exception e){ e.printStackTrace();
        }finally{
            if(bos != null)
                try { bos.close();
                } catch (Exception e) { }
            if(bis != null)
                try { bis.close();
                } catch (Exception e) { }
        }
    }

}
