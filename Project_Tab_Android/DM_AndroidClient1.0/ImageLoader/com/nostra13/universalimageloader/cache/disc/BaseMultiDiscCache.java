/*
 * 文件名: BaseMultiDiscCache.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 根据类型支持多个文件夹硬盘缓存
 * 创建人: chunjiang.shieh <chunjiang.shieh@gmail.com>
 * 创建时间:2013-11-21
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.nostra13.universalimageloader.cache.disc;

import java.io.File;
import java.util.HashMap;

import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.core.DefaultConfigurationFactory;

/**
 * 根据类型支持多个文件夹硬盘缓存<BR>
 * @author zhangwenhai
 * @version [Client V20130911, 2015-3-8]
 */
public abstract class BaseMultiDiscCache implements DiscCacheAware
{
    
    /**
     * 默认文件夹下的图片是可以清除缓存的
     */
    public static final String IMAGE_TYPE_DEFAULT = "default";
    
    private static final String ERROR_ARG_NULL = "\"%s\" argument must be not null";
    
    protected HashMap<String, File> cacheDirMap;
    
    protected File cacheDir;
    
    private FileNameGenerator fileNameGenerator;
    
    public BaseMultiDiscCache(HashMap<String, File> cacheDirMap)
    {
        this(cacheDirMap, DefaultConfigurationFactory.createFileNameGenerator());
    }
    
    public BaseMultiDiscCache(HashMap<String, File> cacheDirMap,
            FileNameGenerator fileNameGenerator)
    {
        if (cacheDirMap == null)
        {
            throw new IllegalArgumentException("cacheDirMap" + ERROR_ARG_NULL);
        }
        if (fileNameGenerator == null)
        {
            throw new IllegalArgumentException("fileNameGenerator"
                    + ERROR_ARG_NULL);
        }
        this.cacheDirMap = cacheDirMap;
        this.fileNameGenerator = fileNameGenerator;
    }
    
    @Override
    public File get(String key)
    {
        if (cacheDir == null)
        {
            cacheDir = cacheDirMap.get(IMAGE_TYPE_DEFAULT);
        }
        if (cacheDir == null)
        {
            throw new IllegalArgumentException("cacheDir" + ERROR_ARG_NULL);
        }
        String fileName = fileNameGenerator.generate(key);
        return new File(cacheDir, fileName);
    }
    
    /**
     * 获取图片文件<BR>
     * @param key  图片uri
     * @param imageType 图片类型
     * @return 返回File
     */
    public File get(String key, String imageType)
    {
        cacheDir = cacheDirMap.get(imageType);
        if (cacheDir == null)
        {
            throw new IllegalArgumentException("cacheDir" + ERROR_ARG_NULL);
        }
        return get(key);
    }
    
    @Override
    public void clear()
    {
        if (cacheDir == null)
        {
            cacheDir = cacheDirMap.get(IMAGE_TYPE_DEFAULT);
        }
        if (cacheDir == null)
        {
            throw new IllegalArgumentException("cacheDir" + ERROR_ARG_NULL);
        }
        File[] files = cacheDir.listFiles();
        if (files != null)
        {
            for (File f : files)
            {
                f.delete();
            }
        }
    }
    
}
