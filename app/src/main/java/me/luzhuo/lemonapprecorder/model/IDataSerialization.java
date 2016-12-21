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
package me.luzhuo.lemonapprecorder.model;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.luzhuo.lemonapprecorder.bean.AppInfo;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/11/17 14:35
 * <p>
 * Description: 数据序列化, 将数据序列化存储, 或者将存储的数据反序列化回来.
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public interface IDataSerialization {

    /**
     * 删除某个目录下的所有文件
     */
    void deleteFilesForDirectory(File directory);

    /**
     * 读取classify文件并解析成list
     * @return list/null
     */
    List<String> loadClassifyForFile(File classifyFile);

    /**
     * 读取应用信息文件并解析成list
     * @param appinfoFile
     * @return
     */
    List<AppInfo> loadAppInfosForFile(File appinfoFile);

    /**
     * 拷贝文件
     * @param shujuyuan 数据源
     * @param file 目的地
     * @throws IOException
     */
    void copyFile(File shujuyuan, File file);

    /**
     * 写入分类信息到文件
     * @param classifyList
     */
    boolean writeClassifyToFile(ArrayList classifyList, File saveFile);

    /**
     * 写入应用信息到文件
     * @param appList
     */
    boolean writeAppinfoToFile(ArrayList<AppInfo> appList, File saveFile);

    /**
     * 保存Drawable图标
     * @param icon
     * @param file
     * @return 保存后的路径
     */
    String saveDrawable(Drawable icon, File file);

    /**
     * 将Bitmap保存到文件
     */
    String saveBitmap(Bitmap bitmap, File file);
}
