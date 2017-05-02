package cn.com.venvy.okhttp3.util.cookie.store;

import java.util.List;

import cn.com.venvy.okhttp3.Cookie;
import cn.com.venvy.okhttp3.HttpUrl;

public interface CookieStore
{

    void add(HttpUrl uri, List<Cookie> cookie);

    List<Cookie> get(HttpUrl uri);

    List<Cookie> getCookies();

    boolean remove(HttpUrl uri, Cookie cookie);

    boolean removeAll();

}
