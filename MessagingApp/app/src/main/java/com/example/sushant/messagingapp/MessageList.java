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

import java.util.ArrayList;
import java.util.Collections;

import de.greenrobot.event.EventBus;

public class MessageList extends AppCompatActivity {

    public static final String INBOX_URI = "content://sms/inbox";

    private RecyclerView recyclerView;
    public EventBus eventBus;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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
                    String[] reqCols = new String[] { "_id", "address", "body" };
                    ContentResolver cr = getContentResolver();
                    Cursor c = cr.query(inboxURI, reqCols, null, null, null);

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
            mAdapter = new MessageListAdapter();
            recyclerView.addItemDecoration(new ContactDivider(getApplicationContext(), LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
