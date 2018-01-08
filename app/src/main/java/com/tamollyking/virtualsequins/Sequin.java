package com.tamollyking.virtualsequins;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by mollyrand on 1/7/18.
 */

public class Sequin extends ConstraintLayout {

    private AnimatorSet show;
    private AnimatorSet hide;
    private View front;
    private View back;
    private boolean isFront;

    public Sequin(Context context) {
        super(context);
        setup(context);
    }

    public Sequin(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }

    public Sequin(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context);
    }

    private void setup(Context context) {
        show = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.back_in);
        show.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                final float translation = isFront? 1.f : 0.f;
                final long delay = getResources().getInteger(R.integer.anim_length_half);
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ViewCompat.setTranslationZ(Sequin.this, translation);
                    }
                }, delay);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                isFront = !isFront;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        hide = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.front_out);
        isFront = true;
        inflate(context, R.layout.sequin, this);
        front = findViewById(R.id.front);
        back = findViewById(R.id.back);
    }

    public void flip() {
        if (isFront) {
            show.setTarget(back);
            hide.setTarget(front);
        } else {
            show.setTarget(front);
            hide.setTarget(back);
        }
        hide.start();
        show.start();
    }
}
