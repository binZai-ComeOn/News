package com.example.news;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.news.network_status.NetworkConnectionStatus;
import com.example.news.search_news.SearchNews;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;


public class FramentNews extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ImageView iBtn_search;
    private ListView listView;
    private ArrayList<HashMap<String,String>> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

//        判断网络是否连接
        if (NetworkConnectionStatus.checkNetworkConnection(FramentNews.this) == true) {
//        查找控件Id
            findViewById();
//        控件点击事件
            setOnClickListener();
            getJson();
        } else {
            Toast.makeText(FramentNews.this, "网络未连接", Toast.LENGTH_SHORT).show();
        }
    }

    private void findViewById() {
        iBtn_search = findViewById(R.id.iBtn_search);
        listView = findViewById(R.id.lv_news);
    }

    private void setOnClickListener() {
        iBtn_search.setOnClickListener(this);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            搜索按钮
            case R.id.iBtn_search:
                startActivity(new Intent(FramentNews.this, SearchNews.class));
                break;
        }
    }

    //    listView点击事件
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        HashMap<String,String> hashMap = list.get(i);
//        使用Intent传送数据
        Intent intent = new Intent(FramentNews.this, NewsInfo.class);
        intent.putExtra("src",hashMap.get("src"));
        intent.putExtra("time",hashMap.get("time"));
        intent.putExtra("title",hashMap.get("title"));
        intent.putExtra("content",hashMap.get("content"));
        intent.putExtra("url",hashMap.get("url"));
        startActivity(intent);
    }

    //      使用BaseAdapter显示listView布局
    public void getJson()
    {
        String path = "https://api.jisuapi.com/news/get?channel=头条&start=0&num=40&appkey=0fb7a9ae38ebc1c4";
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.get(path, new AsyncHttpResponseHandler() {
            @Override
            //            异步请求成功
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {

                try {
                    String strJson = new String(responseBody,"utf-8");
                    ArrayList<NewsUtil> arrayList = ParsingJSON.putInUtil(strJson);
                    if (!arrayList.get(0).getMsg().equals("ok"))
                    {
                        Toast.makeText(FramentNews.this, "请求次数超过限制", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    show(arrayList);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(FramentNews.this, "请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void show(ArrayList<NewsUtil> newsInfos)
    {
        MyBaseAdapter myBaseAdapter = new MyBaseAdapter(FramentNews.this,newsInfos);
        listView.setAdapter(myBaseAdapter);
        ArrayList<NewsUtil> arrayList = myBaseAdapter.getArrayList();
        list = new ArrayList<>();
        for (int i = 0;i < arrayList.size();i++)
        {
            NewsUtil newsUtil = arrayList.get(i);
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("src",newsUtil.getSrc());
            hashMap.put("time",newsUtil.getTime());
            hashMap.put("title",newsUtil.getTitle());
            hashMap.put("content",newsUtil.getContent());
            hashMap.put("url",newsUtil.getUrl());
            list.add(hashMap);
        }
    }
}