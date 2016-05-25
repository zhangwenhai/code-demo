/*
 * 文件名: DisplayInfo.java
 * 版    权： Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 保存屏幕数据的单例
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.utils;

import android.app.Activity;
import android.view.View;
import android.view.Window;

/**
 * 保存屏幕数据的单例<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-9]
 */
public class DisplayInfo
{
    private static DisplayInfo sInstance;
    
    /**
     * 应用整个可见区域的宽度
     */
    private int mWindowVisibleDisplayWidth;
    
    /**
     * 应用整个可见区域的高度（不包括状态栏和标题栏）
     */
    private int mWindowVisibleDisplayHeight;
    
    private DisplayInfo(Activity activity)
    {
        View view = activity.getWindow()
                .findViewById(Window.ID_ANDROID_CONTENT);
        mWindowVisibleDisplayHeight = view.getHeight();
        mWindowVisibleDisplayWidth = view.getWidth();
    }
    
    /**
     * 初始化<BR>
     * 因为window的尺寸再显示之后才能确定，onCreate 与 onResume 时都无法取得，
     * 所以选择在启动页的oPause时初始化，便于其他页面使用
     * @param activity activity
     */
    public static void init(Activity activity)
    {
        sInstance = new DisplayInfo(activity);
    }
    
    /**
     * 获取单例<BR>
     * @return 获取单例
     */
    public static DisplayInfo getInstance()
    {
        if (sInstance == null)
        {
            throw new IllegalStateException("Haven't be initialized");
        }
        return sInstance;
    }
    
    /**
     * 获取应用整个可见区域的宽度
     * @return 应用整个可见区域的宽度 
     */
    public int getWindowVisibleDisplayWidth()
    {
        return mWindowVisibleDisplayWidth;
    }
    
    /**
     * 获取应用整个可见区域的高度(不包括状态栏和标题栏)
     * @return 应用整个可见区域的高度（不包括状态栏和标题栏）
     */
    public int getWindowVisibleDisplayHeight()
    {
        return mWindowVisibleDisplayHeight;
    }
    
}
