/*
 * 文件名: HomeFragment.java
 * 版    权： Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 主页
 * 创建人: jp
 * 创建时间:2015-3-8
 *
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.ui.home;

import android.view.View;

import com.android.ui.basic.BasicTitleBarFragment;
import com.android.R;

/**
 * 主页<BR>
 * @author jp
 * @version [Client V20150307, 2015-3-8]
 */
public class HomeFragment extends BasicTitleBarFragment
{
    @Override public int getLayoutId()
    {
        return R.layout.fragment_home;
    }

    @Override public boolean initializeTitlBar()
    {
        setMiddleTitle(R.string.tab_fragment_home);
        return true;
    }

    @Override protected void initializeViews(View rootView)
    {

    }

    @Override protected void initLogics()
    {

    }

}
