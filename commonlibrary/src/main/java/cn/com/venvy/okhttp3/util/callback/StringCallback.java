package cn.com.venvy.okhttp3.util.callback;

import java.io.IOException;

import cn.com.venvy.okhttp3.Response;

/**
 * Created by zhy on 15/12/14.
 */
public abstract class StringCallback extends Callback<String>
{
    @Override
    public String parseNetworkResponse(Response response, int id) throws IOException
    {
        return response.body().string();
    }
}
