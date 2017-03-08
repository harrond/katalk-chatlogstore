package com.example.hoyoung.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Hoyoung on 2017-01-29.
 */

public class DBHelper extends SQLiteOpenHelper{
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE chatroom (_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,date TEXT,subtitle TEXT);");
        db.execSQL("CREATE TABLE chatitem (_id INTEGER PRIMARY KEY AUTOINCREMENT,type INTEGER,name TEXT,msg TEXT,date TEXT,time TEXT,roomid INTEGER);");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }
    public void insertChatItem(int attribute,String name,String msg,String date,String time,int roomid){ //이건 생각좀 해봐야됨
        SQLiteDatabase db = getWritableDatabase();
        String tmp = msg.replaceAll("'","''");
        db.execSQL("insert into chatitem values(null,"+attribute+",'"+name+ "','"+tmp+"','"+date+"','"+time+"',"+roomid+");");
        db.close();
    }
    public void insertChatItemList(ArrayList list,String name){ //이건 생각좀 해봐야됨
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from chatroom where name='"+name+"'",null);
        int id=0;
        while(cursor.moveToNext()){
            id=cursor.getInt(cursor.getColumnIndex("_id"));
        }
        cursor.close();
        db.close();
        SQLiteDatabase wdb = getWritableDatabase();
        wdb.beginTransaction();
        try{
            for(Object tmp : list){
                MsgItem item=(MsgItem) tmp;
                String str = item.getMsg().replaceAll("'","''");
                wdb.execSQL("insert into chatitem values(null,"+item.getType()+",'"+item.getName()+ "','"+str+"','"+item.getDate()+"','"+item.getTime()+"',"+id+");");
                //insertChatItem(item.getType(),item.getName(),item.getMsg(),item.getDate(),item.getTime(),id);
            }
            wdb.setTransactionSuccessful();
        }catch (Exception e){

        }finally {
            wdb.endTransaction();
        }

        wdb.close();
    }
    public void insertChatRoom(String roomName,String date,String subTitle){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("insert into chatroom values(null,'"+roomName+"','"+date+ "','"+subTitle+"');");
        db.close();
    }
    public void updateChatRoomName(int id,String roomName){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("update chatroom SET name='"+roomName+"' where _id="+id+";");

        db.close();
    }
    public void removeChatRoom(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from chatroom where _id="+id+";");
        db.execSQL("delete from chatitem where roomid="+id+";");
        db.close();
    }
    public void removeChatRoomAll(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from chatroom;");
        db.execSQL("delete from chatitem;");
        db.close();
    }
    public ArrayList<ChatListItem> getChatList(){
        ArrayList<ChatListItem> list=new ArrayList<ChatListItem>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * from chatroom",null);
        while(cursor.moveToNext()){
            list.add(new ChatListItem(cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("date")),
                    cursor.getString(cursor.getColumnIndex("subtitle"))
                    )
            );

        }
        cursor.close();
        db.close();
        return list;

    }
//int id,String msg,int type,String date,String time,String name,int roomid)
    public ArrayList<MsgItem> getMsg(int id){
        ArrayList<MsgItem> list=new ArrayList<MsgItem>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor=db.rawQuery("SELECT * from chatitem where roomid="+id,null);

        while(cursor.moveToNext()){
            list.add(new MsgItem(cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("msg")),
                    cursor.getInt(cursor.getColumnIndex("type")),
                    cursor.getString(cursor.getColumnIndex("date")),
                    cursor.getString(cursor.getColumnIndex("time")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getInt(cursor.getColumnIndex("roomid")))
            );
        }
        cursor.close();
        db.close();

        return list;
    }

}
