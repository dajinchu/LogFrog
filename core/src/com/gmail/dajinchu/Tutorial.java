package com.gmail.dajinchu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * Created by Da-Jin on 7/2/2015.
 */
public class Tutorial extends MainGame{

    public Tutorial(ScreenManager sm, AnalyticsHelper ah, SavedGameHelper sgh) {
        super(sm, ah, sgh);
        level = 1;
    }

    @Override
    public void makeInfoTable(){
        levelinfo = new Label("",sm.labelStyle);
    }

    @Override
    public void nextLevel(){
        level++;
        loadLevel();
    }

    @Override
    public void makeOptionsMenu(){

    }

    @Override
    public void onGameLoad(byte[] data){

    }

    @Override
    public void resize(int w, int h) {
        viewport.update(w,h,true);
    }
    @Override
    protected void loadLevel(){
        if(level == 2){
            sm.mainmenu();
            return;
        }
        model = new Model(Gdx.files.internal("tutorial"+level+".txt"));
    }


}