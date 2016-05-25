/*
 * 文件名: WikiFragment.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 百科
 * 创建人: jp
 * 创建时间:2015-3-8
 *
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.ui.wiki;

import android.view.View;

import com.android.ui.basic.BasicTitleBarFragment;
import com.android.R;

/**
 * 百科<BR>
 * @author jp
 * @version [Client V20150307, 2015-3-8]
 */
public class WikiFragment extends BasicTitleBarFragment
{

    @Override public int getLayoutId()
    {
        return R.layout.fragment_wiki;
    }

    @Override public boolean initializeTitlBar()
    {
        setMiddleTitle(R.string.tab_fragment_wiki);
        return true;
    }

    @Override protected void initializeViews(View rootView)
    {

    }

    @Override protected void initLogics()
    {

    }

}
