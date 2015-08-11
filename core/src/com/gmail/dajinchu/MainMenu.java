package com.gmail.dajinchu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Created by Da-Jin on 5/31/2015.
 */
public abstract class MainMenu implements Screen {
    public ScreenManager sm;
    public Stage stage;
    public Table table;
    public TextButton startGame;

    public void setScreenManager(ScreenManager sm) {
        this.sm=sm;
    }

    @Override
    public void show() {

        if(!sm.prefs.contains("level")){
            sm.setScreen(new Tutorial(sm,createAH(),createSGH()));
            return;
        }

        stage = sm.uistage;
        stage.clear();
        table = new Table();
        table.setFillParent(true);
        table.pad(10);

        startGame = new TextButton("Play", sm.buttonStyleLarge);
        startGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MainMenu.this.dispose();
                MainGame maingame = new MainGame(sm,createAH(),createSGH());
                sm.prefs.flush();
                sm.setScreen(maingame);
            }
        });
        TextButton tutorial = new TextButton("Tutorial", sm.buttonStyle);
        tutorial.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MainMenu.this.dispose();
                Tutorial tut = new Tutorial(sm, createAH(), createSGH());
                sm.prefs.flush();
                sm.setScreen(tut);
            }
        });
        table.add(startGame).center();
        table.row();
        table.add(tutorial).pad(20);

        stage.addActor(table);

    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        sm.uiviewport.apply();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    public abstract AnalyticsHelper createAH();
    public abstract SavedGameHelper createSGH();
}
