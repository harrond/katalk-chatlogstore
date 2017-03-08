package com.example.hoyoung.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Hoyoung on 2017-01-30.
 */

public class ChatActivity extends AppCompatActivity {
    ListView m_ListView;
    CustomAdapter m_Adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

       Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.parseColor("#FF8C00"));
        }


        m_Adapter = new CustomAdapter();

        m_ListView = (ListView)findViewById(R.id.listView1);


        m_ListView.setAdapter(m_Adapter);
        Intent intent= new Intent(this.getIntent());

        ArrayList<MsgItem> list =(ArrayList<MsgItem>) ChatListHolder.getList();
        m_Adapter.add(list);
        toolbar.setTitle(intent.getStringExtra("name"));


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
