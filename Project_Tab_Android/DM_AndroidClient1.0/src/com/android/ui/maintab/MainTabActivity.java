/*
 * 文件名: MainTabActivity.java
 * 版    权： Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 应用的主UI
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.ui.maintab;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.android.ui.basic.BasicActivity;
import com.android.ui.deal.DealFragment;
import com.android.ui.home.HomeFragment;
import com.android.ui.profile.ProfileFragment;
import com.android.ui.wiki.WikiFragment;
import com.android.R;

/**
 * 应用的主UI<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public class MainTabActivity extends BasicActivity
{
    /**
     * 定义Tab TAG
     */
    private static final String[] FRAGMENT_TAGS = new String[] {
            "HomeFragment", "WikiFragment", "DealFragment", "ProfileFragment" };

    /**
     * TabHost
     */
    private TabHost mTabHost;

    /**
     * TabWidget
     */
    private TabWidget mTabWidget;

    /**
     * Tab管理类，用来添加fragment到每个tab中
     */
    private TabManager mTabManager;

    /**
     * 主页
     */
    private TabIndicatorView mHomeView;

    /**
     * 百科
     */
    private TabIndicatorView mWikiView;

    /**
     * 订单
     */
    private TabIndicatorView mDealView;

    /**
     * 我的
     */
    private TabIndicatorView mProfileView;

    /**
     * 返回一个boolean表示展示该页面是否需要登录成功
     * @return boolean 是否是登录后的页面
     */
    @Override
    protected boolean needLogin()
    {
        // 此页面不需要先登录
        return false;
    }

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.main_tab);
        initTabHost();
        setTabContent();
    }

    /**
     * 初始化TabHost
     */
    private void initTabHost()
    {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();
        mTabWidget = (TabWidget) findViewById(android.R.id.tabs);

        mTabManager = new TabManager(this, mTabHost, R.id.real_tabcontent);
    }

    @Override
    protected void initLogics()
    {
        super.initLogics();
    }

    private void setTabContent()
    {
        mHomeView = getIndicatorView(R.string.tab_fragment_home);
        mWikiView = getIndicatorView(R.string.tab_fragment_wiki);
        mDealView = getIndicatorView(R.string.tab_fragment_deal);
        mProfileView = getIndicatorView(R.string.tab_fragment_profile);
        mHomeView.setUnreadCount(0);
        mProfileView.setUnreadCount(0);
        mHomeView.hideDivider();

        //添加标签 主页
        mTabManager.addTab(mTabHost.newTabSpec(FRAGMENT_TAGS[0])
                        .setIndicator(mHomeView),
                HomeFragment.class,
                null);
        //添加标签 百科
        mTabManager.addTab(mTabHost.newTabSpec(FRAGMENT_TAGS[1])
                        .setIndicator(mWikiView),
                WikiFragment.class,
                null);
        //添加标签 订单
        mTabManager.addTab(mTabHost.newTabSpec(FRAGMENT_TAGS[2])
                        .setIndicator(mDealView),
                DealFragment.class,
                null);
        //添加标签 个人
        mTabManager.addTab(mTabHost.newTabSpec(FRAGMENT_TAGS[3])
                        .setIndicator(mProfileView),
                ProfileFragment.class,
                null);
    }

    /**
     * 获得tab的indicator<BR>
     * @param drawableId 图片资源ID
     * @return indicator
     */
    private TabIndicatorView getIndicatorView(int drawableId)
    {
        return new TabIndicatorView(this, drawableId);
    }

    /**
     * 返回键进入后台
     * @param event 点击事件
     * @return dispatchKeyEvent
     * @see android.app.Activity#dispatchKeyEvent(KeyEvent)
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK)
        {
            PackageManager pm = getPackageManager();
            ResolveInfo homeInfo = pm.resolveActivity(new Intent(
                    Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), 0);
            ActivityInfo ai = homeInfo.activityInfo;
            Intent startIntent = new Intent(Intent.ACTION_MAIN);
            startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            startIntent.setComponent(new ComponentName(ai.packageName,
                    ai.name));
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startIntent);
            return false;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void handleStateMessage(Message msg)
    {
    }

    private class TabIndicatorView extends RelativeLayout
    {
        private int mTabTitleId;

        private TextView mTitleView;

        private TextView mUnreadMsgView;

        private View mDividerView;

        public TabIndicatorView(Context context)
        {
            super(context);
        }

        public TabIndicatorView(Context context, int tabTitleId)
        {
            this(context);
            mTabTitleId = tabTitleId;
            initView(context);
        }

        private void initView(Context context)
        {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.tab_indicator, null);
            mTitleView = (TextView) view.findViewById(R.id.tab_title);
            mUnreadMsgView = (TextView) view.findViewById(R.id.tab_unread_msg);
            mDividerView = view.findViewById(R.id.tab_divider);
            mTitleView.setText(mTabTitleId);
            addView(view, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        }

        public void setUnreadCount(int count)
        {
            mUnreadMsgView.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
            mUnreadMsgView.setText(String.valueOf(count));
        }

        public void hideDivider()
        {
            mDividerView.setVisibility(View.GONE);
        }

        public void showDivider()
        {
            mDividerView.setVisibility(View.VISIBLE);
        }
    }
}
