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
package me.luzhuo.lemonapprecorder.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.NotificationCompat;

import me.luzhuo.lemonapprecorder.R;
import me.luzhuo.lemonapprecorder.bean.AppInfo;
import me.luzhuo.lemonapprecorder.model.IAppInfos;
import me.luzhuo.lemonapprecorder.model.impl.IAppinfosImpl;
import me.luzhuo.lemonapprecorder.ui.activity.WarningAcitity;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/11/22 0:17
 * <p>
 * Description: 处理新安装或者卸载的广播
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public class NewAppReceiver extends BroadcastReceiver{
    private IAppInfos iAppInfos;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(iAppInfos == null) iAppInfos = new IAppinfosImpl(context);

        // 安装应用广播
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            String packageName = intent.getDataString().substring(8);

            AppInfo appInfo = new AppInfo();
            appInfo.packName = packageName;
            // 检查新安装的app是否存在
            if(iAppInfos.isExistAppifo(appInfo)){
                // 存在, 通知栏弹出通知
                showNotify(context, appInfo);
            }
        }
        
        // 卸载广播
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
        	System.out.println("PACKAGE_REMOVED");
        }
    }

    /**
     * 通知栏通知
     * @param context
     */
    private void showNotify(Context context, AppInfo appInfo) {
        // 通知栏弹出提醒(可取消),用户点击打开activity
        Intent intent1 = new Intent(context,WarningAcitity.class);
        intent1.putExtra("baseAppInfo", appInfo);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, appInfo.hashCode(), intent1, 0);

        Notification noti = new NotificationCompat.Builder(context)
                .setContentTitle(appInfo.appName)
                .setContentText("新安装的 " + appInfo.appName + " 应用已经存在记录,点击查看详情.")
                .setContentIntent(pendingIntent)
                .setVibrate(new long[] {0, 300}) // 不用权限也能振动,我发现了什么秘密...
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(((BitmapDrawable)appInfo.getIcon(context)).getBitmap())
                .build();

        noti.flags = Notification.FLAG_ONLY_ALERT_ONCE | Notification.FLAG_AUTO_CANCEL;
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(appInfo.hashCode(), noti);
    }
}
