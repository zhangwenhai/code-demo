/*
 * 文件名: FileUtil.java
 * 版    权： Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 关于文件的操作类
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.utils;

import java.io.*;
import java.nio.channels.FileChannel;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.android.component.log.Logger;

/**
 * 关于文件的操作类<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-9]
 */
public class FileUtil
{
    /**
     * TAG
     */
    private static final String TAG = "FileUtil";
    
    /**
     * 缓冲区大小
     */
    private static final int BUFFER_SIZE = 100 * 1024;
    
    /**
     * uri转图片路径<BR>
     * 
     * @param activity
     *            Activity
     * @param uri
     *            uri
     * @return 图片路径
     */
    public static String getImagePath(Activity activity, Uri uri)
    {
        String[] proj = { MediaStore.Images.Media.DATA };
        String imgpath = null;
        Cursor imagecursor = activity.managedQuery(uri, proj, null, null, null);
        try
        {
            
            if (null != imagecursor && imagecursor.moveToFirst())
            {
                int imagecolumnindex = imagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                imgpath = imagecursor.getString(imagecolumnindex);
            }
        }
        catch (Exception e)
        {
            Logger.d(TAG, "an error occured while running getImagePath : " + e);
        }
        return imgpath;
    }
    
    /**
     * 复制文件
     * 
     * @param origin
     *            原始文件
     * @param dest
     *            目标文件
     * @return 是否复制成功
     */
    public static boolean copyFile(File origin, File dest)
    {
        if (origin == null || dest == null)
        {
            return false;
        }
        if (!dest.exists())
        {
            File parentFile = dest.getParentFile();
            if (!parentFile.exists())
            {
                boolean succeed = parentFile.mkdirs();
                if (!succeed)
                {
                    Logger.i(TAG, "copyFile failed, cause mkdirs return false");
                    return false;
                }
            }
            try
            {
                dest.createNewFile();
            }
            catch (Exception e)
            {
                Logger.i(TAG, "copyFile failed, cause createNewFile failed");
                return false;
            }
        }
        FileInputStream in = null;
        FileOutputStream out = null;
        try
        {
            in = new FileInputStream(origin);
            out = new FileOutputStream(dest);
            FileChannel inC = in.getChannel();
            FileChannel outC = out.getChannel();
            int length = BUFFER_SIZE;
            while (true)
            {
                if (inC.position() == inC.size())
                {
                    return true;
                }
                if ((inC.size() - inC.position()) < BUFFER_SIZE)
                {
                    length = (int) (inC.size() - inC.position());
                }
                else
                {
                    length = BUFFER_SIZE;
                }
                inC.transferTo(inC.position(), length, outC);
                inC.position(inC.position() + length);
            }
        }
        catch (Exception e)
        {
            return false;
        }
        finally
        {
            closeStream(in);
            closeStream(out);
        }
    }
    
    /**
     * 专门用来关闭可关闭的流
     * 
     * @param beCloseStream
     *            需要关闭的流
     * @return 已经为空或者关闭成功返回true，否则返回false
     */
    public static boolean closeStream(java.io.Closeable beCloseStream)
    {
        if (beCloseStream != null)
        {
            try
            {
                beCloseStream.close();
                return true;
            }
            catch (IOException e)
            {
                Logger.e(TAG, "close stream error", e);
                return false;
            }
        }
        return true;
    }
    
    /**
     * 获取Sdcard路径<BR>
     * 
     * @return 获取sdcard
     */
    public static String getSdCardPaths()
    {
        try
        {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            String line;
            String mount = new String();
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null)
            {
                if (line.contains("secure"))
                {
                    continue;
                }
                
                if (line.contains("asec"))
                {
                    continue;
                }
                
                if (line.contains("fat"))
                {
                    String[] columns = line.split(" ");
                    if (columns != null && columns.length > 1)
                    {
                        // mount = mount.concat("*" + columns[1] + "\n");
                        mount = mount.concat("," + columns[1]);
                    }
                }
                else if (line.contains("fuse"))
                {
                    String[] columns = line.split(" ");
                    if (columns != null && columns.length > 1)
                    {
                        mount = mount.concat(columns[1]);
                    }
                }
            }
            return mount;
        }
        catch (FileNotFoundException e)
        {
            Logger.d(TAG, "Error: " + e.toString());
        }
        catch (IOException e)
        {
            Logger.d(TAG, "Error: " + e.toString());
        }
        return null;
    }
}
