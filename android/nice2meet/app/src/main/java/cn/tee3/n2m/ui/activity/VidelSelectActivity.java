package cn.tee3.n2m.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import cn.tee3.n2m.Constants;
import cn.tee3.n2m.R;
import cn.tee3.n2m.ui.fragment.VideoSelectFragment;

/**
 * Created by xiaxin on 15-1-17.
 */
public class VidelSelectActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_video_select);
        super.initUI();
        getSupportFragmentManager().beginTransaction().add(R.id.videoSetContainer, new VideoSelectFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra(Constants.DATA, true);
        setResult(RESULT_OK, data);
        super.onBackPressed();
    }
}
