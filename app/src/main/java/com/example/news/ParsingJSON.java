package com.example.news;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParsingJSON {
    public static ArrayList<NewsUtil> putInUtil(String StringInput)
    {
        try {
            ArrayList<NewsUtil> list = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(StringInput);
            String msg = jsonObject.getString("msg");
            if (!msg.equals("ok"))
            {
                NewsUtil newsUtil = new NewsUtil();
                newsUtil.setMsg(msg);
                list.add(newsUtil);
                return list;
            }
            JSONObject result = jsonObject.getJSONObject("result");
            JSONArray jsonArray = result.getJSONArray("list");
            for (int i = 0;i < jsonArray.length();i++)
            {
                JSONObject jsonObjectList = jsonArray.getJSONObject(i);
                String title = jsonObjectList.getString("title");
                String time = jsonObjectList.getString("time");
                String src = jsonObjectList.getString("src");
                String pic = jsonObjectList.getString("pic");
                String content = jsonObjectList.getString("content");
                String url = jsonObjectList.getString("url");
                String weburl = jsonObjectList.getString("weburl");

                NewsUtil newsUtil = new NewsUtil();
                newsUtil.setMsg(msg);
                newsUtil.setTitle(title);
                newsUtil.setTime(time);
                newsUtil.setSrc(src);
                newsUtil.setPic(pic);
                newsUtil.setContent(content);
                newsUtil.setUrl(url);
                newsUtil.setWeburl(weburl);

                list.add(newsUtil);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
