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
package com.handmark.pulltorefresh.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.R;
import com.handmark.pulltorefresh.library.internal.EmptyViewMethodAccessor;
import com.handmark.pulltorefresh.library.internal.LoadingLayout;

public class PullToRefreshListView extends
        PullToRefreshAdapterViewBase<ListView>
{
    
    private LoadingLayout mHeaderLoadingView;
    
    private LoadingLayout mFooterLoadingView;
    
    private FrameLayout mLvFooterLoadingFrame;
    
    private boolean mListViewExtrasEnabled;
    
    public PullToRefreshListView(Context context)
    {
        super(context);
    }
    
    public PullToRefreshListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    
    public PullToRefreshListView(Context context, Mode mode)
    {
        super(context, mode);
    }
    
    public PullToRefreshListView(Context context, Mode mode,
            AnimationStyle style)
    {
        super(context, mode, style);
    }
    
    public LoadingLayout getHeaderView()
    {
        return mHeaderLoadingView;
    }
    
    @Override
    public final Orientation getPullToRefreshScrollDirection()
    {
        return Orientation.VERTICAL;
    }
    
    @Override
    public void onRefreshing(final boolean doScroll)
    {
        /**
         * If we're not showing the Refreshing view, or the list is empty, the
         * the header/footer views won't show so we use the normal method.
         */
        ListAdapter adapter = mRefreshableView.getAdapter();
        if (!mListViewExtrasEnabled || !getShowViewWhileRefreshing()
                || null == adapter || adapter.isEmpty())
        {
            super.onRefreshing(doScroll);
            return;
        }
        
        super.onRefreshing(false);
        
        final LoadingLayout origLoadingView, listViewLoadingView, oppositeListViewLoadingView;
        final int selection, scrollToY;
        
        switch (getCurrentMode())
        {
            case MANUAL_REFRESH_ONLY:
            case PULL_FROM_END:
                origLoadingView = getFooterLayout();
                listViewLoadingView = mFooterLoadingView;
                oppositeListViewLoadingView = mHeaderLoadingView;
                selection = mRefreshableView.getCount() - 1;
                scrollToY = getScrollY() - getFooterSize();
                break;
            case PULL_FROM_START:
            default:
                origLoadingView = getHeaderLayout();
                listViewLoadingView = mHeaderLoadingView;
                oppositeListViewLoadingView = mFooterLoadingView;
                selection = 0;
                scrollToY = getScrollY() + getHeaderSize();
                break;
        }
        
        // Hide our original Loading View
        origLoadingView.reset();
        origLoadingView.hideAllViews();
        
        // Make sure the opposite end is hidden too
        oppositeListViewLoadingView.setVisibility(View.GONE);
        
        // Show the ListView Loading View and set it to refresh.
        listViewLoadingView.setVisibility(View.VISIBLE);
        listViewLoadingView.refreshing();
        
        if (doScroll)
        {
            // We need to disable the automatic visibility changes for now
            disableLoadingLayoutVisibilityChanges();
            
            // We scroll slightly so that the ListView's header/footer is at the
            // same Y position as our normal header/footer
            setHeaderScroll(scrollToY);
            
            // Make sure the ListView is scrolled to show the loading
            // header/footer
            mRefreshableView.setSelection(selection);
            
            // Smooth scroll as normal
            smoothScrollTo(0);
        }
    }
    
    @Override
    protected void onReset()
    {
        /**
         * If the extras are not enabled, just call up to super and return.
         */
        if (!mListViewExtrasEnabled)
        {
            super.onReset();
            return;
        }
        
        final LoadingLayout originalLoadingLayout, listViewLoadingLayout;
        final int scrollToHeight, selection;
        final boolean scrollLvToEdge;
        
        switch (getCurrentMode())
        {
            case MANUAL_REFRESH_ONLY:
            case PULL_FROM_END:
                originalLoadingLayout = getFooterLayout();
                listViewLoadingLayout = mFooterLoadingView;
                selection = mRefreshableView.getCount() - 1;
                scrollToHeight = getFooterSize();
                scrollLvToEdge = Math.abs(mRefreshableView.getLastVisiblePosition()
                        - selection) <= 1;
                break;
            case PULL_FROM_START:
            default:
                originalLoadingLayout = getHeaderLayout();
                listViewLoadingLayout = mHeaderLoadingView;
                scrollToHeight = -getHeaderSize();
                selection = 0;
                scrollLvToEdge = Math.abs(mRefreshableView.getFirstVisiblePosition()
                        - selection) <= 1;
                break;
        }
        
        // If the ListView header loading layout is showing, then we need to
        // flip so that the original one is showing instead
        if (listViewLoadingLayout.getVisibility() == View.VISIBLE)
        {
            
            // Set our Original View to Visible
            originalLoadingLayout.showInvisibleViews();
            
            // Hide the ListView Header/Footer
            listViewLoadingLayout.setVisibility(View.GONE);
            
            if (DEBUG)
            {
                Log.d(LOG_TAG, "scrollLvToEdge: " + scrollLvToEdge
                        + " getState: " + getState());
            }
            /**
             * Scroll so the View is at the same Y as the ListView
             * header/footer, but only scroll if: we've pulled to refresh, it's
             * positioned correctly
             */
            if (scrollLvToEdge && getState() != State.MANUAL_REFRESHING)
            {
                mRefreshableView.setSelection(selection);
                setHeaderScroll(scrollToHeight);
            }
        }
        
        // Finally, call up to super
        super.onReset();
    }
    
    @Override
    protected LoadingLayoutProxy createLoadingLayoutProxy(
            final boolean includeStart, final boolean includeEnd)
    {
        LoadingLayoutProxy proxy = super.createLoadingLayoutProxy(includeStart,
                includeEnd);
        
        if (mListViewExtrasEnabled)
        {
            final Mode mode = getMode();
            
            if (includeStart && mode.showHeaderLoadingLayout())
            {
                proxy.addLayout(mHeaderLoadingView);
            }
            if (includeEnd && mode.showFooterLoadingLayout())
            {
                proxy.addLayout(mFooterLoadingView);
            }
        }
        
        return proxy;
    }
    
    protected ListView createListView(Context context, AttributeSet attrs)
    {
        final ListView lv;
        if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD)
        {
            lv = new InternalListViewSDK9(context, attrs);
        }
        else
        {
            lv = new InternalListView(context, attrs);
        }
        return lv;
    }
    
    @Override
    protected ListView createRefreshableView(Context context, AttributeSet attrs)
    {
        ListView lv = createListView(context, attrs);
        
        // Set it to this so it can be used in ListActivity/ListFragment
        lv.setId(android.R.id.list);
        return lv;
    }
    
    public ListView getListView()
    {
        return mRefreshableView;
    }
    
    @TargetApi(Build.VERSION_CODES.FROYO)
    @Override
    protected void handleStyledAttributes(TypedArray a)
    {
        super.handleStyledAttributes(a);
        
        mListViewExtrasEnabled = a.getBoolean(R.styleable.PullToRefresh_ptrListViewExtrasEnabled,
                true);
        
        if (mListViewExtrasEnabled)
        {
            final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER_HORIZONTAL);
            
            // Create Loading Views ready for use later
            FrameLayout frame = new FrameLayout(getContext());
            mHeaderLoadingView = createLoadingLayout(getContext(),
                    Mode.PULL_FROM_START,
                    a);
            mHeaderLoadingView.setVisibility(View.GONE);
            frame.addView(mHeaderLoadingView, lp);
            mRefreshableView.addHeaderView(frame, null, false);
            
            mLvFooterLoadingFrame = new FrameLayout(getContext());
            mFooterLoadingView = createLoadingLayout(getContext(),
                    Mode.PULL_FROM_END,
                    a);
            mFooterLoadingView.setVisibility(View.GONE);
            mLvFooterLoadingFrame.addView(mFooterLoadingView, lp);
            
            /**
             * If the value for Scrolling While Refreshing hasn't been
             * explicitly set via XML, enable Scrolling While Refreshing.
             */
            if (!a.hasValue(R.styleable.PullToRefresh_ptrScrollingWhileRefreshingEnabled))
            {
                setScrollingWhileRefreshingEnabled(true);
            }
        }
    }
    
    @TargetApi(9)
    final class InternalListViewSDK9 extends InternalListView
    {
        
        public InternalListViewSDK9(Context context, AttributeSet attrs)
        {
            super(context, attrs);
        }
        
        @Override
        protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
                int scrollY, int scrollRangeX, int scrollRangeY,
                int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent)
        {
            
            final boolean returnValue = super.overScrollBy(deltaX,
                    deltaY,
                    scrollX,
                    scrollY,
                    scrollRangeX,
                    scrollRangeY,
                    maxOverScrollX,
                    maxOverScrollY,
                    isTouchEvent);
            
            // Does all of the hard work...
            OverscrollHelper.overScrollBy(PullToRefreshListView.this,
                    deltaX,
                    scrollX,
                    deltaY,
                    scrollY,
                    isTouchEvent);
            
            return returnValue;
        }
    }
    
    protected class InternalListView extends ListView implements
            EmptyViewMethodAccessor
    {
        
        // private String TAG = InternalListView.class.getSimpleName();
        
        private boolean mAddedLvFooter = false;
        
        public InternalListView(Context context, AttributeSet attrs)
        {
            super(context, attrs);
        }
        
        @Override
        protected void dispatchDraw(Canvas canvas)
        {
            /**
             * This is a bit hacky, but Samsung's ListView has got a bug in it
             * when using Header/Footer Views and the list is empty. This masks
             * the issue so that it doesn't cause an FC. See Issue #66.
             */
            try
            {
                super.dispatchDraw(canvas);
            }
            catch (IndexOutOfBoundsException e)
            {
                e.printStackTrace();
            }
        }
        
        @Override
        public boolean dispatchTouchEvent(MotionEvent ev)
        {
            /**
             * This is a bit hacky, but Samsung's ListView has got a bug in it
             * when using Header/Footer Views and the list is empty. This masks
             * the issue so that it doesn't cause an FC. See Issue #66.
             */
            try
            {
                return super.dispatchTouchEvent(ev);
            }
            catch (IndexOutOfBoundsException e)
            {
                e.printStackTrace();
                return false;
            }
        }
        
        @Override
        public void setAdapter(ListAdapter adapter)
        {
            // Add the Footer View at the last possible moment
            if (null != mLvFooterLoadingFrame && !mAddedLvFooter)
            {
                addFooterView(mLvFooterLoadingFrame, null, false);
                mAddedLvFooter = true;
            }
            
            super.setAdapter(adapter);
        }
        
        @Override
        public void setEmptyView(View emptyView)
        {
            PullToRefreshListView.this.setEmptyView(emptyView);
        }
        
        @Override
        public void setEmptyViewInternal(View emptyView)
        {
            super.setEmptyView(emptyView);
        }
        
        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt)
        {
            // TODO Auto-generated method stub
            // Log.d(TAG, "l: "+l +" t:"+t +" oldl: "+oldl+" oldt:"+oldt);
            super.onScrollChanged(l, t, oldl, oldt);
        }
        
        private static final int MAX_SCROLLY = 250;
        
        private float startY, currentY;
        
        @Override
        public boolean onTouchEvent(MotionEvent ev)
        {
            int action = ev.getAction();
            
            switch (action)
            {
                case MotionEvent.ACTION_DOWN:
                    startY = ev.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    currentY = ev.getY();
                    int offsetY = (int) (currentY - startY);
                    // Log.d(TAG, "InternalListView scroll offsetY: "+offsetY);
                    if (offsetY <= -MAX_SCROLLY)
                    {
                        if (mOnMovingListener != null)
                        {
                            mOnMovingListener.onMovingDownQuick();
                        }
                    }
                    if (offsetY >= MAX_SCROLLY)
                    {
                        if (mOnMovingListener != null)
                        {
                            mOnMovingListener.onMovingUpQuick();
                        }
                    }
                    break;
                
                default:
                    break;
            }
            return super.onTouchEvent(ev);
        }
        
        // private float mLastMotionX;
        
        // /**
        // * Touch事件是否在当前控件进行拦截
        // */
        // @Override
        // public boolean onInterceptTouchEvent(MotionEvent ev)
        // {
        // // TODO Auto-generated method stub
        //
        // if (ev.getAction() == MotionEvent.ACTION_DOWN)
        // {
        // mLastMotionX = ev.getX();
        // return super.onInterceptTouchEvent(ev);
        // }
        // else if (ev.getAction() == MotionEvent.ACTION_MOVE)
        // {
        // final float x = ev.getX();
        // final int deltaX = (int) (mLastMotionX - x);
        // if (Math.abs(deltaX) > 20)
        // { //左右滑动事件不拦截，会交给子View处理
        // return false;
        // }
        // else
        // {
        // return super.onInterceptTouchEvent(ev);
        // }
        // }
        // else
        // {
        // return super.onInterceptTouchEvent(ev);
        // }
        // }
        
        public String getActionStr(int action)
        {
            String actionStr = "";
            switch (action)
            {
                case MotionEvent.ACTION_DOWN:
                    actionStr = "ACTION_DOWN";
                    break;
                case MotionEvent.ACTION_UP:
                    actionStr = "ACTION_UP";
                    break;
                case MotionEvent.ACTION_MOVE:
                    actionStr = "ACTION_MOVE";
                    break;
                case MotionEvent.ACTION_CANCEL:
                    actionStr = "ACTION_CANCEL";
                    break;
                
                default:
                    actionStr = "other";
                    break;
            }
            return actionStr;
        }
        
    }
    
    /**
     * add move listener
     */
    
    public interface OnMovingListener
    {
        public void onMovingUpQuick();
        
        public void onMovingDownQuick();
    }
    
    private OnMovingListener mOnMovingListener;
    
    public void setOnMovingListener(OnMovingListener l)
    {
        mOnMovingListener = l;
    }
    
    /**
     * add header method 只有当PULL_FROM_START 不可用时 该方法才有效
     */
    public void addHeaderView(View child)
    {
        final Mode mode = getMode();
        /**
         * can remove the condition
         */
        if (!mode.showHeaderLoadingLayout())
        {
            mRefreshableView.addHeaderView(child);
        }
    }
    
    public void setDividerHeight(int height)
    {
        mRefreshableView.setDividerHeight(height);
    }
    
}
