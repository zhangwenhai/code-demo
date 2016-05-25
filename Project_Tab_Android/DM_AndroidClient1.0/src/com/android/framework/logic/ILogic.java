/*
 * 文件名: ILogic.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: logic 框架初始对外接口定义
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.framework.logic;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;

/**
 * logic 框架初始对外接口定义<BR>
 * 
 * @author zhangwenhai
 * @version [Paitao Client V20130911, 2013-9-12]
 */
public interface ILogic
{

    /**
     * 初始化方法<BR>
     * 在被系统管理的logic在注册到LogicBuilder中后立即被调用的初始化方法。
     * 
     * @param context
     *            系统的context对象
     */
    public void init(Context context);

    /**
     * 对logic增加handler<BR>
     * 在logic对象里加入UI的handler
     * 
     * @param handler
     *            UI传入的handler对象
     */
    public void addHandler(Handler handler);

    /**
     * 对logic移除handler<BR>
     * 在logic对象里移除UI的handler
     * 
     * @param handler
     *            UI传入的handler对象
     */
    public void removeHandler(Handler handler);

    /**
     * 对URI注册鉴定数据库表<BR>
     * 根据URI监听数据库表的变化，放入监听对象缓存
     * 
     * @param uri
     *            数据库的Content Provider的 Uri
     */
    public void registerObserver(final Uri uri);

    /**
     * 对URI解除注册鉴定数据库表<BR>
     * 根据URI移除对数据库表变化的监听，移出监听对象缓存
     * 
     * @param uri
     *            数据库的Content Provider的 Uri
     */
    public void unRegisterObserver(Uri uri);

    /**
     * 当对数据库表定义的Uri进行监听后，被回调方法<BR>
     * 子类中需重载该方法，可以在该方法的代码中对表变化进行监听实现
     * 
     * @param selfChange
     *            如果是true，被监听是由于代码执行了commit造成的
     * @param uri
     *            被监听的Uri
     */
    public void onChangeByUri(boolean selfChange, Uri uri);

}
