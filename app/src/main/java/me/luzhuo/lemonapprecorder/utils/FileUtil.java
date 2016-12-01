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
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

import me.luzhuo.lemonapprecorder.bean.AppInfo;
import me.luzhuo.lemonapprecorder.model.IConfigInfo;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/12/1 21:21
 * <p>
 * Description:
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public class FileUtil {

    /**
     * 获取手机内存中的文件File对象(/data/data/packname/files/filename)
     * @param context
     * @param fileName
     * @return file/null
     */
    public static File getFilesFile(Context context, String fileName){
        if(TextUtils.isEmpty(fileName) || context == null) return null;

        return new File(context.getFilesDir(), fileName);
    }

    /**
     * 获取sd卡中的图片文件File对象
     * @param context
     * @param fileName
     * @return file/null
     */
    public static File getExternalPicFile(Context context, String fileName) {
        if(TextUtils.isEmpty(fileName) || context == null) return null;

        return new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);
    }

    /**
     * 获取图标的File对象
     * @param context
     * @param appInfo
     * @return file/null
     */
    public static File getIconFile(Context context, AppInfo appInfo){
        if(TextUtils.isEmpty(appInfo.iconFileName) || context == null) return null;

        return appInfo.saveIconpath == IConfigInfo.Mobile ? FileUtil.getFilesFile(context, appInfo.iconFileName) : FileUtil.getExternalPicFile(context, appInfo.iconFileName);
    }
}
