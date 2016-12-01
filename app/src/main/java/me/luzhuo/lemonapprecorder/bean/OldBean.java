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

import static me.luzhuo.lemonapprecorder.model.impl.IDataSerializationImpl.classify;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/12/1 22:49
 * <p>
 * Description: 旧版本的bean, 主要用于旧版本的备份数据的导入
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public class OldBean {
	
	public static class OldClassify{
		public String classify;
	}
	
	public static class OldAppinfo{
		public String classify;
		public String remarks;
		public String icon;
		public String packname;
		public String name;
		public long date;
	}

}
