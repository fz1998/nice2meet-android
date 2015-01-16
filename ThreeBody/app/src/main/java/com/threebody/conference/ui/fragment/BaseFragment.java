package com.threebody.conference.ui.fragment;

import android.support.v4.app.Fragment;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by xiaxin on 15-1-14.
 */
public class BaseFragment extends Fragment implements View.OnClickListener{
    protected void initView(View view ){
        ButterKnife.inject(this, view);
    }

    @Override
    public void onClick(View v) {

    }
}
