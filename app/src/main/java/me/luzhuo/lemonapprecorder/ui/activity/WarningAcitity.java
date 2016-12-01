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
package me.luzhuo.lemonapprecorder.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.luzhuo.lemonapprecorder.R;
import me.luzhuo.lemonapprecorder.app.BaseActivity;
import me.luzhuo.lemonapprecorder.bean.AppInfo;
import me.luzhuo.lemonapprecorder.presenter.WarningPersenter;
import me.luzhuo.lemonapprecorder.ui.view.IWarningView;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/11/21 23:40
 * <p>
 * Description: 警告活动页, 提示用户是否删除新安装的记录(数据库已记录)
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public class WarningAcitity extends BaseActivity implements IWarningView{
	@BindView(R.id.iv_warning_app_icon)
	ImageView iv_warning_app_icon;
	@BindView(R.id.tv_warning_appname)
	TextView tv_warning_appname;
	@BindView(R.id.tv_warning_classify)
	TextView tv_warning_classify;
	@BindView(R.id.et_warning_remarks)
	EditText et_warning_remarks;
	@BindView(R.id.tv_warning_packname)
	TextView tv_warning_packname;

	private AppInfo appInfo;
	private WarningPersenter warningPersenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_warning);
		ButterKnife.bind(this);
		warningPersenter = new WarningPersenter(this);
		
		try{
			appInfo = (AppInfo) getIntent().getSerializableExtra("baseAppInfo");
		}catch(Exception e){
			e.printStackTrace();
			return;
		}
		
		initData();
	}

	private void initData() {
		// 设置数据
		tv_warning_appname.setText(appInfo.appName);
		tv_warning_packname.setText(appInfo.packName);
		tv_warning_classify.setText(appInfo.classify);
		iv_warning_app_icon.setImageDrawable(appInfo.getIcon(this));
		et_warning_remarks.setText(appInfo.remarks);
	}

	@OnClick({R.id.iv_warning_cancel, R.id.ll_warning_delete, R.id.ll_warning_ok})
	public void onclick(View v) {
		switch (v.getId()){
			case R.id.iv_warning_cancel: // 关闭
			case R.id.ll_warning_ok: // 保留
				break;
			case R.id.ll_warning_delete:
				// 删除应用
				warningPersenter.uninstallApk(appInfo.packName);
				break;
		}
		finish();
	}

}
