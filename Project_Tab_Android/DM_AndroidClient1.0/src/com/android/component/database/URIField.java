/*
 * 文件名: UpgradeDbUtil.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 数据库URI值定义
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.component.database;

import android.net.Uri;

/**
 * 数据库URI值定义<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public class URIField
{
    /**
     * ContentProvider权限字符 需要与androidMainfest.xml中对应的provider中的authorities属性对应
     */
    public static final String AUTHORITY = "com.android.db";
    
    /**
     * 权限URI
     */
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);
    
}
