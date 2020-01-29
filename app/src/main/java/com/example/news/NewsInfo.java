package com.example.news;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class NewsInfo extends AppCompatActivity implements View.OnClickListener {
    private Button btn_return;
    private ImageView btn_menu;
    private TextView tv_title, tv_src, tv_time, tv_content, tv_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_info);
//        查找控件id
        findViewId();
//        控件点击事件
        setOnClickListener();
//        为TextView控件赋值
        assignment();
    }

    private void findViewId() {
        btn_return = findViewById(R.id.btn_return);
        btn_menu = findViewById(R.id.btn_menu);
        tv_title = findViewById(R.id.tv_title);
        tv_src = findViewById(R.id.tv_src);
        tv_time = findViewById(R.id.tv_time);
        tv_content = findViewById(R.id.tv_content);
        tv_url = findViewById(R.id.tv_url);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void assignment() {
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(point);
        int width = point.x;

        Intent intent = getIntent();
        tv_title.setText(intent.getStringExtra("title"));
        tv_src.setText(intent.getStringExtra("src"));
        tv_time.setText(intent.getStringExtra("time"));
        tv_content.setText(Html.fromHtml(intent.getStringExtra("content"), new HTMLAnalysis(tv_content, getApplicationContext(),width), null));
        tv_url.setText("本文来自：" + intent.getStringExtra("url"));

//        实现长按的时候触发手机自带的复制、粘贴、分享等功能
        tv_title.setTextIsSelectable(true);
        tv_src.setTextIsSelectable(true);
        tv_time.setTextIsSelectable(true);
        tv_content.setTextIsSelectable(true);
        tv_url.setTextIsSelectable(true);
    }

    private void setOnClickListener() {
        btn_return.setOnClickListener(this);
        btn_menu.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_return:
                finish();
                break;
            case R.id.btn_menu:
                showPopupMenu(btn_menu);
                break;
        }
    }

    private void showPopupMenu(ImageView view) {
//        View当前PopupMenu显示相对View的位置
        PopupMenu popupMenu = new PopupMenu(this, view);
//        获得menu布局，第一个参数是menu的布局，第二个参数使用PopupMenu来获得Menu
        popupMenu.getMenuInflater().inflate(R.menu.top_right_menu, popupMenu.getMenu());
//        Menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
//                获得点击的MenuItemId
                int menuItemId = menuItem.getItemId();
//                2131165310 是top_right_menu.xml文件的share的id
//                    复制原文的链接
//                    获取粘贴板管理器
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                Intent intent = getIntent();
//                    创建普通字符型ClipData 第一个参数是Labe 第二个参数 需要复制的内容
                ClipData clipData = ClipData.newPlainText("Label", intent.getStringExtra("url"));
//                    将ClipData的内容放到系统粘贴板
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(NewsInfo.this, "本文链接已复制", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
/*
        // PopupMenu弹出来后点击其他地方执行的代码
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                Toast.makeText(getApplicationContext(), "关闭PopupMenu", Toast.LENGTH_SHORT).show();
            }
        });*/
//      将PopupMenu显示出来
        popupMenu.show();
    }
}