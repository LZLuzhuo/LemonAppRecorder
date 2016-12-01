package me.luzhuo.lemonapprecorder.callback.dialog;

import me.luzhuo.lemonapprecorder.ui.dialog.WarnDialog;

/**
 * =================================================
 * <p>
 * Author: Luzhuo
 * <p>
 * Version: 1.0
 * <p>
 * Creation Date: 2016/11/14 19:12
 * <p>
 * Description:
 * <p>
 * Revision History:
 * <p>
 * Copyright: Copyright 2016 Luzhuo. All rights reserved.
 * <p>
 * =================================================
 **/
public interface WarnDialogCallBack {

    /**
     * 取消
     */
    void onCancel();

    /**
     * 确认
     * @param warntype 警告类型
     */
    void onConfirm(WarnDialog.WarnType warntype);
}
