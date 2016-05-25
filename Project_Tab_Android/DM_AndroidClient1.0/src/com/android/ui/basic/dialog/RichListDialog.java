/*
 * 文件名: RichListDialog.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 支持给每个list条目指定主标题(必选),icon和副标题的“列表型dialog”
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.ui.basic.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.*;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.R;

/**
 * 支持给每个list条目指定主标题(必选),icon和副标题的“列表型dialog”<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public class RichListDialog
{
    /**
     * 上下文
     */
    private Context mContext;
    
    /**
     * richlistdialog实例
     */
    private BasicDialog mDialog = null;

    /**
     * dialog的标题
     */
    private CharSequence mTitle;

    /**
     * list条目的icon
     */
    private int[] mIcons;

    /**
     * list条目的主标题
     */
    private CharSequence[] mItemTitles;

    /**
     * list条目的副标题
     */
    private CharSequence[] mItemSubTitles;

    /**
     * 条目的可用属性
     */
    private boolean[] mEnable;

    private BounceMode mBounceMode;

    /**
     * listview的点击监听
     */
    private DialogInterface.OnClickListener mOnClickListener;

    /**
     * 构造函数
     * 创建一个basicdialog实例
     * @param context context
     */
    public RichListDialog(Context context)
    {
        mContext = context;
    }

    /**
     * 封装BasicDialog.Builder的创建方法<BR>
     * @return RichListDialog实例
     */
    public RichListDialog create()
    {
        final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //listview的adapter
        BaseAdapter adapter = new BaseAdapter()
        {
            @Override
            public int getCount()
            {
                return mItemTitles.length;
            }

            @Override
            public Object getItem(int position)
            {
                return mItemTitles[position];
            }

            @Override
            public long getItemId(int position)
            {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                convertView = inflater.inflate(R.layout.dialog_list_item, null);
                //主标题，必须有主标题
                TextView tv = (TextView) convertView.findViewById(R.id.dialog_item_text);
                tv.setText(mItemTitles[position]);
                if (null != mEnable)
                {
                    tv.setEnabled(mEnable[position]);
                }
                //图标
                if (null != mIcons)
                {
                    ImageView icon = (ImageView) convertView.findViewById(R.id.dialog_item_icon);
                    icon.setVisibility(View.VISIBLE);
                    icon.setImageResource(mIcons[position]);
                }
                //副标题
                if (null != mItemSubTitles)
                {
                    TextView subTv = (TextView) convertView.findViewById(R.id.dialog_item_sub_text);
                    subTv.setVisibility(View.VISIBLE);
                    subTv.setText(mItemSubTitles[position]);
                    if (null != mEnable)
                    {
                        subTv.setEnabled(mEnable[position]);
                    }
                }
                return convertView;
            }

            @Override
            public boolean isEnabled(int position)
            {
                if (null != mEnable)
                {
                    return mEnable[position];
                }
                return super.isEnabled(position);
            }
        };
        BasicDialog.Builder builder = new BasicDialog.Builder(mContext);
        mDialog = builder.setTitle(mTitle)
                .setAdapter(adapter, mOnClickListener)
                .create();
        
        if (BounceMode.TOP == mBounceMode)
        {
            Window window = mDialog.getWindow();
            window.setGravity(Gravity.TOP);
            window.setWindowAnimations(R.style.dialog_anima_from_top_style);
        }
        else if (BounceMode.BOTTOM == mBounceMode)
        {
            Window window = mDialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.dialog_anima_from_bottom_style);
        }
        return this;
    }
    
    /**
     * 设置每个条目的标题，icon(如果有的话)，副标题(如果有的话)<BR>
     * @param itemTitles 每个条目的标题
     * @param icons 每个条目的icon
     * @param itemSubTitles 每个条目的副标题
     * @param onClickListener listview的点击监听
     * @return RichListDialog实例
     */
    public RichListDialog setItems(CharSequence[] itemTitles, int[] icons,
            CharSequence[] itemSubTitles,
            DialogInterface.OnClickListener onClickListener)
    {
        mItemTitles = itemTitles;
        mIcons = icons;
        mItemSubTitles = itemSubTitles;
        mOnClickListener = onClickListener;
        return this;
    }
    
    /**
     * 设置dialog标题<BR>
     * @param title 标题资源文件
     * @return RichListDialog实例
     */
    public RichListDialog setTitle(CharSequence title)
    {
        mTitle = title;
        return this;
    }
    
    /**
     * 设置条目有效性<BR>
     * @param enable 有效性
     * @return RichListDialog实例
     */
    public RichListDialog setItemEnable(boolean[] enable)
    {
        mEnable = enable;
        return this;
    }
    
    /**
     * 
     * 设置对话框的弹起模式<BR>
     * @param bounceMode BounceMode类型
     * @return 返回RichListDialog实例
     */
    public RichListDialog setBounceMode(BounceMode bounceMode)
    {
        mBounceMode = bounceMode;
        return this;
    }
    
    /**
     * showdialog<BR>
     */
    public void show()
    {
        mDialog.show();
    }
    
    /**
     * 关闭dialog<BR>
     */
    public void dismiss()
    {
        mDialog.dismiss();
    }
    
    /**
     * 
     * 显示指定宽高的窗口<BR>
     * @param width 窗口width 
     * @param height 窗口高度
     */
    public void showSpecifiedSize(int width, int height)
    {
        
        mDialog.show();
        
        //设置窗口大小,一定要在show后面执行 不然不起作用
        
        Window window = mDialog.getWindow();
        
        //重新设置内容面板的宽度，替代默认宽度256dp
        LinearLayout contentPanel = (LinearLayout) window.findViewById(R.id.contentPanel);
        LayoutParams vlp = contentPanel.getLayoutParams();
        vlp.width = width;
        contentPanel.setLayoutParams(vlp);
        
        //设置窗口的宽度和高度，如已经重新设置了内容面板的宽度的话可不设置
        //        WindowManager.LayoutParams lp = window.getAttributes();
        //        
        //        if (width > 0)
        //        {
        //            lp.width = width;
        //        }
        //        
        //        if (height > 0)
        //        {
        //            lp.height = height;
        //        }
        //        mDialog.getWindow().setAttributes(lp);
        
    }
    
    /**
     * dialog是否在显示<BR>
     * @return true 在显示 ；false 不在显示
     */
    public boolean isShowing()
    {
        return mDialog.isShowing();
    }
    
    /**
     * 
     * Dialog的弹起模式<BR>
     * @author chunjiang.shieh <chunjiang.shieh@gmail.com>
     * @version [Paitao Client V20130911, 2013-10-23]
     */
    public enum BounceMode
    {
        /**
         * 顶部弹起
         */
        TOP,
        /**
         * 中间弹起
         */
        CENTER,
        
        /**
         * 底部弹起
         */
        BOTTOM,
    }
}
