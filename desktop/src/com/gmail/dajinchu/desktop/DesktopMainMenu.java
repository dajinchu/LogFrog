package com.gmail.dajinchu.desktop;

import com.gmail.dajinchu.MainMenu;
import com.gmail.dajinchu.ScreenManager;
import com.gmail.dajinchu.Tutorial;

/**
 * Created by Da-Jin on 6/2/2015.
 */
public class DesktopMainMenu implements MainMenu {
    private ScreenManager sm;

    @Override
    public void setScreenManager(ScreenManager sm) {
        this.sm=sm;
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        sm.setScreen(new Tutorial(sm, new DesktopAnalyticsHelper(), new DesktopSavedGameHelper()));


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
