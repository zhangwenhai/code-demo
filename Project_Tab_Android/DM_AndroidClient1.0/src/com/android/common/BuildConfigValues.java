/*
 * 文件名: BuildConfigValues.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 编译环境配置属性值
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.common;

/**
 * 编译环境配置属性值<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public interface BuildConfigValues
{
    /**
     * 开发版本 debug 属性设置为TRUE
     */
    public static final boolean ISDEBUG_DEBUG_BUILD_VALUE = true;
    
    /**
     * 发布版本 debug 属性设置为FALSE
     */
    public static final boolean ISDEBUG_RELEASE_BUILD_VALUE = false;
}
