package cn.tee3.n2m.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.tee3.n2m.R;
import cn.tee3.n2m.biz.domain.MessageBean;

/**
 * Created by xiaxin on 15-1-17.
 */
public class MessageAdapter extends BaseAdapter {
    Context context;
    List<MessageBean> messageBeans;

    public MessageAdapter(Context context, List<MessageBean> messageBeans) {
        this.context = context;
        this.messageBeans = messageBeans;
    }

    @Override
    public int getCount() {
        return messageBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return messageBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }




    public int getType(int position) {
        if(messageBeans.get(position).isMe()){
            return 1;
        }
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getType(position);
        ViewHolder holder = null;
        MessageBean messageBean = messageBeans.get(position);
        if(type == 1){
            convertView = LayoutInflater.from(context).inflate(R.layout.chat_message_left, null);
            if(convertView.getTag() == null){
                holder = new ViewHolder();
                holder.tvName = (TextView)convertView.findViewById(R.id.tvUserName);
                holder.tvMessage = (TextView)convertView.findViewById(R.id.tvMessage);
                holder.ivHead = (ImageView)convertView.findViewById(R.id.ivHead);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }

        }else{
            convertView = LayoutInflater.from(context).inflate(R.layout.chat_message_right, null);
            if(convertView.getTag() == null){

                holder = new ViewHolder();
                holder.tvName = (TextView)convertView.findViewById(R.id.tvUserName);
                holder.tvMessage = (TextView)convertView.findViewById(R.id.tvMessage);
                holder.ivHead = (ImageView)convertView.findViewById(R.id.ivHead);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }
        }
        holder.tvName.setText(messageBean.getName());
        holder.tvMessage.setText(messageBean.getMessage());
        return convertView;
    }
    public void refresh(List<MessageBean> messageBeans){
        this.messageBeans = messageBeans;
        notifyDataSetChanged();
    }
    class ViewHolder {
        TextView tvName;
        ImageView ivHead;
        TextView tvMessage;
    }
}
