/*
 * 文件名: UpgradeDbUtil.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 数据库创建和升级工具类
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.component.database;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.android.component.log.Logger;
import com.android.component.xml.XmlParser;
import com.android.component.database.DatabaseHelper.BaseColumns;
import com.android.component.database.DatabaseInfo.Database;
import com.android.component.database.DatabaseInfo.Database.Field;
import com.android.component.database.DatabaseInfo.Database.Table;

/**
 * 数据库创建和升级工具类<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public class UpgradeDbUtil
{
    /**
     * 用于输出日志的TAG
     */
    private static final String TAG = "UpgradeDbUtil";
    
    /**
     * 数据库配置文件后缀名
     */
    private static final String SUFFIX_DB_FILE_NAME = "_database.xml";
    
    /**
     * 标识备用字段类型，整型
     */
    private static final String PREFIX_RESERVE_INT = "RESERVE_INT";
    
    @SuppressWarnings("unused")
    private static final String PREFIX_RESERVE_LONG = "RESERVE_LONG";
    
    @SuppressWarnings("unused")
    private static final String PREFIX_RESERVE_STR = "RESERVE_STR";
    
    /**
     * 数据库存结构保存xml的路径
     */
    private static final String XML_DIR = "db_sql/";
    
    /**
     * 当前操作的上下文对象
     */
    private Context mContext;
    
    /**
     * 创建备用字段的字符串
     */
    private String mBaseColumnsCreateStr;
    
    /**
     * 构造方法，初始化基础表信息
     * @param context Context实例
     */
    public UpgradeDbUtil(Context context)
    {
        mContext = context;
        try
        {
            initBasicColumns();
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 创建表结构
     * @param fileName 文件名称
     * @param db 数据库存操作实例
     * @return true表示创建成功 false 表示创建失败
     */
    public boolean createTableFromXml(String fileName, SQLiteDatabase db)
    {
        boolean isSuccess = false;
        try
        {
            String snsDatabaseXml;
            snsDatabaseXml = ParseDatabaseInfo.getFromAssets(XML_DIR + fileName
                    + SUFFIX_DB_FILE_NAME, mContext);
            Database info = new XmlParser().parseXmlString(Database.class,
                    snsDatabaseXml);
            addTable(info, db, snsDatabaseXml);
            isSuccess = true;
        }
        catch (Exception ex)
        {
            //            Crashlytics.logException(ex);
            ex.printStackTrace();
            Logger.i(TAG, "==创建数据库失败==" + ex.toString());
        }
        return isSuccess;
    }
    
    /**
     * 升级数据库表结构
     * @param fileName 数据库xml文件名称
     * @param db 数据库操作实例
     * @param version 旧的数据库版本号
     * @return true 表示升级成功,false 表示升级失败
     */
    public boolean upgradeTableFromXml(String fileName, SQLiteDatabase db,
            String version)
    {
        boolean isSuccess = false;
        //        GlobalDBVerInfo oldGlobalDbVerInfo = UpgradeDbUtil.getInstance(mContext)
        //                .queryGlobalInfo(db);
        //        //在这里做数据库的版本号信息比较
        //        if (oldGlobalDbVerInfo == null)
        //        {
        //            try
        //            {
        //                isSuccess = createTableFromXml(fileName, db);
        //            }
        //            catch (Exception ex)
        //            {
        //                ex.printStackTrace();
        //                isSuccess = false;
        //                Logger.i(TAG, "==创建数据库失败==" + ex.toString());
        //            }
        //        }
        //        else
        //        {
        //            // ***BEGIN***  [修改数据库存的版本号比对] zhouxin 2012-9-6 modify
        //            if (compareVersion(version,oldGlobalDbVerInfo.getGlobalDBVer()))
        //            {
        //                isSuccess = true;
        //                Logger.i(TAG, "数据库版本一致或者当前的数据库存版本号比之前的低不需要升级");
        //            }
        //            // ***END***  [修改数据库存的版本号比对] zhouxin 2012-9-6 modify
        //            else
        //            {
        //                String snsDataXml;
        //                try
        //                {
        //                    Logger.i(TAG,
        //                            "数据库版本号比对结束时间(版本号不一致):"
        //                                    + System.currentTimeMillis());
        //                    Logger.i(TAG,
        //                            "数据库升级后解析文件开始时间:" + System.currentTimeMillis());
        //                    snsDataXml = ParseDatabaseInfo.getFromAssets(XML_DIR
        //                            + fileName, mContext);
        //                    DatabaseInfo.Database info = null;
        //                    info = new XmlParser().parseXmlString(DatabaseInfo.Database.class,
        //                            snsDataXml);
        //                    Logger.i(TAG,
        //                            "数据库升级后解析结束时间开始时间:" + System.currentTimeMillis());
        //                    Logger.i(TAG, "数据库版本号比对开始时间:" + System.currentTimeMillis());
        //                    Logger.i(TAG, "数据库开始比对：" + System.currentTimeMillis());
        //                    String oldXml = oldGlobalDbVerInfo.getDesc();
        //                    DatabaseInfo.Database oldinfo = new XmlParser().parseXmlString(DatabaseInfo.Database.class,
        //                            oldXml);
        //                    compareDbInfo(oldinfo, info, db);
        //                    //这里还需要做数据的更新
        //                    oldGlobalDbVerInfo.setGlobalDBVer(info.getVersion());
        //                    oldGlobalDbVerInfo.setDesc(snsDataXml);
        //                    updateGlobalInfo(oldGlobalDbVerInfo, db);
        //                    isSuccess = true;
        //                }
        //                catch (Exception ex)
        //                {
        //                    ex.printStackTrace();
        //                    isSuccess = false;
        //                    Logger.i(TAG, "==数据版本升级失败：==" + ex.toString());
        //                }
        //            }
        //        }
        return isSuccess;
    }
    
    /**
     * 用于比对数据的版本号
     * @param newVersion 新的数据库存版本号
     * @param oldVersion 旧的数据库版本号
     * @return true 表示不需要更新 false 表示版本号不一致需要更新数据库的版本信息
     */
    private boolean compareVersion(String newVersion, String oldVersion)
    {
        boolean isEqual = true;
        if (!newVersion.equals(oldVersion))
        {
            String[] oldArray = oldVersion.split("\\.");
            String[] newArray = newVersion.split("\\.");
            for (int i = 0; i < oldArray.length; i++)
            {
                if (Integer.valueOf(newArray[i]) > Integer.valueOf(oldArray[i]))
                {
                    isEqual = false;
                }
            }
        }
        return isEqual;
    }
    
    /**
     * 用于添加表信息<BR>
     * @param baseInfo Database实体类
     * @param db 数据库操作实例
     * @param desc 描述信息
     */
    private void addTable(Database baseInfo, SQLiteDatabase db, String desc)
    {
        Logger.e(TAG, "首次创建表结构开始时间：" + System.currentTimeMillis());
        for (Table table : baseInfo.getListTable())
        {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("CREATE TABLE ");
            stringBuffer.append(table.getName());
            stringBuffer.append(" ( ");
            
            List<Field> listFiled = table.getListFiled();
            int size = listFiled.size();
            for (int i = 0; i < size; i++)
            {
                Field field = listFiled.get(i);
                stringBuffer.append(field.getName());
                stringBuffer.append(" ");
                stringBuffer.append(field.getType());
                stringBuffer.append(" ");
                stringBuffer.append(field.getObligatory());
                if (i != size - 1)
                {
                    stringBuffer.append(", ");
                }
                else
                {
                    if (!TextUtils.isEmpty(mBaseColumnsCreateStr))
                    {
                        stringBuffer.append("," + mBaseColumnsCreateStr);
                    }
                    stringBuffer.append(");");
                }
            }
            Logger.i(TAG, "create table sql is : " + stringBuffer.toString());
            db.execSQL(stringBuffer.toString());
        }
        //        insertGlobalInfo(baseInfo, desc, db);
        Logger.e(TAG, "首次创建表结构结束时间：" + System.currentTimeMillis());
    }
    
    private void initBasicColumns() throws IllegalArgumentException,
            IllegalAccessException
    {
        StringBuffer sb = new StringBuffer();
        java.lang.reflect.Field[] fields = BaseColumns.class.getDeclaredFields();
        for (int i = 0; i < fields.length; i++)
        {
            String fieldName = fields[i].getName();
            if (fieldName.indexOf(PREFIX_RESERVE_INT) != -1)
            {
                sb.append(ColumnsType.INTEGER.getAppendStr((String) fields[i].get(BaseColumns.class)));
                if (i != fields.length - 1)
                {
                    sb.append(",");
                }
            }
            else
            {
                sb.append(ColumnsType.TEXT.getAppendStr((String) fields[i].get(BaseColumns.class)));
                if (i != fields.length - 1)
                {
                    sb.append(",");
                }
            }
        }
        mBaseColumnsCreateStr = sb.toString();
    }
    
    /**
     * 比对当前数据库存的版本号是否一致
     * @param oldVerSion 旧的数据版本号
     * @param newVersion 新的数据库版本号
     * @return true 表示一致 false 表示不一致
     */
    public boolean compareDBVersion(String oldVerSion, String newVersion)
    {
        return newVersion.equals(oldVerSion);
    }
    
    /**
     * 比对数据库的版本信息 
     * @param oldDatabase 旧的数据库存版本信息
     * @param newDatabase 新的数据库存版本信息
     * @param db 数据库实例
     */
    public void compareDbInfo(Database oldDatabase, Database newDatabase,
            SQLiteDatabase db)
    {
        Logger.e(TAG, "比对表结构开始时间: " + System.currentTimeMillis());
        List<Table> listAddTable = new ArrayList<Table>();
        List<Table> listUpdateTable = new ArrayList<Table>();
        for (Table table : newDatabase.getListTable())
        {
            boolean isExitTableName = false;
            for (Table oldTab : oldDatabase.getListTable())
            {
                if (table.getName().equals(oldTab.getName()))
                {
                    Table updateTable = new Table();
                    updateTable.setName(table.getName());
                    isExitTableName = true;
                    List<Field> updateFieldList = new ArrayList<Field>();
                    for (Field field : table.getListFiled())
                    {
                        boolean isExitField = false;
                        for (Field oldField : oldTab.getListFiled())
                        {
                            if (oldField.getName().equals(field.getName()))
                            {
                                isExitField = true;
                            }
                        }
                        if (!isExitField)
                        {
                            updateFieldList.add(field);
                        }
                    }
                    if (updateFieldList != null && updateFieldList.size() > 0)
                    {
                        updateTable.setListFiled(updateFieldList);
                        listUpdateTable.add(updateTable);
                    }
                }
            }
            if (!isExitTableName)
            {
                listAddTable.add(table);
            }
        }
        Logger.e(TAG, "比对表结构结束时间:" + System.currentTimeMillis());
        if (listAddTable != null && listAddTable.size() > 0)
        {
            // 在这里做新增表的操作
            addTableStructure(listAddTable, db);
        }
        if (listUpdateTable != null && listUpdateTable.size() > 0)
        {
            // 在这里做新增列的操作
            addColumn(listUpdateTable, db);
        }
    }
    
    /**
     * 添加新的表结构
     * @param listAddTable 需要添加的新的表结构
     */
    private void addTableStructure(List<Table> listAddTable, SQLiteDatabase db)
    {
        Logger.e(TAG, "==比对完成后对新增表结构开始时间==" + System.currentTimeMillis());
        for (Table table : listAddTable)
        {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("CREATE TABLE ");
            stringBuffer.append(table.getName());
            stringBuffer.append(" ( ");
            List<Field> listFiled = table.getListFiled();
            int size = listFiled.size();
            for (int i = 0; i < size; i++)
            {
                Field field = listFiled.get(i);
                stringBuffer.append(field.getName());
                stringBuffer.append(" ");
                stringBuffer.append(field.getType());
                stringBuffer.append(" ");
                stringBuffer.append(field.getObligatory());
                if (i != size - 1)
                {
                    stringBuffer.append(", ");
                }
                else
                {
                    stringBuffer.append(");");
                }
            }
            Logger.i(TAG,
                    "addTableStructure sql is :" + stringBuffer.toString());
            db.execSQL(stringBuffer.toString());
        }
        Logger.e(TAG, "比对完成后对新增表结构结束时间:" + System.currentTimeMillis());
    }
    
    /**
     * 做新增列操作
     * @param listUpdateTable 需要更新的列表
     * @param db 数据库操作实例
     */
    private void addColumn(List<Table> listUpdateTable, SQLiteDatabase db)
    {
        Logger.e(TAG, "比对完后对新增字段开始时间：" + System.currentTimeMillis());
        for (Table table : listUpdateTable)
        {
            for (Field field : table.getListFiled())
            {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("ALTER TABLE ");
                stringBuffer.append(table.getName());
                stringBuffer.append(" ADD COLUMN ");
                stringBuffer.append(field.getName());
                stringBuffer.append(" ");
                stringBuffer.append(field.getType());
                stringBuffer.append(" ");
                stringBuffer.append(field.getObligatory());
                Logger.i(TAG, " addColumn sql is :" + stringBuffer.toString());
                db.execSQL(stringBuffer.toString());
            }
        }
        Logger.e(TAG, "比对完后对新增字段结束时间：" + System.currentTimeMillis());
    }
    
    /**
     * 数据库表字段类型<BR>
     * 用于生成数据库具体字段的创建语句
     * @author zhangwenhai
     * @version [Client V20150307, 2015-3-8]
     */
    private enum ColumnsType
    {
        TEXT
        {
            @Override
            String getAppendStr(String columnName)
            {
                StringBuffer sb = new StringBuffer();
                sb.append(" ").append(columnName).append(" TEXT");
                return sb.toString();
            }
        },
        INTEGER
        {
            @Override
            String getAppendStr(String columnName)
            {
                StringBuffer sb = new StringBuffer();
                sb.append(" ").append(columnName).append(" INTEGER");
                return sb.toString();
            }
        };
        abstract String getAppendStr(String columnName);
    }
}
