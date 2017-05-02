package cn.com.venvy.mvc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

/**
 * Created by yanjiangbo on 2017/5/2.
 */

public abstract class BaseView extends View implements IVenvy {

    public BaseView(Context context) {
        super(context);
    }

    @Override
    public Activity getActivity() throws IllegalArgumentException {
        if (this.getContext() instanceof Activity) {
            return (Activity) this.getContext();
        } else {
            throw (new IllegalArgumentException("View not add to activity"));
        }
    }

    @Override
    public void setController() {

    }

    @Override
    public void startActivity(Intent intent) {

    }

    @Override
    public void addView() {

    }

    @Override
    public void removeView() {

    }
}
