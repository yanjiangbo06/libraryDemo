package cn.com.venvy.common.interf;

/**
 * 数据加载成功接口
 * Created by YanQiu on 2017/3/15.
 */

public interface LoadSucessListener<T> {
    /**
     * 当OkHttp请求获取返回的时候调用此接口
     * @param data okhttp请求返回的数据
     */
    void loadSuccess(T data);
}
