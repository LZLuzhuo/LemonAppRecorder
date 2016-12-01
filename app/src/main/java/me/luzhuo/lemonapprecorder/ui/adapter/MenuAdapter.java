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

import butterknife.BindView;
import butterknife.ButterKnife;
import me.luzhuo.lemonapprecorder.R;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/11/14 17:53
 * <p>
 * Description: 主界面左侧菜单栏适配器
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public class MenuAdapter extends BaseAdapter {
    private int[] menuIcon;
    private String[] menuString;
    private Context context;

    public MenuAdapter(Context context, int[] menuIcon, String[] menuString) {
        this.menuIcon = menuIcon;
        this.menuString = menuString;
        this.context = context;
    }

    @Override
    public int getCount() {
        return menuString.length;
    }

    @Override
    public Object getItem(int position) {
        return menuString[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.menu_item, parent, false);
            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.menu_icon.setImageResource(menuIcon[position]);
        viewHolder.menu_button.setText(menuString[position]);
        return convertView;
    }

    class ViewHolder{
        @BindView(R.id.menu_icon)
        ImageView menu_icon;
        @BindView(R.id.menu_button)
        TextView menu_button;

        public ViewHolder(View convertView) {
            ButterKnife.bind(this, convertView);
        }
    }
}
