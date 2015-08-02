package com.gmail.dajinchu.logfrog;

import android.content.Context;

import com.gmail.dajinchu.AnalyticsHelper;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by Da-Jin on 5/29/2015.
 */
public class AndroidAnalyticsHelper implements AnalyticsHelper {
    private static GoogleAnalytics analytics;
    private static Tracker tracker;

    public static Tracker getTracker(Context context){
        if(tracker==null){
            analytics = GoogleAnalytics.getInstance(context);
            tracker=analytics.newTracker(R.xml.app_tracker);
        }
        return tracker;
    }

    public AndroidAnalyticsHelper(Context context){
        getTracker(context);
    }

    @Override
    public void sendCustomTiming(String category, int value, String variable, String label) {
        tracker.send(new HitBuilders.TimingBuilder()
                .setCategory(category)
                .setValue(value)
                .setVariable(variable)
                .setLabel(label)
                .build());
    }

    @Override
    public void sendEvent(String category, String action, String label, long value) {
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .setValue(value)
                .build());
    }
}
