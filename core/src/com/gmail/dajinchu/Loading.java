package com.gmail.dajinchu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Da-Jin on 7/8/2015.
 */
public class Loading implements Screen {

    private final ScreenManager sm;
    Texture hi = new Texture("loading.png");
    private boolean go = false;

    public Loading(ScreenManager sm){
        this.sm= sm;
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(go){
            sm.setupAssets();
        }

        Gdx.app.log("loading", Gdx.graphics.getWidth()+" "+hi.getWidth());
        sm.batch.begin();
        //TODO USE A FUCKING VIEWPORT
        sm.batch.draw(hi, Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()/2,Gdx.graphics.getWidth()/2,Gdx.graphics.getWidth()/2*(hi.getHeight()/(float)hi.getWidth()));
        sm.batch.end();
        go = true;

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

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
