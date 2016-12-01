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
import me.luzhuo.lemonapprecorder.callback.adapter.CleanClassifyAdapterClick;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/11/19 19:42
 * <p>
 * Description: 清除分类信息的适配器
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public class CleanClassifyAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<String> arrayList;
	private CleanClassifyAdapterClick onClick;

	public CleanClassifyAdapter(Context context, ArrayList<String> arrayList, CleanClassifyAdapterClick onClick) {
		this.context = context;
		this.arrayList = arrayList;
		this.onClick = onClick;
	}

	@Override
	public int getCount() {
		return arrayList == null ? 0 : arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return arrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
        if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.dialog_item_clean_classify, parent, false);
			viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        
        String classify = arrayList.get(position);
        viewHolder.tv_cleanclassify_classifyName.setText(classify);
        if(classify.equals("精品") || classify.equals("一般") || classify.equals("垃圾")){
        	viewHolder.iv_cleanclassify_clean.setVisibility(View.INVISIBLE);
        }else{
        	viewHolder.iv_cleanclassify_clean.setVisibility(View.VISIBLE);
	        viewHolder.iv_cleanclassify_clean.setTag(position);
        }
        
        return convertView;
	}
	
    class ViewHolder{
		@BindView(R.id.tv_cleanclassify_classifyName)
		TextView tv_cleanclassify_classifyName;
		@BindView(R.id.iv_cleanclassify_clean)
		ImageView iv_cleanclassify_clean;

		public ViewHolder(View convertView) {
			ButterKnife.bind(this, convertView);
		}

		@OnClick({R.id.iv_cleanclassify_clean})
		public void onclick(View v) {
			if(v.getId() == R.id.iv_cleanclassify_clean){
				int position = (Integer) v.getTag();
				onClick.onCleanClick(position, arrayList.get(position));
			}
		}
    }

}
