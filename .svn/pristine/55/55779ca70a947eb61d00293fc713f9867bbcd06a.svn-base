/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tyt.bbs.dialog;

import com.tyt.bbs.utils.Property;

import android.os.Bundle;
import android.app.Dialog;
import android.content.Context;
import android.graphics.*;
import android.view.MotionEvent;
import android.view.View;

public class ColorPickerDialog extends Dialog {

    public interface OnColorChangedListener {
        void colorChanged(int color);
    }

    private OnColorChangedListener mListener;
    private int mInitialColor;

    private static class ColorPickerView extends View {
    	private Paint mPaint;  
        private Paint mCenterPaint;  
        private Paint mHSVPaint;  
        private final int[] mColors;  
        private int[] mHSVColors;  
        private boolean mRedrawHSV;  
        private OnColorChangedListener mListener;  
        private boolean mTrackingCenter;  
        private boolean mHighlightCenter;  
        private static final int CENTER_X = (int) (100*Property.Ratio);  
        private static final int CENTER_Y = CENTER_X;  
        private static final int CENTER_RADIUS = (int) (CENTER_X*0.3);  
        private static final int OUTER_RADIUS = CENTER_X;  
        private static final int HSV_X = CENTER_X;  
        private static final int HSV_Y_TOP = CENTER_Y + 10;  
        private static final int HSV_Y_BOTOM = HSV_Y_TOP + 20;  
        private static final float PI = 3.1415926f;  
        public ColorPickerView(Context context, OnColorChangedListener listener,  
                int color) {  
            super(context);  
            this.setBackgroundColor(Color.LTGRAY);  
            mListener = listener;  
            mColors = new int[] { 0xFFFF0000, 0xFFFF00FF, 0xFF0000FF, 0xFF00FFFF,  
                    0xFF00FF00, 0xFFFFFF00, 0xFFFF0000 };  
            Shader s = new SweepGradient(0, 0, mColors, null);  
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);  
            mPaint.setShader(s);  
            mPaint.setStyle(Paint.Style.STROKE);  
            mPaint.setStrokeWidth(55);  
            mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);  
            mCenterPaint.setColor(color);  
            mCenterPaint.setStrokeWidth(5);  
            mHSVColors = new int[] { 0xFF000000, color, 0xFFFFFFFF };  
            mHSVPaint = new Paint(Paint.ANTI_ALIAS_FLAG);  
            mHSVPaint.setStrokeWidth(10);  
            mRedrawHSV = true;  
        }  
        @Override  
        protected void onDraw(Canvas canvas) {  
            float r = CENTER_X - mPaint.getStrokeWidth() * 0.5f;  
            canvas.translate(CENTER_X, CENTER_X);  
            int c = mCenterPaint.getColor();  
            if (mRedrawHSV) {  
                mHSVColors[1] = c;  
                mHSVPaint.setShader(new LinearGradient(0 - HSV_X, 0, HSV_X, 0,  
                        mHSVColors, null, Shader.TileMode.CLAMP));  
            }  
            canvas.drawOval(new RectF(-r, -r, r, r), mPaint);  
            canvas.drawCircle(0, 0, CENTER_RADIUS, mCenterPaint);  
            canvas.drawRect(new RectF(0 - HSV_X, HSV_Y_TOP, HSV_X, HSV_Y_BOTOM),  
                    mHSVPaint);  
            if (mTrackingCenter) {  
                mCenterPaint.setStyle(Paint.Style.STROKE);  
                if (mHighlightCenter) {  
                    mCenterPaint.setAlpha(0xFF);  
                } else {  
                    mCenterPaint.setAlpha(0x80);  
                }  
                canvas.drawCircle(0, 0, CENTER_RADIUS  
                        + mCenterPaint.getStrokeWidth(), mCenterPaint);  
                mCenterPaint.setStyle(Paint.Style.FILL);  
                mCenterPaint.setColor(c);  
            }  
            mRedrawHSV = true;  
        }  
        @Override  
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
            setMeasuredDimension(CENTER_X * 2, HSV_Y_BOTOM * 2 - 20);  
        }  
        @Override  
        public boolean onTouchEvent(MotionEvent event) {  
            float x = event.getX() - CENTER_X;  
            float y = event.getY() - CENTER_Y;  
            double radius = Math.sqrt(x * x + y * y);  
            boolean inCenter = radius <= CENTER_RADIUS;  
            boolean inOuter = radius <= OUTER_RADIUS;  
            switch (event.getAction()) {  
            case MotionEvent.ACTION_DOWN:  
                mTrackingCenter = inCenter;  
                if (inCenter) {  
                    mHighlightCenter = true;  
                    invalidate();  
                    break;  
                }  
            case MotionEvent.ACTION_MOVE:  
                if (mTrackingCenter) {  
                    if (mHighlightCenter != inCenter) {  
                        mHighlightCenter = inCenter;  
                        invalidate();  
                    }  
                } else if ((x >= 0 - HSV_X && x <= HSV_X)  
                        && (y <= HSV_Y_BOTOM && y >= HSV_Y_TOP)) {  
                    // see if we are in the hsv slider  
                    int a, r, g, b, c0, c1;  
                    float p;  
                    // set the center paint to this color  
                    if (x < 0) {  
                        c0 = mHSVColors[0];  
                        c1 = mHSVColors[1];  
                        p = (x + 100) / 100;  
                    } else {  
                        c0 = mHSVColors[1];  
                        c1 = mHSVColors[2];  
                        p = x / 100;  
                    }  
                    a = ave(Color.alpha(c0), Color.alpha(c1), p);  
                    r = ave(Color.red(c0), Color.red(c1), p);  
                    g = ave(Color.green(c0), Color.green(c1), p);  
                    b = ave(Color.blue(c0), Color.blue(c1), p);  
                    mCenterPaint.setColor(Color.argb(a, r, g, b));  
                    mRedrawHSV = false;  
                    invalidate();  
                } else if (inOuter) {  
                    float angle = (float) Math.atan2(y, x);  
                    // need to turn angle [-PI ... PI] into unit [0....1]  
                    float unit = angle / (2 * PI);  
                    if (unit < 0) {  
                        unit += 1;  
                    }  
                    mCenterPaint.setColor(interpColor(mColors, unit));  
                    invalidate();  
                }  
                break;  
            case MotionEvent.ACTION_UP:  
                if (mTrackingCenter) {  
                    if (inCenter && mListener != null) {  
                        mListener.colorChanged(mCenterPaint.getColor());  
                    }  
                    mTrackingCenter = false;  
                    invalidate();  
                }  
                break;  
            }  
            return true;  
        }  
        private int interpColor(int colors[], float unit) {  
            if (unit <= 0) {  
                return colors[0];  
            }  
            if (unit >= 1) {  
                return colors[colors.length - 1];  
            }  
            float p = unit * (colors.length - 1);  
            int i = (int) p;  
            p -= i;  
            // now p is just the fractional part [0...1) and i is the index  
            int c0 = colors[i];  
            int c1 = colors[i + 1];  
            int a = ave(Color.alpha(c0), Color.alpha(c1), p);  
            int r = ave(Color.red(c0), Color.red(c1), p);  
            int g = ave(Color.green(c0), Color.green(c1), p);  
            int b = ave(Color.blue(c0), Color.blue(c1), p);  
            return Color.argb(a, r, g, b);  
        }  
        private int ave(int s, int d, float p) {  
            return s + Math.round(p * (d - s));  
        }  
        public int getColor() {  
            return mCenterPaint.getColor();  
        }  
        public void setColor(int color) {  
            mCenterPaint.setColor(color);  
        }  
    }

    public ColorPickerDialog(Context context,
                             OnColorChangedListener listener,
                             int initialColor) {
        super(context);
        
        mListener = listener;
        mInitialColor = initialColor;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnColorChangedListener l = new OnColorChangedListener() {
            public void colorChanged(int color) {
                mListener.colorChanged(color);
                dismiss();
            }
        };

        setContentView(new ColorPickerView(getContext(), l, mInitialColor));
        setTitle("Pick a Color");
    }
}
