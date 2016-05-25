/*
 * 文件名: AppDatabseUtil.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 数据库操作工具类
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.component.database;

import java.util.Map;

import android.database.Cursor;
import android.database.DatabaseUtils;

/**
 * 数据库操作工具类<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public class AppDatabseUtil extends DatabaseUtils
{
    private static final int INVALID_INDEX = -1;
    
    /**
     * 关闭cursor
     * @param cursor 要关闭的cursor
     */
    public static void closeCursor(Cursor cursor)
    {
        if (null != cursor)
        {
            cursor.close();
            cursor = null;
        }
    }
    
    /**
     * 从cursor中获取String类型的值
     * @param cursor Cursor对象
     * @param columnName 要取值的字段
     * @param columnsIndexMap 字段名与索引的缓存
     * @return 字段值
     */
    public static String getCursorStringValue(Cursor cursor, String columnName,
            Map<String, Integer> columnsIndexMap)
    {
        int columnIndex = getColumnIndex(cursor, columnName, columnsIndexMap);
        
        if (INVALID_INDEX != columnIndex)
        {
            return cursor.getString(columnIndex);
        }
        else
        {
            return null;
        }
    }
    
    /**
     * 从cursor中获取long类型的值
     * @param cursor Cursor对象
     * @param columnName 要取值的字段
     * @param columnsIndexMap 字段名与索引的缓存
     * @return 字段值
     */
    public static Integer getCursorIntValue(Cursor cursor, String columnName,
            Map<String, Integer> columnsIndexMap)
    {
        int columnIndex = getColumnIndex(cursor, columnName, columnsIndexMap);
        
        if (INVALID_INDEX != columnIndex)
        {
            return cursor.getInt(columnIndex);
        }
        else
        {
            return null;
        }
    }
    
    /**
     * 从cursor中获取String类型的值
     * @param cursor Cursor对象
     * @param columnName 要取值的字段
     * @param columnsIndexMap 字段名与索引的缓存
     * @return 字段值
     */
    public static Long getCursorLongValue(Cursor cursor, String columnName,
            Map<String, Integer> columnsIndexMap)
    {
        int columnIndex = getColumnIndex(cursor, columnName, columnsIndexMap);
        
        if (INVALID_INDEX != columnIndex)
        {
            return cursor.getLong(columnIndex);
        }
        else
        {
            return null;
        }
    }
    
    private static int getColumnIndex(Cursor cursor, String columnName,
            Map<String, Integer> columnsIndexMap)
    {
        int columnIndex = INVALID_INDEX;
        if (null == columnsIndexMap)
        {
            columnIndex = cursor.getColumnIndex(columnName);
        }
        else
        {
            if (null == columnsIndexMap.get(columnName))
            {
                columnIndex = cursor.getColumnIndex(columnName);
                columnsIndexMap.put(columnName, columnIndex);
            }
            else
            {
                columnIndex = columnsIndexMap.get(columnName);
            }
        }
        return columnIndex;
    }
}
