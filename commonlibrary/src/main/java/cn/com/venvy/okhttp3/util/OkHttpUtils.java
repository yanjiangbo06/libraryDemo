package cn.com.venvy.okhttp3.util;

import java.io.IOException;
import java.util.concurrent.Executor;

import cn.com.venvy.okhttp3.Call;
import cn.com.venvy.okhttp3.OkHttpClient;
import cn.com.venvy.okhttp3.Response;
import cn.com.venvy.okhttp3.util.builder.GetBuilder;
import cn.com.venvy.okhttp3.util.builder.HeadBuilder;
import cn.com.venvy.okhttp3.util.builder.OtherRequestBuilder;
import cn.com.venvy.okhttp3.util.builder.PostFileBuilder;
import cn.com.venvy.okhttp3.util.builder.PostFormBuilder;
import cn.com.venvy.okhttp3.util.builder.PostStringBuilder;
import cn.com.venvy.okhttp3.util.callback.Callback;
import cn.com.venvy.okhttp3.util.request.RequestCall;
import cn.com.venvy.okhttp3.util.utils.Platform;

/**
 * Created by zhy on 15/8/17.
 */
public class OkHttpUtils {
	public static final long DEFAULT_MILLISECONDS = 10_000L;
	private volatile static OkHttpUtils mInstance;
	private OkHttpClient mOkHttpClient;
	private Platform mPlatform;
    public static final String TAG = "VENVY";
	public OkHttpUtils(OkHttpClient okHttpClient) {
		if (okHttpClient == null) {
			mOkHttpClient = new OkHttpClient();
		} else {
			mOkHttpClient = okHttpClient;
		}

		mPlatform = Platform.get();
	}

	public static OkHttpUtils initClient(OkHttpClient okHttpClient) {
		if (mInstance == null) {
			synchronized (OkHttpUtils.class) {
				if (mInstance == null) {
					mInstance = new OkHttpUtils(okHttpClient);
				}
			}
		}
		return mInstance;
	}

	public static OkHttpUtils getInstance() {
		return initClient(null);
	}

	public Executor getDelivery() {
		return mPlatform.defaultCallbackExecutor();
	}

	public OkHttpClient getOkHttpClient() {
		return mOkHttpClient;
	}

	
	public static GetBuilder get() {
		return new GetBuilder().tag(TAG);
	}

	public static GetBuilder load(String url) {
		return new GetBuilder().url(url);
	}
	public static PostStringBuilder postString() {
		return new PostStringBuilder();
	}

	public static PostFileBuilder postFile() {
		return new PostFileBuilder();
	}

	public static PostFormBuilder post() {
		return new PostFormBuilder();
	}

	public static OtherRequestBuilder put() {
		return new OtherRequestBuilder(METHOD.PUT);
	}

	public static HeadBuilder head() {
		return new HeadBuilder();
	}

	public static OtherRequestBuilder delete() {
		return new OtherRequestBuilder(METHOD.DELETE);
	}

	public static OtherRequestBuilder patch() {
		return new OtherRequestBuilder(METHOD.PATCH);
	}

	public void execute(final RequestCall requestCall, Callback callback) {
		if (callback == null)
			callback = Callback.CALLBACK_DEFAULT;
		final Callback finalCallback = callback;
		final int id = requestCall.getOkHttpRequest().getId();

		requestCall.getCall().enqueue(new cn.com.venvy.okhttp3.Callback() {
			@Override
			public void onFailure(Call call, final IOException e) {
				sendFailResultCallback(call, e, finalCallback, id);
			}

			@Override
			public void onResponse(final Call call, final Response response) {
				if (call.isCanceled()) {
					sendFailResultCallback(call, new IOException("Canceled!"), finalCallback, id);
					return;
				}

				if (!finalCallback.validateReponse(response, id)) {
					sendFailResultCallback(call,
							new IOException("request failed , reponse's code is : " + response.code()), finalCallback,
							id);
					return;
				}

				try {
					Object o = finalCallback.parseNetworkResponse(response, id);
					sendSuccessResultCallback(o, finalCallback, id);
				} catch (Exception e) {
					sendFailResultCallback(call, e, finalCallback, id);

				}

			}
		});
	}

	public void sendFailResultCallback(final Call call, final Exception e, final Callback callback, final int id) {
		if (callback == null)
			return;

		mPlatform.execute(new Runnable() {
			@Override
			public void run() {
				callback.onError(call, e, id);
				callback.onAfter(id);
			}
		});
	}

	public void sendSuccessResultCallback(final Object object, final Callback callback, final int id) {
		if (callback == null)
			return;
		mPlatform.execute(new Runnable() {
			@Override
			public void run() {
				callback.onResponse(object, id);
				callback.onAfter(id);
			}
		});
	}

	public void cancelAll(){
		mOkHttpClient.dispatcher().cancelAll();
	}
	public void cancelTag(Object tag) {

		for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
			if (tag.equals(call.request().tag())) {
				call.cancel();
			}
		}
		for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
			if (tag.equals(call.request().tag())) {
				call.cancel();
			}
		}
	}

	public static class METHOD {
		public static final String HEAD = "HEAD";
		public static final String DELETE = "DELETE";
		public static final String PUT = "PUT";
		public static final String PATCH = "PATCH";
	}
}
