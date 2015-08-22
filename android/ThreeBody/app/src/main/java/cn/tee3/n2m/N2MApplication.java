package cn.tee3.n2m;

import android.app.Application;
import android.util.Log;

import org.st.RoomInfo;
import org.st.RoomSystem;

/**
 * Created by uyu on 2015/8/21.
 */
public class N2MApplication extends Application implements RoomSystem.RoomSystemListener {

    @Override
    public void onCreate() {
        super.onCreate();

        RoomSystem.initializeAndroidGlobals(this, true, true);
        RoomSystem.setVideoOptions(Constants.VIDEO_HGIGHT, Constants.VIDEO_WIDTH, Constants.VIDEO_FRAME_RATE);
        RoomSystem.logEnable(true);

        // TODO: 2015/8/22  RoomSystem should be a signleton
        SingletonRoomSystem.getInstance().init(this, Constants.SERVER_URL, Constants.ACCESS_KEY, Constants.SECRET_KEY);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        //// TODO: 2015/8/22 singleton
        SingletonRoomSystem.getInstance().unInit();
    }

    @Override
    public void onInit(int i) {
        if (i != 0) {
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

