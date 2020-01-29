package com.example.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.news.loopj.android.image.SmartImageView;

import java.util.ArrayList;

public class MyBaseAdapter extends BaseAdapter {
    public Context context;
    public ArrayList<NewsUtil> arrayList;
    public ViewHolder viewHolder;

    //    通过构造方法来为本类的常量赋值，从而使其他方法避开空指针异常
    public MyBaseAdapter(Context context, ArrayList<NewsUtil> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    //    返回list的条目数
    @Override
    public int getCount() {
        return arrayList.size();
    }

    //    返回第i个条目
    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    //    将id返回
    @Override
    public long getItemId(int i) {
        return i;
    }

    //    返回视图view
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
//            实例化类一定要在getView里实例化，否则会出现listView重复使用的现象
            viewHolder = new ViewHolder();
//            如果view为null，先用布局孵化器的inflate方法将布局文件孵化为视图
            view = LayoutInflater.from(context).inflate(R.layout.news_list, null);
            viewHolder.pic = view.findViewById(R.id.pic);
            viewHolder.title = view.findViewById(R.id.title);
            viewHolder.src = view.findViewById(R.id.src);
            viewHolder.time = view.findViewById(R.id.time);
            view.setTag(viewHolder);
        }
//        如果view不为空，调用视图的getTag方法将视图取出
        viewHolder = (ViewHolder) view.getTag();
//        得到图片的地址
        String imgUrl = arrayList.get(i).getPic();
//        SmartImageView提供了一个setImageUrl的方法，可以通过地址显示图片
        viewHolder.pic.setImageUrl(imgUrl);

        String title = arrayList.get(i).getTitle();
        viewHolder.title.setText(title);

        String src = arrayList.get(i).getSrc();
        viewHolder.src.setText(src);

        String time = arrayList.get(i).getTime();
        viewHolder.time.setText(time);

        return view;
    }

    public ArrayList<NewsUtil> getArrayList() {
        return arrayList;
    }

    static class ViewHolder {
        SmartImageView pic;
        TextView title;
        TextView src;
        TextView time;
    }
}
