/*
 * 文件名: FusionConfig.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 全局配置类，单例模式
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.common;

/**
 * 全局配置类，单例模式<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public class FusionConfig
{
    /**
     * 单例对象
     */
    private static FusionConfig mInstance = new FusionConfig();
    
    /**
     * 是否打日志
     */
    private boolean isDebug = BuildConfig.IS_DEBUG;
    
    /**
     * 获取单例的 FusionConfig对象
     * 
     * @return FusionConfig对象
     */
    public static FusionConfig getInstance()
    {
        return mInstance;
    }
    
    /**
     * 是否打日志
     * 
     * @return the isDebug
     */
    public boolean isDebug()
    {
        return isDebug;
    }
    
    /**
     * 是否打日志<BR>
     * 
     * @param isdebug
     *            是否打日志
     */
    public void setDebug(boolean isdebug)
    {
        this.isDebug = isdebug;
    }
    
}
