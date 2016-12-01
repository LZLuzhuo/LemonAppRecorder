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
import android.graphics.Color;
import android.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import org.xclcharts.chart.PieData;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.luzhuo.lemonapprecorder.R;
import me.luzhuo.lemonapprecorder.bean.AppDatasCount;
import me.luzhuo.lemonapprecorder.callback.dialog.StatisticsDialogCallBack;
import me.luzhuo.lemonapprecorder.model.IAppInfos;
import me.luzhuo.lemonapprecorder.model.impl.IAppinfosImpl;
import me.luzhuo.lemonapprecorder.widget.piechart.PieChartView;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/11/14 22:42
 * <p>
 * Description: 统计Dialog
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public class StatisticsDialog {
    @BindView(R.id.pcv_piechart_statistics)
    PieChartView pcv_piechart_statistics;
    @BindView(R.id.tv_statistics_hint)
    TextView tv_statistics_hint;

    /**
     * 饼图颜色, 颜色还太少, 需要继续扩展.
     */
    public static final int[] cocors = new int[]{Color.rgb(90, 79, 88),Color.rgb(60, 173, 213),Color.rgb(191, 79, 75),Color.rgb(155, 187, 90),Color.rgb(242, 167, 69)};

    private AlertDialog dialog;
    private StatisticsDialogCallBack callback;
    private IAppInfos iAppInfos;

    public StatisticsDialog(Context context, StatisticsDialogCallBack callback){
        this.callback = callback;
        iAppInfos = new IAppinfosImpl(context);

        View view = View.inflate(context, R.layout.dialog_menu_statistics, null);
        ButterKnife.bind(this, view);

        // 设置Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.setView(view, 0, 0, 0, 0);

        initPiechart();
    }

    private void initPiechart() {
        // TODO: 2016/11/22 RXJava
        final AppDatasCount appclassifycount = iAppInfos.queryClassifyAppCount();

        tv_statistics_hint.setText("全部数据总量:".concat(String.valueOf(appclassifycount.total).concat("条")));

        // 设置PieChartView
        final ArrayList<PieData> chartData = new ArrayList<>();
        int coclorssum = cocors.length;
        int residue = coclorssum;
        Random random = new Random();
        //设置图表数据源
        for (AppDatasCount.ClassifyAppCount appcount : appclassifycount.classifyAppCount) {
            if(residue>0){
                chartData.add(new PieData(appcount.classify, "", ((float)appcount.count / (float)appclassifycount.total) * 100f, cocors[residue-1]));
            }else{
                int nextInt = random.nextInt(coclorssum);
                chartData.add(new PieData(appcount.classify, "", ((float)appcount.count / (float)appclassifycount.total) * 100f, cocors[nextInt]));
            }
            residue --;
        }
        pcv_piechart_statistics.setData(chartData);

        pcv_piechart_statistics.setOnClickItemLinstener(new PieChartView.OnClickItem() {
            @Override
            public void onClick(int position, boolean isOpen) {
                if(isOpen){
                    AppDatasCount.ClassifyAppCount count = appclassifycount.classifyAppCount.get(position);
                    tv_statistics_hint.setText(count.classify+": "+count.count+"条");
                }else{
                    tv_statistics_hint.setText("全部数据总量:".concat(String.valueOf(appclassifycount.total).concat("条")));
                }
            }
        });
        pcv_piechart_statistics.show();
    }

    @OnClick({R.id.iv_statistics_cancel})
    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.iv_statistics_cancel:
                if(callback != null) callback.onCancel();
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
