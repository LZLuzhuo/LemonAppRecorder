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
package me.luzhuo.lemonapprecorder.callback.dialog;

import me.luzhuo.lemonapprecorder.bean.AppInfo;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/11/20 21:20
 * <p>
 * Description: 清除应用信息的回调
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public interface CleanAppinfoDialogCallBack {

    /**
     * 用户点击了清除按钮
     * @param position
     * @param appInfo
     */
    void onCleanButton(int position, AppInfo appInfo);

    /**
     * 用户点击了关闭或者取消按钮
     */
    void onCancle();
}
