package cn.com.venvy.okhttp3.util.request;

import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import cn.com.venvy.okhttp3.FormBody;
import cn.com.venvy.okhttp3.Headers;
import cn.com.venvy.okhttp3.MediaType;
import cn.com.venvy.okhttp3.MultipartBody;
import cn.com.venvy.okhttp3.Request;
import cn.com.venvy.okhttp3.RequestBody;
import cn.com.venvy.okhttp3.util.OkHttpUtils;
import cn.com.venvy.okhttp3.util.builder.PostFormBuilder;
import cn.com.venvy.okhttp3.util.callback.Callback;

/**
 * Created by zhy on 15/12/14.
 */
public class PostFormRequest extends OkHttpRequest {
	private List<PostFormBuilder.FileInput> files;

	public PostFormRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers,
			List<PostFormBuilder.FileInput> files, int id) {
		super(url, tag, params, headers, id);
		this.files = files;
	}

	@Override
	protected RequestBody buildRequestBody() {
		if (files == null || files.isEmpty()) {
			FormBody.Builder builder = new FormBody.Builder();
			addParams(builder);
			return builder.build();
		} else {
			MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
			addParams(builder);

			for (int i = 0; i < files.size(); i++) {
				PostFormBuilder.FileInput fileInput = files.get(i);
				RequestBody fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileInput.filename)),
						fileInput.file);
				builder.addFormDataPart(fileInput.key, fileInput.filename, fileBody);
			}
			return builder.build();
		}
	}

	@Override
	protected RequestBody wrapRequestBody(RequestBody requestBody, final Callback callback) {
		if (callback == null)
			return requestBody;
		CountingRequestBody countingRequestBody = new CountingRequestBody(requestBody,
				new CountingRequestBody.Listener() {
					@Override
					public void onRequestProgress(final long bytesWritten, final long contentLength) {

						OkHttpUtils.getInstance().getDelivery().execute(new Runnable() {
							@Override
							public void run() {
								callback.inProgress(bytesWritten * 1.0f / contentLength, contentLength, id);
							}
						});

					}
				});
		return countingRequestBody;
	}

	@Override
	protected Request buildRequest(RequestBody requestBody) {
		return builder.post(requestBody).build();
	}

	private String guessMimeType(String path) {
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
		String contentTypeFor = null;
		try {
			contentTypeFor = fileNameMap.getContentTypeFor(URLEncoder.encode(path, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			
		}
		if (contentTypeFor == null) {
			contentTypeFor = "application/octet-stream";
		}
		return contentTypeFor;
	}

	private void addParams(MultipartBody.Builder builder) {
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
						RequestBody.create(null, params.get(key)));
			}
		}
	}

	private void addParams(FormBody.Builder builder) {
		if (params != null) {
			for (String key : params.keySet()) {
				builder.add(key, params.get(key));
			}
		}
	}

}
