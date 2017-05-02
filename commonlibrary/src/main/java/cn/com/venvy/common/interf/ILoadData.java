package cn.com.venvy.common.interf;

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
    void loadData();
}
