package com.threebody.conference.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.threebody.conference.R;
import com.threebody.sdk.domain.Message;

import java.util.List;

/**
 * Created by xiaxin on 15-1-17.
 */
public class MessageAdapter extends BaseAdapter {
    Context context;
    List<Message> messages;

    public MessageAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if(messages.get(position).isMe()){
            return 1;
        }
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        ViewHolder holder = null;
        Message message = messages.get(position);
        if(type == 1){
            if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.item_message_left, null);
                holder = new ViewHolder();
                holder.tvName = (TextView)convertView.findViewById(R.id.tvUserName);
                holder.tvMessage = (TextView)convertView.findViewById(R.id.tvMessage);
                holder.ivHead = (ImageView)convertView.findViewById(R.id.ivHead);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }

        }else{
            if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.item_message_right, null);
                holder = new ViewHolder();
                holder.tvName = (TextView)convertView.findViewById(R.id.tvUserName);
                holder.tvMessage = (TextView)convertView.findViewById(R.id.tvMessage);
                holder.ivHead = (ImageView)convertView.findViewById(R.id.ivHead);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }
        }
        holder.tvName.setText(message.getName());
        holder.tvMessage.setText(message.getMessage());
        return convertView;
    }
    public void refresh(){

    }
    class ViewHolder {
        TextView tvName;
        ImageView ivHead;
        TextView tvMessage;
    }
}
