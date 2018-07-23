package com.planday.deliveroo;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by longnguyen on 11:44 AM, 7/20/18.
 */
public class SubscriptionManager {

    private static ConcurrentHashMap<String, Object> subscriptionMap;

    public static <T> void subscribe(Class<T> clazz, Subscription subscription) {
        subscribe(clazz.getCanonicalName(), subscription);
    }

    public static <T> void subscribe(String clazzKey, Subscription subscription) {
        if(subscription == null) return;

        if(subscriptionMap == null) {
            subscriptionMap = new ConcurrentHashMap<>();
        }

        if(!subscriptionMap.containsKey(clazzKey) || subscriptionMap.get(clazzKey) == null) {
            subscriptionMap.put(clazzKey, new CompositeSubscription());
        }

        CompositeSubscription compositeSubscription = (CompositeSubscription) subscriptionMap.get(clazzKey);

        compositeSubscription.add(subscription);
    }

    public static <T> void unsubscribe(Class<T> clazz) {
        unsubscribe(clazz.getCanonicalName());
    }

    public static <T> void unsubscribe(String clazzKey) {
        if(subscriptionMap != null) {

            if (subscriptionMap.containsKey(clazzKey) && subscriptionMap.get(clazzKey) != null) {
                CompositeSubscription compositeSubscription = (CompositeSubscription) subscriptionMap.get(clazzKey);

                compositeSubscription.unsubscribe();
                compositeSubscription.clear();

                subscriptionMap.remove(clazzKey);
            }
        }
    }

    public static void unsubscribeAll() {
        if(subscriptionMap != null) {
            Iterator<ConcurrentHashMap.Entry<String, Object>> it = subscriptionMap.entrySet().iterator();
            while (it.hasNext()) {
                ConcurrentHashMap.Entry<String, Object> entry = it.next();
                Object obj = entry.getValue();
                if (obj != null) {
                    CompositeSubscription subscription = (CompositeSubscription) obj;

                    subscription.unsubscribe();
                    subscription.clear();
                }

                it.remove();
            }
        }
    }
}
