package com.threebody.conference.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.threebody.conference.R;
import com.threebody.sdk.domain.N2MVideo;

import org.st.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaxin on 15-1-17.
 */
public class VideoSetAdapter extends BaseAdapter {
    Context context;

    public List<N2MVideo> getVideoList() {
        return n2MVideos;
    }

    List<N2MVideo> n2MVideos;
    int firstCheck, secondCheck;
    int checkCount = 0;
    public VideoSetAdapter(Context context, List<N2MVideo> n2MVideos) {
        this.context = context;
        this.n2MVideos = n2MVideos;
//        checkDevice();
    }
    private void checkDevice(){
        for(int i = 0; i < n2MVideos.size(); i++){
            N2MVideo n2MVideo = n2MVideos.get(i);
            if(n2MVideo.isVideoChecked()){
                if(checkCount == 0){
                    firstCheck = i;
                }else if(checkCount == 1){
                    secondCheck = i;
                }
                checkCount++;
            }
        }
    }
    @Override
    public int getCount() {
        return n2MVideos.size();
    }

    @Override
    public Object getItem(int position) {
        return n2MVideos.get(position);
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
        final N2MVideo n2MVideo = n2MVideos.get(position);
        final User user = n2MVideo.getUser();
        cb.setText(user.getUserName());
        cb.setChecked(n2MVideo.isVideoChecked());
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
                        n2MVideos.get(firstCheck).setVideoChecked(false);
                        firstCheck = secondCheck;
                        secondCheck = position;
                    }
                }else{
                    checkCount--;
                    if(position == firstCheck){
                        firstCheck = secondCheck;
                    }

                }
                n2MVideo.setVideoChecked(cb.isChecked());
                notifyDataSetChanged();
            }
        });
    }

}
