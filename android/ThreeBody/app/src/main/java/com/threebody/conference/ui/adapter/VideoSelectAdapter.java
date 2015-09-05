package com.threebody.conference.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.threebody.conference.R;
import com.threebody.sdk.domain.N2MVideo;

import org.st.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaxin on 15-1-17.
 */
public class VideoSelectAdapter extends BaseAdapter {
    Context context;

    public List<N2MVideo> getVideoList() {
        return n2MVideos;
    }

    List<N2MVideo> n2MVideos;
    int firstCheck, secondCheck;
//    List<Integer> positionArray = new ArrayList<Integer>();
    int checkCount = 0;

    public VideoSelectAdapter(Context context, List<N2MVideo> n2MVideos) {
        this.context = context;
        this.n2MVideos = n2MVideos;
//        checkDevice();
    }

    private void checkDevice(){
        checkCount = 0;
        for(int position = 0; position < n2MVideos.size(); position++){
            N2MVideo n2MVideo = n2MVideos.get(position);
            if(n2MVideo.isVideoChecked()){
                if(checkCount == 0){
                    firstCheck = position;
                }else if(checkCount == 1){
                    secondCheck = position;
                }
                checkCount++;

//                replaceArrayWithData(checkCount, position);
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
           convertView = LayoutInflater.from(context).inflate(R.layout.item_video_select, null);
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
                checkDevice();
                if (cb.isChecked()) {
                    if (checkCount == 0) {
                        firstCheck = position;
//                        replaceArrayWithData(checkCount, position);
                        checkCount++;
                    } else if (checkCount == 1) {
                        secondCheck = position;
//                        replaceArrayWithData(checkCount, position);
                        checkCount++;
                    } else {
                        cb.setChecked(false);
                        Toast.makeText(context, "最多加入两路视频", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    checkCount--;
                    if (position == firstCheck) {
                        firstCheck = secondCheck;
//                        replaceArrayWithData(0, positionArray.get(1));
                    }
                }
//                Toast.makeText(context, "firstCheck:" + firstCheck + "," + positionArray.get(0).toString() +
//                        "  secondCheck:" + secondCheck + "," + positionArray.get(1).toString(), Toast.LENGTH_SHORT).show();
                n2MVideo.setVideoChecked(cb.isChecked());
                notifyDataSetChanged();
            }
        });
    }

//    private void replaceArrayWithData(int checkCount, int position) {
//        if (positionArray.size() >= checkCount && positionArray.get(checkCount - 1) != null) positionArray.remove(checkCount - 1);
//        positionArray.add(checkCount - 1, Integer.valueOf(position));
//    }
}
