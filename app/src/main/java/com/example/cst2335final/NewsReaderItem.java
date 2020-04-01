package com.example.cst2335final;

//TODO Java docs/comments/descrip
//todo create a NewsReaderItem object that will be added to db and displayed in list in NewsReaderView
public class NewsReaderItem extends Object {

    private String title;
    private String date;
    private String link;
    private String desc;
    private String note;

    public NewsReaderItem() { }

    //public NewsReaderItem(String title) {
        //this.title = title;
   // }

    public NewsReaderItem (String title, String desc, String date, String link, String note) {
        this.title = title;
        this.desc = desc;
        this.date = date;
        this.link = link;
        this.note = note;
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

    public void setNote(String link) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }
}