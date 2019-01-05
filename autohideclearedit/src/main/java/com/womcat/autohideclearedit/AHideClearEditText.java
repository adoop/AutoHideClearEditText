package com.womcat.autohideclearedit;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;



public class AHideClearEditText extends AppCompatEditText {

    private AttributeSet mAttrs;
    private Drawable mCloseDrawable;
    private Drawable mPasswordTogDrawableOpen;
    private Drawable passwordTogDrawableClose;
    private boolean mCloseEnabled;
    private boolean mPasswordEnabled = false;
    private boolean mPasswordVisible = false;

    Paint mPaint;
    Paint mPaint2;
    boolean isFocused = false;



    public AHideClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }

    private void init(Context context, AttributeSet attrs) {
        mPaint = getPaint();
        mPaint2 = getPaint();
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.autoHideClearEditAttrs);
            mPasswordTogDrawableOpen = ta.getDrawable(R.styleable.autoHideClearEditAttrs_passwordTogDrawableOpen);
            passwordTogDrawableClose = ta.getDrawable(R.styleable.autoHideClearEditAttrs_passwordTogDrawableClose);
            mCloseDrawable = ta.getDrawable(R.styleable.autoHideClearEditAttrs_closeTogDrawable);

            if (mCloseDrawable != null) {
                mCloseDrawable.setBounds(0, 0, mCloseDrawable.getIntrinsicWidth(), mCloseDrawable.getIntrinsicHeight());
            }

            if (mPasswordTogDrawableOpen != null) {
                mPasswordTogDrawableOpen.setBounds(0, 0, mPasswordTogDrawableOpen.getIntrinsicWidth(), mPasswordTogDrawableOpen.getIntrinsicHeight());
            }
            if (passwordTogDrawableClose != null) {
                passwordTogDrawableClose.setBounds(0, 0, passwordTogDrawableClose.getIntrinsicWidth(), passwordTogDrawableClose.getIntrinsicHeight());
            }

            mCloseEnabled = ta.getBoolean(R.styleable.autoHideClearEditAttrs_closeTogEnabled, false);
            mPasswordEnabled = ta.getBoolean(R.styleable.autoHideClearEditAttrs_passwordTogEnabled, false);

            ta.recycle();
        }



    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        if (mCloseEnabled) {
            if (isFocused && getText().toString().length() > 0) {
                closeDraw(canvas);
            }

        }

        if (isFocused && mPasswordEnabled && getText().toString().length() > 0) {
            if (mPasswordVisible) {

                passwrodDrawOpen(canvas);
            } else {

                passwrodDrawClose(canvas);
            }
        }
    }

    private void closeDraw(Canvas canvas) {

        canvas.save();
        canvas.translate(getWidth() - mCloseDrawable.getIntrinsicWidth(), getHeight() / 2 - mCloseDrawable.getIntrinsicHeight() / 2);
        if (mCloseDrawable != null) {
            mCloseDrawable.draw(canvas);
        }
        canvas.restore();
    }

    private void passwrodDrawOpen(Canvas canvas) {

        canvas.save();
        canvas.translate(getWidth() - mPasswordTogDrawableOpen.getIntrinsicWidth() * 2 - 15, getHeight() / 2 - mPasswordTogDrawableOpen.getIntrinsicHeight() / 2);
        if (mPasswordTogDrawableOpen != null) {
            mPasswordTogDrawableOpen.draw(canvas);
        }
        canvas.restore();
    }

    private void passwrodDrawClose(Canvas canvas) {

        canvas.save();
        canvas.translate(getWidth() - passwordTogDrawableClose.getIntrinsicWidth() * 2 - 15, getHeight() / 2 - passwordTogDrawableClose.getIntrinsicHeight() / 2);
        if (passwordTogDrawableClose != null) {
            passwordTogDrawableClose.draw(canvas);
        }
        canvas.restore();
    }


    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {

        isFocused = focused;

        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:

                if (mCloseEnabled
                        && event.getX() > getWidth() - mCloseDrawable.getIntrinsicWidth()) {
                    setText("");
                    return true;
                }

                if (mPasswordEnabled
                        && event.getX() > getWidth() - mCloseDrawable.getIntrinsicWidth() * 2 - 15
                        && event.getY() > 0
                        && event.getY() < getHeight()
                        ) {
                    if (mPasswordVisible) {
                        setTransformationMethod(PasswordTransformationMethod.getInstance());
                        mPasswordVisible = false;
                    } else {
                        setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        mPasswordVisible = true;
                    }
                    return true;
                }
                break;

        }

        return super.onTouchEvent(event);
    }
}
