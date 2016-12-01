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
package me.luzhuo.lemonapprecorder.ui.pop;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.luzhuo.lemonapprecorder.R;
import me.luzhuo.lemonapprecorder.callback.dialog.AddClassifyDialogCallBack;
import me.luzhuo.lemonapprecorder.callback.pop.SelectClassifyPopCallBack;
import me.luzhuo.lemonapprecorder.model.IAppInfos;
import me.luzhuo.lemonapprecorder.model.impl.IAppinfosImpl;
import me.luzhuo.lemonapprecorder.ui.adapter.SelectClassifyAdapter;
import me.luzhuo.lemonapprecorder.ui.dialog.AddClassifyDialog;
import me.luzhuo.lemonapprecorder.utils.LemonUtils;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/11/7 0:59
 * <p>
 * Description: 选择分类的PopWindow
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public class SelectClassifyPop extends PopupWindow {
	@BindView(R.id.classify_listview)
	ListView classify_listview;

	private SelectClassifyAdapter classifyAdapter;
	private ArrayList<String> classifys = new ArrayList<>();
	private IAppInfos iAppInfos;
	private Context context;
	private AddClassifyDialog classifyDialog;
	private SelectClassifyPopCallBack callBack;

	public SelectClassifyPop(Context context, SelectClassifyPopCallBack callback) {
		this.context = context;
		this.callBack = callback;

		View view = View.inflate(context, R.layout.pop_select_classify, null);
		ButterKnife.bind(this, view);
		iAppInfos = new IAppinfosImpl(context);

		this.setContentView(view);
		this.setWidth(LemonUtils.dipTopx(context, 165));
		this.setHeight(LemonUtils.dipTopx(context, 100));
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		this.update();
		this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		classifyAdapter = new SelectClassifyAdapter(context, classifys);
		classify_listview.setAdapter(classifyAdapter);
		classify_listview.setOnItemClickListener(listviewItemCallback);
	}

	@OnClick({R.id.tv_classify_add})
	public void onclick(View v) {
		switch (v.getId()){
			case R.id.tv_classify_add:
				// 添加自定义分类
				classifyDialog = new AddClassifyDialog(context, addCallback);
				classifyDialog.show();
				break;
		}
	}

	AddClassifyDialogCallBack addCallback = new AddClassifyDialogCallBack() {
		@Override
		public void onConfirm(String text) {
			if(TextUtils.isEmpty(text)) {
				LemonUtils.showQuickToast(context, "内容为空, 请先输入内容!");
				return;
			}

			// 查询分类是否存在
			if(iAppInfos.isExistClassify(text)) {
				LemonUtils.showQuickToast(context, "该分类已存在, 请填写其他分类名!");
				return;
			}

			// 添加分类
			if(!iAppInfos.addClassify(text)) {
				LemonUtils.showQuickToast(context, "添加分类失败, 请重试!");
				return;
			}

			// 刷新数据
			updateClassify();
			classifyDialog.dismiss();
		}
	};

	OnItemClickListener listviewItemCallback = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// 将用户选择的分类传出去, 并关闭弹框
			if(callBack != null) callBack.onSelectClassify(classifys.get(position));
		}
	};
	
	/**
	 * 展示pop
	 * @param view
	 */
	public void show(View view) {
		updateClassify();

        if (!this.isShowing()) {
        	int[] location = new int[2];
            view.getLocationInWindow(location);
			this.showAtLocation(view, Gravity.LEFT | Gravity.TOP, location[0]+LemonUtils.dipTopx(context, 35), location[1] + LemonUtils.dipTopx(context, 20)); //在哪个位置显示,距屏幕的距离px
        } else {
            this.dismiss();
        }
	}

	/**
	 * 更新数据
	 */
	public void updateClassify(){
		// TODO: 2016/11/22 RXJava
		ArrayList<String> cls = iAppInfos.queryAllClassifys();
		classifys.clear();
		classifys.addAll(cls);
		classifyAdapter.notifyDataSetChanged();
	}

}
