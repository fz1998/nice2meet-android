package com.threebody.conference.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.threebody.conference.R;

import butterknife.InjectView;

/**
 * Created by xiaxin on 15-1-14.
 */
public class SetFragment extends BaseFragment {
    @InjectView(R.id.ivVideo)ImageView ivVideo;
    @InjectView(R.id.ivAudio)ImageView ivAudio;
    @InjectView(R.id.llHelp)LinearLayout llHelp;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set, null);
        initView(view);
        return view;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);

        ivVideo.setOnClickListener(this);
        ivAudio.setOnClickListener(this);
        llHelp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}
