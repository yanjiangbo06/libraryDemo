package cn.com.venvy.mvc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by yanjiangbo on 2017/4/28.
 */

public interface IVenvy {

    Activity getActivity();
    Context getContext();
    void startActivity(Intent intent);
    void addView();
    void removeView();
    void setController();

}
