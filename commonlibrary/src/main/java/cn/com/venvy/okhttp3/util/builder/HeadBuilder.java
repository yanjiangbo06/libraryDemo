package cn.com.venvy.okhttp3.util.builder;

import cn.com.venvy.okhttp3.util.OkHttpUtils;
import cn.com.venvy.okhttp3.util.request.OtherRequest;
import cn.com.venvy.okhttp3.util.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
