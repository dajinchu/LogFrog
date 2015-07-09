package com.gmail.dajinchu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.badlogic.gdx.utils.async.AsyncTask;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Da-Jin on 5/31/2015.
 */
public class ScreenManager extends Game {
    MainMenu menuScreen;
    public SpriteBatch batch;
    public ShapeRenderer renderer;

    public Stage uistage;
    public Viewport uiviewport;

    public BitmapFont font;
    public Label.LabelStyle labelStyle, labelStyleLarge;
    public TextButton.TextButtonStyle buttonStyle, buttonStyleLarge;
    public CheckBox.CheckBoxStyle checkBoxStyle;
    public Preferences prefs;
    public InputMultiplexer multiplexer;
    AssetManager assets;
    AsyncExecutor executor = new AsyncExecutor(1);
    boolean done = false;

    public ScreenManager(MainMenu menuScreen){
        this.menuScreen = menuScreen;
        menuScreen.setScreenManager(this);
    }
    @Override
    public void create() {
        Bench.start("smload");
        prefs = Gdx.app.getPreferences("My Prefs");
        Gdx.input.setCatchBackKey(true);

        batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        font = new BitmapFont();

        Bench.start("viewport");
        uiviewport = new ScreenViewport();
        uiviewport.update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        uistage = new Stage(uiviewport);
        Bench.end("viewport");

        Bench.start("multiplex");
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(uistage);
        Gdx.input.setInputProcessor(multiplexer);
        Bench.start("multiplex");



        setScreen(new Loading(this));

    }

    public void setupAssets(){
        assets = new AssetManager();

        Bench.start("smartfontinit");
        final SmartFontGenerator fontGenerator = new SmartFontGenerator();
        Bench.end("smartfontinit");
        Bench.start("loadfont");
        final FileHandle exoFile = Gdx.files.internal("LiberationMono-Regular.ttf");
        Bench.end("loadfont");

        Gdx.app.log("Sm", "async task goin");

        Bench.start("createFont");
        final BitmapFont fontSmall = fontGenerator.createFont(exoFile, "exo-medium", (int) (Gdx.graphics.getWidth() * .05f));
        final BitmapFont fontLarge = fontGenerator.createFont(exoFile, "exo-large", (int) (Gdx.graphics.getWidth() * .15f));
        Bench.end("createFont");

        executor.submit(new AsyncTask<Object>() {
            @Override
            public Object call() throws Exception {
                assets.load("buttonuplight.png", Texture.class);
                assets.finishLoading();

                NinePatchDrawable buttonup = new NinePatchDrawable(new NinePatch(assets.get("buttonuplight.png", Texture.class), 1, 1, 1, 1));
                NinePatchDrawable buttondown = new NinePatchDrawable(new NinePatch(new Texture("buttondownlight.png"), 1, 1, 1, 1));
                labelStyle = new Label.LabelStyle();
                labelStyle.font = fontSmall;
                labelStyle.fontColor = Color.BLACK;

                labelStyleLarge = new Label.LabelStyle(labelStyle);
                labelStyleLarge.font = fontLarge;

                buttonStyle = new TextButton.TextButtonStyle();
                buttonStyle.font = fontSmall;
                buttonStyle.downFontColor = Color.WHITE;
                buttonStyle.fontColor = Color.BLACK;
                buttonStyle.up = buttonup;
                buttonStyle.down = buttondown;

                buttonStyleLarge = new TextButton.TextButtonStyle(buttonStyle);
                buttonStyleLarge.font = fontLarge;

                assets.load("checked.png", Texture.class);
                assets.finishLoading();
                TextureRegionDrawable checked = new TextureRegionDrawable(new TextureRegion(assets.get("checked.png",Texture.class)));
                TextureRegionDrawable unchecked = new TextureRegionDrawable(new TextureRegion(new Texture("unchecked.png")));
                checkBoxStyle = new CheckBox.CheckBoxStyle(unchecked, checked, fontSmall, Color.WHITE);
                checkBoxStyle.up = buttonup;
                Bench.end("smload");
                done=true;
                return null;

            }
        });


    }



    public void mainmenu(){
        prefs.flush();
        setScreen(menuScreen);
    }

    @Override
    public void pause(){
        prefs.flush();
    }

    @Override
    public void render(){
        super.render();
    }
    
    @Override
    public void dispose(){
        font.dispose();
        batch.dispose();
        renderer.dispose();
    }

    @Override
    public void resize(int w,int h){
        super.resize(w, h);
        if(uiviewport!=null) {
            uiviewport.update(w, h);
            uistage.setViewport(uiviewport);
        }
    }
}

