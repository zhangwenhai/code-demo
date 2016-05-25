/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.handmark.pulltorefresh.library.internal;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.View;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 * @author zhangwenhai
 * @version [Client V20130911, 2015-3-8]
 */
public class ViewCompat
{
    /**
     * postOnAnimation on view<BR>
     * @param view the target view
     * @param runnable the Runnable obj
     */
    public static void postOnAnimation(View view, Runnable runnable)
    {
        //        if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
        //            SDK16.postOnAnimation(view, runnable);
        //        } else {
        //            view.postDelayed(runnable, 16);
        //        }
        view.postDelayed(runnable, 16);
    }
    
    /**
     * 
     * setBackground to view<BR>
     * @param view the target view
     * @param background the bg drawable
     */
    public static void setBackground(View view, Drawable background)
    {
        //        if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
        //            SDK16.setBackground(view, background);
        //        } else {
        //            view.setBackgroundDrawable(background);
        //        }
        view.setBackgroundDrawable(background);
    }
    
    /**
     * 
     * set layer type<BR>
     * @param view the target vuew
     * @param layerType the layerType
     */
    public static void setLayerType(View view, int layerType)
    {
        if (VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB)
        {
            SDK11.setLayerType(view, layerType);
        }
    }
    
    /**
     * 
     * [一句话功能简述]<BR>
     * [功能详细描述]
     * @author zhangwenhai
     * @version [Client V20130911, 2015-3-8]
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    static class SDK11
    {
        
        public static void setLayerType(View view, int layerType)
        {
            view.setLayerType(layerType, null);
        }
    }
    
    /**
     * 
     * [一句话功能简述]<BR>
     * [功能详细描述]
     * @author zhangwenhai
     * @version [Client V20130911, 2015-3-8]
     */
    static class SDK16
    {
        
        public static void postOnAnimation(View view, Runnable runnable)
        {
            //            view.postOnAnimation(runnable);
        }
        
        public static void setBackground(View view, Drawable background)
        {
            //            view.setBackground(background);
        }
        
    }
    
}
