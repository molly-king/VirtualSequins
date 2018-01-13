package com.tamollyking.virtualsequins.views;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.widget.ImageView;

import com.tamollyking.virtualsequins.R;

/**
 * Created by mollyrand on 1/7/18.
 */

public class Sequin extends ConstraintLayout {

    private AnimatorSet show;
    private AnimatorSet hide;
    private ImageView front;
    private ImageView back;
    private boolean isFront;
    float lastY = 0;

    public Sequin(Context context, SequinModel model) {
        super(context);
        setup(context, model);
    }


    private void setup(Context context, SequinModel dataModel) {
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
        Drawable frontDrawable = ContextCompat.getDrawable(context, R.drawable.sequin_a);
        frontDrawable.setColorFilter(new
                PorterDuffColorFilter(Color.parseColor(dataModel.getFrontColorHex()), PorterDuff.Mode.DST_OVER));
        front.setImageDrawable(frontDrawable);
        Drawable backDrawable = ContextCompat.getDrawable(context, R.drawable.sequin_b);
        backDrawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor(dataModel.getBackColorHex())
                , PorterDuff.Mode.DST_OVER));
        back = findViewById(R.id.back);
        back.setImageDrawable(backDrawable);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(dataModel.getDiameter(), dataModel.getDiameter());
        front.setLayoutParams(params);
        back.setLayoutParams(params);
    }

    public void flip(float delta) {
        if (isFront && delta > 10) {
            show.setTarget(back);
            hide.setTarget(front);
            hide.start();
            show.start();
            lastY = 0;
        }
        if (!isFront && delta < 10){
            show.setTarget(front);
            hide.setTarget(back);
            hide.start();
            show.start();
            lastY = 0;
        }
    }
}
