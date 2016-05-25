package com.example.sushant.messagingapp.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sushant.messagingapp.R;

/**
 * Created by sushant on 25/5/16.
 */
public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_card, parent, false);
        ViewHolder holder = new ViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout contact_view;
        private TextView f_name,l_name,sent_time,short_msg;
        public ViewHolder(View v) {
            super(v);
            contact_view = (RelativeLayout)v.findViewById(R.id.contact);
            f_name = (TextView)v.findViewById(R.id.first_name);
            l_name = (TextView)v.findViewById(R.id.last_name);
            sent_time = (TextView)v.findViewById(R.id.time);
            short_msg = (TextView)v.findViewById(R.id.short_message);
        }

    }
}
