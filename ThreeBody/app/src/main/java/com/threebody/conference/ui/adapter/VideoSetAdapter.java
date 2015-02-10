package com.threebody.conference.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.threebody.conference.R;

import org.st.User;

import java.util.List;

/**
 * Created by xiaxin on 15-1-17.
 */
public class VideoSetAdapter extends BaseAdapter {
    Context context;
    List<User> users;
    int firstCheck, secondCheck;
    int checkCount = 0;
    public VideoSetAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
           convertView = LayoutInflater.from(context).inflate(R.layout.item_video_set, null);
        }
        initView(convertView, position);
        return convertView;
    }
    private void initView(View view, final int position){
        final CheckBox cb = (CheckBox)view.findViewById(R.id.cbUser);
        final User user = users.get(position);
        cb.setText(user.getUserName());
        cb.setChecked(user.isVideoChecked());
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb.isChecked()){
                    if(checkCount == 0){
                        firstCheck = position;
                        checkCount++;

                    }else if(checkCount == 1){
                        secondCheck = position;
                        checkCount++;
                    }else {
                        users.get(firstCheck).setVideoChecked(false);
                        firstCheck = secondCheck;
                        secondCheck = position;
                    }
                }else{
                    if(position == firstCheck){
                        firstCheck = secondCheck;
                        checkCount--;
                    }

                }
                user.setVideoChecked(cb.isChecked());
                notifyDataSetChanged();
            }
        });
    }

}
