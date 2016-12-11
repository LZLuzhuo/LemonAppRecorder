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
import android.os.SystemClock;
import android.view.View;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.OnClick;
import me.luzhuo.lemonapprecorder.R;
import me.luzhuo.lemonapprecorder.app.AppManager;
import me.luzhuo.lemonapprecorder.presenter.MainPersenter;
import me.luzhuo.lemonapprecorder.ui.dialog.AppDetailDialog;
import me.luzhuo.lemonapprecorder.ui.view.IMainView;
import me.luzhuo.lemonapprecorder.utils.StatusBarUtils;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/11/4 15:23
 * <p>
 * Description: Main
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public class MainActivity extends SlidingFragmentActivity implements IMainView {
    private MainPersenter homePersenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.addActivity(this);
        setContentView(R.layout.activity_home);
        homePersenter = new MainPersenter(this);

        initData();
    }

    private void initData() {
        StatusBarUtils.setTranslucentStatus(this);

        // 初始化SlidingMenu
        homePersenter.initSlidingMenu();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.removeActivity(this);
    }

    // 两次返回键才推出应用
    private long[] mHits = new long[2];
    @Override
    public void onBackPressed() {
        System.arraycopy(mHits, 1, mHits, 0, mHits.length-1);
        mHits[mHits.length-1] = SystemClock.uptimeMillis();
        if(mHits[0]>=(SystemClock.uptimeMillis()-500)){
            //super.onBackPressed();
            finish();
        }
    }

}
