/*
 * 文件名: FusionCode.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 客户端请求码定义的聚合
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.common;

/**
 * 客户端请求码定义的聚合<BR>
 * 
 * @author zhangwenhai
 * @version [Paitao Client V20130911, 2013-9-13]
 */
public class FusionCode
{
    /**
     * 一些公共信息
     */
    public interface Common
    {
        /**
         * 程序保存shared preferences的名字
         */
        String SHARED_PREFERENCE_NAME = "Android";
        
        /**
         * shared preference 键 存储当前登录userId
         */
        String KEY_USER_ID = "userId";
        
        /**
         * shared preference 键 标识是否登录状态
         */
        String KEY_SESSION_ID = "sessionId";
    }
}
