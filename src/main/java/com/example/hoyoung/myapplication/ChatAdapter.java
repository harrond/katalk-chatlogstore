package com.example.hoyoung.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Hoyoung on 2017-01-27.
 */

public class ChatAdapter extends BaseAdapter {
    private ArrayList list;

    public ChatAdapter(){
        list = new ArrayList();
    }


    @Override
    public int getCount(){
        return list.size();
    }
    @Override
    public long getItemId(int position){
        return position;
    }
    @Override
    public Object getItem(int position){
        return list.get(position);
    }
    public void add(int id,String name,String date,String subtitle){
        list.add(new ChatListItem(id,name,date,subtitle));
    }
    public void add(ArrayList temp){
        list=temp;
    }
    public void remove(int position){
        list.remove(position);
    }
    public void modifyName(int index,String name){
        ChatListItem item = ((ChatListItem)list.get(index));
        item.setName(name);
        list.set(index,item);
    }
    public void removeAll(){
        list.clear();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final int pos = position;
        final Context context = parent.getContext();
        View view =convertView;

      //  if(view ==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item,parent,false);
            ImageView profile = (ImageView)view.findViewById(R.id.profile_image);
            TextView name = (TextView)view.findViewById(R.id.name);
            TextView chat = (TextView)view.findViewById(R.id.chat);
            ChatListItem item = ((ChatListItem)getItem(pos));
            //profile.setImageResource(item.getProfile());
            profile.setImageResource(R.drawable.a);
            name.setText(item.getName());
            chat.setText(item.getDate());

        //}
        return view;
    }

}
