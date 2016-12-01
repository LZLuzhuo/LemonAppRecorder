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

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
 * Creation Date: 2016/11/6 14:15
 * <p>
 * Description: 首页 的数据适配器
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public class HomeAdapter extends BaseAdapter {
	private ArrayList<Object> appInfos;
	private Context context;

	public HomeAdapter(Context context, ArrayList<Object> appInfos) {
		this.appInfos = appInfos;
		this.context = context;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.fragment_app_item, parent, false);
			viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Object object = appInfos.get(position);
        if(object instanceof String){
        	viewHolder.linear.setVisibility(View.GONE);
        	viewHolder.frag_app_classify.setVisibility(View.VISIBLE);
        	
        	viewHolder.frag_app_classify.setText((String)object);

        }else if(object instanceof AppInfo){
        	viewHolder.linear.setVisibility(View.VISIBLE);
        	viewHolder.frag_app_classify.setVisibility(View.GONE);
        	
        	AppInfo appInfo = ((AppInfo)object);
        	viewHolder.frag_app_name.setText(appInfo.appName);
        	viewHolder.frag_app_packname.setText(appInfo.packName);

        	if(appInfo.getIcon(context) == null) viewHolder.frag_app_icon.setImageResource(R.mipmap.activity_home_add_apk_icon);
        	else viewHolder.frag_app_icon.setImageDrawable(appInfo.getIcon(context));
        }
        return convertView;
	}
	
    class ViewHolder{
		@BindView(R.id.linear)
		LinearLayout linear;
		@BindView(R.id.frag_app_classify)
		TextView frag_app_classify;
		@BindView(R.id.frag_app_name)
		TextView frag_app_name;
		@BindView(R.id.frag_app_packname)
		TextView frag_app_packname;
		@BindView(R.id.frag_app_icon)
		ImageView frag_app_icon;

		public ViewHolder(View convertView) {
			ButterKnife.bind(this, convertView);
		}
    }
}
