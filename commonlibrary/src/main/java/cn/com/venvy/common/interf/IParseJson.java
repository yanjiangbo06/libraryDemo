package cn.com.venvy.common.interf;


/**
 * 数据解析接口
 * R:返回类型，S解析数据源
 * example:
 * class ParsePic implement IParseJson<Pic,String>{
 *      @Overide
 *      pubic Pic parseData(String json){
 *          Pic pic = new Pic();
 *
 *          JSONObject jsonObj = new JSONObject(json);
 *          pic.setUrl(jsonObj.optString("url"));
 *          pic.setTitle(jsonObj.optString("title"));
 *
 *          return pic;
 *      }
 * }
* Created by YanQiu on 2017/3/7.
 */

public interface IParseJson<R,S> {
    /**
     *
     * @param s 解析数据源
     * @return 返回解析后的数据类型，
     */
    R parseData(S s);
}
