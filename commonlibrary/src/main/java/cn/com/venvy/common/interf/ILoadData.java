package cn.com.venvy.common.interf;

import cn.com.venvy.common.http.base.IRequestHandler;
import cn.com.venvy.common.http.base.Request;

/**
 * 实现此接口用来发起get请求，比如
 * class LoadData implement ILoadData {
 *     private String url;
 *     @Overide
 *     void loadData(){
 *           OkHttpUtils.get().url(url).id(id).build().execute();
 *     }
 * }
 * Created by YanQiu on 2017/3/9.
 */

public interface ILoadData {
    /**
     * 发起get请求的方法
     */
    void loadData( );

    void loadData(Request request, IRequestHandler requestHandler);
    /**
     * 取消加载数据
     */
    void cancel(Request request);

    void cancel();
}
