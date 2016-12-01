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

import java.util.ArrayList;

import me.luzhuo.lemonapprecorder.bean.AppDatasCount;
import me.luzhuo.lemonapprecorder.bean.AppInfo;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/11/6 15:04
 * <p>
 * Description: App信息的存取操作
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public interface IAppInfos {

    /**
     * 添加分类
     * @param classify
     */
    boolean addClassify(String classify);

    /**
     * 添加应用信息
     * @param appInfo
     */
    boolean addAppinfo(AppInfo appInfo);

    /**
     * 查询所有APP信息.(倒序)
     */
    ArrayList<AppInfo> queryAllAppInfos();

    /**
     * 根据分类查询所有APP信息. 倒序(新添加的在最前面)
     */
    ArrayList<Object> queryAllAppInfosForClassify();

    /**
     * 获取所有分类
     */
    ArrayList<String> queryAllClassifys();

    /**
     * 获取过滤后的应用信息集合, 过滤了数据库已经存在的应用信息
     */
    ArrayList<AppInfo> queryAppInfosFilter();

    /**
     * 获取手机中安装的所有应用信息
     */
    ArrayList<AppInfo> queryPhoneAppInfos();

    /**
     * 查询分类下的数据库记录的App数量
     */
    AppDatasCount queryClassifyAppCount();

    /**
     * 删除某个应用信息
     */
    void deleteAppInfo(AppInfo appInfo);

    /**
     * 删除分类信息
     */
    void deleteClassify(String classify);

    /**
     * 删除数据库所有应用信息
     */
    void deleteAllAppInfos();

    /**
     * 删除数据库所有分类记录
     */
    void deleteAllClassifys();

    /**
     * 某个分类下是否存在应用信息
     * @return 有则返回true, 没有则返回false, 出错则返回true
     */
    boolean isHavaAppinfoForClassify(String classify);

    /**
     * 该分类是否存在
     */
    boolean isExistClassify(String classify);

    /**
     * 该应用信息是否存在, 存在的话将其设置数值.
     */
    boolean isExistAppifo(AppInfo appInfo);

    /**
     * 更新应用信息
     * @param appInfo
     */
    boolean updateAppinfo(AppInfo appInfo);

}
