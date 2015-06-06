package com.gmail.dajinchu.desktop;

import com.gmail.dajinchu.MainGame;
import com.gmail.dajinchu.SavedGameHelper;

/**
 * Created by Da-Jin on 6/2/2015.
 */
public class DesktopSavedGameHelper implements SavedGameHelper {
    @Override
    public void write(byte[] data) {

    }

    @Override
    public void load(MainGame game) {
        game.loadLevel(1);
    }
}
