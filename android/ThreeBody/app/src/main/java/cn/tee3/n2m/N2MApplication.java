package cn.tee3.n2m;

import android.app.Application;
import android.util.Log;
import com.threebody.sdk.common.STSystem;
import org.st.RoomSystem;

/**
 * Created by uyu on 2015/8/21.
 */
public class N2MApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        RoomSystem.initializeAndroidGlobals(this, true, true);
        RoomSystem.setVideoOptions(640, 480, 10);
        RoomSystem.logEnable(true);

        STSystem.getInstance().init(new STSystem.ConferenceSystemCallback() {
            @Override
            public void onInitResult(int result) {
                if (result != 0) {
                    Log.i("n2m", "init error.");
                }
            }
        }, Constants.SERVER_URL, Constants.ACCESS_KEY, Constants.SECRET_KEY);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        STSystem.getInstance().unInit();
    }
}

