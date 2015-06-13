package com.gmail.dajinchu;

/**
 * Created by Da-Jin on 6/9/2015.
 */
public interface SavedGameListener {
    int resolveConflict(byte[] data1, byte[] data2);

    public void onGameLoad(byte[] data);
}
