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

import android.content.Context;
import android.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.luzhuo.lemonapprecorder.R;
import me.luzhuo.lemonapprecorder.bean.AppInfo;
import me.luzhuo.lemonapprecorder.callback.dialog.SelectAppInfoDiaglogCallBack;
import me.luzhuo.lemonapprecorder.model.IAppInfos;
import me.luzhuo.lemonapprecorder.model.impl.IAppinfosImpl;
import me.luzhuo.lemonapprecorder.ui.adapter.SelectAppInfoAdapter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/11/11 12:16
 * <p>
 * Description: 选择应用弹框, 只在创建类时更新资源数据, 展示时不更新数据.
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public class SelectAppInfoDiaglog {
    @BindView(R.id.ll_select_load)
    LinearLayout ll_select_load;
    @BindView(R.id.lv_select_listview)
    ListView lv_select_listview;

    private AlertDialog dialog;
    private SelectAppInfoDiaglogCallBack callback;
    private IAppInfos iAppInfos;
    private ArrayList<AppInfo> appinfosFilter = new ArrayList<>();
    private SelectAppInfoAdapter selectAdapter;

    public SelectAppInfoDiaglog(Context context, SelectAppInfoDiaglogCallBack callback){
        this.callback = callback;
        iAppInfos = new IAppinfosImpl(context);

        View view = View.inflate(context, R.layout.dialog_select_appinfo, null);
        ButterKnife.bind(this, view);

        // 设置Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.setView(view, 0, 0, 0, 0);

        lv_select_listview.setOnItemClickListener(listItemClick);
        selectAdapter = new SelectAppInfoAdapter(context, appinfosFilter);
        lv_select_listview.setAdapter(selectAdapter);

        // 为避免多次点开SelectAppInfoDialog时多次处理数据, 将只在本类创建时获取(1次),以节省资源.
        updateAppinfos();
    }

    AdapterView.OnItemClickListener listItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(callback != null) callback.onSelectAppInfo(appinfosFilter.get(position));
        }
    };

    /**
     * 显示
     */
    public void show() {
        dialog.show();
    }

    /**
     * 更新应用数据
     */
    private void updateAppinfos(){
        // RxJava
        Observable.defer(new Func0<Observable<ArrayList<AppInfo>>>() {
            @Override
            public Observable<ArrayList<AppInfo>> call() {
                return Observable.just(iAppInfos.queryAppInfosFilter());
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<ArrayList<AppInfo>>() {
            @Override
            public void call(ArrayList<AppInfo> appInfos) {
                ll_select_load.setVisibility(View.GONE);
                appinfosFilter.clear();
                appinfosFilter.addAll(appInfos);
                selectAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick({R.id.iv_select_cancel})
    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.iv_select_cancel:
                if(callback != null) callback.onSelectCancel();
                break;
        }
    }

    /**
     * 关闭
     */
    public void dismiss(){
        dialog.dismiss();
    }
}
