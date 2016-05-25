/*
 * 文件名: ProfileFragment.java
 * 版    权： Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 我的界面
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 *
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.ui.profile;

import android.view.View;

import com.android.ui.basic.BasicTitleBarFragment;
import com.android.R;

/**
 * 我的<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public class ProfileFragment extends BasicTitleBarFragment
{

    @Override public int getLayoutId()
    {
        return R.layout.fragment_profile;
    }

    @Override public boolean initializeTitlBar()
    {
        setMiddleTitle(R.string.tab_fragment_profile);
        return true;
    }

    @Override protected void initializeViews(View rootView)
    {

    }

    @Override protected void initLogics()
    {

    }

}
