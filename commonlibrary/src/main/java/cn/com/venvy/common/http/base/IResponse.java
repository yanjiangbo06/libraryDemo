package cn.com.venvy.common.http.base;

/**
 * Created by yanjiangbo on 2017/4/27.
 */

public interface IResponse {
    Object getHeaders();
    Object getData();
    boolean isSuccess();
}
