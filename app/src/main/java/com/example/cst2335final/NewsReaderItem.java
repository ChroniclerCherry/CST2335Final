package com.example.cst2335final;

//TODO Java docs/comments/descrip
//todo create a NewsReaderItem object that will be added to db and displayed in list in NewsReaderView
public class NewsReaderItem extends Object {

    private String title;
    private String date;
    private String link;
    private String desc;

    public NewsReaderItem() { }

    public NewsReaderItem(String title) {
        this.title = title;
    }

    public NewsReaderItem (String title, String desc, String date, String link) {
        this.title = title;
        this.desc = desc;
        this.date = date;
        this.link = link;
    }

    public void setTitle(String title) { this.title = title; }

    public String getTitle() {
        return title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

}