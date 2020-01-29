package com.example.news;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.text.Html;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

public class HTMLAnalysis implements Html.ImageGetter {

    //    解析HTML格式
    public static String contentFilter(String content) {
        return String.valueOf(Html.fromHtml(content));
    }

    Context c;
    TextView container;
    int winWidth;

    public HTMLAnalysis(TextView text, Context c,int winWidth) {
        this.c = c;
        this.container = text;
        this.winWidth = winWidth;
    }

    public Drawable getDrawable(String source) {
        final LevelListDrawable drawable = new LevelListDrawable();
        Glide.with(c).load(source).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (resource != null) {
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(resource);
                    drawable.addLevel(1, 1,bitmapDrawable);
                    int w = resource.getWidth();
                    while (w > winWidth)
                    {
                        w--;
                    }
                    if (w == winWidth)
                    {
                        w = w - 72;
                    }
                    drawable.setBounds(0, 0, w, resource.getHeight());
                    drawable.setLevel(1);
                    container.invalidate();
                    container.setText(container.getText());
                }
            }
        });
        return drawable;
    }
}
