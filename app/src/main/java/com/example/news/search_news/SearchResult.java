package com.example.news.search_news;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.news.MyBaseAdapter;
import com.example.news.NewsInfo;
import com.example.news.NewsUtil;
import com.example.news.ParsingJSON;
import com.example.news.R;
import com.example.news.sqlite.MyDbHelp;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;


public class SearchResult extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, TextWatcher {
    private ImageView btn_return, search_news;
    private EditText condition;
    private ListView search_result;
    private ArrayList<HashMap<String,String>> list;
    private MyDbHelp myDbHelp;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        myDbHelp = new MyDbHelp(SearchResult.this,"mydb.db",null,1);
        findViewId();
        setOnClickListener();
        initData();
    }

    private void findViewId() {
        btn_return = findViewById(R.id.btn_return);
        search_news = findViewById(R.id.search_news);
        condition = findViewById(R.id.condition);
        search_result = findViewById(R.id.search_result);

        Intent intent = getIntent();
        String str = intent.getStringExtra("keyWord");
        condition.setText(str);
        searchNews(str);
    }

    private void setOnClickListener() {
        btn_return.setOnClickListener(this);
        search_news.setOnClickListener(this);

        condition.addTextChangedListener(this);

        search_result.setOnItemClickListener(this);
    }

    private void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_return:
                finish();
                break;
            case R.id.search_news:
                String str = condition.getText().toString().trim();
                searchNews(str);

//                如果有相同的数据，先进行删除再插入数据，这样就可以改变搜索时间了
                sqLiteDatabase = myDbHelp.getWritableDatabase();
                sqLiteDatabase.delete("search_history","name = ?",new String[]{str});

                sqLiteDatabase = myDbHelp.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("name",str);
                sqLiteDatabase.insert("search_history",null,contentValues);
                sqLiteDatabase.close();
                break;
        }
    }

    private void searchNews(String key) {
        String str =  condition.getText().toString().trim();
        if (str.equals(""))
        {
            Toast.makeText(SearchResult.this,"搜索内容不能为空",Toast.LENGTH_LONG);
            return;
        }
        String path = "https://api.jisuapi.com/news/search?keyword=" + key + "&appkey=0fb7a9ae38ebc1c4";
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(1000);
        asyncHttpClient.get(path, new AsyncHttpResponseHandler() {
            //            异步请求成功
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String strJson = new String(responseBody, "utf-8");
                    ArrayList<NewsUtil> arrayList = ParsingJSON.putInUtil(strJson);
                    if (!arrayList.get(0).getMsg().equals("ok")) {
                        Toast.makeText(SearchResult.this, "请求次数超过限制", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    show(arrayList);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }

            //            异步请求失败
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(SearchResult.this, "请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void show(ArrayList<NewsUtil> arrayList) {
        MyBaseAdapter myBaseAdapter = new MyBaseAdapter(SearchResult.this, arrayList);
        search_result.setAdapter(myBaseAdapter);
        ArrayList<NewsUtil> array_list = myBaseAdapter.getArrayList();
        list = new ArrayList<>();
        for (int i = 0; i < array_list.size(); i++) {
            NewsUtil newsUtil = array_list.get(i);
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("src", newsUtil.getSrc());
            hashMap.put("time", newsUtil.getTime());
            hashMap.put("title", newsUtil.getTitle());
            hashMap.put("content", newsUtil.getContent());
            hashMap.put("url", newsUtil.getUrl());
            list.add(hashMap);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String,String> hashMap = list.get(position);
//        使用Intent传送数据
        Intent intent = new Intent(SearchResult.this, NewsInfo.class);
        intent.putExtra("src",hashMap.get("src"));
        intent.putExtra("time",hashMap.get("time"));
        intent.putExtra("title",hashMap.get("title"));
        intent.putExtra("content",hashMap.get("content"));
        intent.putExtra("url",hashMap.get("url"));
        startActivity(intent);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String str = String.valueOf(s);
        searchNews(str);
    }
}