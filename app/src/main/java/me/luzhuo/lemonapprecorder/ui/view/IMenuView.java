package me.luzhuo.lemonapprecorder.ui.view;

import me.luzhuo.lemonapprecorder.ui.dialog.WarnDialog;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/11/12 8:45
 * <p>
 * Description:
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public interface IMenuView {

    /**
     * 显示进度Dialog
     * @param isShow true显示 / false不显示
     */
    void showProgressDialog(boolean isShow);

    /**
     * 显示进度Dialog异常信息
     */
    void showProgressError(String error);

    /**
     * 显示 初始化 进度条Dialog
     * @param init
     */
    void showProgressInit(String init);

    /**
     * 设置 进度条Dialog 最大值
     * @param hint
     */
    void setProgressMax(int max, String hint);

    /**
     * 显示进度条Dialog完成
     */
    void showProgressComplete();

    /**
     * 设置进度
     * @param progress
     */
    void setProgress(int progress);

    // ===

    /**
     * 显示警告框
     * @param warnType
     */
    void showWarnDialog(WarnDialog.WarnType warnType);
}
