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
package me.luzhuo.lemonapprecorder.dp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/11/6 15:00
 * <p>
 * Description: 数据库帮助类,代码中的被注释的代码在升级数据库版本的时候使用.
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public class LemonOpenHelper extends SQLiteOpenHelper {
	private static LemonOpenHelper openHelper;
	
	private static final String DATABASENAME = "LemonAppRecorder.db";
	private static final int START_VERSION = 1;
	//private static final int HISTORY_VERSION_1 = 1; //历史版本
	//private static final int CURRENT_VERSION = 1; //现在版本
	
	public static final String TABLE_ID = "_id";
	
	// 类型数据表
	public static final String TABLE_CLASSIFY_TABLENAME = "classify"; //表明
	public static final String TABLE_CLASSIFY_NAME = "classifyName";
	// app信息数据表
	public static final String TABLE_APP_TABLENAME = "app";
	public static final String TABLE_APP_ICONFILENAEM = "iconFileName";
	public static final String TABLE_APP_SAVEICONPATH = "saveIconPath";
	public static final String TABLE_APP_APPNAME = "name";
	public static final String TABLE_APP_APPPACKNAME = "packName";
	public static final String TABLE_APP_CLASSIFY = "classify";
	public static final String TABLE_APP_REMARKS = "remarks";
	public static final String TABLE_APP_DATE = "date";
	// 新app未处理信息表
	public static final String TABLE_NEWAPP_TABLENAME = "newApp";
	public static final String TABLE_NEWAPP_APPNAME = "name";
	public static final String TABLE_NEWAPP_APPPACKNAME = "packName";


	private LemonOpenHelper(Context context, String name, CursorFactory factory,int version) {
		super(context, name, factory, version);
	}
	
	public static LemonOpenHelper getInstance(Context context){
		// 单例模式获取数据库帮助类对象
		if(openHelper == null){
			synchronized (LemonOpenHelper.class){
				if(openHelper == null){
					openHelper = new LemonOpenHelper(context, DATABASENAME, null, START_VERSION);
				}
			}
		}
		return openHelper;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 创建表
		String sql = "create table "+TABLE_CLASSIFY_TABLENAME+" ("+TABLE_ID+" integer primary key, "+TABLE_CLASSIFY_NAME+" varchar(30) ); ";
		db.execSQL(sql);
		
		sql = "create table "+TABLE_APP_TABLENAME+" ("+TABLE_ID+" integer primary key, "+TABLE_APP_ICONFILENAEM+" varchar(300), "+TABLE_APP_SAVEICONPATH+" varchar(10), "+TABLE_APP_APPNAME+" varchar(50), "+TABLE_APP_APPPACKNAME+" varchar(200), "+TABLE_APP_CLASSIFY+" varchar(30), "+TABLE_APP_REMARKS+" varchar(500), "+TABLE_APP_DATE+" integer ); ";
		db.execSQL(sql);
		
		sql = "create table "+TABLE_NEWAPP_TABLENAME+" ("+TABLE_ID+" integer primary key, "+TABLE_NEWAPP_APPNAME+" varchar(50), "+TABLE_NEWAPP_APPPACKNAME+" varchar(200) ); ";
		db.execSQL(sql);
		
		// 插入初始数据
		sql = "insert into "+TABLE_CLASSIFY_TABLENAME+" values(null, '精品' ); ";
		db.execSQL(sql);
		sql = "insert into "+TABLE_CLASSIFY_TABLENAME+" values(null, '一般' ); ";
		db.execSQL(sql);
		sql = "insert into "+TABLE_CLASSIFY_TABLENAME+" values(null, '垃圾' ); ";
		db.execSQL(sql);
		
		// 更新表
		//onUpgrade(db, START_VERSION, CURRENT_VERSION);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		switch (oldVersion) {
//		case START_VERSION:
//			db.execSQL("");
//		case HISTORY_VERSION_1:
//			db.execSQL("");
//		case 3:
//			break;
//		}
	}
}
