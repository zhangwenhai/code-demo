/*
 * 文件名: CCImageLoader.java
 * 版    权： Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 图片加载
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.ImageView;

import com.android.R;
import com.nostra13.universalimageloader.cache.disc.BaseMultiDiscCache;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedMultiDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

/**
 * 图片加载<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-9]
 */
public class CCImageLoader
{
    /**
     * IM
     */
    public static final String IMAGE_TYPE_IM = "image_type_im";
    
    private static final String IMAGE_IM_PATH = "/%packagename%/image/IM";
    
    private static final String IMAGE_CACHE_PATH = "/%packagename%/image/Cache";
    
    private static final String FILE = "file://";
    
    private static final String NETWORK = "http://";
    
    private static final String ASSETS = "assets://icons/";
    
    private static final String RESOURCE = "res://";
    
    private static final int DEFAULT_IMAGE_RES = R.drawable.icon_common_avatar_large;
    
    private static CCImageLoader mLoader;
    
    private String mSpecialSdCardPath = FileUtil.getSdCardPaths();

    /**
     * 构造器
     */
    private CCImageLoader()
    {
    }

    /**
     * 获取CCImageLoader的单例<BR>
     * @return 返回CCImageLoader对象
     */
    public static CCImageLoader getInstance()
    {
        if (mLoader == null)
        {
            mLoader = new CCImageLoader();
        }
        return mLoader;
    }

    /**
     *
     * init image loader<BR>
     * 应用启动初始化的时候被调用
     * This configuration tuning is custom.
     * You can tune every option, you may tune some of them,
     * or you can create default configuration by ImageLoaderConfiguration.createDefault(context);
     * @param context Context对象
     */
    public void init(Context context)
    {
        File imDir = StorageUtils.getOwnCacheDirectory(context,
                IMAGE_IM_PATH.replaceFirst("%packagename%",
                        context.getPackageName()));

        File cacheDir = StorageUtils.getOwnCacheDirectory(context,
                IMAGE_CACHE_PATH.replaceFirst("%packagename%",
                        context.getPackageName()));
        HashMap<String, File> cacheDirMap = new HashMap<String, File>();
        cacheDirMap.put(IMAGE_TYPE_IM, imDir);
        cacheDirMap.put(BaseMultiDiscCache.IMAGE_TYPE_DEFAULT, cacheDir);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
        //设置线程优先级
        .threadPriority(Thread.NORM_PRIORITY - 2)
                //自动缩放
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                //设置缓存大小，UsingFreqLimitedMemoryCache类可以扩展
                //                .memoryCache(new UsingFreqLimitedMemoryCache(4 * 1024 * 1024))
                //设置硬盘缓存,default cacheDir is /Android/data ,比其他limitedcache快30%
                .discCache(new UnlimitedMultiDiscCache(cacheDirMap))
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                // 是否需要日志打印，正式情况下不需要
                .enableLogging()
                .build();
        /**
         * Initialize ImageLoader with configuration.
         */
        ImageLoader.getInstance().init(config);
    }

    //    /**
    //     * 加载本地图片<BR>
    //     * @param targetView 要显示的目标View
    //     * @param localPath 本地路径
    //     */
    //    public void displayLocalImage(ImageView targetView, String localPath)
    //    {
    //        String path = FILE + localPath;
    //        DisplayImageOptions options = getLocalDisplayImageOptions(null,
    //                DEFAULT_IMAGE_RES,
    //                DEFAULT_IMAGE_RES,
    //                DEFAULT_IMAGE_RES);
    //        ImageLoader.getInstance().displayImage(path, targetView, options);
    //    }

    /**
     *
     * 显示指定尺寸的图片显示到ImageView中<BR>
     * @param targetView 要显示的目标View
     * @param urlOrPath 网络连接地址或路径
     * @param type 图片资源类型
     */
    public void displayImage(ImageView targetView, String urlOrPath,
            AllResourceType type)
    {
        displayImage(targetView, urlOrPath, type, DEFAULT_IMAGE_RES);
    }

    /**
     * 显示指定尺寸的图片显示到ImageView中<BR>
     * @param targetView 要显示的目标View
     * @param urlOrPath 网络连接地址或路径
     * @param type 图片资源类型
     * @param defaultResId 默认图片资源id
     */
    public void displayImage(ImageView targetView, String urlOrPath,
            AllResourceType type, int defaultResId)
    {
        //最终拼接后的URL
        String finalUrlOrPath = getUrlOrPath(urlOrPath, type);
        if (!TextUtils.isEmpty(finalUrlOrPath)
                && finalUrlOrPath.startsWith(FILE))
        {
            ImageLoader.getInstance().displayImage(finalUrlOrPath,
                    targetView,
                    getLocalDisplayImageOptions(null,
                            defaultResId,
                            defaultResId,
                            defaultResId));
        }
        else
        {
            ImageLoader.getInstance().displayImage(finalUrlOrPath,
                    targetView,
                    getDefaultDisplayImageOptions(null,
                            defaultResId,
                            defaultResId,
                            defaultResId));
        }
    }

