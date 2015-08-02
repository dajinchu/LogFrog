package com.gmail.dajinchu.desktop;

import com.gmail.dajinchu.SavedGameHelper;
import com.gmail.dajinchu.SavedGameListener;

/**
 * Created by Da-Jin on 6/2/2015.
 */
public class DesktopSavedGameHelper implements SavedGameHelper {
    @Override
    public void write(byte[] data) {

    }

    @Override
    public void load(SavedGameListener game) {
       game.onGameLoad(new byte[]{(byte)1});
    }

    @Override
    public void setStepsAchievement(String id, int steps) {

    }
}
