package com.example.news.search_news;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.news.R;
import com.example.news.sqlite.MyDbHelp;
import com.example.news.view.MyListView;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchNews extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, TextWatcher {
    private ImageView btn_return, search_news;
    private EditText condition;
    private TextView text;
    private ImageView trash;
    private MyListView search_history;
    private MyDbHelp myDbHelp;
    private SQLiteDatabase sqLiteDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_news);
        findViewId();
        setOnClickListener();
        initData();
    }

    private void findViewId() {
        btn_return = findViewById(R.id.btn_return);
        search_news = findViewById(R.id.search_news);
        condition = findViewById(R.id.condition);
        text = findViewById(R.id.text);
        trash = findViewById(R.id.trash);

        search_history = findViewById(R.id.search_history);

        myDbHelp = new MyDbHelp(SearchNews.this,"mydb.db",null,1);
    }

    private void setOnClickListener() {
        btn_return.setOnClickListener(this);
        search_news.setOnClickListener(this);
        trash.setOnClickListener(this);

        search_history.setOnItemClickListener(this);
//        添加输入框文本变化监听器
        condition.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_return:
                finish();
            break;
            case R.id.search_news:
                String str =  condition.getText().toString().trim();
                if (str.equals(""))
                {
                    break;
                }
                Intent intent = new Intent(SearchNews.this,SearchResult.class);
                intent.putExtra("keyWord",str);
                startActivity(intent);
//                如果有相同的数据，先进行删除再插入数据，这样就可以改变搜索时间了
                sqLiteDatabase = myDbHelp.getWritableDatabase();
                sqLiteDatabase.delete("search_history","name = ?",new String[]{str});

                sqLiteDatabase = myDbHelp.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("name",str);
                sqLiteDatabase.insert("search_history",null,contentValues);
                sqLiteDatabase.close();
             break;
            case R.id.trash:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("是否清空历史记录？");
                alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sqLiteDatabase = myDbHelp.getWritableDatabase();
                        sqLiteDatabase.delete("search_history",null,null);
                        sqLiteDatabase.close();
                        initData();
                    }
                });
                alertDialog.setNegativeButton("取消",null);
                alertDialog.show();
                break;
        }
    }

    public void initData() {
        sqLiteDatabase = myDbHelp.getReadableDatabase();
//         order by id desc为倒叙显示数据库数据
        Cursor cursor = sqLiteDatabase.rawQuery("select * from search_history order by _id desc",null);
        int i = 0;
        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
        while (cursor.moveToNext())
        {
            i++;
            HashMap<String,String> hashMap = new HashMap<>();
            String name = cursor.getString(cursor.getColumnIndex("name"));
            hashMap.put("name",name);
            arrayList.add(hashMap);
        }
        if (i > 0)
        {
            text.setText("搜索历史");
            trash.setClickable(true);
            trash.setImageResource(R.drawable.trash);
        }else {
            text.setText("");
            trash.setClickable(false);
            trash.setImageResource(R.drawable.liar);
        }
        sqLiteDatabase.close();
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,arrayList,R.layout.search_history,new String[]{"name"},new int[]{R.id.text});
        search_history.setAdapter(simpleAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        HashMap<String,String> hashMap = (HashMap<String, String>) adapterView.getItemAtPosition(i);
        condition.setText(hashMap.get("name"));
    }
//当活动可见时执行
    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
//    文本发生变化前
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }
//    文本发生变化时
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }
//    文本发生变化后
    @Override
    public void afterTextChanged(Editable editable) {
        String str = String.valueOf(editable);
        sqLiteDatabase = myDbHelp.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from search_history where name like '%"+str+"%' ORDER BY _id DESC",null);
        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
        while (cursor.moveToNext())
        {
            HashMap<String,String> hashMap = new HashMap<>();
            String name = cursor.getString(cursor.getColumnIndex("name"));
            hashMap.put("name",name);
            arrayList.add(hashMap);
        }
        sqLiteDatabase.close();
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,arrayList,R.layout.search_history,new String[]{"name"},new int[]{R.id.text});
        search_history.setAdapter(simpleAdapter);
    }
}