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
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

import me.luzhuo.lemonapprecorder.bean.AppInfo;
import me.luzhuo.lemonapprecorder.model.IAppInfos;
import me.luzhuo.lemonapprecorder.model.IConfigInfo;
import me.luzhuo.lemonapprecorder.model.IDataSerialization;
import me.luzhuo.lemonapprecorder.model.impl.IAppinfosImpl;
import me.luzhuo.lemonapprecorder.model.impl.IConfigInfoImpl;
import me.luzhuo.lemonapprecorder.model.impl.IDataSerializationImpl;
import me.luzhuo.lemonapprecorder.ui.adapter.HomeAdapter;
import me.luzhuo.lemonapprecorder.ui.fragment.HomeFragment;
import me.luzhuo.lemonapprecorder.ui.view.IHomeView;
import me.luzhuo.lemonapprecorder.utils.FileUtil;
import me.luzhuo.lemonapprecorder.utils.LemonUtils;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/11/6 14:10
 * <p>
 * Description: 首页 的控制层
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public class HomePersenter {
    private IHomeView iHomeView;
    private Context context;
    private IAppInfos iAppInfos;
    private IDataSerialization iDataSerialization;
    private IConfigInfo iConfigInfo;

    private ArrayList<Object> appinfos = new ArrayList<>();
    private HomeAdapter homeAdapter;

    public HomePersenter(IHomeView iHomeView){
        this.iHomeView = iHomeView;
        this.context = ((HomeFragment)iHomeView).getContext();
        iAppInfos = new IAppinfosImpl(context);
        iDataSerialization = new IDataSerializationImpl(context);
        iConfigInfo = new IConfigInfoImpl(context);
    }

    /**
     * 初始化listview
     * @param fragment_listview
     */
    public void initListView(ListView fragment_listview) {
        homeAdapter = new HomeAdapter(context, appinfos);
        fragment_listview.setAdapter(homeAdapter);
        fragment_listview.setOnItemClickListener(listviewItemClick);

        updateListViewAppinfo();
    }

    /**
     * 更新listview数据信息
     */
    public void updateListViewAppinfo(){
        // 查询信息并刷新界面
        ArrayList<Object> apps = iAppInfos.queryAllAppInfosForClassify();
        appinfos.clear();
        appinfos.addAll(apps);

        homeAdapter.notifyDataSetChanged();
    }

    AdapterView.OnItemClickListener listviewItemClick = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // 首页app查看点击
            if(appinfos.get(position) instanceof AppInfo) iHomeView.watchAppInfoDetail((AppInfo) appinfos.get(position));
        }
    };

    /**
     * 添加应用信息
     * @param appInfo
     */
    public void addAppinfo(AppInfo appInfo) {
        // 保存图标, 并生成图标路径, 并设置iconpath值
        String filename = appInfo.appName.concat(String.valueOf(appInfo.date)).concat(".lemon");
        appInfo.iconFileName = filename;

        File iconFile;
        if(iConfigInfo.getIconSavePath() == IConfigInfo.Mobile) iconFile = FileUtil.getFilesFile(context, filename);
        else iconFile = FileUtil.getExternalPicFile(context, filename);

        appInfo.saveIconpath = iConfigInfo.getIconSavePath();

        String iconpath = iDataSerialization.saveDrawable(appInfo.getIcon(context), iconFile);
        if(TextUtils.isEmpty(iconpath)){
            LemonUtils.showQuickToast(context, "保存图标失败!");
        }

        // 更新数据
        if(iAppInfos.addAppinfo(appInfo)) updateListViewAppinfo();
        else LemonUtils.showQuickToast(context, "添加应用信息失败!");
    }

    /**
     * 更新应用信息
     * @param appInfo
     */
    public void updateAppinfo(AppInfo appInfo){
        if(iAppInfos.updateAppinfo(appInfo)) updateListViewAppinfo();
        else LemonUtils.showQuickToast(context, "更新应用信息失败!");
    }

}
