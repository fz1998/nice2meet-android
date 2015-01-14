package com.threebody.conference.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by xiaxin on 15-1-13.
 */
public class BaseActivity extends ActionBarActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }
    protected void initUI(){
        ButterKnife.inject(this);
    }
    @Override
    public void onClick(View v) {

    }
}
