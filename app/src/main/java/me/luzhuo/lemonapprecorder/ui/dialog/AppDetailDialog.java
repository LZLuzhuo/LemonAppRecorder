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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.luzhuo.lemonapprecorder.R;
import me.luzhuo.lemonapprecorder.bean.AppInfo;
import me.luzhuo.lemonapprecorder.callback.dialog.AppDetailDialogCallBack;
import me.luzhuo.lemonapprecorder.callback.dialog.SelectAppInfoDiaglogCallBack;
import me.luzhuo.lemonapprecorder.callback.pop.SelectClassifyPopCallBack;
import me.luzhuo.lemonapprecorder.ui.pop.SelectClassifyPop;
import me.luzhuo.lemonapprecorder.utils.LemonUtils;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/11/4 16:50
 * <p>
 * Description: 添加/查看/修改 应用详情的弹窗
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public class AppDetailDialog {
    // Text
    @BindView(R.id.tv_appname)
    TextView tv_appname;
    @BindView(R.id.tv_packname)
    TextView tv_packname;
    @BindView(R.id.tv_classify)
    TextView tv_classify;
    @BindView(R.id.et_remarks)
    EditText et_remarks;
    // Image
    @BindView(R.id.iv_icon)
    ImageView iv_icon;

    @BindView(R.id.lr_addlayout)
    LinearLayout lr_addlayout;
    @BindView(R.id.lr_watchlayout)
    LinearLayout lr_watchlayout;

    @BindView(R.id.tv_update_date)
    TextView tv_update_date;

    private AlertDialog dialog;
    private AppDetailDialogCallBack callback;
    private SelectClassifyPop classifyPop;
    private Context context;
    private SelectAppInfoDiaglog selectDialog;

    /**
     * AppDialog类型
     */
    public enum AppDialogType{
        /** 添加 */
        Add,
        /** 查看 */
        Watch
    }

    public AppDetailDialog(Context context, AppDialogType dialogtype, AppDetailDialogCallBack callback){
        this.context = context;
        this.callback = callback;

        View view = View.inflate(context, R.layout.dialog_appdetail, null);
        ButterKnife.bind(this, view);

        if(dialogtype == AppDialogType.Add) {
            lr_addlayout.setVisibility(View.VISIBLE);
            lr_watchlayout.setVisibility(View.GONE);
        }else if(dialogtype == AppDialogType.Watch){
            lr_addlayout.setVisibility(View.GONE);
            lr_watchlayout.setVisibility(View.VISIBLE);
        }

        // 设置Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.setView(view, 0, 0, 0, 0);
    }

    /**
     * 显示
     */
    public void show() {
        dialog.show();
    }

    @OnClick({R.id.bt_ok, R.id.bt_cancel, R.id.bt_select, R.id.tv_classify, R.id.bt_update_appinfo})
    public void onclick(View v) {
        AppInfo appInfo;
        switch (v.getId()){
            case R.id.bt_ok:
                if(callback == null) return;

                appInfo = new AppInfo(new Date().getTime(), tv_appname.getText().toString().trim(), tv_packname.getText().toString().trim(), tv_classify.getText().toString().trim(), et_remarks.getText().toString().trim(), null);
                appInfo.setIcon(iv_icon.getDrawable());
                callback.onConfirm(appInfo);
                break;
            case R.id.bt_cancel:
                if(callback != null) callback.onCancel();
                break;
            case R.id.bt_select:
                if(selectDialog == null) selectDialog = new SelectAppInfoDiaglog(context, selectCallback);

                selectDialog.show();
                break;
            case R.id.tv_classify:
                if(classifyPop == null) classifyPop = new SelectClassifyPop(context, classifyPopCallBack);

                classifyPop.show(tv_classify);
                break;
            case R.id.bt_update_appinfo:
                if(callback == null) return;

                // 除了图标信息未赋值外, 其他均已赋值.
                appInfo = new AppInfo(new Date().getTime(), tv_appname.getText().toString().trim(), tv_packname.getText().toString().trim(), tv_classify.getText().toString().trim(), et_remarks.getText().toString().trim(), null);
                appInfo.setIcon(iv_icon.getDrawable());
                callback.onUpdate(appInfo);
                break;
        }
    }

    SelectClassifyPopCallBack classifyPopCallBack = new SelectClassifyPopCallBack(){
        @Override
        public void onSelectClassify(String classify) {
            tv_classify.setText(classify);
            classifyPop.show(tv_classify);
        }
    };

    SelectAppInfoDiaglogCallBack selectCallback = new SelectAppInfoDiaglogCallBack(){
        @Override
        public void onSelectAppInfo(AppInfo appInfo) {
            if(appInfo == null) {
                LemonUtils.showQuickToast(context, "请选择应用!");
                return;
            }

            appInfo.classify = tv_classify.getText().toString().trim(); // 分类是可以单独选的
            setAppInfo(appInfo);
            selectDialog.dismiss();
        }

        @Override
        public void onSelectCancel() {
            selectDialog.dismiss();
        }
    };

    /**
     * 关闭
     */
    public void dismiss(){
        dialog.dismiss();
    }

    /**
     * 设置App信息
     * @param appInfo
     */
    public void setAppInfo(AppInfo appInfo){
        tv_appname.setText(appInfo.appName == null ? "" : appInfo.appName);
        tv_packname.setText(appInfo.packName == null ? "" : appInfo.packName);
        tv_update_date.setText(LemonUtils.getStringDate(appInfo.date)); // 解析成时间
        et_remarks.setText(appInfo.remarks == null ? "" : appInfo.remarks);
        tv_classify.setText(appInfo.classify == null ? "" : appInfo.classify);
        if(appInfo.getIcon(context) != null) iv_icon.setImageDrawable(appInfo.getIcon(context));
        else iv_icon.setImageResource(R.mipmap.activity_home_add_apk_icon);
    }
}
