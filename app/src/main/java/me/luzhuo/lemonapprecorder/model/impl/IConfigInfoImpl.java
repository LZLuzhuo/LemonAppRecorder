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
package me.luzhuo.lemonapprecorder.model.impl;

import android.content.Context;

import me.luzhuo.lemonapprecorder.model.IConfigInfo;
import me.luzhuo.lemonapprecorder.utils.SharePrefUtil;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/11/15 16:54
 * <p>
 * Description:
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public class IConfigInfoImpl implements IConfigInfo {
    private Context context;

    public IConfigInfoImpl(Context context){
        this.context = context;
    }

    @Override
    public int getIconSavePath() {
        if(SharePrefUtil.getBoolean(context, SharePrefUtil.ICONSAVAEXTERNAL, true)) return SDCard;
        else return Mobile;
    }

    @Override
    public void saveIconSavePath(int iconSavaPath) {
        if(iconSavaPath == SDCard) SharePrefUtil.saveBoolean(context, SharePrefUtil.ICONSAVAEXTERNAL, true);
        else SharePrefUtil.saveBoolean(context, SharePrefUtil.ICONSAVAEXTERNAL, false);
    }
}
