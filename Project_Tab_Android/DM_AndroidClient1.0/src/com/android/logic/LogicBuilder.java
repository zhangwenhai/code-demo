/*
 * 文件名: LogicBuilder.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: logic对象创建工厂 创建所有的logic对象
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.logic;

import android.content.Context;

import com.android.component.service.app.ServiceSender;
import com.android.framework.logic.BaseLogicBuilder;

/**
 * logic对象创建工厂 创建所有的logic对象<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public class LogicBuilder extends BaseLogicBuilder
{
    
    /**
     * 单例
     */
    private static BaseLogicBuilder instance;
    
    /**
     * 构造方法 <BR>
     * 继承BaseLogicBuilder的构造方法，由父类BaseLogicBuilder对所有logic进行初始化。
     * 
     * @param context
     *            系统的context对象
     */
    private LogicBuilder(Context context)
    {
        super(context);
    }
    
    /**
     * 获取BaseLogicBuilder单例<BR>
     * 单例模式
     * @param context
     *            系统的context对象
     * @return BaseLogicBuilder 单例对象
     */
    public static synchronized BaseLogicBuilder getInstance(Context context)
    {
        if (null == instance)
        {
            instance = new LogicBuilder(context);
        }
        return instance;
    }
    
    /**
       * LogicBuidler的初始化方法<BR>
     * 初始对象创建工厂,系统初始化的时候执行
     * 
     * @param context
     *            系统的context对象
     * @see com.paitao.basic.android.framework.logic.BaseLogicBuilder#init(Context)
     */
    @Override
    protected void init(Context context)
    {
        registerAllLogics(context);
    }
    
    /**
     * 
     * 所有logic对象初始化及注册的方法<BR>
     * 
     * @param context
     *            系统的context对象
     */
    private void registerAllLogics(Context context)
    {
        //启动service
        ServiceSender.getIServiceSender(context);
        //        registerLogic(ILoginLogic.class, new LoginLogic(context));
    }
}
