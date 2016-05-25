/*
 * 文件名: BuildConfig.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 编译环境配置
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.common;

/**
 * 编译环境配置<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public interface BuildConfig
{
    /**
     * 是否开启debug
     */
    public static final boolean IS_DEBUG = BuildConfigValues.ISDEBUG_DEBUG_BUILD_VALUE;
}
