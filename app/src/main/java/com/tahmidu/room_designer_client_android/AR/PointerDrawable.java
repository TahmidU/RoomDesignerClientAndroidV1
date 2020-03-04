package com.tahmidu.room_designer_client_android.AR;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PointerDrawable extends Drawable
{
    private final Paint PAINT = new Paint();
    private boolean enabled;

    private final int CIRCLE_RADIUS = 10;

    @Override
    public void draw(@NonNull Canvas canvas)
    {
        float cx = canvas.getWidth()/2;
        float cy = canvas.getHeight()/2;

        if(enabled)
        {
            PAINT.setColor(Color.GREEN);
            canvas.drawCircle(cx,cy, 10, PAINT);
        }else
        {
            PAINT.setColor(Color.GRAY);
            canvas.drawText("X", cx, cy, PAINT);
        }
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
