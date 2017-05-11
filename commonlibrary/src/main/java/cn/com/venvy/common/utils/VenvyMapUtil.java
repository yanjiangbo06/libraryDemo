package cn.com.venvy.common.utils;

import java.util.Map;
import java.util.Set;

/**
 * Created by yanjiangbo on 2017/5/10.
 */

public class VenvyMapUtil {

    /***
     * 将Map转化为Json
     * @return
     */
    public static <T> String mapToJson(Map<String, T> map) {
        if (map == null || map.isEmpty()) {
            return "null";
        }
        String jsonStr = "{";
        Set<?> keySet = map.keySet();
        for (Object key : keySet) {
            jsonStr += "\"" + key + "\":\"" + map.get(key) + "\",";
        }
        jsonStr = jsonStr.substring(0, jsonStr.length() - 1);
        jsonStr += "}";
        return jsonStr;
    }
}
