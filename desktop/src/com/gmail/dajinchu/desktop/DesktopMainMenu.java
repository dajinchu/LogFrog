package com.gmail.dajinchu.desktop;

import com.gmail.dajinchu.AnalyticsHelper;
import com.gmail.dajinchu.MainMenu;
import com.gmail.dajinchu.SavedGameHelper;

/**
 * Created by Da-Jin on 6/2/2015.
 */
public class DesktopMainMenu extends MainMenu {
    @Override
    public AnalyticsHelper createAH() {
        return new DesktopAnalyticsHelper();
    }

    @Override
    public SavedGameHelper createSGH() {
        return new DesktopSavedGameHelper();
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
