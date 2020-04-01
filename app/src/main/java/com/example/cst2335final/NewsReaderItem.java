package com.example.cst2335final;

/**
 * Class to create NewsReaderItem object
 *  @Author Lia Brophy
 *  @Version 1.0
 *  @Date 2020-04-01
 */
public class NewsReaderItem extends Object {

    private String title;
    private String date;
    private String link;
    private String desc;
    private String note;

    /**
     * Default constructor
     */
    public NewsReaderItem() { }

    /**
     * Parameterized constructor
     */
    public NewsReaderItem (String title, String desc, String date, String link, String note) {
        this.title = title;
        this.desc = desc;
        this.date = date;
        this.link = link;
        this.note = note;
    }

    /**
     * Access to set object title
     * @param title
     */
    public void setTitle(String title) { this.title = title; }

    /**
     * Access to get object title
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * Access to set object desc
     * @param desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * Access to get object desc
     * @return
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Access to set object date
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Access to get object date
     * @return
     */
    public String getDate() {
        return date;
    }

    /**
     * Access to set object link
     * @param link
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Access to get object link
     * @return
     */
    public String getLink() {
        return link;
    }

    /**
     * Access to set object note
     * @param link
     */
    public void setNote(String link) {
        this.note = note;
    }

    /**
     * Access to get object note
     * @return
     */
    public String getNote() {
        return note;
    }
}