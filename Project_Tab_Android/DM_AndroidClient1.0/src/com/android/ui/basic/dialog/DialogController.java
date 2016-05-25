/*
 * 文件名: DialogController.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: DialogController
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.android.R;

/**
 * DialogController<BR>
 * @author zhangwenhai
 * @version [Paitao Client V20130911, 2013-9-23] 
 */
public class DialogController
{
    /**
     * DialogInterface
     */
    private final DialogInterface mDialogInterface;
    
    /**
     * 窗体
     */
    private final Window mWindow;
    
    /**
     * 标题
     */
    private CharSequence mTitle;
    
    /**
     * 标题视图
     */
    private TextView mTitleView;
    
    /**
     * 消息体
     */
    private CharSequence mMgeText;
    
    /**
     * 消息体视图
     */
    private TextView mMessageView;
    
    /**
     * 左按钮文字
     */
    private CharSequence mPositiveButtonText;
    
    /**
     * 左按钮
     */
    private Button mPositiveButton;
    
    /**
     * 中立按钮文字
     */
    private CharSequence mNeutralButtonText;
    
    /**
     * 中立按钮
     */
    private Button mNeutralButton;
    
    /**
     * 右按钮文字
     */
    private CharSequence mNegativeButtonText;
    
    /**
     * 右按钮
     */
    private Button mNegativeButton;
    
    /**
     * 自定义视图
     */
    private View mCustomContentView;
    
    /**
     * 列表式dialog的listview
     */
    private ListView mListView;
    
    /**
     * 列表式dialog的listview的adapter
     */
    private ListAdapter mAdapter;
    
    /**
     * listview被选中的item
     */
    private int mCheckedItem = -1;
    
    /**
     * 带有checkbox的dialog的checkbox布局数组
     */
    private View[] mCheckBoxViews;
    
    /**
     * 左按钮监听
     */
    private DialogInterface.OnClickListener mPositiveButtonListener;
    
    /**
     * 中立按钮监听
     */
    private DialogInterface.OnClickListener mNeutralButtonListener;
    
    /**
     * 右按钮监听
     */
    private DialogInterface.OnClickListener mNegativeButtonListener;
    
    /**
     * [构造简要说明]
     * @param context context
     * @param di DialogInterface
     * @param window window
     */
    public DialogController(Context context, DialogInterface di, Window window)
    {
        mDialogInterface = di;
        mWindow = window;
    }
    
    /**
     * 绘制界面<BR>
     */
    public void installContent()
    {
        mWindow.setContentView(R.layout.dialog_basic);
        setUpView();
    }
    
    private void setUpView()
    {
        //title面板
        LinearLayout topPanel = (LinearLayout) mWindow.findViewById(R.id.topPanel);
        setUpTitle(topPanel);
        
        //button面板
        LinearLayout bottomPanel = (LinearLayout) mWindow.findViewById(R.id.bottomPanel);
        setUpButton(bottomPanel);
        
        //内容面板
        LinearLayout contentPanel = (LinearLayout) mWindow.findViewById(R.id.contentPanel);
        setUpContent(contentPanel);
    }
    
    private void setUpTitle(LinearLayout topPanel)
    {
        //有标题
        if (null != mTitle && !"".equals(mTitle))
        {
            topPanel.setVisibility(View.VISIBLE);
            // 设置标题
            mTitleView = (TextView) topPanel.findViewById(R.id.title);
            mTitleView.setText(mTitle);
        }
    }
    
