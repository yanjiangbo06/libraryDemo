package cn.com.venvy.common.track;

import android.content.Context;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.com.venvy.common.http.HttpRequest;
import cn.com.venvy.common.http.RequestFactory;
import cn.com.venvy.common.http.base.IRequestConnect;
import cn.com.venvy.common.http.base.IRequestHandler;
import cn.com.venvy.common.http.base.IResponse;
import cn.com.venvy.common.http.base.Request;
import cn.com.venvy.common.utils.VenvyLog;
import cn.com.venvy.common.utils.VenvyPreferenceHelper;

/**
 * Created by Arthur on 2017/5/10.
 */

public class TrackHelper {

    private static final String TRACK_HOST_URL = "http://va.videojj.com/";
    protected static final String TRACK_URL = TRACK_HOST_URL + "track/v4/va.gif";
    protected static IRequestConnect requestConnect = RequestFactory.initConnect(RequestFactory.HttpPlugin.OK_HTTP);

    public static void getAction(HashMap<String, String> params) {
        Request request = HttpRequest.get(TRACK_URL, params);
        requestConnect.connect(request, null);
    }

    public static void postAction(HashMap<String, String> params) {
        Request request = HttpRequest.post(TrackHelper.TRACK_URL, params);
        requestConnect.connect(request, null);
    }


}
