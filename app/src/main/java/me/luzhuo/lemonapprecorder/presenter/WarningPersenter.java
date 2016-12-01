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
package me.luzhuo.lemonapprecorder.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import me.luzhuo.lemonapprecorder.ui.view.IWarningView;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/11/22 0:05
 * <p>
 * Description: 警告活动页的控制层
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public class WarningPersenter {
    private IWarningView iWarningView;
    private Context context;

    public WarningPersenter(IWarningView iWarningView){
        this.iWarningView = iWarningView;
        this.context = (Context)iWarningView;
    }

    /**
     * 卸载应用
     */
    public void uninstallApk(String packageName){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.DELETE");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("package:".concat(packageName)));
        context.startActivity(intent);
    }
}
