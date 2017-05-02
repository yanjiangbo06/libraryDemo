package cn.com.venvy.common.observer;

import android.os.Bundle;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class VenvyObservable {

    protected HashMap<String, List<WeakReference<VenvyObserver>>> observerMap = new HashMap();

    /**
     * @param tag  action 标识
     * @param observer
     */
    public void addObserver(String tag, VenvyObserver observer) {

        if (observer == null) {
            throw new NullPointerException("observer == null");
        }
        synchronized (this) {
            if (tag != null) {
                if (observerMap.containsKey(tag)) {
                    List<WeakReference<VenvyObserver>> list = observerMap.get(tag);
                    if (list != null) {
                        boolean needToAdd = true;
                        for (WeakReference<VenvyObserver> weakReference : list) {
                            if (weakReference != null && weakReference.get() != null && weakReference.get() == observer) {
                                needToAdd = false;
                            }
                        }
                        if (needToAdd) {
                            list.add(new WeakReference<>(observer));
                        }
                        observerMap.put(tag, list);
                        return;
                    }
                }
                List<WeakReference<VenvyObserver>> listObserver = new ArrayList();
                listObserver.add(new WeakReference<>(observer));
                observerMap.put(tag, listObserver);
            }
        }
    }

    public synchronized boolean removeObserverByTag(String tag) {
        return observerMap.containsKey(tag) && observerMap.remove(tag) != null;
    }


    public synchronized boolean removeObserver(String tag, VenvyObserver observer) {
        if (observerMap.containsKey(tag)) {
            List<WeakReference<VenvyObserver>> list = observerMap.get(tag);
            WeakReference<VenvyObserver> removeTag = null;
            if (list != null) {
                for (WeakReference<VenvyObserver> weakReference : list) {
                    if (weakReference != null && weakReference.get() != null && weakReference.get() == observer) {
                        removeTag = weakReference;
                    }
                }
                if (removeTag != null) {
                    return list.remove(removeTag);
                }
            }
        }
        return false;
    }

    public void removeAllObserver() {
        observerMap.clear();
    }

    public void sendToTarget(String tag, Bundle bundle) {
        if (observerMap.containsKey(tag)) {
            List<WeakReference<VenvyObserver>> list = observerMap.get(tag);
            if (list == null) {
                return;
            }
            for (WeakReference<VenvyObserver> observerWeakReference : list) {
                if (observerWeakReference != null) {
                    VenvyObserver observerResult = observerWeakReference.get();
                    if (observerResult != null) {
                        observerResult.notifyChanged(this, tag, bundle);
                    }
                }
            }
        }
    }

    public void sendToTarget(String tag) {
        this.sendToTarget(tag, null);
    }

}
