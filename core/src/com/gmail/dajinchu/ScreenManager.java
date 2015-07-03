package com.gmail.dajinchu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
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
    public Label.LabelStyle labelStyle;
    public TextButton.TextButtonStyle buttonStyle, buttonStyleLarge;
    public CheckBox.CheckBoxStyle checkBoxStyle;
    public Preferences prefs;
    public InputMultiplexer multiplexer;

    public ScreenManager(MainMenu menuScreen){
        this.menuScreen = menuScreen;
        menuScreen.setScreenManager(this);
    }
    @Override
    public void create() {
        prefs=Gdx.app.getPreferences("My Prefs");
        Gdx.input.setCatchBackKey(true);

        batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        font = new BitmapFont();

        SmartFontGenerator fontGenerator = new SmartFontGenerator();
        FileHandle exoFile = Gdx.files.internal("LiberationMono-Regular.ttf");
        BitmapFont fontSmall = fontGenerator.createFont(exoFile, "exo-medium", (int) (Gdx.graphics.getWidth() * .05f));
        BitmapFont fontLarge = fontGenerator.createFont(exoFile, "exo-large", (int) (Gdx.graphics.getWidth()*.15f));

        NinePatchDrawable buttonup =new NinePatchDrawable(new NinePatch(new Texture("buttonuplight.png"),1,1,1,1));
        NinePatchDrawable buttondown =new NinePatchDrawable(new NinePatch(new Texture("buttondownlight.png"),1,1,1,1));
        labelStyle = new Label.LabelStyle();
        labelStyle.font=fontSmall;
        labelStyle.fontColor=Color.BLACK;

        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = fontSmall;
        buttonStyle.downFontColor= Color.WHITE;
        buttonStyle.fontColor=Color.BLACK;
        buttonStyle.up=buttonup;
        buttonStyle.down=buttondown;

        buttonStyleLarge = new TextButton.TextButtonStyle(buttonStyle);
        buttonStyleLarge.font = fontLarge;

        TextureRegionDrawable checked = new TextureRegionDrawable(new TextureRegion(new Texture("checked.png")));
        TextureRegionDrawable unchecked = new TextureRegionDrawable(new TextureRegion(new Texture("unchecked.png")));
        checkBoxStyle = new CheckBox.CheckBoxStyle(unchecked,checked,fontSmall,Color.WHITE);
        checkBoxStyle.up=buttonup;

        uiviewport = new ScreenViewport();
        uistage = new Stage(uiviewport);

        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(uistage);
        Gdx.input.setInputProcessor(multiplexer);

        mainmenu();
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
        super.resize(w,h);
        uiviewport.update(w,h);
        uistage.setViewport(uiviewport);
    }
}

