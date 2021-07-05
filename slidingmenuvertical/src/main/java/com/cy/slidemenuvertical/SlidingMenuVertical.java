package com.cy.slidemenuvertical;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by lenovo on 2017/7/1.
 */

public class SlidingMenuVertical extends LinearLayout {
    private Scroller mScroller;
    private View view_top;
    private View view_bottom;
    private float downX;
    private float downY;
    private boolean opened = true;//状态是否开闭


    private OnSwitchListener onSwitchListener;


    private int duration_max = 300;//最长过度时间

    private int ambit_scroll = 100;//滑动界限，开闭

    private int y_opened = -1;    // * y_opened:抽屉打开时view_bootom的top y

    private boolean isTopSlide = true;
    private int pointer_others_count = 0;

    public SlidingMenuVertical(Context context) {
        this(context, null);
    }


    public SlidingMenuVertical(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
        setOrientation(VERTICAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // 当xml解析完成时的回调
        view_top = getChildAt(0);
        view_bottom = getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isTopSlide) {
            view_bottom.measure(widthMeasureSpec, heightMeasureSpec);
        } else {
            view_top.measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public boolean isTopSlide() {
        return isTopSlide;
    }

    public void setTopSlide(boolean topSlide) {
        isTopSlide = topSlide;
        opened = isTopSlide;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                pointer_others_count = 0;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                pointer_others_count++;
                break;

        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        setY_opened();
        // 拦截
        // 竖直滑动时，去拦截
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();
                //canScrollVertically(1)表示手指是否能往上拖
                // 竖直滑动
                LogUtils.log("onInterceptTouchEvent", opened);

                //考虑双指触摸
                if ((Math.abs(moveY - downY) > Math.abs(moveX - downX))) {
                    //抽屉打开了
                    if (opened) {
                        if (isTopSlide) {
                            //手指往下拖
                            if (moveY > downY) {
                                if (ScreenUtils.isInViewRange(view_top, event)) return false;
                                if (view_bottom.canScrollVertically(-1) && ScreenUtils.isInViewRange(view_bottom, event))
                                    return false;
                            }
                            //手指往上拖
                            if (moveY < downY) {
                                if (view_top.canScrollVertically(1) && ScreenUtils.isInViewRange(view_top, event))
                                    return false;
                                if (view_bottom.canScrollVertically(1) && ScreenUtils.isInViewRange(view_bottom, event))
                                    return false;
                            }
                        } else {
                            //手指往下拖
                            if (moveY > downY) {
                                if (view_bottom.canScrollVertically(-1) && ScreenUtils.isInViewRange(view_top, event))
                                    return false;
                                if (view_bottom.canScrollVertically(-1) && ScreenUtils.isInViewRange(view_bottom, event))
                                    return false;
                            }
                            //手指往上拖
                            if (moveY < downY) {
                                if (view_top.canScrollVertically(1) && ScreenUtils.isInViewRange(view_top, event))
                                    return false;
                                if (view_bottom.canScrollVertically(1) && ScreenUtils.isInViewRange(view_bottom, event))
                                    return false;
                            }
                        }
                    } else {
                        if (isTopSlide) {
                            //手指往下拖
                            if (moveY > downY) {
                                if (view_bottom.canScrollVertically(-1) && ScreenUtils.isInViewRange(view_bottom, event))
                                    return false;
                            }
                            //手指往上拖
                            if (moveY < downY) {
                                if (view_bottom.canScrollVertically(1) && ScreenUtils.isInViewRange(view_bottom, event))
                                    return false;
                            }
                        } else {
                            //手指往下拖
                            if (moveY > downY) {
                                if (view_top.canScrollVertically(-1) && ScreenUtils.isInViewRange(view_top, event))
                                    return false;
                            }
                            //手指往上拖
                            if (moveY < downY) {
                                if (view_top.canScrollVertically(1) && ScreenUtils.isInViewRange(view_top, event))
                                    return false;
                            }
                        }
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    private boolean noIntercept(View view, float moveY, float downY, MotionEvent event) {
        LogUtils.log("view.canScrollVertically(1)", view.canScrollVertically(1));
        LogUtils.log("view.canScrollVertically(-1)", view.canScrollVertically(-1));
        //手指往下拖
        if (moveY > downY) {
            //如果view还能滑动，不拦截
            if (view.canScrollVertically(1)) {
                if (ScreenUtils.isInViewRange(view, event)) return false;
            } else {
                return false;
            }
        }
        //手指往上拖
        if (moveY < downY) {
            if (view.canScrollVertically(-1)) {
                if (ScreenUtils.isInViewRange(view, event)) return false;
            } else {
                return false;
            }
        }

        return true;
    }

    private int getD() {
        return isTopSlide ? getHeight_top() : getHeight_bottom();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() ) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();

                int dy = (int) (downY - moveY + 0.5f);// 四舍五入 20.9 + 0.5-->20


                LogUtils.log("pointer_others_count", pointer_others_count);

                int scrollY = getScrollY();
                //mDownY - moveY>0上滑

                if (pointer_others_count <= 0 && scrollY + dy > 0) {
                    scrollBy(0, dy);
                    if (scrollY + dy > getD()) scrollTo(0, getD());
                }

                downX = moveX;
                downY = moveY;

                break;
            case MotionEvent.ACTION_UP:
//                Log.e("heigth_top", "+++++++++++++++++" + height_top);
//                Log.e("scrollY", "+++++++++++++++++" + getScrollY());
                if (pointer_others_count > 0){
                    pointer_others_count=0;
                    break;
                }
                if (opened) {
                    if (isTopSlide) {
                        open(getScrollY() <= ambit_scroll && getScrollY() <= getD() / 3);
                    } else {
                        open((getScrollY() >= getD() - ambit_scroll) && getScrollY() >= getD() * 2 / 3);
                    }

                } else {
                    if (isTopSlide) {
                        open(getScrollY() < getD() - ambit_scroll || getScrollY() < getD() * 2 / 3);
                    } else {
                        open(getScrollY() > ambit_scroll || getScrollY() > getD() / 3);
                    }
                }

                break;

        }
        // 消费掉
        return true;
    }

    /**
     * 开闭抽屉
     *
     * @param open
     */
    public void open(boolean open) {
        setY_opened();

        LogUtils.log("open", open);
//        this.opened = isTopSlide ?open:!open;
        this.opened = open;
        //打开
        if (open) {

//            Log.e("打开", "+++++++++++++++++++++++++++++");


            int startX = getScrollX();// 起始的坐标X
            int startY = getScrollY();// 起始的坐标Y

            int endX = 0;
            int endY = isTopSlide ? 0 : getHeight_bottom();

            int dx = endX - startX;// 增量X
            int dy = endY - startY;// 增量Y
            // 1px = 10
            int duration = Math.abs(dy) * 10;
            if (duration > duration_max) {
                duration = duration_max;
            }

            mScroller.startScroll(startX, startY, dx, dy, duration);
        } else {


            Log.e("关闭", "+++++++++++++++++++++++++++++" + getScrollY());
            int startX = getScrollX();// 起始的坐标X
            int startY = getScrollY();// 起始的坐标Y

            int endX = 0;
            int endY = isTopSlide ? getHeight_top() : 0;

            int dx = endX - startX;// 增量X
            int dy = endY - startY;// 增量Y

            // 1px = 10
            int duration = Math.abs(dy) * 10;
            if (duration > duration_max) {
                duration = duration_max;
            }

            // 模拟数据变化
            mScroller.startScroll(startX, startY, dx, dy, duration);
        }

        invalidate();// 触发ui绘制 --> draw() --> dispatchDraw()--> drawChild -->
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {// 如果正在计算的过程中
            // 更新滚动的位置
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

//        Log.e("y_now", ScreenUtils.getViewScreenLocation(view_bottom)[1] + "++++++++++++++++++++++");
//
//        Log.e("y_closed", y_opened - height_top + "++++++++++++++++++++++");

        if (onSwitchListener != null) {
            onSwitchListener.onSwitching(t - oldt < 0 ? isTopSlide : !isTopSlide,
                    getY_now(), getY_opened(), getY_opened() - getD());
            if (getY_now() == getY_opened()) {
//                Log.e("true", "++++++++++++++++++++++++");
                onSwitchListener.onSwitched(true);
            }
            if (getY_now() == getY_opened() - getD()) {
//                Log.e("false", "++++++++++++++++++++++++");

                onSwitchListener.onSwitched(false);
            }

        }
    }

    public boolean isOpened() {
        return opened;
    }

    public int getDuration_max() {
        return duration_max;
    }

    /**
     * 设置松手后 开闭最长过渡时间
     *
     * @param duration_max
     */
    public void setDuration_max(int duration_max) {
        this.duration_max = duration_max;
    }

    public View getView_top() {
        return view_top;
    }

    public View getView_bottom() {
        return view_bottom;
    }

    public int getHeight_top() {
        return view_top.getMeasuredHeight();
    }

    public int getHeight_bottom() {
        return view_bottom.getMeasuredHeight();
    }

    /**
     * 获取 * y_opened:抽屉打开时view_bootom的top y
     */
    private void setY_opened() {

        if (y_opened < 0) {

            y_opened = getViewScreenLocation(view_bottom)[1];
            Log.e("y _open", y_opened + "++++++++++++++++++++");
        }
    }

    /**
     * y_opened:抽屉打开时view_bootom的top y
     *
     * @return
     */
    public int getY_opened() {
        if (y_opened < 0) {
            Log.e("还未计算出来", "+++++++++++++++++++++++++++++++++++");
            return 0;
        }
        return y_opened;
    }

    /**
     * y_now:抽屉实时view_bootom的top y
     *
     * @return
     */
    public int getY_now() {
        return getViewScreenLocation(view_bottom)[1];
    }

    public int getAmbit_scroll() {
        return ambit_scroll;
    }

    /**
     * 修改滑动界限 值，值越大  开闭越难  单位ms
     *
     * @param ambit_scroll <height_top
     */

    public void setAmbit_scroll(int ambit_scroll) {
        this.ambit_scroll = ambit_scroll;
    }

    /**
     * 计算指定的 View 在屏幕中的坐标。
     */
    public int[] getViewScreenLocation(View view) {
        int[] location = new int[2];
        // 获取控件在屏幕中的位置，返回的数组分别为控件左顶点的 x、y 的值
        view.getLocationOnScreen(location);

        return location;
    }

    public interface OnSwitchListener {

        /*
        滑动中
        y_now:实时view_bottom的top y, y_opened:抽屉打开时view_bootom的top y,y_closed:抽屉关闭时view_bottom的top y  top y:在屏幕中的top y坐标

         */
        public void onSwitching(boolean isToOpen, int y_now, int y_opened, int y_closed);

        /*
        滑动停止，状态是否开闭
         */
        public void onSwitched(boolean opened);
    }

    public void setOnSwitchListener(OnSwitchListener onSwitchListener) {
        this.onSwitchListener = onSwitchListener;
    }
}