package me.luzhuo.lemonapprecorder.bean;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.gson.annotations.Expose;

import java.io.File;
import java.io.Serializable;

import me.luzhuo.lemonapprecorder.model.IConfigInfo;
import me.luzhuo.lemonapprecorder.model.IDataSerialization;
import me.luzhuo.lemonapprecorder.model.impl.IDataSerializationImpl;
import me.luzhuo.lemonapprecorder.utils.FileUtil;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/11/5 18:57
 * <p>
 * Description: <br>应用信息对象;</br>
 *              <br>数据库中存储的是图标的路径信息, 在用户添加的应用信息的瞬间将图标信息存到指定的目录下, 并生成bean对象.</br>
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public class AppInfo implements Serializable{

    /**
     * 添加/更新 日期时间
     */
    @Expose
    public long date;

    /**
     * 应用名
     */
    @Expose
    public String appName;

    /**
     * 包名
     */
    @Expose
    public String packName;

    /**
     * 分类
     */
    @Expose
    public String classify;

    /**
     * 备注信息
     */
    @Expose
    public String remarks;

    /**
     * 图标文件名
     */
    @Expose
    public String iconFileName;

    private Drawable icon;

    /**
     * 图标保存的路径位置(sd卡0/手机1), 如果数据没有路径信息, 则默认保存到sd卡上
     */
    @Expose
    public int saveIconpath;

    public AppInfo(){ }

    /**
     * 获取图标对象, 对象内有数据则从对象内获取, 没有则试图解析文件.
     * @return
     */
    public Drawable getIcon(Context context){
        if(icon != null) return icon;

        File iconFile = FileUtil.getIconFile(context, this);
        if(!iconFile.exists()) return null;

        return Drawable.createFromPath(iconFile.getPath());
    }

    /**
     * 设置图标对象
     * @param icon
     */
    public void setIcon(Drawable icon){
        this.icon = icon;
    }

    public AppInfo(long date, String appName, String packName, String classify, String remarks, String iconFileName){
        this.date = date;
        this.appName = appName;
        this.packName = packName;
        this.classify = classify;
        this.remarks = remarks;
        this.iconFileName = iconFileName;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "date=" + date +
                ", appName='" + appName + '\'' +
                ", packName='" + packName + '\'' +
                ", classify='" + classify + '\'' +
                ", remarks='" + remarks + '\'' +
                ", iconFileName=" + iconFileName +
                '}';
    }
}
