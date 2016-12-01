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
package me.luzhuo.lemonapprecorder.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.luzhuo.lemonapprecorder.R;
import me.luzhuo.lemonapprecorder.bean.AppInfo;
import me.luzhuo.lemonapprecorder.callback.adapter.CleanAppinfoAdapterClick;
import me.luzhuo.lemonapprecorder.callback.dialog.CleanAppinfoDialogCallBack;
import me.luzhuo.lemonapprecorder.ui.adapter.CleanAppinfoAdapter;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/11/20 21:18
 * <p>
 * Description: 清除应用信息的Dialog
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public class CleanAppinfoDialog {
    @BindView(R.id.lv_clean_appinfo)
    ListView lv_clean_appinfo;

    private Context context;
    private AlertDialog dialog;
    private CleanAppinfoDialogCallBack callback;
    private ArrayList<AppInfo> appInfos = new ArrayList<>();
    private CleanAppinfoAdapter cleanAppinfoAdapter;

    public CleanAppinfoDialog(Context context, CleanAppinfoDialogCallBack callback){
        this.context = context;
        this.callback = callback;

        View view = View.inflate(context, R.layout.dialog_clean_appinfo, null);
        ButterKnife.bind(this, view);

        // 设置Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.setView(view, 0, 0, 0, 0);

        initListView();
    }

    /**
     * 初始化ListView
     */
    private void initListView() {
        cleanAppinfoAdapter = new CleanAppinfoAdapter(context, appInfos, cleanCallback);
        lv_clean_appinfo.setAdapter(cleanAppinfoAdapter);
    }

    /**
     * 设置数据
     * @param aps
     */
    public void setData(ArrayList<AppInfo> aps){
        appInfos.clear();
        appInfos.addAll(aps);

        cleanAppinfoAdapter.notifyDataSetChanged();
    }

    /**
     * 获取数据
     * @return
     */
    public ArrayList<AppInfo> getData(){
        return appInfos;
    }

    /**
     * 移除集合中的额某个元素
     * @param position 索引位置
     */
    public void removeList(int position){
        appInfos.remove(position);
        cleanAppinfoAdapter.notifyDataSetChanged();
    }

    CleanAppinfoAdapterClick cleanCallback = new CleanAppinfoAdapterClick() {
        @Override
        public void onCleanClick(int position, AppInfo appInfo) {
            if(callback != null) callback.onCleanButton(position, appInfo);
        }
    };

    @OnClick({R.id.iv_clean_appinfo_cancel})
    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.iv_clean_appinfo_cancel:
                if(callback != null) callback.onCancle();
                break;
        }
    }

    /**
     * 显示
     */
    public void show() {
        dialog.show();
    }

    /**
     * 关闭
     */
    public void dismiss(){
        dialog.dismiss();
    }

}
