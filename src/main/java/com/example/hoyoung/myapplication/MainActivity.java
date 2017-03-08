package com.example.hoyoung.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView chatListView;
    private ChatAdapter chatAdapter;
    DBHelper dbHelper;
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            // Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, SplashActivity.class));

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("저장된 채팅목록");

        if(Build.VERSION.SDK_INT >=23) {
            verifyStoragePermissions(this);
        }

        setSupportActionBar(toolbar);
        dbHelper = new DBHelper(getApplicationContext(), "ChatStorage.db", null, 1);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        chatListView = (ListView) findViewById(R.id.list);
        chatAdapter = new ChatAdapter();
        chatListView.setAdapter(chatAdapter);

        chatListView.setOnItemClickListener(new ListViewItemClickListener());
        chatListView.setOnItemLongClickListener(new ListViewItemLongClickListener());
        chatAdapter.add(dbHelper.getChatList());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //+버튼 클릭시
                OpenDialog _Dialog = new OpenDialog(MainActivity.this);
                _Dialog.setOnFileSelected(new OnFileSelectedListener() {
                                              @Override
                                              public void onSelected(String path, String fileName) {
                                                  if (fileName.length() > 0) {
                                                      ChatTxtParsing parsing = new ChatTxtParsing(path, fileName);
                                                      ArrayList msgList = parsing.parsing();
                                                      dbHelper.insertChatRoom(parsing.getRoomName(), parsing.getCreateDate(), parsing.getCreateDate());

                                                      chatAdapter.add(dbHelper.getChatList());
                                                      chatAdapter.notifyDataSetChanged();
                                                      dbHelper.insertChatItemList(msgList, parsing.getRoomName());
                                                  } else {
                                                      Toast.makeText(MainActivity.this, "파일명을 입력 해주세요.", Toast.LENGTH_LONG).show();
                                                  }
                                              }
                                          }
                );

                _Dialog.setOnCanceled(new OnNotifyEventListener() {

                    @Override

                    public void onNotify(Object sender) {

                        Toast.makeText(MainActivity.this, "선택이 취소 되었습니다.", Toast.LENGTH_LONG).show();

                    }

                });

                _Dialog.Show();
                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //메뉴 선택시
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            chatAdapter.removeAll();

            chatAdapter.notifyDataSetChanged();
            dbHelper.removeChatRoomAll();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private class ListViewItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ChatListItem item = (ChatListItem) parent.getItemAtPosition(position);
            Intent intent = new Intent(MainActivity.this, ChatActivity.class);

            ArrayList tmpList = dbHelper.getMsg(item.getId());
            ChatListHolder.setList(tmpList);
            intent.putExtra("name", item.getName());
            startActivity(intent);
        }

    }

    private class ListViewItemLongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            PopupMenu popup = new PopupMenu(MainActivity.this, view);
            getMenuInflater().inflate(R.menu.menu_chatoption, popup.getMenu());
            final ChatListItem chatitem = (ChatListItem) parent.getItemAtPosition(position);
            final int index = position;
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.modify:
                            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                            alert.setTitle("채팅 기록 이름 설정");
                            alert.setMessage("채팅 기록 이름을 입력해주세요.");
                            final EditText input = new EditText(MainActivity.this);
                            alert.setView(input);
                            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String name = input.getText().toString();
                                    chatAdapter.modifyName(index, name);
                                    chatAdapter.notifyDataSetChanged();
                                    dbHelper.updateChatRoomName(chatitem.getId(), name);
                                }
                            });
                            alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }
                            });
                            alert.show();

                            break;
                        case R.id.delete:
                            chatAdapter.remove(index);

                            chatAdapter.notifyDataSetChanged();
                            dbHelper.removeChatRoom(chatitem.getId());

                            break;
                    }

                    return false;
                }
            });
            popup.show();


            return true;
        }
    }


}
