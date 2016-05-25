package com.example.sushant.messagingapp;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.sushant.messagingapp.Adapter.MessageListAdapter;
import com.example.sushant.messagingapp.POJO.SMSObject;

import java.util.ArrayList;
import java.util.Collections;

import de.greenrobot.event.EventBus;

public class MessageList extends AppCompatActivity {

    public static final String INBOX_URI = "content://sms/inbox";

    private RecyclerView recyclerView;
    public EventBus eventBus;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<SMSObject> smsObjectArrayList = new ArrayList<>();

    public static final int GET_RESPONSE = 1;
    public static final int POST_GET_RESPONSE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.contact_list);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        eventBus = new EventBus();
        eventBus.register(this);
        new HomeDataLoadThread(GET_RESPONSE).start();

    }

    @Override
    public void onResume() {
        new HomeDataLoadThread(GET_RESPONSE).start();
        super.onResume();
    }

    public class HomeDataLoadThread extends Thread {
        int requestType;
        boolean isThreadRunning = false;

        public HomeDataLoadThread(int requestType) {
            this.requestType = requestType;
        }

        @Override
        public void run() {
            super.run();
            try {
                if (isThreadRunning) {
                    return;
                }
                isThreadRunning = true;
                if(requestType == GET_RESPONSE) {

                    Uri inboxURI = Uri.parse(INBOX_URI);
                    ContentResolver cr = getContentResolver();
                    Cursor cursor = cr.query(inboxURI, null, null, null, null);

                    if (cursor != null && cursor.getCount() > 0) {
                        cursor.moveToFirst();

                        while (!cursor.isAfterLast()) {
                            int id = cursor.getInt(cursor.getColumnIndex("_id"));
                            String address = cursor.getString(cursor.getColumnIndex("address"));
                            String msgBody = cursor.getString(cursor.getColumnIndex("body"));
                            String readState = cursor.getString(cursor.getColumnIndex("read"));
                            String time = cursor.getString(cursor.getColumnIndex("date"));
                            smsObjectArrayList.add(new SMSObject(id, address, msgBody, readState, time));
                            cursor.moveToNext();
                        }
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                    eventBus.post(new EventBusContext(POST_GET_RESPONSE));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                isThreadRunning=false;
            }
        }
    }

    public void onEventMainThread(EventBusContext eventBusContext) {
        if (eventBusContext.getActionCode() == POST_GET_RESPONSE) {
            mAdapter = new MessageListAdapter(getApplicationContext(), smsObjectArrayList);
            recyclerView.addItemDecoration(new ContactDivider(getApplicationContext(), LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
