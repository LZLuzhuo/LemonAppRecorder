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
package me.luzhuo.lemonapprecorder.bean;

import java.util.ArrayList;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/11/15 14:59
 * <p>
 * Description: 数据库记录的应用信息数量
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public class AppDatasCount {

    /**
     * 总数
     */
    public long total;

    /**
     * 分类下的App数量
     */
    public ArrayList<ClassifyAppCount> classifyAppCount;

    public static class ClassifyAppCount{
        /**
         * 分类名
         */
        public String classify;

        /**
         * 数量
         */
        public long count;
    }
}
