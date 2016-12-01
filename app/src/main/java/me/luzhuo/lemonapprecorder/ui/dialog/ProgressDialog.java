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
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.luzhuo.lemonapprecorder.R;
import me.luzhuo.lemonapprecorder.callback.dialog.ProgressDialogCallBack;
import me.luzhuo.lemonapprecorder.widget.numberprogressbar.NumberProgressBar;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/11/17 14:19
 * <p>
 * Description: 进度条Dialog
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public class ProgressDialog {
    @BindView(R.id.np_menu_bakcups_numberbar)
    NumberProgressBar np_menu_bakcups_numberbar;
    @BindView(R.id.tv_menu_bakcups_hint)
    TextView tv_menu_bakcups_hint;
    @BindView(R.id.ll_menu_dialog_ok)
    LinearLayout ll_menu_dialog_ok;

    private AlertDialog dialog;
    private ProgressDialogCallBack callback;

    public ProgressDialog(Context context, ProgressDialogCallBack callback){
        this.callback = callback;
        View view = View.inflate(context, R.layout.dialog_progress, null);
        ButterKnife.bind(this, view);

        // 设置Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.setView(view, 0, 0, 0, 0);
    }

    @OnClick({R.id.ll_menu_dialog_ok})
    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.ll_menu_dialog_ok:
                if(callback != null) callback.onComplete();
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

    /**
     * 设置提示内容
     * @param hit
     */
    public void setHit(String hit){
        tv_menu_bakcups_hint.setText(hit);
    }

    /**
     * 设置进度条最大值
     * @param max
     */
    public void setMax(int max){
        np_menu_bakcups_numberbar.setMax(max);
    }

    /**
     * 是否显示关闭按钮, 默认不显示, 只在成功或者发生错误时显示
     * @param isShow true显示/false不显示
     */
    public void showOffButton(boolean isShow){
        if(isShow) ll_menu_dialog_ok.setVisibility(View.VISIBLE);
        else ll_menu_dialog_ok.setVisibility(View.GONE);
    }

    /**
     * 设置进度
     * @param progress
     */
    public void setProgress(int progress){
        np_menu_bakcups_numberbar.setProgress(progress);
    }

}
