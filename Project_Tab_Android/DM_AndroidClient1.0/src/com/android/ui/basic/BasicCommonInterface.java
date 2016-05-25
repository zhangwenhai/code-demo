/*
 * 文件名: BasicCommonInterface.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 基类的公共方法接口
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.ui.basic;

import android.content.DialogInterface;
import android.content.SharedPreferences;

import com.android.ui.basic.dialog.EditDialog;

/**
 * 基类的公共方法接口<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
interface BasicCommonInterface
{
    /**
     * 是否登录<BR>
     * @return 是否登录
     */
    boolean hasLogined();
    
    /**
     * 从底部滑进 <BR>
     */
    void slideInFromBottom();
    
    /**
     * 从底部滑出 <BR>
     */
    void slideOutToBottom();
    
    /**
     * 从右边滑入 <BR>
     */
    void slideInFromRight();
    
    /**
     * 从左边滑出 <BR>
     */
    void slideOutFromLeft();
    
    /**
     * 获取shared preferences<BR>
     * @return SharedPreferences
     */
    SharedPreferences getSharedPreferences();
    
    /**
     * 弹编辑框<BR>
     * @param title 标题字符串
     * @param right 右按钮字符串
     * @param left 左按钮字符串
     * @param okListener  右按钮事件
     * @return 弹编辑框对象
     */
    EditDialog showEditDialog(String title, String left, String right,
            DialogInterface.OnClickListener okListener);
    
}
