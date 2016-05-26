package com.example.sushant.messagingapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.sushant.messagingapp.Adapter.MessageListAdapter;
import com.example.sushant.messagingapp.Adapter.MsgDetailAdapter;
import com.example.sushant.messagingapp.POJO.ParticularSMS;
import com.example.sushant.messagingapp.POJO.SMSObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by sushant on 25/5/16.
 */
public class MsgDetail extends AppCompatActivity {

    public static final String INBOX_URI = "content://sms/";

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    public EventBus eventBus;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ParticularSMS> particularSMSes = new ArrayList<>();
    private ArrayList<String> sms = new ArrayList<>();
    private String id,address,name;

    public static final int GET_RESPONSE = 1;
    public static final int POST_GET_RESPONSE = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=this.getIntent();
        id = intent.getStringExtra("_id");
        address = intent.getStringExtra("address");
        name = intent.getStringExtra("name");

        setContentView(R.layout.activity_msgdetail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(name == null)
            setTitle(address);
        else
            setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = (RecyclerView)findViewById(R.id.msg_list);
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
                    String[] projection = {"body"};
                    String selection = "address = ?";
                    String[] selectionArgs = {address};
                    Cursor cursor = cr.query(inboxURI, projection, selection, selectionArgs, null);

                    if (cursor != null && cursor.getCount() > 0) {
                        cursor.moveToFirst();

                        while (!cursor.isAfterLast()) {
                            String sms = cursor.getString(cursor.getColumnIndex("body"));
                            particularSMSes.add(new ParticularSMS(address, sms));
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
            mAdapter = new MsgDetailAdapter(getApplicationContext(), particularSMSes);
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
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                break;

        }
        return true;
    }


}
