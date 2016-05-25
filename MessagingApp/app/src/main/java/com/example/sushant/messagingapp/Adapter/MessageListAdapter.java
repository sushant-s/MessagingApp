package com.example.sushant.messagingapp.Adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sushant.messagingapp.POJO.SMSObject;
import com.example.sushant.messagingapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by sushant on 25/5/16.
 */
public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {

    private static Context sContext;
    private ArrayList<SMSObject> smsObjectArrayList = new ArrayList<>();

    public MessageListAdapter(Context context, ArrayList<SMSObject> smsObjectArrayList) {
        sContext = context;
        this.smsObjectArrayList = smsObjectArrayList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_card, parent, false);
        ViewHolder holder = new ViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String address = smsObjectArrayList.get(getItemPosition(position)).getAddress();
        String name = getContactName(sContext,address);
        String time = getDate(Long.parseLong(smsObjectArrayList.get(getItemPosition(position)).getTime()));
        if(name == null)
            holder.sender_add.setText(address);
        else
            holder.sender_add.setText(name);
        holder.time.setText(time);
        holder.short_msg.setText(smsObjectArrayList.get(getItemPosition(position)).getMsgBody());

    }

    private String getDate(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, h:mm a");
        final String date = sdf.format(time);
        return date;
    }

    public static String getContactName(Context context, String phoneNumber)
    {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null)
        {
            return null;
        }
        String contactName = null;
        if(cursor.moveToFirst())
        {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }

        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return contactName;
    }

    public int getItemPosition(int position){
        return position;
    }


    @Override
    public int getItemCount() {
        return smsObjectArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout contact_view;
        private TextView sender_add,time,short_msg;
        public ViewHolder(View v) {
            super(v);
            contact_view = (RelativeLayout)v.findViewById(R.id.contact);
            sender_add = (TextView)v.findViewById(R.id.address);
            time = (TextView)v.findViewById(R.id.time);
            short_msg = (TextView)v.findViewById(R.id.short_message);
        }

    }
}
