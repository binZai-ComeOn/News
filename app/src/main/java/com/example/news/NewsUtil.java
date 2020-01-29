package com.example.news;

public class NewsUtil {
    private String title;   //标题
    private String time;    //时间
    private String src;     //来源
    private String pic;     //图片
    private String content; //内容
    private String url;     //原文手机网址
    private String weburl;  //原文pc网址

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWeburl() {
        return weburl;
    }

    public void setWeburl(String weburl) {
        this.weburl = weburl;
    }

    @Override
    public String toString() {
        return "NewsUtil{" +
                "title='" + title + '\'' +
                ", time='" + time + '\'' +
                ", src='" + src + '\'' +
                ", pic='" + pic + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", weburl='" + weburl + '\'' +
                '}';
    }
}
