package cn.com.venvy.common.track;

import java.util.HashMap;

import cn.com.venvy.Platform;
import cn.com.venvy.common.http.HttpRequest;
import cn.com.venvy.common.http.RequestFactory;
import cn.com.venvy.common.http.base.IRequestConnect;
import cn.com.venvy.common.http.base.Request;

/**
 * Created by Arthur on 2017/5/10.
 */

public class TrackHelper {

    private static final String TRACK_HOST_URL = "http://va.videojj.com/";
    protected static final String TRACK_URL = TRACK_HOST_URL + "track/v4/va.gif";
    protected IRequestConnect requestConnect;

    public void init(Platform platform) {
        requestConnect = platform.getRequestConnect();
    }

    public void getAction(HashMap<String, String> params) {
        if (requestConnect == null) {
            return;
        }
        Request request = HttpRequest.get(TRACK_URL, params);
        requestConnect.connect(request, null);
    }

    public void postAction(HashMap<String, String> params) {
        if (requestConnect == null) {
            return;
        }
        Request request = HttpRequest.post(TrackHelper.TRACK_URL, params);
        requestConnect.connect(request, null);
    }

    public void onDestroy() {
        //TODO
    }
}
