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

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.luzhuo.lemonapprecorder.bean.AppDatasCount;
import me.luzhuo.lemonapprecorder.bean.AppInfo;
import me.luzhuo.lemonapprecorder.dp.LemonOpenHelper;
import me.luzhuo.lemonapprecorder.model.IAppInfos;

import static me.luzhuo.lemonapprecorder.dp.LemonOpenHelper.TABLE_APP_TABLENAME;
import static me.luzhuo.lemonapprecorder.dp.LemonOpenHelper.TABLE_CLASSIFY_TABLENAME;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/11/6 15:05
 * <p>
 * Description:
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public class IAppinfosImpl implements IAppInfos {
    private LemonOpenHelper openHelper;
    private Context context;

    public IAppinfosImpl(Context context){
        this.context = context;
        openHelper = LemonOpenHelper.getInstance(context);
    }

    @Override
    public ArrayList<AppInfo> queryAllAppInfos() {
        SQLiteDatabase db = openHelper.getReadableDatabase();

        if(!db.isOpen()) return null;

        ArrayList<AppInfo> appinfos = new ArrayList<>();

        Cursor appInfoCursor = db.query(TABLE_APP_TABLENAME, null, null, null, null, null, LemonOpenHelper.TABLE_ID.concat(" DESC"));
        if(appInfoCursor != null && appInfoCursor.getCount() > 0) {
            while(appInfoCursor.moveToNext()) {
                AppInfo appInfo = new AppInfo();
                appInfo.appName = appInfoCursor.getString(appInfoCursor.getColumnIndex(LemonOpenHelper.TABLE_APP_APPNAME));
                appInfo.packName = appInfoCursor.getString(appInfoCursor.getColumnIndex(LemonOpenHelper.TABLE_APP_APPPACKNAME));
                appInfo.classify = appInfoCursor.getString(appInfoCursor.getColumnIndex(LemonOpenHelper.TABLE_APP_CLASSIFY));
                appInfo.date = appInfoCursor.getLong(appInfoCursor.getColumnIndex(LemonOpenHelper.TABLE_APP_DATE));
                appInfo.iconFileName = appInfoCursor.getString(appInfoCursor.getColumnIndex(LemonOpenHelper.TABLE_APP_ICONFILENAEM));
                appInfo.saveIconpath = appInfoCursor.getInt(appInfoCursor.getColumnIndex(LemonOpenHelper.TABLE_APP_SAVEICONPATH));
                appInfo.remarks = appInfoCursor.getString(appInfoCursor.getColumnIndex(LemonOpenHelper.TABLE_APP_REMARKS));

                appinfos.add(appInfo);
            }
        }
        appInfoCursor.close();
        db.close();

        return appinfos;
    }

    @Override
    public ArrayList<Object> queryAllAppInfosForClassify(){
        SQLiteDatabase db = openHelper.getReadableDatabase();

        if(!db.isOpen()) return null;

        ArrayList<Object> appinfos = new ArrayList<>();
        Cursor classifyCursor = db.query(TABLE_CLASSIFY_TABLENAME, null, null, null, null, null, null);

        if(classifyCursor != null && classifyCursor.getCount() > 0) {
            String classifyName;
            while(classifyCursor.moveToNext()) {
                // 获取到分类名
                classifyName = classifyCursor.getString(classifyCursor.getColumnIndex(LemonOpenHelper.TABLE_CLASSIFY_NAME));

                // 搜索每个分类下的记录
                if(!db.isOpen()) return null;

                Cursor appInfoCursor = db.query(TABLE_APP_TABLENAME, null, LemonOpenHelper.TABLE_APP_CLASSIFY + " = ?", new String[]{classifyName}, null, null, LemonOpenHelper.TABLE_ID.concat(" DESC"));
                if(appInfoCursor != null && appInfoCursor.getCount() > 0) {
                    appinfos.add(classifyName); //String,该分类下有数据才添加到list集合
                    while(appInfoCursor.moveToNext()) {
                        AppInfo appInfo = new AppInfo();
                        appInfo.appName = appInfoCursor.getString(appInfoCursor.getColumnIndex(LemonOpenHelper.TABLE_APP_APPNAME));
                        appInfo.packName = appInfoCursor.getString(appInfoCursor.getColumnIndex(LemonOpenHelper.TABLE_APP_APPPACKNAME));
                        appInfo.classify = appInfoCursor.getString(appInfoCursor.getColumnIndex(LemonOpenHelper.TABLE_APP_CLASSIFY));
                        appInfo.date = appInfoCursor.getLong(appInfoCursor.getColumnIndex(LemonOpenHelper.TABLE_APP_DATE));
                        appInfo.iconFileName = appInfoCursor.getString(appInfoCursor.getColumnIndex(LemonOpenHelper.TABLE_APP_ICONFILENAEM));
                        appInfo.saveIconpath = appInfoCursor.getInt(appInfoCursor.getColumnIndex(LemonOpenHelper.TABLE_APP_SAVEICONPATH));
                        appInfo.remarks = appInfoCursor.getString(appInfoCursor.getColumnIndex(LemonOpenHelper.TABLE_APP_REMARKS));

                        appinfos.add(appInfo);
                    }
                }
                appInfoCursor.close();
            }
            classifyCursor.close();
        }
        db.close();
        return appinfos;
    }

    @Override
    public boolean updateAppinfo(AppInfo appInfo) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        if(!db.isOpen() || appInfo == null) return false;

        ContentValues initialValues = new ContentValues();
        initialValues.put(LemonOpenHelper.TABLE_APP_CLASSIFY, appInfo.classify);
        initialValues.put(LemonOpenHelper.TABLE_APP_DATE, appInfo.date);
        initialValues.put(LemonOpenHelper.TABLE_APP_REMARKS, appInfo.remarks);

        int count = db.update(TABLE_APP_TABLENAME, initialValues, LemonOpenHelper.TABLE_APP_APPPACKNAME + " = ?", new String[]{appInfo.packName});

        db.close();
        return count != -1 ? true : false;
    }

    @Override
    public boolean addAppinfo(AppInfo appInfo) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        if(!db.isOpen() || appInfo == null) return false;

        ContentValues initialValues = new ContentValues();
        initialValues.put(LemonOpenHelper.TABLE_APP_APPNAME, appInfo.appName);
        initialValues.put(LemonOpenHelper.TABLE_APP_APPPACKNAME, appInfo.packName);
        initialValues.put(LemonOpenHelper.TABLE_APP_CLASSIFY, appInfo.classify);
        initialValues.put(LemonOpenHelper.TABLE_APP_DATE, appInfo.date);
        initialValues.put(LemonOpenHelper.TABLE_APP_REMARKS, appInfo.remarks);
        initialValues.put(LemonOpenHelper.TABLE_APP_ICONFILENAEM, appInfo.iconFileName);
        initialValues.put(LemonOpenHelper.TABLE_APP_SAVEICONPATH, appInfo.saveIconpath);

        long id = db.insert(TABLE_APP_TABLENAME, null, initialValues);

        db.close();
        return id != -1 ? true : false;
    }

    @Override
    public boolean isExistAppifo(AppInfo appInfo) {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        if(appInfo == null) throw new NullPointerException("AppInfo为null.");
        if(!db.isOpen()) throw new IllegalArgumentException("数据库已关闭.");

        Cursor cursor = db.query(TABLE_APP_TABLENAME, null, LemonOpenHelper.TABLE_APP_APPPACKNAME + " = ? ", new String[]{appInfo.packName}, null, null, null);
        if(cursor == null && cursor.getCount() <= 0) return false;

        while(cursor.moveToNext()) {
            appInfo.classify = cursor.getString(cursor.getColumnIndex(LemonOpenHelper.TABLE_APP_CLASSIFY));
            appInfo.iconFileName = cursor.getString(cursor.getColumnIndex(LemonOpenHelper.TABLE_APP_ICONFILENAEM));
            appInfo.saveIconpath = cursor.getInt(cursor.getColumnIndex(LemonOpenHelper.TABLE_APP_SAVEICONPATH));
            appInfo.appName = cursor.getString(cursor.getColumnIndex(LemonOpenHelper.TABLE_APP_APPNAME));
            appInfo.packName = cursor.getString(cursor.getColumnIndex(LemonOpenHelper.TABLE_APP_APPPACKNAME));
            appInfo.remarks = cursor.getString(cursor.getColumnIndex(LemonOpenHelper.TABLE_APP_REMARKS));
            appInfo.date = cursor.getLong(cursor.getColumnIndex(LemonOpenHelper.TABLE_APP_DATE));
        }
        cursor.close();

        db.close();
        return true;
    }

    @Override
    public ArrayList<String> queryAllClassifys() {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        if(!db.isOpen()) return null;

        ArrayList<String> classifys = new ArrayList<>();
        Cursor classifyCursor = db.query(TABLE_CLASSIFY_TABLENAME, null, null, null, null, null, null);

        if(classifyCursor == null && classifyCursor.getCount() <= 0) return null;
        while(classifyCursor.moveToNext()) {
            String classifyName = classifyCursor.getString(classifyCursor.getColumnIndex(LemonOpenHelper.TABLE_CLASSIFY_NAME));
            classifys.add(classifyName);
        }

        classifyCursor.close();

        db.close();
        return classifys;
    }

    @Override
    public boolean isExistClassify(String classify) {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        if(TextUtils.isEmpty(classify)) throw new NullPointerException("classify为空.");
        if(!db.isOpen()) throw new IllegalArgumentException("数据库已关闭.");

        Cursor cursor = db.query(TABLE_CLASSIFY_TABLENAME, null, "classifyName = ?", new String[]{classify}, null, null, null);

        boolean isExist = false;
        if(cursor != null && cursor.getCount() > 0) isExist = true;

        db.close();
        return isExist;
    }

    @Override
    public boolean addClassify(String classify) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        if(!db.isOpen() || TextUtils.isEmpty(classify)) return false;

        ContentValues initialValues = new ContentValues();
        initialValues.put(LemonOpenHelper.TABLE_CLASSIFY_NAME, classify);
        long id = db.insert(TABLE_CLASSIFY_TABLENAME, null, initialValues);

        db.close();
        return id != -1 ? true : false;
    }

    @Override
    public ArrayList<AppInfo> queryAppInfosFilter() {
        // 查询手机中的所有包信息
        ArrayList<AppInfo> appinfos = queryPhoneAppInfos();

        SQLiteDatabase db = openHelper.getReadableDatabase();
        if(!db.isOpen()) return null;

        Cursor cursor = db.query(TABLE_APP_TABLENAME, null, null, null, null, null, null);
        if(cursor == null || cursor.getCount() <= 0) return appinfos;

        // 查询所有记录的包名
        while(cursor.moveToNext()) {
            String packNameValue = cursor.getString(cursor.getColumnIndex(LemonOpenHelper.TABLE_APP_APPPACKNAME));

            // 遍历appinfos, 删除已存在的数据
            Iterator<AppInfo> iterator = appinfos.iterator();
            while(iterator.hasNext()) {
                AppInfo appinfo = iterator.next();
                if(packNameValue.equals(appinfo.packName)) iterator.remove();
            }
        }
        cursor.close();

        db.close();
        return appinfos;
    }

    @Override
    public ArrayList<AppInfo> queryPhoneAppInfos() {
        ArrayList<AppInfo> appInfos = new ArrayList<>();

        //所有的安装在系统上的应用程序包信息。
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packInfos = pm.getInstalledPackages(0);
        for(PackageInfo packInfo : packInfos){
            int flags = packInfo.applicationInfo.flags;//应用程序信息的标记
            if((flags& ApplicationInfo.FLAG_SYSTEM)==0){
                AppInfo appInfo = new AppInfo();
                appInfo.packName = packInfo.packageName;
                appInfo.appName = packInfo.applicationInfo.loadLabel(pm).toString();
                appInfo.setIcon(packInfo.applicationInfo.loadIcon(pm));

                appInfos.add(appInfo);
            }
        }

        return appInfos;
    }

    @Override
    public AppDatasCount queryClassifyAppCount() {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        AppDatasCount appDatasCount = new AppDatasCount();

        if(!db.isOpen()) return null;

        // 查询所有分类
        Cursor cursor = db.query(LemonOpenHelper.TABLE_CLASSIFY_TABLENAME, null, null, null, null, null, null);
        if(cursor == null || cursor.getCount() <= 0) return null;

        appDatasCount.classifyAppCount = new ArrayList<>();
        while(cursor.moveToNext()) {
            // 获取到分类名
            String classifyName = cursor.getString(cursor.getColumnIndex(LemonOpenHelper.TABLE_CLASSIFY_NAME));

            // 搜索每个分类下的记录
            SQLiteDatabase itemDb = openHelper.getReadableDatabase();
            if(!itemDb.isOpen()) return null;

            Cursor itemCursor = itemDb.rawQuery("select count(*) from "+LemonOpenHelper.TABLE_APP_TABLENAME+" where classify = ? ;", new String[]{classifyName});
            if(itemCursor == null || itemCursor.getCount() <= 0) return null;

            itemCursor.moveToFirst();
            long appscount = itemCursor.getLong(0);
            if(appscount > 0){
                AppDatasCount.ClassifyAppCount appCount = new AppDatasCount.ClassifyAppCount();
                appCount.classify = classifyName;
                appCount.count = appscount;
                appDatasCount.classifyAppCount.add(appCount);
            }
            itemCursor.close();
        }
        cursor.close();

        SQLiteDatabase countDB = openHelper.getReadableDatabase();
        // 查询所有的app信息个数
        if(!db.isOpen()) return null;

        Cursor itemCursor = countDB.rawQuery("select count(*) from "+LemonOpenHelper.TABLE_APP_TABLENAME+" ;",null);
        itemCursor.moveToFirst();
        appDatasCount.total = itemCursor.getLong(0);
        itemCursor.close();

        db.close();
        return appDatasCount;
    }

    @Override
    public void deleteAppInfo(AppInfo appInfo) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        if(!db.isOpen()) return;

        int count = db.delete(TABLE_APP_TABLENAME, LemonOpenHelper.TABLE_APP_APPPACKNAME + " = ? ", new String[]{appInfo.packName});
    }

    @Override
    public void deleteClassify(String classify) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        if(!db.isOpen()) return;

        int count = db.delete(TABLE_CLASSIFY_TABLENAME, LemonOpenHelper.TABLE_CLASSIFY_NAME + " = ? ", new String[]{classify});

        db.close();
    }

    @Override
    public void deleteAllAppInfos() {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        if(!db.isOpen()) return;

        int count = db.delete(TABLE_APP_TABLENAME, null, null);

        db.close();
    }

    @Override
    public void deleteAllClassifys() {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        if(!db.isOpen()) return;

        int count = db.delete(TABLE_CLASSIFY_TABLENAME, null, null);

        db.close();
    }

    @Override
    public boolean isHavaAppinfoForClassify(String classify) {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        if(!db.isOpen()) return true;

        Cursor cursor = db.query(TABLE_APP_TABLENAME, null, LemonOpenHelper.TABLE_APP_CLASSIFY+ " = ? ", new String[]{classify}, null, null, null);

        boolean isHava = false;
        if(cursor != null && cursor.getCount() > 0) isHava = true;

        db.close();
        return isHava;
    }
}
