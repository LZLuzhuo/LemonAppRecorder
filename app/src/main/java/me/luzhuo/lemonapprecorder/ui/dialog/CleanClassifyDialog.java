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
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.luzhuo.lemonapprecorder.R;
import me.luzhuo.lemonapprecorder.callback.adapter.CleanClassifyAdapterClick;
import me.luzhuo.lemonapprecorder.callback.dialog.CleanClassifyDialogCallBack;
import me.luzhuo.lemonapprecorder.ui.adapter.CleanClassifyAdapter;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/11/19 18:33
 * <p>
 * Description: 清除分类信息的Dialog
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public class CleanClassifyDialog {
    @BindView(R.id.lv_clean_classify)
    ListView lv_clean_classify;

    private Context context;
    private AlertDialog dialog;
    private CleanClassifyDialogCallBack callback;
    private ArrayList<String> classifys = new ArrayList<>();
    private CleanClassifyAdapter cleanClassifyAdapter;

    public CleanClassifyDialog(Context context, CleanClassifyDialogCallBack callback){
        this.context = context;
        this.callback = callback;

        View view = View.inflate(context, R.layout.dialog_clean_classify, null);
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
        cleanClassifyAdapter = new CleanClassifyAdapter(context, classifys, cleanCallback);
        lv_clean_classify.setAdapter(cleanClassifyAdapter);
    }

    /**
     * 设置数据, 可以先展示, 后设置数据.
     * @param cls
     */
    public void setData(ArrayList<String> cls){
        classifys.clear();
        classifys.addAll(cls);

        cleanClassifyAdapter.notifyDataSetChanged();
    }

    /**
     * 获取数据
     * @return
     */
    public ArrayList<String> getData(){
        return classifys;
    }

    /**
     * 移除集合中的某个元素
     * @param position 索引位置
     */
    public void removeList(int position){
        classifys.remove(position);
        cleanClassifyAdapter.notifyDataSetChanged();
    }

    CleanClassifyAdapterClick cleanCallback = new CleanClassifyAdapterClick() {
        @Override
        public void onCleanClick(int position, String content) {
            if(callback != null) callback.onCleanButton(position, content);
        }
    };

    @OnClick({R.id.iv_clean_classify_cancel})
    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.iv_clean_classify_cancel:
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
