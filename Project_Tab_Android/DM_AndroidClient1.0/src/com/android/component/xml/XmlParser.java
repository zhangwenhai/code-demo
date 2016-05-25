/*
 * 文件名: BootReceiver.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: xml解析工具
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.component.xml;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 * xml解析工具<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public class XmlParser
{
    /**
     * 解析工具
     * <p>
     * simple xml 解析工具
     * </p>
     */
    private Serializer mSerializer = null;
    
    /**
     * 构造解析器
     */
    public XmlParser()
    {
        mSerializer = new Persister();
    }
    
    /**
     * 把字符串解析成对象<BR>
     * @param <T> 对象的class
     * @param type 对象类型
     * @param source 需要解析 的字符串
     * @return 解析后的对象
     * @throws Exception 需要解析的字符串不符合已定义的规范异常
     */
    public <T> T parseXmlString(Class<? extends T> type, String source)
            throws Exception
    {
        return mSerializer.read(type, source);
    }
    
    /**
     * 对象解析成xml串<BR>
     * @param source 数据源
     * @return 解析后的串
     */
    public String getXmlString(Object source)
    {
        return source == null ? "" : source.toString();
    }
    
}
