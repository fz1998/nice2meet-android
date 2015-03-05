package com.threebody.conference.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.threebody.conference.R;
import com.threebody.sdk.domain.DeviceBean;

import org.st.User;

import java.util.List;

/**
 * Created by xiaxin on 15-1-17.
 */
public class VideoSetAdapter extends BaseAdapter {
    Context context;
    List<DeviceBean> deviceBeans;
    int firstCheck, secondCheck;
    int checkCount = 0;
    public VideoSetAdapter(Context context, List<DeviceBean> deviceBeans) {
        this.context = context;
        this.deviceBeans = deviceBeans;
//        checkDevice();
    }
    private void checkDevice(){
        for(int i = 0; i < deviceBeans.size(); i++){
            DeviceBean deviceBean = deviceBeans.get(i);
            if(deviceBean.isVideoChecked()){
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
        return deviceBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return deviceBeans.get(position);
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
        final DeviceBean deviceBean = deviceBeans.get(position);
        final User user = deviceBean.getUser();
        cb.setText(user.getUserName());
        cb.setChecked(deviceBean.isVideoChecked());
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
                        deviceBeans.get(firstCheck).setVideoChecked(false);
                        firstCheck = secondCheck;
                        secondCheck = position;
                    }
                }else{
                    if(position == firstCheck){
                        firstCheck = secondCheck;
                        checkCount--;
                    }

                }
                deviceBean.setVideoChecked(cb.isChecked());
                notifyDataSetChanged();
            }
        });
    }

}
