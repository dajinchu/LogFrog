package com.gmail.dajinchu;

/**
 * Created by Da-Jin on 6/9/2015.
 */
public class IosSavedGameHelper implements SavedGameHelper {
    @Override
    public void write(byte[] data) {

    }

    @Override
    public void load(MainGame game) {
        game.level=(1);
    }
}