    private void setUpButton(LinearLayout bottomPanel)
    {
        // 设置确定按钮
        if (null != mPositiveButtonText)
        {
            //按钮父类布局可见
            bottomPanel.setVisibility(View.VISIBLE);
            mPositiveButton = (Button) bottomPanel.findViewById(R.id.positiveButton);
            //确定按钮可见
            mPositiveButton.setVisibility(View.VISIBLE);
            mPositiveButton.setText(mPositiveButtonText);
            mPositiveButton.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    if (mPositiveButtonListener != null)
                    {
                        mPositiveButtonListener.onClick(mDialogInterface,
                                DialogInterface.BUTTON_POSITIVE);
                    }
                    mDialogInterface.dismiss();
                }
            });
        }
        
        //设置中立按钮
        if (null != mNeutralButtonText)
        {
            bottomPanel.setVisibility(View.VISIBLE);
            mNeutralButton = (Button) bottomPanel.findViewById(R.id.neutralButton);
            mNeutralButton.setVisibility(View.VISIBLE);
            mNeutralButton.setText(mNeutralButtonText);
            mNeutralButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (mNeutralButtonListener != null)
                    {
                        mNeutralButtonListener.onClick(mDialogInterface,
                                DialogInterface.BUTTON_NEUTRAL);
                    }
                    mDialogInterface.dismiss();
                }
            });
        }
        
        // 设置取消按鈕
        if (null != mNegativeButtonText)
        {
            //按钮父类布局可见
            bottomPanel.setVisibility(View.VISIBLE);
            mNegativeButton = (Button) bottomPanel.findViewById(R.id.negativeButton);
            //右按钮可见性
            mNegativeButton.setVisibility(View.VISIBLE);
            mNegativeButton.setText(mNegativeButtonText);
            mNegativeButton.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    if (mNegativeButtonListener != null)
                    {
                        mNegativeButtonListener.onClick(mDialogInterface,
                                DialogInterface.BUTTON_NEGATIVE);
                    }
                    mDialogInterface.dismiss();
                }
            });
            
        }
        
        //根据设置的按钮显示需要的分割线
        if (null != mPositiveButtonText && null != mNeutralButtonText
                && null != mNegativeButtonText)
        {
            bottomPanel.findViewById(R.id.dialog_button_divider_1)
                    .setVisibility(View.VISIBLE);
            bottomPanel.findViewById(R.id.dialog_button_divider_2)
                    .setVisibility(View.VISIBLE);
        }
        else if ((null != mPositiveButtonText && null != mNegativeButtonText)
                || (null != mPositiveButtonText && null != mNeutralButtonText))
        {
            bottomPanel.findViewById(R.id.dialog_button_divider_1)
                    .setVisibility(View.VISIBLE);
        }
        else if (null != mNeutralButtonText && null != mNegativeButtonText)
        {
            bottomPanel.findViewById(R.id.dialog_button_divider_2)
                    .setVisibility(View.VISIBLE);
        }
    }
    
    private void setUpContent(LinearLayout contentPanel)
    {
        //scrollview 当内容长度过长时，用来进行滚动
        ScrollView scrollView = (ScrollView) contentPanel.findViewById(R.id.scrollView);
        
        //如果消息体没有 设置为不可见
        if (null != mMgeText)
        {
            mMessageView = (TextView) scrollView.findViewById(R.id.message_text);
            mMessageView.setVisibility(View.VISIBLE);
            mMessageView.setText(mMgeText);
        }
        
        //如果有自定义布局，为可见，将自定义布局放在scrollView中，当内容长度过长时，可以进行滚动
        //故自定义布局是listview,listview与scrollView结合会有问题，不放在scrollview中
        if (null != mCustomContentView)
        {
            if (mCustomContentView instanceof ListView)
            {
                mListView = (ListView) mCustomContentView;
            }
            else
            {
                //设置自定义的布局，去除其中的用来显示消息的视图
                contentPanel.removeAllViews();
                contentPanel.addView(mCustomContentView, new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            }
        }
        
        //ListView不为空，是list类型的dialog，需要将scrollView移除直接放在contentPanel中
        if (null != mListView)
        {
            if (null != mAdapter)
            {
                mListView.setAdapter(mAdapter);
            }
            if (mCheckedItem > -1)
            {
                mListView.setItemChecked(mCheckedItem, true);
            }
            //将listview放在contentPanel中，去除其中的scrollview布局
            contentPanel.removeAllViews();
            contentPanel.addView(mListView, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
        //带有checkbox的
        if (null != mCheckBoxViews)
        {
            for (View view : mCheckBoxViews)
            {
                //直接加在contentPanel中，不去除其中用于显示消息的视图，
                //因为checkbox类的dialog一般需要显示提示消息
                contentPanel.addView(view, new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            }
        }
    }
    
    /**
     * 设置消息体
     * @param message message
     */
    public void setMessage(CharSequence message)
    {
        mMgeText = message;
    }
    
    /**
     * 设置标题
     * @param title title
     */
    public void setTitle(CharSequence title)
    {
        mTitle = title;
        if (mTitleView != null)
        {
            mTitleView.setText(title);
        }
    }
    
    /**
     * 设置自定义布局
     * @param v View
     */
    public void setContentView(View v)
    {
        mCustomContentView = v;
    }
    
    /**
     * 设置确定按钮<BR>
     * @param positiveButtonText positiveButtonText
     * @param listener listener
     */
    public void setPositiveButton(CharSequence positiveButtonText,
            DialogInterface.OnClickListener listener)
    {
        mPositiveButtonText = positiveButtonText;
        mPositiveButtonListener = listener;
    }
    
    /**
     * 设置中立按钮<BR>
     * @param neutralButtonText CharSequence
     * @param listener listener
     */
    public void setNeutralButton(CharSequence neutralButtonText,
            DialogInterface.OnClickListener listener)
    {
        mNeutralButtonText = neutralButtonText;
        mNeutralButtonListener = listener;
    }
    
    /**
     * 设置取消按钮
     * @param negativeButtonText negativeButtonText
     * @param listener listener
     */
    public void setNegativeButton(CharSequence negativeButtonText,
            DialogInterface.OnClickListener listener)
    {
        mNegativeButtonText = negativeButtonText;
        mNegativeButtonListener = listener;
    }
    
    /**
     * 设置button<BR>
     * @param whichButton 
     *               DialogInterface.BUTTON_POSITIVE 左按钮
     *               DialogInterface.BUTTON_NEGATIVE 右按钮
     * @param text  button上显示的文字
     * @param listener  button监听
     */
    public void setButton(int whichButton, CharSequence text,
            DialogInterface.OnClickListener listener)
    {
        switch (whichButton)
        {
            case DialogInterface.BUTTON_POSITIVE:
                setPositiveButton(text, listener);
                break;
            
            case DialogInterface.BUTTON_NEGATIVE:
                setNegativeButton(text, listener);
                break;
            default:
                throw new IllegalArgumentException("Button does not exist");
        }
    }
    
    /**
     * dialog中的listview<BR>
     * @return dialog中的listview
     */
    public ListView getListView()
    {
        return mListView;
    }
    
    /**
     * 获取dialog上的button<BR>
     * @param whichButton 
     *               DialogInterface.BUTTON_POSITIVE 左按钮
     *               DialogInterface.BUTTON_NEGATIVE 右按钮
     * @return button
     */
    public Button getButton(int whichButton)
    {
        switch (whichButton)
        {
        //左按钮
            case DialogInterface.BUTTON_POSITIVE:
                return mPositiveButton;
                //左按钮
            case DialogInterface.BUTTON_NEUTRAL:
                return mNeutralButton;
                //右按钮
            case DialogInterface.BUTTON_NEGATIVE:
                return mNegativeButton;
            default:
                return null;
        }
    }
    
    /**
     * 获取自定义视图<BR>
     * @return 自定义视图 
     */
    public View getContentView()
    {
        return mCustomContentView;
    }
    
    /**
     * dialog参数类<BR>
     * @author zhangwenhai
     * @version [Paitao Client V20130911, 2013-9-23]
     */
    public static class DialogParams
    {
        /**
         * Context
         */
        public final Context mContext;
        
        /**
         * LayoutInflater
         */
        public final LayoutInflater mInflater;
        
        /**
         * 标题
         */
        public CharSequence mTitle;
        
        /**
         * 消息体
         */
        public CharSequence mMgeText;
        
        /**
         * 确定按钮文字
         */
        public String mPositiveButtonText;
        
        /**
         * 中立按钮文字
         */
        public String mNeutralButtonText;
        
        /**
         * 取消按钮文字
         */
        public String mNegativeButtonText;
        
        /**
         * 自定义布局view
         */
        public View mContentView;
        
        /**
         * 自定义布局resId
         */
        public int mContentViewResId = -1;
        
        /**
         * 列表条目数组
         */
        public CharSequence[] mItems;
        
        /**
         * 单选列表数组默认选择的条目
         */
        public int mCheckedItem = -1;
        
        /**
         * 多选列表数组默认选择的条目
         */
        public boolean[] mCheckedItems;
        
        /**
         * checkbox消息数组
         */
        public String[] mCheckMsg;
        
        /**
         * checkBox初始状态
         */
        public boolean[] mDefaultCheckState;
        
        /**
         * list的adapter
         */
        public ListAdapter mAdapter;
        
        /**
         * 是否多选模式
         */
        public boolean mIsMultiChoice = false;
        
        /**
         * 是否单选模式
         */
        public boolean mIsSingleChoice = false;
        
        /**
         * 列表按钮
         */
        public DialogInterface.OnClickListener mOnClickListener;
        
        /**
         * 确定按钮监听
         */
        public DialogInterface.OnClickListener mPositiveButtonListener;
        
        /**
         * 中立按钮监听
         */
        public DialogInterface.OnClickListener mNeutralButtonListener;
        
        /**
         * 取消按钮监听
         */
        public DialogInterface.OnClickListener mNegativeButtonListener;
        
        /**
         * 列表的选中监听
         */
        public DialogInterface.OnMultiChoiceClickListener mOnListCheckedListener;
        
        /**
         * checkbox监听
         */
        public BasicDialog.OnCheckStateChangeListener mCheckBoxStateChangeListner;
        
        /**
         * 构造器
         * @param context context
         */
        public DialogParams(Context context)
        {
            mContext = context;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        
        /**
         * 设置DialogController<BR>
         * @param dialog DialogController
         */
        public void apply(DialogController dialog)
        {
            if (null != mTitle && !"".equals(mTitle))
            {
                dialog.setTitle(mTitle);
            }
            // 设置确定按钮
            if (null != mPositiveButtonText)
            {
                dialog.setPositiveButton(mPositiveButtonText,
                        mPositiveButtonListener);
            }
            
            // 设置中立按钮
            if (null != mNeutralButtonText)
            {
                
                dialog.setNeutralButton(mNeutralButtonText,
                        mNeutralButtonListener);
            }
            
            // 设置取消按鈕
            if (null != mNegativeButtonText)
            {
                dialog.setNegativeButton(mNegativeButtonText,
                        mNegativeButtonListener);
            }
            
            //如果消息体没有 设置为不可见
            if (null != mMgeText)
            {
                dialog.setMessage(mMgeText);
            }
            
            //设置的是布局资源文件，先inflate得到view
            if (mContentViewResId >= 0x01000000)
            {
                mContentView = mInflater.inflate(mContentViewResId, null);
            }
            //设置了自定义视图或自定义视图的resId
            if (null != mContentView)
            {
                dialog.setContentView(mContentView);
            }
            
            //如果自定义布局为ListView没有 设置为不可见
            if (null != mItems || null != mAdapter)
            {
                createListView(dialog);
            }
            //带有checkbox的
            if (null != mCheckMsg)
            {
                createCheckboxView(dialog);
            }
        }
        
        //根据属性创建相应的listview
        private void createListView(final DialogController dialog)
        {
            final ListView listView = (ListView) mInflater.inflate(R.layout.dialog_listview,
                    null);
            ListAdapter adapter;
            if (mIsMultiChoice)
            {
                adapter = new ArrayAdapter<CharSequence>(mContext,
                        R.layout.dialog_simple_multi_choice, mItems)
                {
                    @Override
                    public View getView(int position, View convertView,
                            ViewGroup parent)
                    {
                        View view = super.getView(position, convertView, parent);
                        if (null != mCheckedItems)
                        {
                            boolean isItemChecked = mCheckedItems[position];
                            if (isItemChecked)
                            {
                                listView.setItemChecked(position, true);
                            }
                        }
                        return view;
                    }
                };
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            }
            //带有单选按钮的ListView或普通listview
            else
            {
                int layout = mIsSingleChoice ? R.layout.dialog_simple_single_choice
                        : R.layout.dialog_simple_item;
                adapter = (null != mAdapter) ? mAdapter
                        : new ArrayAdapter<CharSequence>(mContext, layout,
                                mItems);
                //设置默认选择的单选框
                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            }
            
            dialog.mAdapter = adapter;
            dialog.mCheckedItem = mCheckedItem;
            
            //设置listview OnItemClick点击事件
            if (null != mOnClickListener)
            {
                listView.setOnItemClickListener(new OnItemClickListener()
                {
                    @SuppressWarnings("rawtypes")
                    public void onItemClick(AdapterView parent, View v,
                            int position, long id)
                    {
                        mOnClickListener.onClick(dialog.mDialogInterface,
                                position);
                    }
                });
            }
            else if (null != mOnListCheckedListener)
            {
                listView.setOnItemClickListener(new OnItemClickListener()
                {
                    @SuppressWarnings("rawtypes")
                    public void onItemClick(AdapterView parent, View v,
                            int position, long id)
                    {
                        if (null != mCheckedItems)
                        {
                            mCheckedItems[position] = listView.isItemChecked(position);
                        }
                        mOnListCheckedListener.onClick(dialog.mDialogInterface,
                                position,
                                listView.isItemChecked(position));
                    }
                });
            }
            dialog.mListView = listView;
        }
        
        private void createCheckboxView(final DialogController dialog)
        {
            View[] checkboxs = new View[mCheckMsg.length];
            for (int i = 0; i < mCheckMsg.length; i++)
            {
                View checkBoxItem = mInflater.inflate(R.layout.dialog_check_box,
                        null);
                CheckBox cb = (CheckBox) checkBoxItem.findViewById(R.id.dialog_checkbox);
                cb.setText(mCheckMsg[i]);
                //设置checkbox初始状态
                if (null != mDefaultCheckState)
                {
                    cb.setChecked(mDefaultCheckState[i]);
                }
                final int which = i;
                cb.setOnCheckedChangeListener(new OnCheckedChangeListener()
                {
                    
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                            boolean isChecked)
                    {
                        if (null != mCheckBoxStateChangeListner)
                        {
                            mCheckBoxStateChangeListner.onCheckedChanged(which,
                                    isChecked);
                        }
                    }
                });
                checkboxs[i] = checkBoxItem;
            }
            dialog.mCheckBoxViews = checkboxs;
        }
    }
}
