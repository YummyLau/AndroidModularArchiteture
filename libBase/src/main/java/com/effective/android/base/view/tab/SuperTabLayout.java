package com.effective.android.base.view.tab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.effective.android.base.R;
import com.effective.android.base.util.DisplayUtils;
import com.effective.android.base.util.ResourceUtils;
import com.effective.android.base.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 可自定义指示器： 下划线类型的或者是丢一张图片做指示器或是设IndicatorDrawer, 优先级：IndicatorDrawer, mIndicatorResId, 下划线<br/>
 * 　- a.下划线类型:<br/>
 * 　　　1. tabPadding(作用是改变tab文字之间的间距)和indicatorPadding(indicator相对于tab的padding)配合, 当两值一样时就是指示器和文字一样宽.<br/>
 * 　　　2. 不指定indicatorWidth时, 不同宽度的tab的indicator长度不同, 在不同宽度的tab之间滑动时indicator的长度逐渐变成下一个tab的宽度.<br/>
 * 　- b.图片类型:<br/>
 * 　　　indicatorImg指定图片id<br/>
 * 　- c.自己画:<br/>
 * 　　　传入实现了IndicatorDrawer的对象, 会在下划线类型的基础上计算出下划线的位置, 再按IndicatorDrawer.getWidth()帮你给canvas设好offset, 直接画上去就是居中的。<br/>
 * 　　　要实现每个tab的indicator不同大小的话，修改一下IndicatorDrawer的draw方法，额外传一些参数进去再自己怎么画都行。<br/>
 * 可自定义tab的布局:<br/>
 * 　　- 看setCustomView()方法。<br/>
 * 可添加未读标志:<br/>
 * 　　- addSmallDot(只是个小点) addUnreadDot(带数字的大点) dismissRedDot showRedDot<br/>
 * 　　- add时可以传入背景图片id, 或者自己构造好TextView丢进来(全都在这个view用xml的属性声明的话参数太多了...)<br/>
 * 　　- 可以指定redDotMarginXXX和redDotGravity对红点进行定位<br/>
 * 可指定TabMode：<br/>
 * 　　- 类似原生的TabLayout
 * 可仅通过String数组来设置Tab：<br/>
 * 　　- 看setupWithTexts()方法。<br/>
 * 可设置点击事件：<br/>
 * 　　- OnTabClickListener类：点击事件<br/>
 * 　　- OnTabSelectedListener类： 类似原生TabLayout里面的<br/>
 * tab的左右边距(Padding和Margin)：<br/>
 * 　　- tabPadding属性, 是设tab里面的TextView的左右padding<br/>
 * 　　- ctl_tabMargin属性，是设tab里面TextView的左右margin, 和padding不同的是, margin是不设置第一个tab的左边和最后一个tab的右边的<br/>
 * 支持设置选中的tab的文本大小:
 * 　　- tabTextSizeSelected 设置选择文本大小。
 * 　　- textSizeTransition 是否在滑动viewpager时文本大小做过渡变化
 */
public class SuperTabLayout extends HorizontalScrollView{
    private static final String TAG = "CustomTabLayout2";

    public static final int TABMODE_SCROLLABLE = 0;
    public static final int TABMODE_FIX = 1;

    private Typeface mTypefaceLight = Typeface.create("sans-serif-light", Typeface.NORMAL);
    private Typeface mTypefaceNormal = Typeface.create("sans-serif", Typeface.NORMAL);

    private boolean mEnableTextSizeTransition = false;
    private boolean mTextAlignBottom = false;
    private int mTextSize = DisplayUtils.dip2px(getContext(), 17);   //px
    private int mSelectedTextSize;   //px
    private int mTabPadding = DisplayUtils.dip2px(getContext(), 18);   //px
    private int mTabMargin = 0;         //px
    private int mTabWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
    @ColorInt
    private int mIndicatorColor = Color.WHITE;
    private int mIndicatorResId = -1;//R.drawable.title_tab_indicator;
    private boolean mIsBoldText = true;
    private boolean mIsThinUnselected = false;
    @ColorInt
    private int mSelectedTextColor = Color.parseColor("#FFFFFF");           //不要把这个置空
    @ColorInt
    private int mNormalTextColor = Color.parseColor("#99989ABF");
    private int mIndicatorWidth = -1;      //px
    private int mIndicatorStrokeWidth = DisplayUtils.dip2px(getContext(), 3);      //px
    private int mIndicatorPadding = 0;              //px
    private int mIndicatorBottomMargin = 0;         //px
    private int mRedDotBgResId = R.drawable.base_sh_round_rect_red_10;
    private int mSmallRedDotBgResId = R.drawable.base_sh_round_rect_red_10;
    private int mRedDotGravity = Gravity.TOP | Gravity.END;
    private int mRedDotMarginTop = DisplayUtils.dip2px(getContext(), 11);                       //px
    private int mRedDotMarginBottom = 0;
    private int mRedDotMarginRight = DisplayUtils.dip2px(getContext(), 9);                       //px
    private int mRedDotMarginLeft = 0;
    private int mScrollThreshold = DisplayUtils.dip2px(getContext(), 100);
    private int mTabBackgroundResId = -1;
    private int mTabMode = 0;
    private boolean mShowIndicator = true;

