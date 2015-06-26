package com.gmail.dajinchu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Created by Da-Jin on 6/25/2015.
 */
public class End implements Screen {
    private final ScreenManager sm;
    private final MainGame game;
    private Stage stage;
    private Table table;


    public End(ScreenManager sm, MainGame game){
        this.sm = sm;
        this.game = game;
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(1,1,1,1);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        table = new Table();
        table.setFillParent(true);
        Label nomore = new Label("No more levels.", sm.labelStyle);
        Label youwin = new Label("I guess that means you win!", sm.labelStyle);
        TextButton menu = new TextButton("Return to Menu", sm.buttonStyle);
        menu.padTop(10);
        menu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sm.mainmenu();
            }
        });
        TextButton restart = new TextButton("Restart from beginning", sm.buttonStyle);
        restart.padTop(10);
        restart.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.forceLevel(1);
                sm.setScreen(game);
            }
        });

        VerticalGroup column = new VerticalGroup();
        column.space(15);

        column.addActor(nomore);
        column.addActor(youwin);
        column.addActor(menu);
        column.addActor(restart);

        table.add(column).center();
        stage.addActor(table);
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
