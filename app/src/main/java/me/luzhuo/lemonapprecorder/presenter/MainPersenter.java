package me.luzhuo.lemonapprecorder.presenter;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import me.luzhuo.lemonapprecorder.R;
import me.luzhuo.lemonapprecorder.model.IAppInfos;
import me.luzhuo.lemonapprecorder.model.impl.IAppinfosImpl;
import me.luzhuo.lemonapprecorder.ui.dialog.AppDetailDialog;
import me.luzhuo.lemonapprecorder.ui.fragment.HomeFragment;
import me.luzhuo.lemonapprecorder.ui.fragment.MenuFragment;
import me.luzhuo.lemonapprecorder.ui.view.IMainView;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/11/4 16:26
 * <p>
 * Description: Main的控制层
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public class MainPersenter {
	private IMainView iMainView;
	private Context context;
	private SlidingFragmentActivity sfa;

	private SlidingMenu sm;
	private AppDetailDialog appDetailDialog;
	private IAppInfos iAppInfos;

	public MainPersenter(IMainView iMainView){
        this.iMainView = iMainView;
		this.context = (Context)iMainView;
		this.sfa = (SlidingFragmentActivity) iMainView;
		iAppInfos = new IAppinfosImpl(context);
	}

	/**
	 * 初始化 SlidingMenu
	 */
	public void initSlidingMenu() {
		sfa.setBehindContentView(R.layout.activity_menu); // 设置菜单页

		// 进入就显示fragment1
		HomeFragment appsFragment = new HomeFragment();
		sfa.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, appsFragment).commit();

		sm = sfa.getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT); // 左边
		sm.setBehindWidthRes(R.dimen.slidingmenu_offset);
		sm.setShadowWidth(R.dimen.shadow_width);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		// 创建fragment
		MenuFragment menuFragment = new MenuFragment();
		sfa.getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, menuFragment, "Menu").commit();
	}

	/**
	 * 切换fragment
	 * @param f
	 */
	public void switchFragment(Fragment f) {
		sfa.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, f).commit();
		sm.toggle(); // 自动切换
	}
}
