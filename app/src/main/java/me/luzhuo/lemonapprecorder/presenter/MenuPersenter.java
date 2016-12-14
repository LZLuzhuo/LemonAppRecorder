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
package me.luzhuo.lemonapprecorder.presenter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import me.luzhuo.lemonapprecorder.R;
import me.luzhuo.lemonapprecorder.bean.AppInfo;
import me.luzhuo.lemonapprecorder.callback.dialog.CleanAppinfoDialogCallBack;
import me.luzhuo.lemonapprecorder.callback.dialog.CleanClassifyDialogCallBack;
import me.luzhuo.lemonapprecorder.callback.dialog.RadioButtonDialogCallBack;
import me.luzhuo.lemonapprecorder.callback.dialog.StatisticsDialogCallBack;
import me.luzhuo.lemonapprecorder.event.RefreshDataEvent;
import me.luzhuo.lemonapprecorder.model.IAppInfos;
import me.luzhuo.lemonapprecorder.model.IConfigInfo;
import me.luzhuo.lemonapprecorder.model.IDataSerialization;
import me.luzhuo.lemonapprecorder.model.impl.IAppinfosImpl;
import me.luzhuo.lemonapprecorder.model.impl.IConfigInfoImpl;
import me.luzhuo.lemonapprecorder.model.impl.IDataSerializationImpl;
import me.luzhuo.lemonapprecorder.ui.adapter.MenuAdapter;
import me.luzhuo.lemonapprecorder.ui.dialog.CleanAppinfoDialog;
import me.luzhuo.lemonapprecorder.ui.dialog.CleanClassifyDialog;
import me.luzhuo.lemonapprecorder.ui.dialog.RadioButtonDialog;
import me.luzhuo.lemonapprecorder.ui.dialog.StatisticsDialog;
import me.luzhuo.lemonapprecorder.ui.dialog.WarnDialog;
import me.luzhuo.lemonapprecorder.ui.fragment.MenuFragment;
import me.luzhuo.lemonapprecorder.ui.view.IMenuView;
import me.luzhuo.lemonapprecorder.utils.FileUtil;
import me.luzhuo.lemonapprecorder.utils.LemonUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.R.id.list;
import static android.content.ContentValues.TAG;
import static android.media.CamcorderProfile.get;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/11/12 8:44
 * <p>
 * Description: 右侧菜单的控制层
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public class MenuPersenter {
    private IMenuView iMenuView;
    private Context context;
    private IConfigInfo iConfigInfo;
    private IAppInfos iAppInfos;
    private IDataSerialization iDataSerialization;

    private static int[] menuIcon = new int[]{R.mipmap.menu_item_import,R.mipmap.menu_item_export,R.mipmap.menu_item_clean,R.mipmap.menu_item_location,R.mipmap.menu_item_statistics};//图标
    private static String[] menuString = new String[]{"导入数据","导出数据","清理数据","图标保存位置","统计数据"}; //对应文字

    private MenuAdapter menuAdapter;
    private RadioButtonDialog cleanDialog, iconSaveDialog;
    private StatisticsDialog statisticsDialog;
    private CleanClassifyDialog cleanClassifyDialog;
    private CleanAppinfoDialog cleanAppinfoDialog;

    public MenuPersenter(IMenuView iMenuView){
        this.iMenuView = iMenuView;
        this.context = ((MenuFragment)iMenuView).getContext();
        iConfigInfo = new IConfigInfoImpl(context);
        iAppInfos = new IAppinfosImpl(context);
        iDataSerialization = new IDataSerializationImpl(context);
    }

    public void initListView(ListView lv_menu_listview) {
        menuAdapter = new MenuAdapter(context, menuIcon, menuString);
        lv_menu_listview.setAdapter(menuAdapter);
        lv_menu_listview.setOnItemClickListener(onListViewClick);
    }

    AdapterView.OnItemClickListener onListViewClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position){
                case 0: // 导入数据
                    iMenuView.showWarnDialog(WarnDialog.WarnType.ImportWarn);
                    break;
                case 1: // 导出数据
                    iMenuView.showWarnDialog(WarnDialog.WarnType.ExportWarn);
                    break;
                case 2: // 清理数据
                    cleanDialog = new RadioButtonDialog(context, cleanCallback);

                    cleanDialog.setTitle(R.string.menu_select_clean);
                    cleanDialog.setRadioButtonContent(new String[]{"清理分类信息", "清理App记录信息"});
                    cleanDialog.show();
                    break;
                case 3: // 图标保存位置
                    iconSaveDialog = new RadioButtonDialog(context, iconSaveCallback);

                    iconSaveDialog.setTitle(R.string.menu_dialog_location);
                    iconSaveDialog.setRadioButtonContent(new String[]{"保存到手机存储", "保存到SD卡"});
                    iconSaveDialog.setPositon(iConfigInfo.getIconSavePath() == IConfigInfo.Mobile ? 0 : 1);
                    iconSaveDialog.show();
                    break;
                case 4: // 统计数据
                    statisticsDialog = new StatisticsDialog(context, StatisticsCallback);

                    statisticsDialog.show();
                    break;
            }
        }
    };

    /**
     * 导入数据
     */
    public void importData() {
        // 如果文件不存在
        if(!IDataSerializationImpl.apps.exists()){
            iMenuView.showProgressError("资源文件不存在!");
            return;
        }

        // RXJava
        Observable.just(true)
        .observeOn(Schedulers.io())
        .map(new Func1<Boolean, Boolean>() {
            @Override
            public Boolean call(Boolean s) {

                // 删除SD卡上的 和 手机存储里的所有 图标 数据
                // 删除SD卡上的图标
                iDataSerialization.deleteFilesForDirectory(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
                // 删除手机存储里的图标 // /data/data/youPackageName/files
                iDataSerialization.deleteFilesForDirectory(context.getFilesDir());

                // 删除表
                iAppInfos.deleteAllAppInfos();
                iAppInfos.deleteAllClassifys();

                return s;
            }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .map(new Func1<Boolean, Boolean>() {
            @Override
            public Boolean call(Boolean s) {

                iMenuView.showProgressInit(IDataSerializationImpl.taskName[3]);
                return s;
            }
        })
        .observeOn(Schedulers.io())
        .map(new Func1<Boolean, List<String>>() {
            @Override
            public List<String> call(Boolean s) {

                // 读取classify文件并解析成list
                List<String> list = iDataSerialization.loadClassifyForFile(IDataSerializationImpl.classify);

                return list;
            }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .map(new Func1<List<String>, List<String>>() {
            @Override
            public List<String> call(List<String> list) {

                if(list == null){
                    iMenuView.showProgressError(IDataSerializationImpl.taskName[6]);

                    throw new NullPointerException(IDataSerializationImpl.taskName[6]);
                }else{
                    iMenuView.setProgressMax(list.size(), IDataSerializationImpl.taskName[4]);
                }

                return list;
            }
        })
        .observeOn(Schedulers.io())
        .map(new Func1<List<String>, Boolean>() {
            @Override
            public Boolean call(List<String> list) {

                // classify表中插入数据
                insertClassify(list);

                return true;
            }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .map(new Func1<Boolean, Boolean>() {
            @Override
            public Boolean call(Boolean s) {

                iMenuView.showProgressInit(IDataSerializationImpl.taskName[3]);

                return s;
            }
        })
        .observeOn(Schedulers.io())
        .map(new Func1<Boolean, List<AppInfo>>() {
            @Override
            public List<AppInfo> call(Boolean s) {

                // 读取app文件并解析成list
                List<AppInfo> listapp = iDataSerialization.loadAppInfosForFile(IDataSerializationImpl.apps);

                return listapp;
            }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .map(new Func1<List<AppInfo>, List<AppInfo>>() {
            @Override
            public List<AppInfo> call(List<AppInfo> listapp) {

                if(listapp == null){
                    iMenuView.showProgressError(IDataSerializationImpl.taskName[6]);
                    throw new NullPointerException(IDataSerializationImpl.taskName[6]);
                }else{
                    iMenuView.setProgressMax(listapp.size(), IDataSerializationImpl.taskName[4]);
                }

                return listapp;
            }
        })
        .observeOn(Schedulers.io())
        .map(new Func1<List<AppInfo>, List<AppInfo>>() {
            @Override
            public List<AppInfo> call(List<AppInfo> listapp) {

                // app表中插入数据
                insertApp(listapp);

                return listapp;
            }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .map(new Func1<List<AppInfo>, List<AppInfo>>() {
            @Override
            public List<AppInfo> call(List<AppInfo> listapp) {

                iMenuView.setProgressMax(listapp.size(), IDataSerializationImpl.taskName[2]);

                return listapp;
            }
        })
        .observeOn(Schedulers.io())
        .map(new Func1<List<AppInfo>, Boolean>() {
            @Override
            public Boolean call(List<AppInfo> listapp) {

                // 拷贝图片
                copyImportImage(listapp);

                return true;
            }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() { }
            @Override
            public void onError(Throwable e) {

                iMenuView.showProgressError("错误!请重试!");
                e.printStackTrace();
            }
            @Override
            public void onNext(Boolean s) {

                iMenuView.showProgressComplete();

                // 导入数据完成后,刷新首页的应用信息列表
                refreshHomeAppinfoList();
            }
        });
    }

    /**
     * 导出数据
     */
    public void exportData() {
        // RXJava
        Observable.just(true)
        .observeOn(Schedulers.io())
        .map(new Func1<Boolean, ArrayList<String>>() {
            @Override
            public ArrayList<String> call(Boolean s) {

                // 创建文件夹
                createFile();

                // 数据库查询classify生成list
                ArrayList<String> classifyList = iAppInfos.queryAllClassifys();

                return classifyList;
            }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .map(new Func1<ArrayList<String>, ArrayList<String>>() {
            @Override
            public ArrayList<String> call(ArrayList<String> classifyList) {

                iMenuView.showProgressInit(IDataSerializationImpl.taskName[1]);

                return classifyList;
            }
        })
        .observeOn(Schedulers.io())
        .map(new Func1<ArrayList<String>, Boolean>() {
            @Override
            public Boolean call(ArrayList<String> classifyList) {

                // 把classifyList中的数据生成Json字符串,并写入文件
                boolean sucessClassify = iDataSerialization.writeClassifyToFile(classifyList, IDataSerializationImpl.classify);

                return sucessClassify;
            }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .map(new Func1<Boolean, Boolean>() {
            @Override
            public Boolean call(Boolean sucessClassify) {

                if(!sucessClassify) {
                    iMenuView.showProgressError("向文件中写入分类信息失败!");
                    throw new RuntimeException("向文件中写入分类信息失败!");
                }

                return true;
            }
        })
        .observeOn(Schedulers.io())
        .map(new Func1<Boolean, ArrayList<AppInfo>>() {
            @Override
            public ArrayList<AppInfo> call(Boolean s) {

                // 查询所有app信息
                ArrayList<AppInfo> appList = iAppInfos.queryAllAppInfos();

                return appList;
            }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .map(new Func1<ArrayList<AppInfo>, ArrayList<AppInfo>>() {
            @Override
            public ArrayList<AppInfo> call(ArrayList<AppInfo> appList) {

                iMenuView.showProgressInit(IDataSerializationImpl.taskName[1]);

                return appList;
            }
        })
        .observeOn(Schedulers.io())
        .map(new Func1<ArrayList<AppInfo>, ArrayList<Object>>() {
            @Override
            public ArrayList<Object> call(ArrayList<AppInfo> appList) {

                // 把appList中的数据生成Json字符串,并写入文件
                boolean sucessAppinfo = iDataSerialization.writeAppinfoToFile(appList, IDataSerializationImpl.apps);

                ArrayList<Object> tempList = new ArrayList<>();
                tempList.add(sucessAppinfo);
                tempList.add(appList);

                return tempList;
            }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .map(new Func1<ArrayList<Object>, ArrayList<AppInfo>>() {
            @Override
            public ArrayList<AppInfo> call(ArrayList<Object> tempList) {

                if(!(Boolean)(tempList.get(0))){
                    iMenuView.showProgressError("向文件中写入应用信息失败!");
                    throw new RuntimeException("向文件中写入应用信息失败!");
                }

                ArrayList<AppInfo> appList = (ArrayList<AppInfo>)(tempList.get(1));
                iMenuView.setProgressMax(appList.size(), IDataSerializationImpl.taskName[2]);

                return appList;
            }
        })
        .observeOn(Schedulers.io())
        .map(new Func1<ArrayList<AppInfo>, Boolean>() {
            @Override
            public Boolean call(ArrayList<AppInfo> appList) {

                // 拷贝图片
                copyExportImage(appList);

                return true;
            }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() { }
            @Override
            public void onError(Throwable e) {

                iMenuView.showProgressError("错误!请重试!");
                e.printStackTrace();
            }
            @Override
            public void onNext(Boolean s) {

                iMenuView.showProgressComplete();
            }
        });
    }

    /**
     * 在UI线程更新进度
     * @param progress
     */
    private void setProgressOnUI(final int progress){
        if(iMenuView instanceof MenuFragment){
            ((MenuFragment)iMenuView).getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    iMenuView.setProgress(progress);
                }
            });
        }
    }

    /**
     * 拷贝图片(导入)
     * @param listapp
     */
    private void copyImportImage(List<AppInfo> listapp) {
        int progress = 0;
        for (AppInfo appinfo : listapp) {
            File iconPath = FileUtil.getIconFile(context, appinfo);
            iDataSerialization.copyFile(new File(IDataSerializationImpl.icons, iconPath.getName()), iconPath);
            setProgressOnUI(++progress);
        }
    }

    /**
     * 拷贝图片(导出)
     * @param listapp
     */
    private void copyExportImage(List<AppInfo> listapp) {
        int progress = 0;
        for (AppInfo appinfo : listapp) {
            File iconpath = FileUtil.getIconFile(context, appinfo);
            iDataSerialization.copyFile(iconpath, new File(IDataSerializationImpl.icons, iconpath.getName()));
            setProgressOnUI(++progress);
        }
    }

    /**
     * 向数据库插入应用信息
     * @param listapp
     */
    private void insertApp(List<AppInfo> listapp) {
        int progress = 0;
        for (AppInfo appinfo : listapp) {
            iAppInfos.addAppinfo(appinfo);
            setProgressOnUI(++progress);

        }
    }

    /**
     * 向数据库插入分类数据
     * @param list
     */
    private void insertClassify(List<String> list) {
        int progress = 1;
        for (String classify : list) {
            iAppInfos.addClassify(classify);
            setProgressOnUI(++progress);
        }
    }

    /**
     * 创建备份文件以及文件夹
     */
    private void createFile() {
        try {
            if(!IDataSerializationImpl.icons.exists()) IDataSerializationImpl.icons.mkdirs();
            if(!IDataSerializationImpl.classify.exists()) IDataSerializationImpl.classify.createNewFile();
            if(!IDataSerializationImpl.apps.exists()) IDataSerializationImpl.apps.createNewFile();
        } catch (IOException e) { }
    }

    RadioButtonDialogCallBack cleanCallback = new RadioButtonDialogCallBack() {
        @Override
        public void onCanel() {
            cleanDialog.dismiss();
        }

        @Override
        public void onClick(int position, String content) {
            cleanDialog.setPositon(position);

            if(position == 0){ // 清理分类信息
                cleanClassify();
            }else if(position == 1){ // 清理App记录信息
                cleanAppInfo();
            }
        }
    };

    /**
     * 清理分类
     */
    private void cleanClassify() {
        cleanClassifyDialog = new CleanClassifyDialog(context, cleanClassifyCallback);
        cleanClassifyDialog.show();

        // RXJava异步查询数据
        Observable.defer(new Func0<Observable<ArrayList<String>>>() {
            @Override
            public Observable<ArrayList<String>> call() {
                return Observable.just(iAppInfos.queryAllClassifys());
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<ArrayList<String>>() {
            @Override
            public void call(ArrayList<String> classifys) {
                cleanClassifyDialog.setData(classifys);
            }
        });
    }
    
    CleanClassifyDialogCallBack cleanClassifyCallback = new CleanClassifyDialogCallBack() {
        @Override
        public void onCleanButton(int position, String content) {
            if(iAppInfos.isHavaAppinfoForClassify(content)){
                // 有则提示无法删除分类信息
                LemonUtils.showQuickToast(context, content + ":该分类下有记录,无法删除");
            }else{
                // 无则删除分类信息, 删除 集合 和 数据库 中的分类数据
                cleanClassifyDialog.removeList(position);
                iAppInfos.deleteClassify(content);
            }
        }

        @Override
        public void onCancle() {
            cleanClassifyDialog.dismiss();
        }
    };

    private void cleanAppInfo() {
        cleanAppinfoDialog = new CleanAppinfoDialog(context, cleanAppinfoCallback);
        cleanAppinfoDialog.show();

        // RXJava异步查询数据
        Observable.defer(new Func0<Observable<ArrayList<AppInfo>>>() {
            @Override
            public Observable<ArrayList<AppInfo>> call() {
                return Observable.just(iAppInfos.queryAllAppInfos());
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<ArrayList<AppInfo>>() {
            @Override
            public void call(ArrayList<AppInfo> appInfos) {
                cleanAppinfoDialog.setData(appInfos);
            }
        });
    }

    /**
     * 刷新首页的应用信息集合(通过事件进行刷新)
     */
    public void refreshHomeAppinfoList(){
        RefreshDataEvent messageEvent = new RefreshDataEvent(RefreshDataEvent.RefreshDataType.Appinfos);
        EventBus.getDefault().post(messageEvent);
    }

    CleanAppinfoDialogCallBack cleanAppinfoCallback = new CleanAppinfoDialogCallBack() {
        @Override
        public void onCleanButton(int position, AppInfo appInfo) {
            // 删除应用信息, 删除 集合 和 数据库 中的应用数据
            cleanAppinfoDialog.removeList(position);
            iAppInfos.deleteAppInfo(appInfo);

            // 更新首页数据
            refreshHomeAppinfoList();

            // 删除图片
            File file = FileUtil.getIconFile(context, appInfo);
            if(file == null) return;
            if(file.exists()) file.delete(); // 删除文件
        }

        @Override
        public void onCancle() {
            cleanAppinfoDialog.dismiss();
        }
    };

    RadioButtonDialogCallBack iconSaveCallback = new RadioButtonDialogCallBack() {
        @Override
        public void onCanel() {
            iconSaveDialog.dismiss();
        }

        @Override
        public void onClick(int position, String content) {
            if(position == 0){ // 保存到手机存储

                iConfigInfo.saveIconSavePath(IConfigInfo.Mobile);
                iconSaveDialog.setPositon(0);
            }else if(position == 1){ // 保存到SD卡

                iConfigInfo.saveIconSavePath(IConfigInfo.SDCard);
                iconSaveDialog.setPositon(1);
            }
        }
    };

    StatisticsDialogCallBack StatisticsCallback = new StatisticsDialogCallBack() {
        @Override
        public void onCancel() {
            statisticsDialog.dismiss();
        }
    };

    public void initVersion(TextView tv_menu_appversion) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            tv_menu_appversion.setText(context.getResources().getString(R.string.app_name).concat(packageInfo.versionName));
        }catch (Exception e){
            e.printStackTrace();
            tv_menu_appversion.setText("版本获取异常...");
        }
    }

}
