/*
 * 文件名: PrefixHighlighter.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 将一段文字中的指定文字高亮，如果指定的文字重复出现，只高亮第一次出现的
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.ui.basic.dialog;

import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.android.utils.StringUtil;

/**
 * 将一段文字中的指定文字高亮，如果指定的文字重复出现，只高亮第一次出现的<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public class PrefixHighlighter
{
    private final int mPrefixHighlightColor;
    
    private ForegroundColorSpan mPrefixColorSpan;
    
    /**
     * 构造函数，并指定高亮颜色
     * @param prefixHighlightColor HighlightColor
     */
    public PrefixHighlighter(int prefixHighlightColor)
    {
        mPrefixHighlightColor = prefixHighlightColor;
    }
    
    /**
     * Sets the text on the given text view, highlighting the word that matches the given prefix.
     *
     * @param view the view on which to set the text
     * @param text the string to use as the text, in upper case letters
     * @param prefix the prefix to look for
     */
    public void setText(TextView view, String text, char[] prefix)
    {
        view.setText(apply(text, prefix));
    }
    
    /**
     * Returns a CharSequence which highlights the given prefix if found in the given text.
     *
     * @param text the text to which to apply the highlight
     * @param prefix the prefix to look for
     * @return a CharSequence which highlights the given prefix if found in the given text.
     */
    public CharSequence apply(CharSequence text, char[] prefix)
    {
        int index = StringUtil.indexOfWordPrefix(text, prefix);
        if (index != -1)
        {
            if (mPrefixColorSpan == null)
            {
                mPrefixColorSpan = new ForegroundColorSpan(
                        mPrefixHighlightColor);
            }
            
            SpannableString result = new SpannableString(text);
            result.setSpan(mPrefixColorSpan, index, index + prefix.length, 0 /* flags */);
            return result;
        }
        else
        {
            return text;
        }
    }
}
