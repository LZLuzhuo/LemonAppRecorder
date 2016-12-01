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
package me.luzhuo.lemonapprecorder.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.luzhuo.lemonapprecorder.R;
import me.luzhuo.lemonapprecorder.bean.AppInfo;
import me.luzhuo.lemonapprecorder.callback.adapter.CleanAppinfoAdapterClick;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/11/20 21:29
 * <p>
 * Description: 清除应用信息的列表的适配器
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public class CleanAppinfoAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<AppInfo> appInfos;
    private CleanAppinfoAdapterClick onClick;

    public CleanAppinfoAdapter(Context context, ArrayList<AppInfo> appInfos, CleanAppinfoAdapterClick onClick) {
        this.context = context;
        this.appInfos = appInfos;
        this.onClick = onClick;
    }

    @Override
    public int getCount() {
        return appInfos == null ? 0 : appInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return appInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.dialog_item_clean_appinfo, parent, false);
            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        AppInfo appInfo = appInfos.get(position);
        viewHolder.tv_clean_app_name.setText(appInfo.appName);
        viewHolder.tv_clean_app_packname.setText(appInfo.packName);

        if(appInfo.getIcon(context) == null) viewHolder.iv_clean_app_icon.setImageResource(R.mipmap.activity_home_add_apk_icon);
        else viewHolder.iv_clean_app_icon.setImageDrawable(appInfo.getIcon(context));

        viewHolder.iv_clean_delete.setTag(position);
        return convertView;
    }

    class ViewHolder{
        @BindView(R.id.tv_clean_app_name)
        TextView tv_clean_app_name;
        @BindView(R.id.tv_clean_app_packname)
        TextView tv_clean_app_packname;
        @BindView(R.id.iv_clean_app_icon)
        ImageView iv_clean_app_icon;
        @BindView(R.id.iv_clean_delete)
        ImageView iv_clean_delete;

        public ViewHolder(View convertView) {
            ButterKnife.bind(this, convertView);
        }

        @OnClick({R.id.iv_clean_delete})
        public void onclick(View v) {
            if(v.getId() == R.id.iv_clean_delete){
                int position = (Integer) v.getTag();
                onClick.onCleanClick(position, appInfos.get(position));
            }
        }
    }

}
