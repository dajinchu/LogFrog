package com.gmail.dajinchu;

/**
 * Created by Da-Jin on 5/29/2015.
 */

/**
 * Interface to let Google Analytics work cross platform.
 * Should be interfaced for each platform using the Analytics SDK for that platform
 * All it does is make the Tracker's available
 */

public interface AnalyticsHelper {

    void sendCustomTiming(String category, int value, String variable, String label);
    void sendEvent(String category, String action, String label, long value);
}
