package com.example.hoyoung.myapplication;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Hoyoung on 2017-01-24.
 */
/*
public class ListContents{
    String msg;
    int type;
    ListContents(String _msg,int _type){
        this.msg=_msg;
        this.type=_type;
    }
}
*/

public class CustomAdapter extends BaseAdapter {

    private ArrayList m_List;
    private ArrayList profileList;
    private int[] image = {R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e,R.drawable.f,R.drawable.g,
            R.drawable.h,R.drawable.i};
    public CustomAdapter(){
        m_List=new ArrayList();
    }
    public void add(int id,String msg,int type,String date,String time,String name,int roomid){
        m_List.add(new MsgItem(id, msg, type, date, time, name, roomid));
    }
    public void add(ArrayList list){
        m_List=list;
        profileList=new ArrayList();

        for(Object tmp: m_List) {  //프로필추가부분
            MsgItem t = (MsgItem) tmp;
            if (t.getType() == 0) {
                if(!profileList.contains(t.getName()))
                    profileList.add(t.getName());
            }
        }
    }

    public void remove(int _position){
        m_List.remove(_position);
    }

    @Override
    public int getCount(){
        return m_List.size();
    }
    @Override
    public Object getItem(int position){
        return m_List.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final int pos =position;
        final Context context = parent.getContext();

        TextView text=null;
        TextView name =null;
        TextView timeR =null;
        TextView timeL =null;
        CustomHolder holder=null;
        LinearLayout layout=null;
        View viewRight=null;
        View viewLeft=null;
        ImageView profile=null; //프로필추가

        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.activity_chatitem,parent,false);

            layout=(LinearLayout)convertView.findViewById(R.id.layout);
            text=(TextView)convertView.findViewById(R.id.text);
            viewRight=(View)convertView.findViewById(R.id.imageViewright);
            viewLeft = (View)convertView.findViewById(R.id.imageViewleft);
            name = (TextView)convertView.findViewById(R.id.nametext);
            timeR = (TextView)convertView.findViewById(R.id.timetext2);
            timeL = (TextView)convertView.findViewById(R.id.timetext1);
            profile = (ImageView)convertView.findViewById(R.id.profile_imageIner); //프로필추가

            holder= new CustomHolder();
            holder.m_TextView = text;
            holder.layout=layout;
            holder.viewRight=viewRight;
            holder.viewLeft=viewLeft;
            holder.name=name;
            holder.timeR=timeR;
            holder.timeL=timeL;
            holder.profile=profile;  //추가
            convertView.setTag(holder);
        }
        else{
            holder=(CustomHolder) convertView.getTag();
            text=holder.m_TextView;
            layout=holder.layout;
            viewRight=holder.viewRight;
            viewLeft=holder.viewLeft;
            name=holder.name;
            timeR = holder.timeR;
            timeL=holder.timeL;
            profile=holder.profile; //프로필추가부분

        }
        MsgItem tmp=((MsgItem) m_List.get(position));
        int type = tmp.getType();
        name.setText("  "+tmp.getName());
        text.setText(tmp.getMsg());
        timeR.setText(tmp.getTime());
        timeL.setText(tmp.getTime());
        int a=-1;
        if(tmp.getType()==0) {
            a = profileList.indexOf(tmp.getName());
            if (a > 7) a = 7;
        }else a=7;
        profile.setImageResource(image[a]);

        if(type==0){
            text.setBackgroundResource(R.drawable.otherbox);
            layout.setGravity(Gravity.LEFT);
            profile.setVisibility(View.VISIBLE);
            name.setVisibility(View.VISIBLE);
            timeL.setVisibility(View.GONE);
            timeR.setVisibility(View.VISIBLE);
            viewRight.setVisibility(View.GONE);
            viewLeft.setVisibility(View.GONE);
        }
        else if(type==1){
            text.setBackgroundResource(R.drawable.mybox);
            name.setVisibility(View.GONE);
            profile.setVisibility(View.GONE);
            timeL.setVisibility(View.VISIBLE);
            timeR.setVisibility(View.GONE);
            layout.setGravity(Gravity.RIGHT);
            viewRight.setVisibility(View.GONE);
            viewLeft.setVisibility(View.GONE);
        }
        else if(type==2){
            text.setBackgroundResource(R.drawable.datebackground);
            layout.setGravity(Gravity.CENTER);
            profile.setVisibility(View.GONE);
            name.setVisibility(View.GONE);
            timeL.setVisibility(View.GONE);
            timeR.setVisibility(View.GONE);
            viewRight.setVisibility(View.VISIBLE);
            viewLeft.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
    private class CustomHolder{
        TextView m_TextView;
        TextView name;
        TextView timeR;
        TextView timeL;
        ImageView profile;
        LinearLayout layout;
        View viewRight;
        View viewLeft;
    }



}
