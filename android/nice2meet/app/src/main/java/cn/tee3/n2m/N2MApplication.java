package cn.tee3.n2m;

import android.app.Application;
import android.util.Log;

import cn.tee3.n2m.biz.service.N2MRoomSystem;

import org.st.RoomInfo;
import org.st.RoomSystem;

/**
 * Created by uyu on 2015/8/21.
 */
public class N2MApplication extends Application implements RoomSystem.RoomSystemListener {

    @Override
    public void onCreate() {
        super.onCreate();

        N2MRoomSystem.instance().getRoomSystem().initializeAndroidGlobals(this, true, true);
        N2MRoomSystem.instance().getRoomSystem().setVideoOptions(Constants.VIDEO_HEIGHT, Constants.VIDEO_WIDTH, Constants.VIDEO_FRAME_RATE);
        N2MRoomSystem.instance().getRoomSystem().logEnable(true);

        // TODO: 2015/8/22  RoomSystem should be a signleton
        N2MRoomSystem.instance().getRoomSystem().init(this, Constants.SERVER_URL, Constants.ACCESS_KEY, Constants.SECRET_KEY);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        N2MRoomSystem.instance().getRoomSystem().unInit();
    }

    @Override
    public void onInit(int result) {
        if (result != 0) {
            //// TODO: 2015/9/5 show error on the screen.
            Log.i("n2m", "init error.");
        } else {
            Log.i("n2m", "init done.");
        }
    }

    @Override
    public void onArrangeRoom(int i, String s) {

    }

    @Override
    public void onCancelRoom(int i, String s) {

    }

    @Override
    public void onQueryRoom(int i, RoomInfo roomInfo) {

    }
}

