package com.example.hoyoung.myapplication;

import java.io.Serializable;

/**
 * Created by Hoyoung on 2017-01-30.
 */

public class MsgItem  implements Serializable {
    private int id;
    private String msg;
    private int type;
    private String date;
    private String time;
    private String name;
    private int roomid;
    public MsgItem(int id,String msg,int type,String date,String time,String name,int roomid){
        this.id=id;
        this.msg=msg;
        this.type=type;
        this.date=date;
        this.time=time;
        this.name=name;
        this.roomid=roomid;
    }
    public MsgItem(String msg,int type,String time,String name){
        this.msg=msg;
        this.type=type;
        this.time=time;
        this.name=name;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public int getRoomid() {
        return roomid;
    }

    public String getMsg() {
        return msg;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }
}
