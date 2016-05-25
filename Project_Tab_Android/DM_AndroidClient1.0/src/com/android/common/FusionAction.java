/*
 * 文件名: FusionAction.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 所有UI跳转界面的action定义
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.common;

/**
 * 所有UI跳转界面的action定义<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public interface FusionAction
{
    /**
     * IM的service服务双进程管理<BR>
     * @author zhangwenhai
     * @version [Client V20150307, 2015-3-8]
     */
    public interface BaseServiceAction
    {
        /**
         * 开启service服务的消息
         */
        String ACTION = "com.android.service";
    }
    
    /**
     * 应用主界面<BR>
     * @author zhangwenhai
     * @version [Client V20150307, 2015-3-8]
     */
    public interface MainTabAction
    {
        /**
         * 跳转mainTab页面的消息
         */
        String ACTION = "com.android.MAINTAB";
    }
    
    /**
     * 跳转登陆界面的消息 <BR>
     * @author zhangwenhai
     * @version [Client V20150307, 2015-3-8]
     */
    public interface LoginAction
    {
        /**
         * 跳转登陆界面的消息
         */
        String ACTION = "";
    }
    
}
