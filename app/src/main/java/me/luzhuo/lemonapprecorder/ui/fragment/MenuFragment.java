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

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.luzhuo.lemonapprecorder.R;
import me.luzhuo.lemonapprecorder.app.BaseFragment;
import me.luzhuo.lemonapprecorder.callback.dialog.ProgressDialogCallBack;
import me.luzhuo.lemonapprecorder.callback.dialog.WarnDialogCallBack;
import me.luzhuo.lemonapprecorder.model.impl.IDataSerializationImpl;
import me.luzhuo.lemonapprecorder.presenter.MenuPersenter;
import me.luzhuo.lemonapprecorder.ui.dialog.ProgressDialog;
import me.luzhuo.lemonapprecorder.ui.dialog.WarnDialog;
import me.luzhuo.lemonapprecorder.ui.view.IMenuView;
import me.luzhuo.lemonapprecorder.utils.StatusBarUtils;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/11/11 12:03
 * <p>
 * Description: 菜单
 * <p>
 * Revision History:
 * 		1. 2016.12.21 添加导入导出数据时, 获取存储运行时权限.
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
@RuntimePermissions
public class MenuFragment extends BaseFragment implements IMenuView {
	@BindView(R.id.tv_menu_appversion)
	TextView tv_menu_appversion;
	@BindView(R.id.lv_menu_listview)
	ListView lv_menu_listview;

	private Unbinder unbinder;
	private MenuPersenter menuPersenter;
	private ProgressDialog progressDialog;
	private WarnDialog warnDialog;

	@Override
	public View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.fragment_menu, null);
		StatusBarUtils.setStatusBarViewHeight(getContext(), view.findViewById(R.id.statusbar));
		unbinder = ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unbinder.unbind();
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		menuPersenter = new MenuPersenter(this);
		menuPersenter.initListView(lv_menu_listview);
		menuPersenter.initVersion(tv_menu_appversion);
	}

	// ====== ProgressDialog 进度提示框 ======
	@Override
	public void showProgressDialog(boolean isShow) {
		progressDialog = new ProgressDialog(context, procallback);
		progressDialog.show();
	}

	@Override
	public void showProgressError(String error) {
		progressDialog.setMax(1);
		progressDialog.setHit(error);
		progressDialog.showOffButton(true);
	}

	@Override
	public void showProgressInit(String init) {
		progressDialog.setMax(1);
		progressDialog.setHit(init);
		progressDialog.setProgress(0);
	}

	@Override
	public void setProgressMax(int max, String hint) {
		progressDialog.setMax(max);
		progressDialog.setHit(IDataSerializationImpl.taskName[4]);
	}

	@Override
	public void showProgressComplete() {
		progressDialog.setHit("完成");
		progressDialog.showOffButton(true);
	}

	@Override
	public void setProgress(int progress) {
		progressDialog.setProgress(progress);
	}

	ProgressDialogCallBack procallback = new ProgressDialogCallBack() {
		@Override
		public void onComplete() {
			progressDialog.dismiss();
		}
	};
	// ====== ProgressDialog ======

	// ====== WarnDialog 警告框 ======
	@Override
	public void showWarnDialog(WarnDialog.WarnType warnType) {
		warnDialog = new WarnDialog(context, warnType, warnCallback);

		warnDialog.show();
	}

	WarnDialogCallBack warnCallback = new WarnDialogCallBack() {
		@Override
		public void onCancel() {
			warnDialog.dismiss();
		}

		@Override
		public void onConfirm(WarnDialog.WarnType warntype) {
			// 显示进度
			showProgressDialog(true);
			warnDialog.dismiss();

			if(warntype == WarnDialog.WarnType.ImportWarn){
				// 导入数据的警告处理
				MenuFragmentPermissionsDispatcher.runImportDataWithCheck(MenuFragment.this); // 导入数据的6.0运行时存储权限的处理

			}else if(warntype == WarnDialog.WarnType.ExportWarn){
				// 导出数据的警告处理
				MenuFragmentPermissionsDispatcher.runExportDataWithCheck(MenuFragment.this); // 导出数据的6.0运行时存储权限的处理
			}
		}
	};
	// ====== WarnDialog ======

	// ====== 6.0 运行时权限请求 ======
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		MenuFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
	}

	@NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
	void runImportData() {
		menuPersenter.importData();
	}

	@NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
	void runExportData() {
		menuPersenter.exportData();
	}

	@OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
	void onOnPermissionDenied() {
		showProgressError("用户拒绝获取存储权限!");
	}
	// ====== 6.0 运行时权限请求 ======

}
