package com.gmail.dajinchu;

/**
 * Created by Da-Jin on 5/30/2015.
 */
public interface SavedGameHelper {
    public void write(byte[] data);
    void load(SavedGameListener game);
}
