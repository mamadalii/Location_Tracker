package com.example.utility;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.polygons.R;

public class SwipeButton extends RelativeLayout {
    private ImageView slidingButton;
    private float initialX;
private static boolean situation =false;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {

        this.active = active;
        if(active==true){
            if(situation==false){
                situation=true;
            }
                else
                    situation=false;

                Log.v("inside","setActive");
                this.swipeButtonListener.onActiveChanged();

            }}
    LayoutParams layoutParams;
    RelativeLayout background;
    private static boolean active;
    private int initialButtonWidth;
    private TextView centerText;
static SwipeButtonListener swipeButtonListener;
    private Drawable disabledDrawable;
    private Drawable disabledDrawablePause;
    private Drawable enabledDrawable;
    public SwipeButton(Context context) {
        super(context);

        init(context, null, -1, -1);
    }

    public SwipeButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs, -1, -1);
    }

    public SwipeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr, -1);
    }

    @TargetApi(21)
    public SwipeButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        background = new RelativeLayout(context);

        LayoutParams layoutParamsView = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParamsView.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        background.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_rounded));

        addView(background, layoutParamsView);

         TextView centerText = new TextView(context);
        this.centerText = centerText;

         layoutParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);


            centerText.setText("SWIPE TO RUN");
        //add any text you need
        centerText.setTextColor(Color.WHITE);
        centerText.setPadding(35, 35, 35, 35);
        background.addView(centerText, layoutParams);



        final ImageView swipeButton = new ImageView(context);
        this.slidingButton = swipeButton;

        disabledDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_play_arrow_black_24dp);
        disabledDrawablePause = ContextCompat.getDrawable(getContext(), R.drawable.ic_pause_black_24dp);
        enabledDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_done_black_24dp);

        slidingButton.setImageDrawable(disabledDrawable);
        slidingButton.setPadding(40, 40, 40, 40);

        LayoutParams layoutParamsButton = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParamsButton.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        layoutParamsButton.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        swipeButton.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_button));
        swipeButton.setImageDrawable(disabledDrawable);
        addView(swipeButton, layoutParamsButton);

        setOnTouchListener(getButtonTouchListener());

    }
    private OnTouchListener getButtonTouchListener() {

        return new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(slidingButton.getX() + slidingButton.getWidth() / 2 < getWidth()){
                    Log.v("dorost","ast");
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        return true;
                    case MotionEvent.ACTION_MOVE:
//                        initialX=0;
                        if (initialX == 0) {
                            initialX = slidingButton.getX();
                            Log.v(slidingButton.getX()+"","inside 1 ast");
                        }

                        if (event.getX() > initialX + slidingButton.getWidth() / 2 &&
                                event.getX() + slidingButton.getWidth() / 2 < getWidth()) {

                            slidingButton.setX(event.getX() - slidingButton.getWidth() / 2);
                            centerText.setAlpha(1 - 1.3f * (slidingButton.getX() + slidingButton.getWidth()) / getWidth());
                            Log.v(getWidth()+"","inside 2 ast");
                        }

                        if  (event.getX() + slidingButton.getWidth() / 2 > getWidth()
                               &&slidingButton.getX() + slidingButton.getWidth() / 2 < getWidth() ) {
                            slidingButton.setX(getWidth() - slidingButton.getWidth());
                            Log.v(slidingButton.getX()+"","inside 3 ast");

                        }

                        if  (event.getX() < slidingButton.getWidth() / 2 &&
                                slidingButton.getX() > 0) {
                            slidingButton.setX(0);
                            Log.v(slidingButton.getX()+"","inside 4 ast");

                        }                        return true;
                    case MotionEvent.ACTION_UP:
                        if (active) {
                            collapseButton();
                        } else {
                            initialButtonWidth = slidingButton.getWidth();

                            if (slidingButton.getX() + slidingButton.getWidth() > getWidth() * 0.85) {
                                expandButton();
                            } else {
                                moveButtonBack();
                            }
                        }

                        return true;

                }

                return false;
            }
        };
    }
    private void expandButton() {
        final ValueAnimator positionAnimator =
                ValueAnimator.ofFloat(slidingButton.getX(), 0);
        positionAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float x = (Float) positionAnimator.getAnimatedValue();
                slidingButton.setX(x);
            }
        });


        final ValueAnimator widthAnimator = ValueAnimator.ofInt(
                slidingButton.getWidth(),
                getWidth());

        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewGroup.LayoutParams params = slidingButton.getLayoutParams();
                params.width = (Integer) widthAnimator.getAnimatedValue();
                slidingButton.setLayoutParams(params);
            }
        });


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                setActive(true);
                slidingButton.setImageDrawable(enabledDrawable);
                slidingButton.setImageDrawable(enabledDrawable);
            }
        });
        animatorSet.playTogether(positionAnimator, widthAnimator);
        animatorSet.start();
    }

    private void collapseButton() {
        //add any text you need



        final ValueAnimator widthAnimator = ValueAnimator.ofInt(
                slidingButton.getWidth(),
                initialButtonWidth);

        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewGroup.LayoutParams params =  slidingButton.getLayoutParams();
                params.width = (Integer) widthAnimator.getAnimatedValue();
                slidingButton.setLayoutParams(params);
            }
        });

        widthAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setActive(false);
                if(situation==false){
                slidingButton.setImageDrawable(disabledDrawable);
                layoutParams = new LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);


                centerText.setText("SWIPE To Run");
                //add any text you need
                centerText.setTextColor(Color.WHITE);
                centerText.setPadding(35, 35, 35, 35);
                background.removeAllViews();
                background.addView(centerText, layoutParams);}
                else {
                    slidingButton.setImageDrawable(disabledDrawablePause);
                    layoutParams = new LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);

                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);


                    centerText.setText("SWIPE To Pause");
                    //add any text you need
                    centerText.setTextColor(Color.WHITE);
                    centerText.setPadding(35, 35, 35, 35);
                    background.removeAllViews();
                    background.addView(centerText, layoutParams);
                }
            }
        });

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(
                centerText, "alpha", 1);

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.playTogether(objectAnimator, widthAnimator);
        animatorSet.start();
    }
    private void moveButtonBack() {
        final ValueAnimator positionAnimator =
                ValueAnimator.ofFloat(slidingButton.getX(), 0);
        positionAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        positionAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float x = (Float) positionAnimator.getAnimatedValue();
                slidingButton.setX(x);
            }
        });

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(
                centerText, "alpha", 1);

        positionAnimator.setDuration(200);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimator, positionAnimator);
        animatorSet.start();
    }

    public void setOnChangeListener ( SwipeButtonListener swipeButtonListener ){
this.swipeButtonListener=(swipeButtonListener);

//Log.v("inside change listener ",this.swipeButtonListener.size()+"");
    }


}