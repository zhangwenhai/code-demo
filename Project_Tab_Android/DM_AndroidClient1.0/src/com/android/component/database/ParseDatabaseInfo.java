/*
 * 文件名: ParseDatabaseInfo.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 用户信息对象
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.component.database;

import java.io.InputStream;

import android.content.Context;

import org.apache.http.util.EncodingUtils;

/**
 * 读取并解析文件种的数据<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public class ParseDatabaseInfo
{
    /**
     * 文件的编码格式
     */
    public static final String ENCODING = "UTF-8";
    
    /**
     * 
     * 从assets 文件夹中获取文件并读取数据<BR>
     * 
     * @param fileName
     *            文件名称
     * @param context
     *            当前操 作的上下文卦象
     * @return xml文件描述
     */
    public static String getFromAssets(String fileName, Context context)
    {
        String result = "";
        InputStream inputStream = null;
        try
        {
            inputStream = context.getResources().getAssets().open(fileName);
            // 获取文件的字节数
            int lenght = inputStream.available();
            // 创建byte数组
            byte[] buffer = new byte[lenght];
            // 将文件中的数据读到byte数组中
            inputStream.read(buffer);
            result = EncodingUtils.getString(buffer, ENCODING);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (inputStream != null)
                {
                    inputStream.close();
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                
            }
        }
        return result;
    }
    
}
