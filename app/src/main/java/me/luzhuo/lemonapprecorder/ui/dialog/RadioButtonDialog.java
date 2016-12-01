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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.luzhuo.lemonapprecorder.R;
import me.luzhuo.lemonapprecorder.callback.dialog.RadioButtonDialogCallBack;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/11/14 21:24
 * <p>
 * Description: 含有多个单选按钮的Dialog
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public class RadioButtonDialog {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.ll_menu_clean)
    LinearLayout ll_menu_clean;

    private AlertDialog dialog;
    private RadioButtonDialogCallBack callback;
    private Context context;
    private String[] contents;
    private ArrayList<RadioButton> radiobuttons = new ArrayList<>();

    public RadioButtonDialog(Context context, RadioButtonDialogCallBack callback){
        this.context = context;
        this.callback = callback;

        View view = View.inflate(context, R.layout.dialog_menu_clean, null);
        ButterKnife.bind(this, view);

        // 设置Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.setView(view, 0, 0, 0, 0);
    }

    /**
     * 设置标题
     * @param title
     */
    public void setTitle(String title){
        tv_title.setText(title);
    }

    public void setTitle(int title){
        tv_title.setText(title);
    }

    /**
     * 设置单选按钮内容
     */
    public void setRadioButtonContent(String[] contents){
        this.contents = contents;
        radiobuttons.clear();

        for(int x = 0; x < contents.length; x++){
            RadioButton rb = new RadioButton(context);
            rb.setText(contents[x]);
            rb.setTag(x);
            rb.setOnClickListener(radiobuttonClick);
            radiobuttons.add(rb);
            ll_menu_clean.addView(rb);
        }
    }

    /**
     * 设置当前的选择, 调用该方法前先调用 setRadioButtonContent 设置内容.
     * @param position
     * @return true设置成功, false设置失败
     */
    public boolean setPositon(int position){
        if(position >= radiobuttons.size()) return false;

        // 设置单选, 并将其他去掉单选
        for(RadioButton rb : radiobuttons){
            rb.setChecked(false);
        }
        radiobuttons.get(position).setChecked(true);
        return true;
    }

    View.OnClickListener radiobuttonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            if(callback != null) callback.onClick(position, contents[position]);

            // 设置被点击后的选择
            setPositon(position);
        }
    };

    @OnClick({R.id.bt_menu_clean_cancel})
    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.bt_menu_clean_cancel:
                if(callback != null) callback.onCanel();
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