    @ColorRes
    private int mSelectedTextColorResId;
    @ColorRes
    private int mNormalTextColorResId;

    private Paint mIndicatorPaint;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private Bitmap mIndicatorBitmap;
    private IndicatorDrawer mIndicatorDrawer;

    private LinearLayout mTabContainerLayout;
    private int selectedPosition = -1;
    private int mIndicatorImgHeight = 0;
    private int mTranslationX = 0;
    private ArrayList<Tab> mTabs = new ArrayList<>();
    private ArrayList<Integer> mIndicatorLeftList = new ArrayList<>();
    private ArrayList<Integer> mIndicatorWidthList = new ArrayList<>();


    private OnTabSelectedListener mOnTabSelectedListener;
    private OnTabClickListener mCustomClickListener;
    private OnClickListener mOnTabClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getTag() instanceof Integer) {
                int index = (int) v.getTag();
                if (mCustomClickListener != null) {
                    mCustomClickListener.onTabClicked(getTabAt(index));
                }
                if (mOnTabSelectedListener != null && selectedPosition == index) {
                    mOnTabSelectedListener.onTabReselected(getTabAt(index));
                }
                if (mViewPager != null) {
                    mViewPager.setCurrentItem(index);
                }
            }
        }
    };

    public SuperTabLayout(Context context) {
        this(context, null);
    }

    public SuperTabLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperTabLayout(@NonNull Context context, @Nullable AttributeSet attrs,
                          @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SuperTabLayout, defStyleAttr, 0);
        mEnableTextSizeTransition = typedArray.getBoolean(R.styleable.SuperTabLayout_super_tab_textSizeTransition, mEnableTextSizeTransition);
        mTextAlignBottom = typedArray.getBoolean(R.styleable.SuperTabLayout_super_tab_textAlignBottom, mTextAlignBottom);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.SuperTabLayout_super_tab_tabTextSize, mTextSize);
        mSelectedTextSize = typedArray.getDimensionPixelSize(R.styleable.SuperTabLayout_super_tab_tabTextSizeSelected, mTextSize);
        mTabPadding = typedArray.getDimensionPixelSize(R.styleable.SuperTabLayout_super_tab_tabPadding, mTabPadding);
        mTabMargin = typedArray.getDimensionPixelOffset(R.styleable.SuperTabLayout_super_tab_tabMargin, mTabMargin);
        mTabWidth = typedArray.getDimensionPixelSize(R.styleable.SuperTabLayout_super_tab_tabWidth, mTabWidth);
        mIndicatorColor = typedArray.getColor(R.styleable.SuperTabLayout_super_tab_indicatorColor, mIndicatorColor);
        mIndicatorResId = typedArray.getResourceId(R.styleable.SuperTabLayout_super_tab_indicatorImg, mIndicatorResId);
        mIndicatorBottomMargin = typedArray.getDimensionPixelSize(R.styleable.SuperTabLayout_super_tab_indicatorBottomMargin, mIndicatorBottomMargin);
        mIsBoldText = typedArray.getBoolean(R.styleable.SuperTabLayout_super_tab_boldSelected, mIsBoldText);
        mIsThinUnselected = typedArray.getBoolean(R.styleable.SuperTabLayout_super_tab_thinUnselected, mIsThinUnselected);
        mNormalTextColorResId = typedArray.getResourceId(R.styleable.SuperTabLayout_super_tab_tabTextColor, -1);
        mSelectedTextColorResId = typedArray.getResourceId(R.styleable.SuperTabLayout_super_tab_selectTextColor, -1);
        mIndicatorWidth = typedArray.getDimensionPixelSize(R.styleable.SuperTabLayout_super_tab_indicatorWidth, mIndicatorWidth);
        mIndicatorStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.SuperTabLayout_super_tab_indicatorStrokeWidth, mIndicatorStrokeWidth);
        mIndicatorPadding = typedArray.getDimensionPixelSize(R.styleable.SuperTabLayout_super_tab_indicatorPadding, mIndicatorPadding);
        mRedDotBgResId = typedArray.getResourceId(R.styleable.SuperTabLayout_super_tab_redDotBgResId, mRedDotBgResId);
        mSmallRedDotBgResId = typedArray.getResourceId(R.styleable.SuperTabLayout_super_tab_smallDotBgResId, mSmallRedDotBgResId);
        mRedDotMarginLeft = typedArray.getDimensionPixelSize(R.styleable.SuperTabLayout_super_tab_redDotMarginLeft, mRedDotMarginLeft);
        mRedDotMarginTop = typedArray.getDimensionPixelSize(R.styleable.SuperTabLayout_super_tab_redDotMarginTop, mRedDotMarginTop);
        mRedDotMarginRight = typedArray.getDimensionPixelSize(R.styleable.SuperTabLayout_super_tab_redDotMarginRight, mRedDotMarginRight);
        mRedDotMarginBottom = typedArray.getDimensionPixelSize(R.styleable.SuperTabLayout_super_tab_redDotMarginBottom, mRedDotMarginBottom);
        mRedDotGravity = typedArray.getInteger(R.styleable.SuperTabLayout_super_tab_redDotGravity, mRedDotGravity);
        mTabBackgroundResId = typedArray.getResourceId(R.styleable.SuperTabLayout_super_tab_tabBackgroundRes, mTabBackgroundResId);
        mTabMode = typedArray.getInt(R.styleable.SuperTabLayout_super_tab_tabMod, mTabMode);
        mShowIndicator = typedArray.getBoolean(R.styleable.SuperTabLayout_super_tab_showIndicator, mShowIndicator);
        typedArray.recycle();
        updateSkinTextColor(context);
    }

    public int getTextSize() {
        return mTextSize;
    }

    public int getTabPadding() {
        return mTabPadding;
    }

    public int getTabWidth() {
        return mTabWidth;
    }

    public void setTabMode(int tabMode) {
        mTabMode = tabMode;
    }

    @SuppressLint("ResourceType")
    private void updateSkinTextColor(Context context) {
        if (mSelectedTextColorResId > 0) {
            mSelectedTextColor = ResourceUtils.getColor(context, mSelectedTextColorResId);
        }
        if (mNormalTextColorResId > 0) {
            mNormalTextColor = ResourceUtils.getColor(context, mNormalTextColorResId);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setHorizontalScrollBarEnabled(false);
    }

    /**
     * 仅仅用于重置setupWithViewPager之后的状态，其他场景使用需要测试后使用
     *
     * @see SuperTabLayout#setupWithViewPager(ViewPager)
     */
    public void resetForsetupWithViewPager() {
        if (mTabContainerLayout != null) {
            mTabContainerLayout.removeAllViews();
            mTabContainerLayout = null;
        }
        if (this.mViewPager != null && this.mOnPageChangeListener != null) {
            this.mViewPager.removeOnPageChangeListener(this.mOnPageChangeListener);
            this.mViewPager = null;
            this.mOnPageChangeListener = null;
        }
        if (this.mPagerAdapter != null) {
            this.mPagerAdapter = null;
        }
        this.removeAllViews();
        mIndicatorLeftList.clear();
        mTabs.clear();
        mIndicatorWidthList.clear();
        invalidate();
    }

    private void addTabContainerLayout() {
        if (mTabContainerLayout != null) {
            return;
        }

        mTabContainerLayout = new LinearLayout(getContext());
        mTabContainerLayout.setOverScrollMode(OVER_SCROLL_NEVER);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mTabContainerLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(mTabContainerLayout, lp);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public void setupWithViewPager(@NonNull ViewPager viewPager, int tabWidth) {
        if (tabWidth > 0) {
            this.mTabWidth = tabWidth;
        }
        setupWithViewPager(viewPager);
    }

    public void setupWithViewPager(@NonNull ViewPager viewPager) {
        if (mTabContainerLayout == null) {
            addTabContainerLayout();
        } else {
            removeAllTabs();
        }
        addIndicator();
        this.mViewPager = viewPager;
        this.mPagerAdapter = viewPager.getAdapter();

        mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                scroll(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                final int beforePosition = selectedPosition;
                selectTab(position,false);
                if (mOnTabSelectedListener != null) {
                    mOnTabSelectedListener.onTabSelected(getTabAt(position));
                    if (position != beforePosition) {
                        mOnTabSelectedListener.onTabUnselected(getTabAt(beforePosition));
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    if (mEnableTextSizeTransition && mTextSize != mSelectedTextSize) {
                        for (Tab tab : mTabs) {
                            int textSize = tab.isSelected ? mSelectedTextSize : mTextSize;
                            if (tab.textView.getTextSize() != textSize) {
                                tab.textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                            }
                        }
                    }
                }
            }
        };
        this.mViewPager.addOnPageChangeListener(mOnPageChangeListener);

        if (mPagerAdapter != null && mPagerAdapter.getCount() > 0) {        //构造tab
            final int adapterCount = mPagerAdapter.getCount();
            if (mTabMode == TABMODE_FIX) {
                calcTabWidthForFixMode(adapterCount);
            }
            for (int i = 0; i < adapterCount; i++) {
                CharSequence pageTitle = mPagerAdapter.getPageTitle(i);
                TextView textView = genTabView(adapterCount, i);
                textView.setText(String.valueOf(pageTitle));
                if (mViewPager.getCurrentItem() == i) {
                    setSelectedStyle(textView);
                } else {
                    setNormalStyle(textView);
                }
                Tab tab = new Tab(i, textView, false);
                this.mTabs.add(tab);
                tab.rootView.setOnClickListener(mOnTabClickListener);
                mTabContainerLayout.addView(tab.rootView);
            }
            if (adapterCount > 0) {         //设置当前项的选中状态
                final int curItem = mViewPager.getCurrentItem();
                if (curItem != selectedPosition) {
                    selectTab(curItem);
                }
            }
            if (mIndicatorLeftList.size() <= mPagerAdapter.getCount()) {
                calcIndicatorParams();
            }
        }
    }

    /**
     * @param tabTextList tab文本数组
     * @param initIndex   初始选择的index
     */
    public void setupWithTexts(List<String> tabTextList, int initIndex) {
        if (tabTextList == null || tabTextList.size() <= 0) {
            return;
        }
        if (mTabContainerLayout == null) {
            addTabContainerLayout();
        } else {
            removeAllTabs();
        }
        addIndicator();
        int tabCount = tabTextList.size();
        if (mTabMode == TABMODE_FIX) {
            calcTabWidthForFixMode(tabCount);
        }
        for (int i = 0; i < tabCount; i++) {
            String tabText = tabTextList.get(i);
            TextView textView = genTabView(tabCount, i);
            textView.setText(tabText);
            if (i == initIndex) {
                setSelectedStyle(textView);
            } else {
                setNormalStyle(textView);
            }
            Tab tab = new Tab(i, textView, i == initIndex);
            this.mTabs.add(tab);
            tab.rootView.setOnClickListener(mOnTabClickListener);
            mTabContainerLayout.addView(tab.rootView);
        }
        selectTab(initIndex);

        calcIndicatorParams();
    }

    private void calcTabWidthForFixMode(int tabCount) {
        if (getMeasuredWidth() > 0) {
            mTabWidth = getMeasuredWidth() / tabCount;
            mTabPadding = 0;
        } else if (getLayoutParams() != null) {
            if (getLayoutParams().width == ViewGroup.LayoutParams.MATCH_PARENT) {
                mTabWidth = ScreenUtils.getDisplayWidth(getContext()) / tabCount;
                mTabPadding = 0;
            } else if (getLayoutParams().width > 0) {
                mTabWidth = getLayoutParams().width / tabCount;
                mTabPadding = 0;
            }
        }
    }

    public int getCurrentTabIndex() {
        return selectedPosition;
    }

    public void addUnreadDot(int index) {
        this.addUnreadDot(index, null);
    }

    public void addUnreadDot(int index, String count) {
        this.addUnreadDot(index, mRedDotBgResId, count);
    }

    /**
     * 有数字的未读角标
     */
    public void addUnreadDot(int index, int dotBgResId, String count) {
        addUnreadDot(index, genRedDotTextView(dotBgResId), count);
    }

    public void addUnreadDot(int index, TextView dotTextView, String count) {
        if (count == null || index < 0 || index > mTabs.size()) {
            return;
        }
        Tab tab = mTabs.get(index);
        dotTextView.setText(count);
        dotTextView.setVisibility(GONE);
        RedDot redDot = new RedDot(dotTextView, count);
        tab.addRedDot(redDot);
    }

    private final int px_14dp = DisplayUtils.dip2px(getContext(), 14);
    private final int px_3dp = DisplayUtils.dip2px(getContext(), 3);

    public TextView genRedDotTextView(int dotBgResId) {
        TextView textView = new TextView(getContext());
        MarginLayoutParams lp = new MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, px_14dp);
        lp.leftMargin = mRedDotMarginLeft;
        lp.topMargin = mRedDotMarginTop;
        lp.rightMargin = mRedDotMarginRight;
        lp.bottomMargin = mRedDotMarginBottom;
        textView.setPadding(px_3dp, 0, px_3dp, 0);
        textView.setLayoutParams(lp);
        textView.setTextSize(10);
        textView.setGravity(Gravity.CENTER);
        if (dotBgResId > 0) {
            textView.setBackgroundResource(dotBgResId);
        }
        textView.setMinWidth(px_14dp);
        textView.setTextColor(ResourceUtils.getColor(textView.getContext(), android.R.color.white));
        return textView;
    }

    /**
     * 只是一个小点
     */
    public void addSmallDot(int index) {
        TextView textView = new TextView(getContext());
        final int size = DisplayUtils.dip2px(getContext(), 10);
        MarginLayoutParams lp = new MarginLayoutParams(size, size);
        lp.leftMargin = mRedDotMarginLeft;
        lp.topMargin = mRedDotMarginTop;
        lp.rightMargin = mRedDotMarginRight;
        lp.bottomMargin = mRedDotMarginBottom;
        textView.setLayoutParams(lp);
        textView.setTextSize(10);
        textView.setVisibility(GONE);
        if (mSmallRedDotBgResId > 0) {
            textView.setBackgroundResource(mSmallRedDotBgResId);
        }
        addSmallDot(index, textView);
    }

    public void addSmallDot(int index, TextView dotTextView) {
        if (index < 0 || index > mTabs.size()) {
            return;
        }
        Tab tab = mTabs.get(index);
        RedDot redDot = new RedDot(dotTextView, "");
        tab.addRedDot(redDot);
    }

    public void showRedDot(int index) {
        if (index < 0 || index > mTabs.size()) {
            return;
        }
        Tab tab = mTabs.get(index);
        if (tab.redDot != null && tab.redDot.vDot != null) {
            tab.redDot.vDot.setVisibility(VISIBLE);
        }
    }

    public void setUnreadCount(int index, String count) {
        if (count == null || index < 0 || index > mTabs.size()) {
            return;
        }
        Tab tab = mTabs.get(index);
        if (tab.redDot != null && tab.redDot.vDot != null) {
            tab.redDot.vDot.setText(count);
            tab.redDot.vDot.setVisibility(VISIBLE);
        }
    }

    public void dismissRedDot(int index) {
        if (index < 0 || index > mTabs.size()) {
            return;
        }
        Tab tab = mTabs.get(index);
        if (tab.redDot != null && tab.redDot.vDot != null) {
            tab.redDot.vDot.setVisibility(GONE);
        }
    }

    private boolean isImgIndicator() {
        return mIndicatorResId > 0;
    }

    private void addIndicator() {
        if (mIndicatorDrawer != null) {
            return;
        }
        if (!isImgIndicator()) {      //画下划线
            if (mIndicatorPaint == null) {
                mIndicatorPaint = new Paint();
                mIndicatorPaint.setColor(mIndicatorColor);
                mIndicatorPaint.setStrokeWidth(mIndicatorStrokeWidth);
            }
        } else {                        //添加图片
            mIndicatorPaint = new Paint();
            Drawable drawable = ResourceUtils.getDrawable(getContext(), mIndicatorResId);
            if (drawable instanceof BitmapDrawable) {
                mIndicatorBitmap = ((BitmapDrawable) drawable).getBitmap();
                mIndicatorWidth = drawable.getIntrinsicWidth();
                mIndicatorImgHeight = drawable.getIntrinsicHeight();
            } else if (drawable instanceof NinePatchDrawable) {
                //TODO 后面支持.9
            }
        }
    }

    private int getIndicatorWidth(int index) {
        if (index >= 0 && index < mIndicatorWidthList.size()) {
            return mIndicatorWidthList.get(index);
        }
        return mIndicatorWidth;
    }

    private int indicatorTop;
    private int lineLength;

    private void drawLineIndicator(Canvas canvas) {
        if (mIndicatorPaint == null) {
            return;
        }
        canvas.save();
        canvas.translate(mTranslationX + mIndicatorPadding, indicatorTop + mIndicatorStrokeWidth / 2);
        canvas.drawLine(0, 0, lineLength, 0, mIndicatorPaint);
        canvas.restore();
    }

    private void drawImgIndicator(Canvas canvas) {
        if (mIndicatorPaint == null || mIndicatorBitmap == null) {
            return;
        }
        canvas.save();
        canvas.translate(mTranslationX, indicatorTop);
        canvas.drawBitmap(mIndicatorBitmap, 0, 0, mIndicatorPaint);
        canvas.restore();
    }

    private void drawCustomIndicator(Canvas canvas) {
        if (mIndicatorDrawer == null) {
            return;
        }
        canvas.save();
        canvas.translate(mTranslationX + mIndicatorPadding +
                (lineLength - mIndicatorDrawer.getLength()) / 2, indicatorTop);
        mIndicatorDrawer.draw(canvas);
        canvas.restore();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mShowIndicator && mTabContainerLayout != null) {
            if (mIndicatorDrawer != null) {
                drawCustomIndicator(canvas);
            } else if (!isImgIndicator()) {
                drawLineIndicator(canvas);
            } else {
                drawImgIndicator(canvas);
            }
        }
        super.dispatchDraw(canvas);
    }


    protected TextView genTabView(int totalCount, int index) {
        TextView tabTv = new TextView(getContext());
        tabTv.setIncludeFontPadding(false);
        if (mTextSize > 0) {
            tabTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        }
        if (mTabBackgroundResId > 0) {
            tabTv.setBackgroundResource(mTabBackgroundResId);
        }
        tabTv.setSingleLine();
        if (mTextAlignBottom) {
            tabTv.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        } else {
            tabTv.setGravity(Gravity.CENTER);
        }
        MarginLayoutParams lp = new LayoutParams(mTabWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        if (mTabMargin >= 0) {
            lp.leftMargin = lp.rightMargin = mTabMargin;
            if (index == 0) {
                lp.leftMargin = 0;
            } else if (index == totalCount - 1) {
                lp.rightMargin = 0;
            }
        }
        tabTv.setLayoutParams(lp);
        tabTv.setPadding(mTabPadding, 0, mTabPadding, 0);
        return tabTv;
    }

    private void removeAllTabs() {
        if (mTabContainerLayout != null) {
            mTabContainerLayout.removeAllViews();
        }
        mIndicatorLeftList.clear();
        mTabs.clear();
    }

    public void resetLastLocation() {
        lastPosition = -1;
    }

    public void selectAndScrollTab(int position) {
        try {
            scroll(position, 0);
            selectTab(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void selectTab(int index,boolean updateSize) {
        if (index < 0 || index >= mTabs.size()) {
            return;
        }
        if (index == selectedPosition) {
            return;
        }
        unSelectTab(selectedPosition,updateSize);
        Tab tab = mTabs.get(index);
        setSelectedStyle(tab.textView,updateSize);
        tab.isSelected = true;
        selectedPosition = index;
    }

    public void selectTab(int index) {
        selectTab(index,true);
    }

    private void unSelectTab(int index,boolean updateSize) {
        if (index < 0 || index >= mTabs.size()) {
            return;
        }
        Tab tab = mTabs.get(index);
        setNormalStyle(tab.textView,updateSize);
        tab.isSelected = false;
    }

    private void unSelectTab(int index) {
        unSelectTab(index,true);
    }

    protected void setNormalStyle(TextView textView) {
        setNormalStyle(textView,true);
    }

    protected void setNormalStyle(TextView textView,boolean updateSizes) {
        textView.setSelected(false);
        if (mIsBoldText) {
            Paint paint = textView.getPaint();
            if (paint.isFakeBoldText()) {
                paint.setFakeBoldText(false);
            }
        }
        if (mIsThinUnselected) {
            if (textView.getTypeface() != mTypefaceLight) {
                textView.setTypeface(mTypefaceLight);
            }
        }
        if (updateSizes && mTextSize != 0 && mSelectedTextSize != mTextSize) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        }
        textView.setTextColor(mNormalTextColor);
    }

    protected void setSelectedStyle(TextView textView,boolean updateSize) {
        textView.setSelected(true);
        if (mIsBoldText) {
            Paint paint = textView.getPaint();
            if (!paint.isFakeBoldText()) {
                paint.setFakeBoldText(true);
            }
        }
        if (mIsThinUnselected) {
            if (textView.getTypeface() != mTypefaceNormal) {
                textView.setTypeface(mTypefaceNormal);
            }
        }
        if (updateSize && mSelectedTextSize != 0 && mSelectedTextSize != mTextSize) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSelectedTextSize);
        }
        textView.setTextColor(mSelectedTextColor);
    }

    protected void setSelectedStyle(TextView textView) {
        setSelectedStyle(textView,true);
    }

    public void reCalcTabWidth() {
        calcIndicatorParams();
    }

    private void calcIndicatorParams() {
        if (mTabContainerLayout == null) {
            return;
        }
        mIndicatorLeftList.clear();
        mIndicatorWidthList.clear();
        int tabCount = mTabContainerLayout.getChildCount();
        int tabLayoutWidth = mTabContainerLayout.getMeasuredWidth();
        if (tabLayoutWidth <= 0) {
            mTabContainerLayout.measure(0, 0);
            tabLayoutWidth = mTabContainerLayout.getMeasuredWidth();
        }
        int totalLeft = 0;
        for (int i = 0; i < tabCount; i++) {//&& totalLeft < tabLayoutWidth
            View view = mTabContainerLayout.getChildAt(i);
            int tabWidth = view.getMeasuredWidth();
            if (tabWidth <= 0) {
                view.measure(0, 0);
                tabWidth = view.getMeasuredWidth();
            }
            if (view.getLayoutParams() instanceof MarginLayoutParams) {
                MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
                tabWidth += (lp.leftMargin + lp.rightMargin);
            }
            if (mIndicatorWidth < 0) {
                if (i == 0) {
                    mIndicatorLeftList.add(totalLeft);
                    mIndicatorWidthList.add(tabWidth - mIndicatorPadding * 2 - mTabMargin);
                } else if (i == tabCount - 1) {
                    mIndicatorLeftList.add(totalLeft + mTabMargin);
                    mIndicatorWidthList.add(tabWidth - mIndicatorPadding * 2 - mTabMargin);
                } else {
                    mIndicatorLeftList.add(totalLeft + mTabMargin);
                    mIndicatorWidthList.add(tabWidth - (mIndicatorPadding + mTabMargin) * 2);
                }
            } else {
                mIndicatorLeftList.add(totalLeft + (tabWidth - mIndicatorWidth) / 2);
            }
            totalLeft += tabWidth;
        }
        mIndicatorLeftList.add(totalLeft);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (isImgIndicator()) {
            indicatorTop = h - mIndicatorImgHeight;
        } else {
            indicatorTop = h - mIndicatorStrokeWidth;
        }
        indicatorTop -= (mIndicatorBottomMargin + getPaddingBottom());
        calcIndicatorParams();
    }

    private int lastPosition = -1;
    private float lastPositionOffset = -1;

    private void scroll(int position, float positionOffset) {
        if (position < 0 || position >= mIndicatorLeftList.size() - 1 ||
                (lastPositionOffset == positionOffset && lastPosition == position)) {
            return;
        }
        lastPosition = position;
        lastPositionOffset = positionOffset;
        int currIndicatorWidth = getIndicatorWidth(position);
        int nextIndicatorWidth = getIndicatorWidth(position + 1);
        int currTabLeft = mIndicatorLeftList.get(position);
        int nextTabLeft = mIndicatorLeftList.get(position + 1);
        lineLength = (int) (currIndicatorWidth + (nextIndicatorWidth - currIndicatorWidth) * positionOffset);
        mTranslationX = (int) (currTabLeft + (nextTabLeft - currTabLeft) * positionOffset);
        if (mSelectedTextSize != mTextSize && mEnableTextSizeTransition) {
            changeTextSize(position, positionOffset);
        }
        scrollView(mTranslationX, lineLength);
        invalidate();
    }

    private void changeTextSize(int position, float positionOffset) {
        if (position + 1 >= getTabCount() || position < 0) {
            return;
        }
        TextView tvLeft = getTabAt(position).textView;
        TextView tvRight = getTabAt(position + 1).textView;
        int leftNewSize = (int) (mSelectedTextSize + (mTextSize - mSelectedTextSize) * positionOffset);
        int rightNewSize = (int) (mSelectedTextSize + (mTextSize - mSelectedTextSize) * (1 - positionOffset));
        tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftNewSize);
        tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightNewSize);
        //检查前后的大小，因为滑动过程中，中间的tab的positionOffset可能没有等于0的情况，textView的字体大小就不对
        TextView otherTextView = null;
        if (selectedPosition <= position) {
            //下一个选中tab在左边，检查右边的右边的tab
            if (selectedPosition + 2 < getTabCount()) {
                otherTextView = getTabAt(selectedPosition + 2).textView;
            }
        } else {
            //下一个选中tab是右边的，检查左边的左边的tab
            if (selectedPosition - 2 >= 0) {
                otherTextView = getTabAt(selectedPosition - 2).textView;
            }
        }
        if (otherTextView != null && otherTextView.getTextSize() != mTextSize) {
            otherTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        }
        Log.d(TAG, "changeTextSize: " + selectedPosition + "->" + position + "," + positionOffset);
    }

    private boolean scrollView(int indicatorLeft, int indicatorLength) {
        int rootWidth = getWidth();
        int leftInView = indicatorLeft - getScrollX();
        int dist = 0;
        if (leftInView < mScrollThreshold) {
            dist = -(mScrollThreshold - leftInView);
        } else if (leftInView + indicatorLength > rootWidth - mScrollThreshold) {
            dist = (leftInView + indicatorLength - rootWidth + mScrollThreshold);
        }
        scrollBy(dist, 0);
        return dist != 0;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public int getTabCount() {
        return mTabs == null ? 0 : mTabs.size();
    }

    public Tab getTabAt(int i) {
        return mTabs.get(i);
    }

    public void setOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
        this.mOnTabSelectedListener = onTabSelectedListener;
    }

    public void setOnTabClickedListener(OnTabClickListener onTabClickedListener) {
        this.mCustomClickListener = onTabClickedListener;
    }

    public void setIndicatorDrawer(IndicatorDrawer indicatorDrawer) {
        this.mIndicatorDrawer = indicatorDrawer;
    }


    public void updateTabTextColors(Integer normalTextColor, Integer selectTextColor) {
        if (normalTextColor != null) {
            mNormalTextColor = normalTextColor;
        }
        if (selectTextColor != null) {
            mSelectedTextColor = selectTextColor;
        }
        if (mTabs != null) {
            for (int i = 0; i < mTabs.size(); i++) {
                Tab tab = mTabs.get(i);
                if (tab.isSelected) {
                    tab.textView.setTextColor(mSelectedTextColor);
                } else {
                    tab.textView.setTextColor(mNormalTextColor);
                }
            }
        }
    }

    public void updateTabText(int index, String text) {
        if (index >= mTabs.size() || mTabs.get(index) == null) {
            return;
        }
        mTabs.get(index).setText(text);
        if (mTabWidth == ViewGroup.LayoutParams.WRAP_CONTENT) {
            mTabs.get(index).getTextView().measure(0, 0);
            mTabs.get(index).getRootView().measure(0, 0);
        }
    }

    public interface OnTabClickListener {
        void onTabClicked(Tab tab);
    }

    public interface OnTabSelectedListener {
        void onTabSelected(Tab tab);

        void onTabUnselected(Tab tab);

        void onTabReselected(Tab tab);
    }

    public interface IndicatorDrawer {
        void draw(Canvas canvas);

        int getLength();
    }

    public class Tab {
        private int index;
        private FrameLayout rootView;
        private TextView textView;
        private RedDot redDot;
        private boolean isSelected;
        private View customView;

        public Tab(int index, TextView textView, boolean isSelected) {
            this(index, textView, null, isSelected);
        }

        public Tab(int index, @NonNull TextView textView, @Nullable RedDot redDot, boolean isSelected) {
            this.index = index;
            this.textView = textView;
            this.isSelected = isSelected;
            this.redDot = redDot;

            this.rootView = new FrameLayout(getContext());
            this.rootView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            ViewGroup.LayoutParams lp = ensureLayoutParam(this.textView);
            this.rootView.addView(this.textView, lp);
            this.rootView.setTag(this.index);
            addRedDotView(this.redDot);
        }

        public String getText() {
            return textView == null ? "" : textView.getText().toString();
        }

        public void setText(String text) {
            textView.setText(text);
        }

        private LayoutParams setRedDotGravity(View redDotView, int gravity) {
            ViewGroup.LayoutParams lp = ensureLayoutParam(redDotView);
            LayoutParams flp;
            if (lp instanceof LayoutParams) {
                flp = (LayoutParams) lp;
            } else {
                flp = new LayoutParams(lp);
                if (lp instanceof MarginLayoutParams) {
                    flp.leftMargin = ((MarginLayoutParams) lp).leftMargin;
                    flp.topMargin = ((MarginLayoutParams) lp).topMargin;
                    flp.rightMargin = ((MarginLayoutParams) lp).rightMargin;
                    flp.bottomMargin = ((MarginLayoutParams) lp).bottomMargin;
                }
            }
            flp.gravity = gravity;
            return flp;
        }

        private ViewGroup.LayoutParams ensureLayoutParam(View view) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp == null) {
                lp = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            return lp;
        }

        public void addRedDot(RedDot redDot) {
            if (this.redDot != null) {
                this.redDot.vDot.setText(redDot.num);
                this.redDot.vDot.setLayoutParams(setRedDotGravity(this.redDot.vDot, redDot.gravity));
            } else {
                this.redDot = redDot;
                addRedDotView(redDot);
            }
        }

        private void addRedDotView(RedDot redDot) {
            if (redDot != null) {
                rootView.addView(redDot.vDot, setRedDotGravity(redDot.vDot, redDot.gravity));
            }
        }

        public void setBackgroundResource(int background) {
            textView.setBackgroundResource(background);
        }

        public void setBackgroundColor(@ColorInt int background) {
            textView.setBackgroundColor(background);
        }

        private Drawable setDrawableBounds(Drawable drawable, int width, int height) {
            if (drawable != null) {
                if (width >= 0 && height >= 0) {
                    drawable.setBounds(0, 0, width, height);
                } else {
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                }
            }
            return drawable;
        }

        public int getPosition() {
            return index;
        }

        public View getCustomView() {
            return customView;
        }

        /**
         * 设置自定义的tab布局，
         * 参数customView会变成tab的布局，原布局的LayoutParams会设给customView(就是宽和高都是match_parent)；
         * <p>
         * 参数textView应该要是customView的子View，主要是方便使用，
         * tab的主要部分是tab的标题，由TextView来做，这里设的textView就是这个标题，设了之后不用额外管理是否选中的样式，这个控件会帮你做，
         * 当然也可以设置null或其他东西，没bug但是也没效果。
         */
        public void setCustomView(@NonNull View customView, @Nullable TextView textView) {
            this.customView = customView;
            if (textView == null) {
                textView = new TextView(customView.getContext());
            }

            if (this.textView != null) {
                if (isSelected) {
                    setSelectedStyle(textView);
                } else {
                    setNormalStyle(textView);
                }
                if (this.textView.getParent() != null) {
                    ViewGroup parent = (ViewGroup) this.textView.getParent();
                    parent.removeView(this.textView);
                    parent.addView(customView, this.textView.getLayoutParams());
                }
            }
            this.textView = textView;
        }

        public FrameLayout getRootView() {
            return rootView;
        }

        public TextView getTextView() {
            return textView;
        }
    }

    private class RedDot {
        TextView vDot;
        String num;
        int gravity;

        public RedDot(TextView vDot, String num) {
            this(vDot, num, Gravity.END | Gravity.TOP);
        }

        public RedDot(TextView vDot, String num, int gravity) {
            this.vDot = vDot;
            this.num = num;
            this.gravity = gravity;
        }
    }

    public View getTabView(int index) {
        return mTabContainerLayout.getChildAt(index);
    }

}
