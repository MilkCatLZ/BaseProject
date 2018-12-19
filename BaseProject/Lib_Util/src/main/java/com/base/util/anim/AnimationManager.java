package com.base.util.anim;


import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.base.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup.LayoutParams;


/**
 * Created by Syokora on 2016/8/28.
 */
public class AnimationManager {

    /**
     * 自动计算View在visible时的动画
     * 传入初始高度既可
     *
     * @param view       需要改变高度的View
     * @param fromHeight view的原始高度
     * @param duration   持续时间
     */
    public static void animationViewHeightVisible(@NonNull final View view, int fromHeight, int duration, @Nullable AnimatorListener animationListener) {

        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        int height = view.getMeasuredHeight();

        Log.d("AnimationManager", "animationViewHeightVisible------------fromHeght=" + fromHeight + "/height=" + height);

        ValueAnimator animator = ValueAnimator.ofObject(new TypeEvaluator<Integer>() {

            @Override
            public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
                int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                view.measure(w, h);
                int height = view.getMeasuredHeight();
                if (endValue != height) {
                    endValue = height;
                }
                float offset = (endValue - startValue) * fraction;
                return startValue + (int) offset;
            }
        }, fromHeight, height).setDuration(duration);

        animator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int height = (int) animation.getAnimatedValue();
                LayoutParams params = view.getLayoutParams();
                params.height = height;
                view.setLayoutParams(params);
            }
        });
        if (animationListener != null) {
            animator.addListener(animationListener);
        }
        animator.start();
    }

    /**
     * 自动计算View在gone时的动画
     * 传入初始高度既可
     *
     * @param view       需要改变高度的View
     * @param fromHeight view的原始高度
     * @param duration   持续时间
     */
    public static void animationViewHeightVisibleClose(@NonNull final View view, int fromHeight, int duration, @Nullable AnimatorListener animationListener) {


        Log.d("AnimationManager", "animationViewHeightVisibleClose-----------fromHeght=" + fromHeight);

        ValueAnimator animator = ValueAnimator.ofObject(new TypeEvaluator<Integer>() {

            @Override
            public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
                float offset = (endValue - startValue) * fraction;
                return startValue + (int) offset;
            }
        }, fromHeight, 0).setDuration(duration);

        animator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int height = (int) animation.getAnimatedValue();
                LayoutParams params = view.getLayoutParams();
                params.height = height;
                view.setLayoutParams(params);;
            }
        });
        animator.addListener(new AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.clearAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        if (animationListener != null) {
            animator.addListener(animationListener);
        }
        animator.start();
    }

    public static void animationViewHeightVisible(@NonNull final View view, int fromHeight, int duration) {
        animationViewHeightVisible(view, fromHeight, duration, null);
    }


    /**
     * 动画消失
     * @param view
     * @param cx
     * @param cy
     * @param startRadius
     * @param endRadius
     * @param duration
     * @param adapter
     */
    public static void animFromLeftToRightDisappear(@NonNull View view, int cx, int cy, float startRadius, float endRadius, int duration,
                                                    @Nullable AnimatorListenerAdapter adapter) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, startRadius, endRadius);
            if (adapter != null)
                anim.addListener(adapter);
            if (duration != 0)
                anim.setDuration(duration);
            anim.start();
        } else {
            if (adapter != null) {
                adapter.onAnimationEnd(null);
            }
        }
    }


    /**
     * 从左到右消失，结束点为宽高
     */
    public static void animFromLeftToRightDisappearNormal(@NonNull View view, int duration, @Nullable AnimatorListenerAdapter adapter) {
        int cx = (view.getLeft() + view.getRight());
        int cy = (view.getTop() + view.getBottom()) / 2;
        animFromLeftToRightDisappear(view, cx, cy, view.getWidth(), view.getHeight(), duration, adapter);
    }


    /**
     * 从左到右消失，结束点为完全消失
     */
    public static void animFromLeftToRightDisappearFull(@NonNull View view, int duration, @Nullable AnimatorListenerAdapter adapter) {
        int cx = (view.getLeft() + view.getRight());
        int cy = (view.getTop() + view.getBottom()) / 2;
        animFromLeftToRightDisappear(view, cx, cy, view.getWidth(), 0, duration, adapter);
    }


    /**
     * 从右到左消失，结束点为控件宽高
     */
    public static void animFromRightToLeftDisappearNormal(@NonNull View view, int duration, @Nullable AnimatorListenerAdapter adapter) {
        int cx = (view.getLeft() + view.getRight()) - view.getWidth();
        int cy = (view.getTop() + view.getBottom()) / 2;
        animFromLeftToRightDisappear(view, cx, cy, view.getWidth(), view.getHeight(), duration, adapter);
    }

    /**
     * 从右到左消失，结束点为完全消失
     */
    public static void animFromRightToLeftDisappearFull(@NonNull View view, int duration, @Nullable AnimatorListenerAdapter adapter) {
        int cx = (view.getLeft() + view.getRight());
        int cy = (view.getTop() + view.getBottom()) / 2;
        animFromLeftToRightDisappear(view, cx, cy, view.getWidth(), 0, duration, adapter);
    }


    /**
     * {@link #animFromLeftToRightDisappearNormal}的逆向动画
     */
    public static void animFromLeftToRightDisappearNormalReverse(@NonNull View view, int duration, @Nullable AnimatorListenerAdapter adapter) {
        int cx = (view.getLeft() + view.getRight());
        int cy = (view.getTop() + view.getBottom()) / 2;
        animFromLeftToRightDisappear(view, cx, cy, view.getHeight(), view.getWidth(), duration, adapter);
    }

    /**
     * {@link #animFromLeftToRightDisappearFull}的逆向动画
     */
    public static void animFromLeftToRightDisappearFullReverse(@NonNull View view, int duration, @Nullable AnimatorListenerAdapter adapter) {
        int cx = (view.getLeft() + view.getRight());
        int cy = (view.getTop() + view.getBottom()) / 2;
        animFromLeftToRightDisappear(view, cx, cy, 0, view.getWidth(), duration, adapter);
    }

    /**
     * {@link #animFromRightToLeftDisappearNormal}的逆向动画
     */
    public static void animFromRightToLeftDisappearNormalReverse(@NonNull View view, int duration, @Nullable AnimatorListenerAdapter adapter) {
        int cx = (view.getLeft() + view.getRight()) - view.getWidth();
        int cy = (view.getTop() + view.getBottom()) / 2;
        animFromLeftToRightDisappear(view, cx, cy, view.getHeight(), view.getWidth(), duration, adapter);
    }

    /**
     * {@link #animFromRightToLeftDisappearFull}的逆向动画
     */
    public static void animFromRightToLeftDisappearFullReverse(@NonNull View view, int duration, @Nullable AnimatorListenerAdapter adapter) {
        int cx = (view.getLeft() + view.getRight()) - view.getWidth();
        int cy = (view.getTop() + view.getBottom()) / 2;
        animFromLeftToRightDisappear(view, cx, cy, 0, view.getWidth(), duration, adapter);
    }
}
