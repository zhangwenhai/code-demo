/*
 * 文件名: DatabaseHelper.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 创建数据库工具类
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.component.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.component.log.Logger;

/**
 * 创建数据库工具类<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public class DatabaseHelper extends SQLiteOpenHelper
{
    /**
     * TAG 用于打印日志信息
     */
    private static final String TAG = DatabaseHelper.class.getSimpleName();
    
    /**
     * 数据库名
     */
    private static final String DB_NAME = "cici";
    
    /**
     * 数据库版本号
     */
    private static final int DB_VERSION = 1;
    
    /**
     * 数据库后缀名
     */
    private static final String DB_NAME_SUFFIX = ".db";
    
    /**
     * DatabaseHelper实例
     */
    private static DatabaseHelper sInstance;
    
    /**
     * 数据库升级工具类实例
     */
    private UpgradeDbUtil mUpgradeDbUtil;

    /**
     *
     * 带有UserId的DatabaseHelper构造方法
     * @param context Context对象
     * @param userId 用户ID
     */
    private DatabaseHelper(Context context, String userId)
    {
        super(context, DB_NAME + "_" + userId + DB_NAME_SUFFIX, null,
                DB_VERSION);
        mUpgradeDbUtil = new UpgradeDbUtil(context);
        Logger.d(TAG, "db  create");
    }
    
    /**
     * 
     * 带有userId的databaseHelper对象的创建<BR>
     * 分库用到的databaseHelper
     * @param context Context
     * @param userId 用户ID
     * @return DatabaseHelper对象
     */
    public static DatabaseHelper getInstance(Context context, String userId)
    {
        sInstance = new DatabaseHelper(context, userId);
        return sInstance;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        mUpgradeDbUtil.createTableFromXml(DB_NAME, db);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // TODO Auto-generated method stub
    }
    
    /**
     * 定义基础的表字段，所有的表字段都包含这些字段<BR>
     * @author zhangwenhai
     * @version [Client V20150307, 2015-3-8]
     */
    public interface BaseColumns
    {
        /**
         * 备用字段: 整型
         */
        String RESERVE_INT1 = "reserve_int1";
        
        /**
         * 备用字段: 整型
         */
        String RESERVE_INT2 = "reserve_int2";
        
        /**
         * 备用字段: 长整型
         */
        String RESERVE_LONG1 = "reserve_long1";
        
        /**
         * 备用字段: 长整型
         */
        String RESERVE_LONG2 = "reserve_long2";
        
        /**
         * 备用字段: 文本
         */
        String RESERVE_STR1 = "reserve_str1";
        
        /**
         * 备用字段: 文本
         */
        String RESERVE_STR2 = "reserve_str2";
        
        /**
         * 备用字段: 文本
         */
        String RESERVE_STR3 = "reserve_str3";
        
        /**
         * 备用字段: 文本
         */
        String RESERVE_STR4 = "reserve_str4";
        
    }
    
    /**
     * 定义数据库中的表名<BR>
     * @author zhangwenhai
     * @version [Client V20150307, 2015-3-8]
     */
    public interface Table
    {
    }
}
