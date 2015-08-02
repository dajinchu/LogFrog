package com.gmail.dajinchu;

/**
 * Created by Da-Jin on 6/9/2015.
 */
public class IosMainMenu extends MainMenu {
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
    public AnalyticsHelper createAH() {
        return null;
    }

    @Override
    public SavedGameHelper createSGH() {
        return null;
    }

    @Override
    public void show() {
        sm.setScreen(new MainGame(sm, new IosAnalyticsHelper(), new IosSavedGameHelper()));


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
