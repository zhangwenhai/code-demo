/*
 * 文件名: UpgradeDbUtil.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 日志工具
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.component.log;

import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.android.LogcatAppender;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.spi.FilterReply;
import com.android.common.FusionConfig;
import org.slf4j.LoggerFactory;

/**
 * 日志工具<BR>
 * 1.添加日志输出选项.控制日志输出位置 2.添加文件日志功能. 3.控制单个日志文件最大限制.由LOG_MAXSIZE常量控制,保留两个最新日志文件
 * 4.文件日志输出目标
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public class Logger
{
    
    /**
     * 该类打印显示的TAG字符串
     */
    private static final String TAG = "Logger";
    
    /**
     * log目录
     */
    private static final String LOG_FILE_DIR = "/com.android/log";
    
    /**
     * 日志打印到控制台
     */
    private static final int TO_CONSOLE = 0x1;
    
    /**
     * 日志打印到文件
     */
    private static final int TO_FILE = 0x10;
    
    /**
     * 日志打印的模式
     */
    // private static final int DEBUG_ALL = TO_CONSOLE;
    
    /**
     * 缓存tag
     */
    private static final ConcurrentHashMap<String, org.slf4j.Logger> LOG_MAP = new ConcurrentHashMap<String, org.slf4j.Logger>();
    
    /**
     * 日志打印到手机的存储路径
     */
    // @SuppressLint("SdCardPath")
    // private static final String LOCAL_LOG_PATH =
    // "/data/data/%packagename%/log/";
    
    /**
     * 日志打印到Sdcard的存储路径
     */
    private static final String SDCARD_LOG_PATH = "/%packagename%/log/files/";
    
    /**
     * 单个文件存储的最大容量
     */
    private static final int LOG_MAXSIZE = 1024 * 200; // double the size
    
    /**
     * 输出日志的文件名称
     */
    private static final String LOG_TEMP_FILE = "log.temp";
    
    /**
     * 最近一次的日志名称
     */
    private static final String LOG_LAST_FILE = "log_last.txt";
    
    /**
     * 打印日志的级别
     */
    private static final int LOG_LEVEL = Log.VERBOSE;
    
    /**
     * 同步锁
     */
    private static Object mLockObj = new Object();
    
    /**
     * 日志打印到文件的路径
     */
    private static String mAppPath;
    
    /**
     * 日志文件名称前的前缀，用于区分是哪个应用的日志，一般默认是应用名称
     */
    private static String mLogFilePrefix;
    
    /**
     * 日志打印对象的实例
     */
    private static Logger mLog;
    
    /**
     * 控制日志输出标志位，打包控制
     */
    private boolean isDebug = FusionConfig.getInstance().isDebug();
    
    /**
     * 日志的输出流
     */
    private OutputStream mLogStream;
    
    /**
     * 文件的大小
     */
    private long mFileSize;
    
    /**
     * 打印文件日志的时间
     */
    private Calendar mDate = Calendar.getInstance();
    
    /**
     * 打印文件日志内容的StringBuffer
     */
    private StringBuffer mBuffer = new StringBuffer();
    
    private static final String CICI_LOG_TAG_PREFIX = "cici.";
    
    /**
     * 无参构造器
     */
    private Logger()
    {
        if (mAppPath == null)
        {
            mAppPath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + LOG_FILE_DIR;
        }
    }
    
    /**
     * 获取文件路径<BR>
     * 
     * @param context
     *            Context
     * @return 返回文件路径
     */
    public static String getLogPath(Context context)
    {
        String dirPath;
        if (FusionConfig.getInstance().isDebug())
        {
            dirPath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath()
                    + SDCARD_LOG_PATH.replaceFirst("%packagename%",
                            context.getPackageName());
        }
        else
        {
            // dirPath = LOCAL_LOG_PATH.replaceFirst("%packagename%",
            // context.getPackageName());
            dirPath = new File(context.getFilesDir().getPath(), "logs").getAbsolutePath();
        }
        File dir = new File(dirPath);
        if (!dir.exists())
        {
            dir.mkdirs();
        }
        return dirPath;
    }
    
    private static class CiciLogcatAppender extends LogcatAppender
    {
        protected String getTag(ILoggingEvent event)
        {
            String tag = super.getTag(event);
            if (tag.startsWith(CICI_LOG_TAG_PREFIX))
            {
                tag = tag.substring(CICI_LOG_TAG_PREFIX.length());
            }
            return tag;
        }
    }
    
    private static class CiciLogFilter extends
            ch.qos.logback.core.filter.Filter<ILoggingEvent>
    {
        
        @Override
        public FilterReply decide(ILoggingEvent loggingEvent)
        {
            if (!isStarted())
            {
                return FilterReply.NEUTRAL;
            }
            
            String name = loggingEvent.getLoggerName();
            if (name.startsWith(CICI_LOG_TAG_PREFIX))
            {
                return FilterReply.NEUTRAL;
            }
            else
            {
                return FilterReply.DENY;
            }
        }
        
    }
    
    private static String getProcessName(Context context)
    {
        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningAppProcessInfo processInfo : manager.getRunningAppProcesses())
        {
            if (processInfo.pid == pid)
            {
                return processInfo.processName.replace(':', '_');
            }
        }
        return "cici";
    }
    
    /**
     * 配置日志打印信息<BR>
     * 
     * @param context
     *            应用上下文
     */
    public static void configureLogback(Context context)
    {
        
        String logPath = getLogPath(context);
        String date = DateFormat.getDateFormat(context).format(new Date());
        StringBuffer fileNameSb = new StringBuffer();
        
        fileNameSb.append(getProcessName(context));
        fileNameSb.append("_");
        fileNameSb.append(date);
        fileNameSb.append(".log");
        File logFile = new File(logPath, fileNameSb.toString());
        // reset the default context (which may already have been initialized)
        // since we want to reconfigure it
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        lc.reset();
        
        boolean debugMode = FusionConfig.getInstance().isDebug();
        // setup FileAppender
        PatternLayoutEncoder encoder1 = new PatternLayoutEncoder();
        encoder1.setContext(lc);
        if (debugMode)
        {
            encoder1.setPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
        }
        else
        {
            encoder1.setPattern("%d{HH:mm:ss.SSS} %-5level %logger{36} - %msg%n");
        }
        encoder1.start();
        
        FileAppender<ILoggingEvent> fileAppender = new FileAppender<ILoggingEvent>();
        fileAppender.setContext(lc);
        fileAppender.setFile(logFile.getAbsolutePath());
        fileAppender.setAppend(true);
        if (!debugMode)
        {
            fileAppender.addFilter(new CiciLogFilter());
        }
        fileAppender.setEncoder(encoder1);
        // fileAppender.start();
        
        // setup LogcatAppender
        PatternLayoutEncoder encoder2 = new PatternLayoutEncoder();
        encoder2.setContext(lc);
        encoder2.setPattern("[%thread] %msg%n");
        encoder2.start();
        
        CiciLogcatAppender logcatAppender = new CiciLogcatAppender();
        logcatAppender.setContext(lc);
        if (!debugMode)
        {
            logcatAppender.addFilter(new CiciLogFilter());
        }
        logcatAppender.setEncoder(encoder2);
        logcatAppender.start();
        
        // add the newly created appenders to the root logger;
        // qualify Logger to disambiguate from org.slf4j.Logger
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        root.addAppender(fileAppender);
        root.addAppender(logcatAppender);
        if (debugMode)
        {
            root.setLevel(Level.DEBUG);
        }
        else
        {
            root.setLevel(Level.INFO);
        }
        
    }
    
    /**
     * 单例<BR>
     * 获取单例打印日志
     * 
     * @return 返回Logger实例
     */
    public static Logger getInstance()
    {
        if (mLog == null)
        {
            mLog = new Logger();
        }
        return mLog;
    }
    
    /**
     * 
     * 初始化Log打印路径<BR>
     * 该方法在Application启动时初始化
     * 
     * @param packageName
     *            应用包名
     * @param filePrefix
     *            文件名的前缀 一般为应用名
     */
    public static void initAppPath(Context context, String filePrefix)
    {
        synchronized (mLockObj)
        {
            // Sdcard可用
            if (Environment.getExternalStorageState()
                    .equals(Environment.MEDIA_MOUNTED))
            {
                File storage = Environment.getExternalStorageDirectory();
                String packageName = context.getPackageName();
                mAppPath = storage
                        + SDCARD_LOG_PATH.replaceFirst("%packagename%",
                                packageName);
            }
            else
            {
                // mAppPath = LOCAL_LOG_PATH.replaceFirst("%packagename%",
                // packageName);
                mAppPath = new File(context.getFilesDir().getPath(), "logs").getAbsolutePath();
            }
            mLogFilePrefix = filePrefix;
            Log.d(TAG, "initAppPath: " + mAppPath);
            File dir = new File(mAppPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }
        }
    }
    
    /**
     * 
     * 打印调试级别的日志<BR>
     * 
     * @param tag
     *            打印消息的标签
     * @param msg
     *            打印消息的内容
     */
    public static void d(String tag, String msg)
    {
        getLogger(tag).debug(msg);
        // getInstance().log(tag, msg, DEBUG_ALL, Log.DEBUG);
    }
    
    /**
     * 
     * 打印VERBOSE级别的日志<BR>
     * 
     * @param tag
     *            打印消息的标签
     * @param msg
     *            打印消息的内容
     */
    public static void v(String tag, String msg)
    {
        // getInstance().log(tag, msg, DEBUG_ALL, Log.VERBOSE);
        d(tag, msg);
    }
    
    private static org.slf4j.Logger getLogger(String tag)
    {
        StringBuilder sb = new StringBuilder(CICI_LOG_TAG_PREFIX);
        sb.append(tag);
        tag = sb.toString();
        org.slf4j.Logger logger = LOG_MAP.get(tag);
        if (logger == null)
        {
            logger = LoggerFactory.getLogger(tag);
            LOG_MAP.put(tag, logger);
        }
        return logger;
    }
    
    /**
     * 打印Error级别的日志<BR>
     * 
     * @param tag
     *            打印消息的标签
     * @param msg
     *            打印消息的内容
     */
    public static void e(String tag, String msg)
    {
        // getInstance().log(tag, msg, DEBUG_ALL, Log.ERROR);
        getLogger(tag).error(msg);
    }
    
    /**
     * 打印Error级别的日志带异常<BR>
     * 
     * @param tag
     *            打印消息的标签
     * @param msg
     *            打印消息的内容
     * @param tr
     *            Throwable
     */
    public static void e(String tag, String msg, Throwable tr)
    {
        // getInstance().log(tag,
        // msg + '\n' + android.util.Log.getStackTraceString(tr),
        // DEBUG_ALL,
        // Log.ERROR);
        getLogger(tag).error(msg, tr);
    }
    
    /**
     * 打印Info级别的日志<BR>
     * 
     * @param tag
     *            打印消息的标签
     * @param msg
     *            打印消息的内容
     */
    public static void i(String tag, String msg)
    {
        // getInstance().log(tag, msg, DEBUG_ALL, Log.INFO);
        getLogger(tag).info(msg);
    }
    
    /**
     * 打印Warn级别的日志<BR>
     * 
     * @param tag
     *            打印消息的标签
     * @param msg
     *            打印消息的内容
     */
    public static void w(String tag, String msg)
    {
        // getInstance().log(tag, msg, DEBUG_ALL, Log.WARN);
        getLogger(tag).warn(msg);
    }
    
    /**
     * 打印Warn级别的日志带异常<BR>
     * 
     * @param tag
     *            打印消息的标签
     * @param msg
     *            打印消息的内容
     * @param tr
     *            Throwable
     */
    public static void w(String tag, String msg, Throwable tr)
    {
        // getInstance().log(tag,
        // msg + '\n' + android.util.Log.getStackTraceString(tr),
        // DEBUG_ALL,
        // Log.WARN);
        
        getLogger(tag).warn(msg, tr);
    }
    
    /**
     * 打印的方法类<BR>
     * 
     * @param tag
     *            打印的标签
     * @param msg
     *            打印的消息
     * @param outdest
     *            打印的模式
     * @param level
     *            打印的级别
     */
    protected void log(String tag, String msg, int outdest, int level)
    {
        if (!isDebug)
        {
            return;
        }
        if (tag == null)
        {
            tag = "PaiTao";
        }
        
        if (msg == null)
        {
            msg = "MSG_NULL";
        }
        if (level >= LOG_LEVEL)
        {
            // 打印到控制台
            if ((outdest & TO_CONSOLE) != 0)
            {
                logToConsole(tag, msg, level);
            }
            
            // 打印到文件
            if ((outdest & TO_FILE) != 0)
            {
                logToFile(tag, msg, level);
            }
        }
    }
    
    /**
     * 
     * 组成Log字符串.添加时间信息<BR>
     * 
     * @param tag
     * @param msg
     * @return 返回组装的字符串
     */
    private String getLogStr(String tag, String msg)
    {
        mDate.setTimeInMillis(System.currentTimeMillis());
        mBuffer.setLength(0);
        mBuffer.append("[");
        mBuffer.append(tag);
        mBuffer.append(" : ");
        mBuffer.append(mDate.get(Calendar.MONTH) + 1);
        mBuffer.append("-");
        mBuffer.append(mDate.get(Calendar.DATE));
        mBuffer.append(" ");
        mBuffer.append(mDate.get(Calendar.HOUR_OF_DAY));
        mBuffer.append(":");
        mBuffer.append(mDate.get(Calendar.MINUTE));
        mBuffer.append(":");
        mBuffer.append(mDate.get(Calendar.SECOND));
        mBuffer.append(":");
        mBuffer.append(mDate.get(Calendar.MILLISECOND));
        mBuffer.append("] ");
        mBuffer.append(msg);
        return mBuffer.toString();
    }
    
    /**
     * 
     * 将日志打印到控制台<BR>
     * 
     * @param tag
     *            打印的标签
     * @param msg
     *            打印的信息
     * @param level
     *            打印的级别
     */
    private void logToConsole(String tag, String msg, int level)
    {
        switch (level)
        {
            case Log.DEBUG:
                Log.d(tag, msg);
                break;
            case Log.ERROR:
                Log.e(tag, msg);
                break;
            case Log.INFO:
                Log.i(tag, msg);
                break;
            case Log.VERBOSE:
                Log.v(tag, msg);
                break;
            case Log.WARN:
                Log.w(tag, msg);
                break;
            default:
                break;
        }
    }
    
    /**
     * 
     * 将log打到文件日志<BR>
     * 
     * @param tag
     *            打印的标签
     * @param msg
     *            打印的信息
     * @param level
     *            打印的级别
     */
    private void logToFile(String tag, String msg, int level)
    {
        synchronized (mLockObj)
        {
            OutputStream outStream = openLogFileOutStream();
            if (outStream != null)
            {
                try
                {
                    byte[] d = getLogStr(tag, msg).getBytes("utf-8");
                    if (mFileSize < LOG_MAXSIZE)
                    {
                        outStream.write(d);
                        outStream.write("\r\n".getBytes());
                        outStream.flush();
                        mFileSize += d.length;
                    }
                    else
                    {
                        closeLogFileOutStream();
                        renameLogFile();
                        logToFile(tag, msg, level);
                    }
                }
                catch (Exception e)
                {
                    Log.e(TAG, e.toString());
                }
            }
        }
    }
    
    /**
     * 
     * 获取日志临时文件输出流<BR>
     * 
     * @return 返回文件的输出流
     */
    private OutputStream openLogFileOutStream()
    {
        if (mLogStream == null)
        {
            try
            {
                if (TextUtils.isEmpty(mAppPath))
                {
                    throw new NullPointerException(
                            "mAppPath is null,please init at Application onCreate");
                }
                File file = openAbsoluteFile(LOG_TEMP_FILE);
                if (file == null)
                {
                    return null;
                }
                if (file.exists())
                {
                    mLogStream = new FileOutputStream(file, true);
                    mFileSize = file.length();
                }
                else
                {
                    mLogStream = new FileOutputStream(file);
                    mFileSize = 0;
                }
            }
            catch (FileNotFoundException e)
            {
                Log.d(TAG, e.toString());
            }
        }
        return mLogStream;
    }
    
    /**
     * 
     * 关闭日志输出流<BR>
     */
    private void closeLogFileOutStream()
    {
        try
        {
            if (mLogStream != null)
            {
                mLogStream.close();
                mLogStream = null;
                mFileSize = 0;
            }
        }
        catch (IOException e)
        {
            Log.d(TAG, e.toString());
        }
    }
    
    /**
     * 
     * 以绝对路径打开文件<BR>
     * 
     * @param name
     *            文件的名称
     * @return 返回日志文件
     */
    public File openAbsoluteFile(String name)
    {
        if (TextUtils.isEmpty(mAppPath))
        {
            throw new NullPointerException(
                    "mAppPath is null,please init at Application onCreate");
        }
        else
        {
            File file = new File(mAppPath + mLogFilePrefix + "_" + name);
            return file;
        }
    }
    
    /**
     * 
     * 重命名日志文件<BR>
     */
    private void renameLogFile()
    {
        synchronized (mLockObj)
        {
            File file = openAbsoluteFile(LOG_TEMP_FILE);
            File destFile = openAbsoluteFile(LOG_LAST_FILE);
            if (destFile.exists())
            {
                destFile.delete();
            }
            file.renameTo(destFile);
        }
    }
    
}
