package cn.com.venvy.common.track;

import android.content.Context;

import java.util.HashMap;

import cn.com.venvy.common.http.HttpRequest;
import cn.com.venvy.common.http.RequestFactory;
import cn.com.venvy.common.http.base.IRequestHandler;
import cn.com.venvy.common.http.base.IResponse;
import cn.com.venvy.common.http.base.Request;
import cn.com.venvy.common.utils.VenvyPreferenceHelper;

/**
 * Created by Arthur on 2017/5/10.
 */

public class TrackHelper {

    private static final String TRACK_FILE_NAME = "VenvyVideoOSTRACK";
    private static final String EMPTY_CACHE = "{params:[]}";
    private static final String TRACK_HOST_URL = "http://va.videojj.com/";
    private static final String TRACK_URL = TRACK_HOST_URL + "track/v4/va.gif";

    public static void getTrackAction(Context context, HashMap<String, String> params) {
        Request request = HttpRequest.get(TRACK_URL, params);
        new RequestFactory().initConnect(RequestFactory.HttpPlugin.OK_HTTP).connect(request, null);
    }

    public static void postTrackAction(Context context, HashMap<String, String> params) {
        //获取缓存的数据
        String cacheJsonStr = VenvyPreferenceHelper.getString(context, TRACK_FILE_NAME, EMPTY_CACHE,"");
    }

}
