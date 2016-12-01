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

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/11/15 16:44
 * <p>
 * Description: 配置信息
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public interface IConfigInfo {

    /**
     * 存储在手机的SD卡中; 有些手机没有SD卡, 将由手机系统指定的SD路径(可能是在手机内部存储), 一旦删除将导致数据无法找回.
     */
    int SDCard = 0;

    /**
     * 存储在手机内部存储中, 是存在于系统目录下的数据目录; 删除应用将删除数据, 清楚缓存无效; 默认是该选项.
     */
    int Mobile = 1;

    /**
     * 获取图标的保存位置选项
     * @return
     */
    int getIconSavePath();

    /**
     * 记录图标的保存位置选项, 默认保存到手机sd中
     * @param iconSavaPath
     */
    void saveIconSavePath(int iconSavaPath);
}
