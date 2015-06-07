package com.gmail.dajinchu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

/**
 * Created by Da-Jin on 5/31/2015.
 */
public class ScreenManager extends Game {
    MainMenu menuScreen;
    public SpriteBatch batch;
    public BitmapFont font;
    public Label.LabelStyle labelStyle;
    public TextButton.TextButtonStyle buttonStyle;

    public ScreenManager(MainMenu menuScreen){
        this.menuScreen = menuScreen;
        menuScreen.setScreenManager(this);
    }
    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();

        SmartFontGenerator fontGenerator = new SmartFontGenerator();
        FileHandle exoFile = Gdx.files.internal("LiberationMono-Regular.ttf");
        BitmapFont fontSmall = fontGenerator.createFont(exoFile, "exo-medium", (int) (Gdx.graphics.getWidth()*.05f));

        Gdx.app.log("SM", Gdx.graphics.getWidth()+" "+fontSmall.getSpaceWidth());

        NinePatchDrawable buttonup =new NinePatchDrawable(new NinePatch(new Texture("buttonup.png"),1,1,1,1));
        NinePatchDrawable buttondown =new NinePatchDrawable(new NinePatch(new Texture("buttondown.png"),1,1,1,1));
        labelStyle = new Label.LabelStyle();
        labelStyle.font=fontSmall;
        labelStyle.fontColor=Color.WHITE;

        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = fontSmall;
        buttonStyle.downFontColor= Color.BLACK;
        buttonStyle.fontColor=Color.WHITE;
        buttonStyle.up=buttonup;
        buttonStyle.down=buttondown;


        setScreen(menuScreen);
    }

    @Override
    public void render(){
        super.render();
    }
    
    @Override
    public void dispose(){
        font.dispose();
        batch.dispose();
    }
}

