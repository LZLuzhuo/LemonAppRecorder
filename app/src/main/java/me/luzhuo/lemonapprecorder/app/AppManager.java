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
package me.luzhuo.lemonapprecorder.app;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/11/4 0:24
 * <p>
 * Description: APP管理包括对Activity和Service的强引用. 使用时请调用init(Application)进行初始化.
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public class AppManager {
    private static Context context;
    private static List<Activity> activitys = new LinkedList<Activity>();
    private static List<Service> services = new LinkedList<Service>();

    /**
     * 初始化,请在application里进行.
     * @param context
     */
    public static void init(Context context){
        AppManager.context = context;
    }

    /**
     * 添加activity
     * @param activity
     */
    public static void addActivity(Activity activity) {
        activitys.add(activity);
    }

    /**
     * 移除指定activity
     * @param activity
     */
    public static void removeActivity(Activity activity) {
        activitys.remove(activity);
    }

    /**
     * 添加Service
     * @param service
     */
    public static void addService(Service service) {
        services.add(service);
    }

    /**
     * 移除指定Service
     * @param service
     */
    public static void removeService(Service service) {
        services.remove(service);
    }

    /**
     * 退出应用程序时,清空activity,service
     */
    public static void closeApplication() {
        closeActivitys();
        closeServices();
    }

    private static void closeActivitys() {
        ListIterator<Activity> iterator = activitys.listIterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            if (activity != null) {
                activity.finish();
            }
        }
        activitys.clear();
    }

    private static void closeServices() {
        ListIterator<Service> iterator = services.listIterator();
        while (iterator.hasNext()) {
            Service service = iterator.next();
            if (service != null) {
                context.getApplicationContext().stopService(new Intent(context, service.getClass()));
            }
        }
        services.clear();
    }

}
