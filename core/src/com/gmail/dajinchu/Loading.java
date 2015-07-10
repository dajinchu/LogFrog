package com.gmail.dajinchu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

/**
 * Created by Da-Jin on 7/8/2015.
 */
public class Loading implements Screen {

    private final ScreenManager sm;
    Texture hi = new Texture("link letter logo.png");
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

        sm.uistage.draw();


        go = true;
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        sm.uistage.clear();
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(new Image(new TextureRegionDrawable(new TextureRegion(hi)), Scaling.fillX)).pad(10);
        sm.uistage.addActor(table);
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
