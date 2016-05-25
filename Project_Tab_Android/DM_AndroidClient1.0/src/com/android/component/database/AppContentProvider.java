/*
 * 文件名: AppContentProvider.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 根据URI实现具体数据库操作
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.component.database;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import android.content.*;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.android.component.log.Logger;
import com.android.common.FusionCode.Common;

/**
 * 根据URI实现具体数据库操作<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public class AppContentProvider extends SQLiteContentProvider implements
        OnSharedPreferenceChangeListener
{
    /**
     * 打印log信息时传入的标志
     */
    private static final String TAG = "AppContentProvider";

    /**
     * URI键值队
     */
    private static final UriMatcher URIMATCHER = new UriMatcher(
            UriMatcher.NO_MATCH);

    /**
     * 数据库通知对象map
     */
    private Vector<Uri> mNotifyChangeUri;

    private SharedPreferences mSharedPreference;

    /**
     * 当前登录用户ID
     */
    private String mUserId;

    /**
     * 缓存数据库
     */
    private ConcurrentHashMap<String, DatabaseHelper> mDBHelperPool = new ConcurrentHashMap<String, DatabaseHelper>();

    /**
     * 加载URI
     */
    static
    {
    }

    /**
     * 创建方法
     * @return  是否创建成功
     */
    @Override
    public boolean onCreate()
    {
        mSharedPreference = getContext().getSharedPreferences(Common.SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        mSharedPreference.registerOnSharedPreferenceChangeListener(this);
        super.onCreate();
        try
        {
            return initialize();

        }
        catch (RuntimeException e)
        {
            Logger.e(TAG, "ErrorOcurred while onCreate provider", e);
            return false;
        }
    }

    /**
     * 初始化<BR>
     * 初始化URI集合，初始化各处理具体业务的handler
     * @return 是否成功
     */
    private boolean initialize()
    {
        SQLiteDatabase db = null;
        if (null != getDatabaseHelper())
        {
            db = getDatabaseHelper().getWritableDatabase();
            initNotifyChangeUri();
        }
        return null != db;
    }

    /**
     * 初始化NotifyChangeUri
     */
    private void initNotifyChangeUri()
    {
        mNotifyChangeUri = new Vector<Uri>();
    }

    /**
     * 获取databaseHelper对象
     * @param context
     *      Context对象
     * @return
     *      SQLiteOpenHelper 对象
     * @see SQLiteContentProvider#getDatabaseHelper(Context)
     */
    @Override
    protected DatabaseHelper getDatabaseHelper(Context context)
    {
        long userId = mSharedPreference.getLong(Common.KEY_USER_ID,
                -1);
        mUserId = userId == -1 ? "" : String.valueOf(userId);
        if (TextUtils.isEmpty(mUserId))
        {
            return null;
        }
        if (null != mDBHelperPool.get(mUserId))
        {
            setDatabaseHelper(mDBHelperPool.get(mUserId));
        }
        else
        {
            DatabaseHelper helper = DatabaseHelper.getInstance(getContext(),
                    mUserId);
            mDBHelperPool.put(mUserId, helper);
            setDatabaseHelper(helper);
        }
        return mDBHelperPool.get(mUserId);
    }

    /**
     * 在事务中保存数据
     * @param uri
     *      数据库表的URI
     * @param values
     *      数据封装对象
     * @param db
     *      database实例
     * @return
     *      数据库表的URI
     * @see SQLiteContentProvider#insertInTransaction(Uri, ContentValues, SQLiteDatabase)
     */
    @Override
    protected Uri insertInTransaction(Uri uri, ContentValues values,
            SQLiteDatabase db)
    {
        String tableName = getTableNameByUri(uri);
        long rowId = db.insert(tableName, null, values);
        if (rowId > 0)
        {
            // 通知数据变更
            notifyChange(uri);
        }
        return ContentUris.withAppendedId(uri, rowId);
    }

    /**
     * 在事务中更新数据
     * @param uri
     *      数据库表的URI
     * @param values
     *      数据封装对象
     * @param selection
     *      查询条件语句
     * @param selectionArgs
     *      查询条件参数
     * @param db
     *      SqliteDabase
     * @return
     *      更新数据数量
     * @see SQLiteContentProvider#updateInTransaction(Uri, ContentValues, String, String[], SQLiteDatabase)
     */
    @Override
    protected int updateInTransaction(Uri uri, ContentValues values,
            String selection, String[] selectionArgs, SQLiteDatabase db)
    {
        String tableName = getTableNameByUri(uri);
        int count = db.update(tableName, values, selection, selectionArgs);
        if (count > 0)
        {
            // 通知数据变更
            notifyChange(uri);
        }
        return count;
    }

    /**
     * 在事务中删除数据
     * @param uri
     *      数据库表的URI
     * @param selection
     *      查询条件语句
     * @param selectionArgs
     *      查询条件参数
     * @param db
     *      数据库对象
     * @return
     *      删除数据数量
     * @see SQLiteContentProvider#deleteInTransaction(Uri, String, String[], SQLiteDatabase)
     */
    @Override
    protected int deleteInTransaction(Uri uri, String selection,
            String[] selectionArgs, SQLiteDatabase db)
    {
        String tableName = getTableNameByUri(uri);
        int count = db.delete(tableName, selection, selectionArgs);
        if (count > 0)
        {
            // 通知数据变更
            notifyChange(uri);
        }
        return count;
    }

    /**
     * 通知数据变更<BR>
     * @param uri
     *      数据库表的URI
     */
    private void notifyChange(Uri uri)
    {
        mNotifyChangeUri.add(uri);
    }

    /**
     * 根据Uri匹配出数据库表名<BR>
     * @param uri
     *      数据库表的URI
     * @return
     *      数据库表名
     */
    private String getTableNameByUri(Uri uri)
    {
        if (uri == null)
        {
            return null;
        }
        int match = URIMATCHER.match(uri);
        switch (match)
        {
            default:
                return null;
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder)
    {
        Cursor cursor = null;
        SQLiteDatabase db = getDatabaseHelper().getReadableDatabase();
        String tableName = this.getTableNameByUri(uri);
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        int match = URIMATCHER.match(uri);
        switch (match)
        {
            default:
                qb.setTables(tableName);
                cursor = qb.query(db,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder,
                        null);
                break;

        }
        if (cursor != null)
        {
            // 监测数据变更
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    /**
     * 根据Uri获取类型
     * @param uri 数据库表的Uri
     * @return 类型字符串
     * @see android.content.ContentProvider#getType(Uri)
     */
    @Override
    public String getType(Uri uri)
    {
        return null;
    }

    /**
     * 通知数据表发生变化<BR>
     * @see SQLiteContentProvider#notifyChange()
     */
    @Override
    protected void notifyChange()
    {
        ContentResolver contentResolver = getContext().getContentResolver();
        synchronized (mNotifyChangeUri)
        {
            for (Uri uri : mNotifyChangeUri)
            {
                Logger.d(TAG, "notify uri changed =====> " + uri);
                contentResolver.notifyChange(uri, null);
            }
            mNotifyChangeUri.clear();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            String key)
    {
        if (TextUtils.equals(key, Common.KEY_USER_ID))
        {
            long userId = mSharedPreference.getLong(Common.KEY_USER_ID,
                    -1);
            mUserId = userId == -1 ? "" : String.valueOf(userId);
            Logger.d(TAG, "onSharedPreferenceChanged  userSysId : " + mUserId);
            getHelperInitial();
        }
        else if (TextUtils.equals(key, Common.KEY_SESSION_ID))
        {
            String sessionid = mSharedPreference.getString(Common.KEY_SESSION_ID,
                    "");
            Logger.d(TAG, "onSharedPreferenceChanged isLogin : " + sessionid);
            if (!"".equals(sessionid))
            {
                getHelperInitial();
            }
        }
    }
    
    /**
     * 获得databseHelper对象，并初始化相关初始值<BR>
     */
    private synchronized void getHelperInitial()
    {
        if (null != mDBHelperPool.get(mUserId))
        {
            setDatabaseHelper(mDBHelperPool.get(mUserId));
        }
        else
        {
            DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getContext(),
                    mUserId);
            mDBHelperPool.put(mUserId, databaseHelper);
            setDatabaseHelper(databaseHelper);
        }
        initialize();
    }
    
}
