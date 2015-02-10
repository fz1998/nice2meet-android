package com.threebody.conference.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.threebody.conference.R;
import com.threebody.conference.ui.util.ToastUtil;
import com.threebody.sdk.common.AudioCommon;
import com.threebody.sdk.common.RoomCommon;
import com.threebody.sdk.common.STSystem;

import butterknife.InjectView;

/**
 * Created by xiaxin on 15-1-14.
 */
public class SetFragment extends BaseFragment {
    @InjectView(R.id.ivVideo)Button ivVideo;
    @InjectView(R.id.ivAudio)Button ivAudio;
    @InjectView(R.id.llHelp)LinearLayout llHelp;
    RoomCommon roomCommon;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set, null);
        initView(view);
        return view;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        roomCommon = STSystem.getInstance().getRoomCommons().get(0);
        ivVideo.setOnClickListener(this);
        ivAudio.setOnClickListener(this);
        llHelp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.ivAudio:
                if(AudioCommon.IS_MIC_ON == AudioCommon.MIC_OFF){
                    if(openAudio()){
                        ivAudio.setText(R.string.closeAudio);

                    }else{
                        ivAudio.setText(R.string.handsup);

                    }
                }else if(AudioCommon.IS_MIC_ON == 1){
                    if(closeAudio()){
                        ivAudio.setText(R.string.openAudio);
                    }else{
                        ToastUtil.showToast(getActivity(), R.string.closefailed);
                    }
                }else{
                    ToastUtil.showToast(getActivity(), R.string.handsTip);
                }
                break;
            case R.id.ivVideo:

                break;
        }
    }
    private boolean openAudio(){
        return roomCommon.getAudioCommon().openMic(roomCommon.getMe().getNodeId());
    }
    private boolean closeAudio(){
        return roomCommon.getAudioCommon().closeMic(roomCommon.getMe().getNodeId());
    }
    public void openLocalAudio(){
        if(ivAudio != null){
            ivAudio.setText(R.string.closeAudio);
        }

    }
    public void closeLocalAudio(){
        if(ivAudio != null ){
            ivAudio.setText(R.string.openAudio);
        }

    }
    public void openLocalVideo(){
        if(ivVideo != null){
            ivVideo.setText(R.string.openVideo);
        }
    }
    public void closeLoacalVideo(){
        if(ivVideo != null){
            ivVideo.setText(R.string.closeVideo);
        }
    }
//    private boolean openVideo(){
//
//    }
//    private boolean closeVideo(){
//
//    }
}
