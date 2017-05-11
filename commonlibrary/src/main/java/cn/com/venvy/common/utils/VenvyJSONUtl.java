package cn.com.venvy.common.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by yanjiangbo on 2017/5/5.
 */

public class VenvyJSONUtl {

    public static JSONObject mpToJSON(Map<String, String> map) {
        if (map == null || map.size() <= 0) {
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                jsonObject.put(entry.getKey(), entry.getValue());
            }
            return jsonObject;
        } catch (JSONException e) {
            VenvyLog.e("", e);
        }
        return null;
    }
}
