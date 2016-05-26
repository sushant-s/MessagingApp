package com.example.sushant.messagingapp.Adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sushant.messagingapp.MsgDetail;
import com.example.sushant.messagingapp.POJO.ParticularSMS;
import com.example.sushant.messagingapp.POJO.SMSObject;
import com.example.sushant.messagingapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by sushant on 26/5/16.
 */
public class MsgDetailAdapter extends RecyclerView.Adapter<MsgDetailAdapter.ViewHolder> {

    private static Context sContext;
    public ArrayList<ParticularSMS> particularSMSes = new ArrayList<>();

    public MsgDetailAdapter(Context context, ArrayList<ParticularSMS> particularSMSes) {
        sContext = context;
        this.particularSMSes = particularSMSes;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.read, parent, false);
        ViewHolder holder = new ViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String address = particularSMSes.get(getItemPosition(position)).getAddress();
        String name = getContactName(sContext,address);
        //String time = getDate(Long.parseLong(smsObjectArrayList.get(getItemPosition(position)).getTime()));
        if(name == null)
            holder.sender_add.setText(address);
        else
            holder.sender_add.setText(name);
        //holder.time.setText(time);
        holder.msg.setText(particularSMSes.get(getItemPosition(position)).getMsg());


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
        return particularSMSes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout msg_view;
        private TextView sender_add,time,msg;
        public ViewHolder(View v) {
            super(v);
            msg_view = (RelativeLayout)v.findViewById(R.id.contact);
            sender_add = (TextView)v.findViewById(R.id.address);
            time = (TextView)v.findViewById(R.id.time);
            msg = (TextView)v.findViewById(R.id.message);
        }

    }
}