    private String getUrlOrPath(String originalUrlOrPath, AllResourceType type)
    {
        if (TextUtils.isEmpty(originalUrlOrPath))
        {
            return null;
        }
        String specialSdCardPath = mSpecialSdCardPath;
        //        Logger.d("CCImageLoader", "specialSdCardPath: " + specialSdCardPath);
        String sdCardPath = getSDCardPath();
        
        //        Logger.d("CCImageLoader", "sdCardPath: " + sdCardPath);
        if (originalUrlOrPath.startsWith(RESOURCE))
        {
            Uri uri = Uri.parse(originalUrlOrPath);
            return ASSETS + uri.getLastPathSegment() + ".png";
        }
        if ((!TextUtils.isEmpty(sdCardPath) && originalUrlOrPath.startsWith(sdCardPath))
                || (!TextUtils.isEmpty(specialSdCardPath) && originalUrlOrPath.startsWith(specialSdCardPath))
                || hasSdCard(getSdCardPaths(specialSdCardPath),
                        originalUrlOrPath))
        {
            return FILE + originalUrlOrPath;
        }
        
        if (!originalUrlOrPath.startsWith(NETWORK))
        {
            return getImageUrl(originalUrlOrPath, type);
        }
        return originalUrlOrPath;
    }
    
    /**
     * 获取默认的DisplayImageOptions对象<BR>
     * @return 返回DisplayImageOptions对象
     */
    private DisplayImageOptions getDefaultDisplayImageOptions(String imageType,
            int stubImageRes, int emptyImageRes, int failImageRes)
    {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
        //在显示真正的图片前，会加载这个资源
        .showStubImage(stubImageRes)
                //空的Url时
                .showImageForEmptyUri(emptyImageRes)
                //加载失败时显示该资源
                .showImageOnFail(failImageRes)
                .bitmapConfig(Bitmap.Config.RGB_565)
                //图片的缩放方式
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                //开启内存缓存
                .cacheInMemory()
                //开启硬盘缓存
                .cacheOnDisc()
                //正常显示一张图片
                .displayer(new SimpleBitmapDisplayer())
                //                .displayer(new FadeInBitmapDisplayer(500))
                .imageType(imageType)
                .build();
        
        return options;
    }
    
    private DisplayImageOptions getLocalDisplayImageOptions(String imageType,
            int stubImageRes, int emptyImageRes, int failImageRes)
    {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
        //在显示真正的图片前，会加载这个资源
        .showStubImage(stubImageRes)
                //空的Url时
                .showImageForEmptyUri(emptyImageRes)
                //加载失败时显示该资源
                .showImageOnFail(failImageRes)
                .bitmapConfig(Bitmap.Config.RGB_565)
                //图片的缩放方式,完全按比例缩小到目标大小
                .imageScaleType(ImageScaleType.EXACTLY)
                //开启内存缓存
                .cacheInMemory()
                //正常显示一张图片
                .displayer(new SimpleBitmapDisplayer())
                .imageType(imageType)
                .build();
        
        return options;
    }
    
    /**
     * 获取外置存储卡的路径集合<BR>
     * @param specialSdCardPath 特殊的Sdcard路径
     * @return 返回外置存储卡的路径集合
     */
    private String[] getSdCardPaths(String specialSdCardPath)
    {
        if (TextUtils.isEmpty(specialSdCardPath))
        {
            return null;
        }
        return specialSdCardPath.split(",");
    }
    
    private boolean hasSdCard(String[] sdcardPaths, String originalUrlOrPath)
    {
        if (sdcardPaths != null)
        {
            for (String sdcardPath : sdcardPaths)
            {
                if (!TextUtils.isEmpty(sdcardPath)
                        && originalUrlOrPath.startsWith(sdcardPath))
                {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * 判断SD卡是否存在<BR>
     * @return if true sdcard is exist
     */
    private static boolean isSDCardExist()
    {
        return Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);
    }
    
    /**
     * 获取SD卡的路径<BR>
     * @return 返回SD卡的路径
     */
    private static String getSDCardPath()
    {
        
        if (isSDCardExist())
        {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }
    
    /**
     * 根据图片的hash值获取图片url
     * @param hash 图片的hash值
     * @param size 缩略图的尺寸(如果size是-1的话 代表取原图大小)
     * @return 返回图片下载的URL
     */
    private String getImageUrl(String hash, AllResourceType type)
    {
        if (type == null)
        {
            return null;
        }
        //TODO
        // return type.hashToUrl(hash);
        return "";
    }
    
    /**
     * 返回图片的 bmp<BR>
     * @param context Context
     * @param hash hash值
     * @return 返回图片的 bmp
     * @throws IOException IOException
     */
    public Bitmap getSmallBitmapFromNetWork(Context context, String hash)
            throws IOException
    {
        //TODO
        //String imageUri = getUrlOrPath(hash, AllResourceType.AVATARIMAGE_BIG);
        String imageUri = "";
        if (TextUtils.isEmpty(imageUri))
        {
            return null;
        }
        BaseImageDownloader downloader = new BaseImageDownloader(context);
        InputStream in = downloader.getStream(imageUri, null);
        if (in != null)
        {
            return BitmapFactory.decodeStream(in);
        }
        return null;
    }
}
