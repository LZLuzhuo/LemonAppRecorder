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
package me.luzhuo.lemonapprecorder.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.luzhuo.lemonapprecorder.R;
import me.luzhuo.lemonapprecorder.app.BaseFragment;
import me.luzhuo.lemonapprecorder.bean.AppInfo;
import me.luzhuo.lemonapprecorder.callback.dialog.AppDetailDialogCallBack;
import me.luzhuo.lemonapprecorder.callback.impl.AppDetailDialogCallBackImpl;
import me.luzhuo.lemonapprecorder.event.RefreshDataEvent;
import me.luzhuo.lemonapprecorder.presenter.HomePersenter;
import me.luzhuo.lemonapprecorder.ui.dialog.AppDetailDialog;
import me.luzhuo.lemonapprecorder.ui.view.IHomeView;
import me.luzhuo.lemonapprecorder.utils.LemonUtils;
import me.luzhuo.lemonapprecorder.utils.StatusBarUtils;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/11/20 22:40
 * <p>
 * Description: 首页, 展示所有app记录信息
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public class HomeFragment extends BaseFragment implements IHomeView {
	@BindView(R.id.lv_fragment_listview)
	ListView lv_fragment_listview;

	private Unbinder unbinder;
	private HomePersenter homePersenter;

	private AppDetailDialog appDetailDialog;

	@Override
	public View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.fragment_apps, null);
		StatusBarUtils.setStatusBarViewHeight(getContext(), view.findViewById(R.id.statusbar));
		unbinder = ButterKnife.bind(this, view);
		EventBus.getDefault().register(this);
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
		unbinder.unbind();
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		homePersenter = new HomePersenter(this);
		homePersenter.initListView(lv_fragment_listview);
	}

	@OnClick({R.id.iv_add_click})
	public void onclick(View v) {
		switch (v.getId()){
			case R.id.iv_add_click:
				// 添加应用
				addAppInfoDetail();
				break;
		}
	}

	/**
	 * 刷新应用信息列表数据
	 * @param messageEvent
     */
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void refreshAppinfoList(RefreshDataEvent messageEvent) {
		if(messageEvent.type != RefreshDataEvent.RefreshDataType.Appinfos) return;

		homePersenter.updateListViewAppinfo();
	}

	// ====== AppDetailDialog 查看应用信息的Dialog ======
	@Override
	public void watchAppInfoDetail(AppInfo appInfo) {
		if(appInfo == null) return;

		appDetailDialog = new AppDetailDialog(context, AppDetailDialog.AppDialogType.Watch, watchappDialogCallback);

		appDetailDialog.setAppInfo(appInfo);
		appDetailDialog.show();
	}

	AppDetailDialogCallBack watchappDialogCallback = new AppDetailDialogCallBackImpl(){
		@Override
		public void onUpdate(AppInfo appInfo) {
			homePersenter.updateAppinfo(appInfo);
			appDetailDialog.dismiss();
		}

		@Override
		public void onCancel() {
			appDetailDialog.dismiss();
		}
	};
	// ====== AppDetailDialog ======

	// ====== addAppInfoDetail 添加应用信息的Dialog ======
	@Override
	public void addAppInfoDetail() {
		appDetailDialog = new AppDetailDialog(context, AppDetailDialog.AppDialogType.Add, addappCallback);

		appDetailDialog.show();
	}

	AppDetailDialogCallBack addappCallback = new AppDetailDialogCallBackImpl(){
		@Override
		public void onConfirm(AppInfo appInfo) {
			// 确认添加app信息
			if(TextUtils.isEmpty(appInfo.packName)){
				LemonUtils.showQuickToast(context, "请选择要添加的应用!!!");
				return;
			}

			// 添加应用信息
			homePersenter.addAppinfo(appInfo);

			appDetailDialog.dismiss();
		}

		@Override
		public void onCancel() {
			appDetailDialog.dismiss();
		}
	};
	// ====== addAppInfoDetail ======

}
