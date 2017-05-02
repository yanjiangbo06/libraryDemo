package cn.com.venvy.mvc;

import android.content.Intent;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import cn.com.venvy.common.http.HttpRequest;
import cn.com.venvy.common.http.base.IRequestHandler;
import cn.com.venvy.common.http.base.IResponse;
import cn.com.venvy.common.http.base.Request;

/**
 * Created by yanjiangbo on 2017/4/28.
 */

public abstract class BaseController {
    private BaseModel mBaseModel;
    private WeakReference<IVenvy> mWeakReference;

    private BaseController(IVenvy venvy) {
        mWeakReference = new WeakReference<>(venvy);
        mBaseModel = getBaseModel();
    }

    public abstract BaseModel getBaseModel();


    /**
     * 仅仅只是网络请求的举例说明，到时需要移除
     */
    private void startLogin() {
        if (mBaseModel != null) {
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("pa", "222");
        Request request = HttpRequest.get(mWeakReference.get().getContext(), "", params);
        mBaseModel.startRequest(request, new IRequestHandler() {
            @Override
            public void requestFinish(Request request, IResponse response) {
                IVenvy venvy = mWeakReference.get();
                if (venvy != null) {
                    Intent intent = new Intent();
                    venvy.startActivity(intent);
                }
            }

            @Override
            public void requestError(Request request, Exception e) {

            }

            @Override
            public void startRequest(Request request) {

            }

            @Override
            public void requestProgress(Request request, int progress) {

            }
        });
    }
}
