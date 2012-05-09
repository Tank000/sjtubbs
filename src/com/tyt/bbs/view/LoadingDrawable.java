package com.tyt.bbs.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;

/**
 * Loading Animation Drawable for mAPPn android project.
 *  
 * @author andrew.wang
 * @version 0.0.1
 */
public class LoadingDrawable extends AnimationDrawable {

    /**
     * LoadingDrawable style -- SMALL
     * 
     * (width: 54px, height: 12px)
     */
    public static final int SIZE_SMALL = 0;
    
    /**
     * LoadingDrawable style -- MEDIUM
     * 
     * (width: 90px, height: 20px)
     */
    public static final int SIZE_MEDIUM = 1;
    
    /**
     * LoadingDrawable style -- LARGE
     * 
     * (width: 180px, height: 40px)
     */
    public static final int SIZE_LARGE = 2;
    
    // default animation duration
    private static final int DURATION = 200;
    
    private static final int SPACE_SMALL = 6;
    private static final int SPACE_SMALL_H = 8;
    private static final int SPACE_MEDIUM = 10;
    private static final int SPACE_MEDIUM_H = 14;
    private static final int SPACE_LARGE = 20;
    private static final int SPACE_LARGE_H = 28;
    
    private static final int WIDTH_SMALL = 54;
    private static final int HEIGHT_SMALL = 12;
    private static final int WIDTH_MEDIUM = 90;
    private static final int HEIGHT_MEDIUM = 20;
    private static final int WIDTH_LARGE = 180;
    private static final int HEIGHT_LARGE = 40;
    
    private static final int BLOCK_NUMBER = 4;
    
    // highlight color
    private int color1;
    
    // default color
    private int color2;
    
    // the default loading style is small
    private int mLoadingStyle;
    
    // the background color of the loading drawable, 
    // use the color same as the background which to be attached 
    // FIXME: ATTENTION: can not be set with TRANSPARENT
    private int mBackgroundColor;
    
    // the animation duration
    private int mDuration;
    
    /**
     * The LoadingDrawable with default settings.<br>
     * 1 --> size style is medium<br>
     * 2 --> highlight color is "ffff9600"<br>
     * 3 --> default color is "ffcdcdcd"<br>
     * 4 --> background color is "Color.WHITE"<br>
     * <br>
     *  If you want to customize your own style, please reference 
     * <pre>LoadingDrawable(int style, int highlightColor, int defaultColor, int backgroundColor)</pre>
     */
    public LoadingDrawable() {
        this(SIZE_MEDIUM, Color.parseColor("#ffff9600"), Color.parseColor("#ffcdcdcd"), Color.WHITE,
                DURATION);
    }
    
    /**
     * The LoadingDrawable with customize style.<br>
     * 
     * @param style The size of loading drawable
     * @param highlightColor The higlight color
     * @param defaultColor The default color
     * @param backgroundColor The background color
     * @param duration The animation duration of each frame
     */
    public LoadingDrawable(int style, int highlightColor, int defaultColor, int backgroundColor, int duration) {
        
        // init configuration info
        mLoadingStyle = style;
        color1 = highlightColor;
        color2 = defaultColor;
        mBackgroundColor = backgroundColor;
        mDuration = duration;
        // repeat the animation
        setOneShot(false);
        
        float space = 0;
        float halfWidth = 0;
        float width = 0;
        float lightHalfWidth = 0;
        int totalWidth = 0;
        int totalHeight = 0;
        
        switch (mLoadingStyle) {
        case SIZE_SMALL:
            space = SPACE_SMALL;
            halfWidth = SPACE_SMALL / 2;
            width = SPACE_SMALL;
            lightHalfWidth = SPACE_SMALL_H / 2;
            totalWidth = WIDTH_SMALL;
            totalHeight = HEIGHT_SMALL;
            break;
        case SIZE_MEDIUM:
            space = SPACE_MEDIUM;
            halfWidth = SPACE_MEDIUM / 2;
            width = SPACE_MEDIUM;
            lightHalfWidth = SPACE_MEDIUM_H / 2;
            totalWidth = WIDTH_MEDIUM;
            totalHeight = HEIGHT_MEDIUM;
            break;
        case SIZE_LARGE:
            space = SPACE_LARGE;
            halfWidth = SPACE_LARGE / 2;
            width = SPACE_LARGE;
            lightHalfWidth = SPACE_LARGE_H / 2;
            totalWidth = WIDTH_LARGE;
            totalHeight = HEIGHT_LARGE;
            break;
        }
        
        Paint highlightColorPaint = new Paint();
        highlightColorPaint.setAntiAlias(true);
        highlightColorPaint.setColor(color1);
        highlightColorPaint.setStyle(Style.FILL);
        Paint normalColorPaint = new Paint();
        normalColorPaint.setAntiAlias(true);
        normalColorPaint.setColor(color2);
        normalColorPaint.setStyle(Style.FILL);
        Canvas canvas = new Canvas();
        int frameIndex = 0;
        while (frameIndex < BLOCK_NUMBER) {
            Bitmap frame = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888);
            canvas.setBitmap(frame);
            canvas.drawColor(mBackgroundColor);
            
            int startPositionX = 0;
            int startPositionY = totalHeight / 2;
            int blockIndex = 0;
            while (blockIndex < BLOCK_NUMBER) {
                // try to draw 4 blocks on the bitmap
                float left = 0;
                float top = 0;
                float right = 0;
                float bottom = 0;

                if (blockIndex == 0) {
                    startPositionX += (space + halfWidth);
                } else {
                    startPositionX += (space + width);
                }
                Paint paint;
                if (blockIndex == frameIndex) {
                    // current loading item is bigger
                    left = startPositionX - lightHalfWidth;
                    top = startPositionY - lightHalfWidth;
                    right = startPositionX + lightHalfWidth;
                    bottom = startPositionY + lightHalfWidth;
                    paint = highlightColorPaint;
                } else {
                    // other item is default size
                    left = startPositionX - halfWidth;
                    top = startPositionY - halfWidth;
                    right = startPositionX + halfWidth;
                    bottom = startPositionY + halfWidth;
                    paint = normalColorPaint;
                }
                canvas.drawRect(left, top, right, bottom, paint);
                
                blockIndex++;
            }
            frameIndex++;
            addFrame(new BitmapDrawable(frame), mDuration);
        }
    }

    @Override
    public int getMinimumHeight() {
        switch (mLoadingStyle) {
        case SIZE_SMALL:
            return HEIGHT_SMALL;
        case SIZE_MEDIUM:
            return HEIGHT_MEDIUM;
        case SIZE_LARGE:
            return HEIGHT_LARGE;
        default:
            break;
        }
        return super.getMinimumHeight();
    }

    @Override
    public int getMinimumWidth() {
        switch (mLoadingStyle) {
        case SIZE_SMALL:
            return WIDTH_SMALL;
        case SIZE_MEDIUM:
            return WIDTH_MEDIUM;
        case SIZE_LARGE:
            return WIDTH_LARGE;
        default:
            break;
        }
        return super.getMinimumWidth();
    }

}