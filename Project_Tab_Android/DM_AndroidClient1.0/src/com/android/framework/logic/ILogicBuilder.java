/*
 * 文件名: ILogicBuilder.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: LogicBuilder的接口
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.framework.logic;

import android.os.Handler;

/**
 * LogicBuilder的接口<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public interface ILogicBuilder
{
    
    /**
     * 根据logic接口类返回logic对象<BR>
     * 如果缓存没有则返回null。
     * 
     * @param interfaceClass
     *            logic接口类
     * @return logic对象
     */
    public ILogic getLogicByInterfaceClass(Class<?> interfaceClass);
    
    /**
     * 对缓存中的所有logic对象增加hander<BR>
     * 对缓存中的所有logic对象增加hander，在该UI的onCreated时被框架执行，
     * 如果该logic对象里执行了sendMessage方法，则所有的活动的UI对象接收到通知。
     * 
     * @param handler
     *            UI的handler对象
     */
    public void addHandlerToAllLogics(Handler handler);
    
    /**
     * 对缓存中的所有logic对象移除hander对象<BR>
     * 在该UI的onDestory时被框架执行，如果该logic对象 执行了sendMessage方法，则所有的UI接收到通知
     * 
     * @param handler
     *            UI的handler对象
     */
    public void removeHandlerToAllLogics(Handler handler);
}
