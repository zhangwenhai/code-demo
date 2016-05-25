package com.handmark.pulltorefresh.library.internal;

import android.util.Log;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 * @author zhangwenhai
 * @version [Client V20130911, 2015-3-8]
 */
public class Utils
{
    
    static final String LOG_TAG = "PullToRefresh";
    
    /**
     * print deprecation info<BR>
     * @param depreacted depreacted info
     * @param replacement replacement info
     */
    public static void warnDeprecation(String depreacted, String replacement)
    {
        Log.w(LOG_TAG, "You're using the deprecated " + depreacted
                + " attr, please switch over to " + replacement);
    }
    
}
