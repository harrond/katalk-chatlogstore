package com.example.hoyoung.myapplication;

import java.util.ArrayList;

/**
 * Created by Hoyoung on 2017-02-01.
 */

public class ChatListHolder {
    private static ArrayList listHolder;
    public static void setList(ArrayList list){
        listHolder=list;
    }
    public static ArrayList getList(){
        return listHolder;
    }
}
