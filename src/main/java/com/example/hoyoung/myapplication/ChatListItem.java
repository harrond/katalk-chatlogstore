package com.example.hoyoung.myapplication;

/**
 * Created by Hoyoung on 2017-01-28.
 */

public class ChatListItem {
    private int profile;
    int id;
    private String name;
    private String date;
    private String subtitle;
    public ChatListItem(int id,String name,String date,String subtitle){
        this.id=id;
        this.name=name;
        this.date=date;
        this.subtitle=subtitle;
        this.profile=id;
    }
    public int getProfile(){
        return profile;
    }
    public String getName(){
        return name;
    }
    public String getDate() { return date; }
    public String getSubtitle() { return subtitle; }
    public int getId() { return id; }
    public void setName(String name){
        this.name=name;
    }
}
