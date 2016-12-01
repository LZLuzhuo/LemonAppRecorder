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

import butterknife.ButterKnife;
import butterknife.OnClick;
import me.luzhuo.lemonapprecorder.R;
import me.luzhuo.lemonapprecorder.callback.dialog.WarnDialogCallBack;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/11/14 19:09
 * <p>
 * Description: 警告Dialog
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public class WarnDialog {
    public enum WarnType { ImportWarn, ExportWarn }

    private AlertDialog dialog;
    private WarnDialogCallBack callback;
    private WarnType warntype;

    public WarnDialog(Context context, WarnType warntype, WarnDialogCallBack callback){
        this.callback = callback;
        this.warntype = warntype;

        View view = View.inflate(context, R.layout.dialog_menu_warn, null);
        ButterKnife.bind(this, view);

        // 设置Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.setView(view, 0, 0, 0, 0);
    }

    @OnClick({R.id.bt_menu_warn_cancel, R.id.bt_menu_warn_ok, R.id.bt_menu_warn_cancel1})
    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.bt_menu_warn_cancel:
            case R.id.bt_menu_warn_cancel1:
                if(callback != null) callback.onCancel();
                break;
            case R.id.bt_menu_warn_ok:
                if(callback != null) callback.onConfirm(warntype);
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
