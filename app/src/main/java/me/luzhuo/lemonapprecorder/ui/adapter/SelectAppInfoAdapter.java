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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.luzhuo.lemonapprecorder.R;
import me.luzhuo.lemonapprecorder.bean.AppInfo;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/11/11 17:38
 * <p>
 * Description: 选择应用信息适配器
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public class SelectAppInfoAdapter extends BaseAdapter {
    private List<AppInfo> appinfos;
    private Context context;

    public SelectAppInfoAdapter(Context context, List<AppInfo> appinfos) {
        this.appinfos = appinfos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return appinfos == null ? 0 : appinfos.size();
    }

    @Override
    public Object getItem(int position) {
        return appinfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.dialog_item_select_app, parent, false);
            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        AppInfo appInfo = appinfos.get(position);

        viewHolder.tv_select_app_name.setText(TextUtils.isEmpty(appInfo.appName) ? "don't know the name" : appInfo.appName);
        viewHolder.tv_select_app_packname.setText(TextUtils.isEmpty(appInfo.packName) ? "don't know the packname" : appInfo.packName);

        if(appInfo.getIcon(context) != null) viewHolder.iv_select_app_icon.setImageDrawable(appInfo.getIcon(context));
        else viewHolder.iv_select_app_icon.setImageResource(R.mipmap.activity_home_add_apk_icon);

        return convertView;
    }

    class ViewHolder{
        @BindView(R.id.iv_select_app_icon)
        ImageView iv_select_app_icon;
        @BindView(R.id.tv_select_app_name)
        TextView tv_select_app_name;
        @BindView(R.id.tv_select_app_packname)
        TextView tv_select_app_packname;

        public ViewHolder(View convertView) {
            ButterKnife.bind(this, convertView);
        }
    }
}
