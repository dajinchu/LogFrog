package com.gmail.dajinchu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * Created by Da-Jin on 7/2/2015.
 */
public class Tutorial extends MainGame{

    Label topText, bottomText, bottomText2;
    Table instructions;
    Texture arrow = new Texture("arrow.jpg");
    private float arrowx, arrowy;
    private boolean isArrow;

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

    public void prevLevel(){
        level--;
        loadLevel();
    }

    @Override
    public void render(float time){
        super.render(time);
        viewport.apply();
        if(isArrow) {
            batch.begin();
            batch.setColor(Color.WHITE);
            batch.draw(arrow, arrowx, arrowy, arrow.getWidth() / 40f, arrow.getHeight() / 40f);
            batch.end();
        }
        switch (level){
            case 2:
                if(model.nodes.get(2).on) {
                    //Great, they placed it in the available spot
                    nextLevel();
                    break;
                }
        }
    }
    @Override
    public void makeOptionsMenu(){
        //No options in the tutorial, don't want to confuse the player.
        //Use this function to create the tut info instead

        instructions = new Table();
        instructions.setFillParent(true);
        instructions.center();

        Label.LabelStyle gLarge = new Label.LabelStyle(sm.labelStyleLarge);
        gLarge.fontColor = Color.LIGHT_GRAY;
        Label.LabelStyle gSmall = new Label.LabelStyle(sm.labelStyle);
        gSmall.fontColor = Color.LIGHT_GRAY;

        topText = new Label("",gLarge);
        bottomText = new Label("",gSmall);
        bottomText2 = new Label("",gSmall);

        instructions.add(topText).expandY().top().padTop(100);
        instructions.row();
        instructions.add(bottomText).bottom().padBottom(50);
        instructions.row();
        instructions.add(bottomText2).bottom().padBottom(50);

        uistage.addActor(instructions);
    }

    @Override
    public void onGameLoad(byte[] data){

    }

    @Override
    public void resize(int w, int h) {
        viewport.update(w, h, true);
    }
    @Override
    protected void loadLevel(){
        FileHandle modelData = Gdx.files.internal("tutorial"+level+".txt");
        FileHandle textData = Gdx.files.internal("tutorial"+level+"Text.txt");

        if(modelData.exists()){
            model = new Model(modelData);
        }
        if(textData.exists()){
            String[] data = textData.readString().split("\n");
            topText.setText(data[0]);
            bottomText.setText(data[1]);
            if(data.length>2)bottomText2.setText(data[2]);

            if(data.length>3) {
                isArrow=true;
                String[] nodes = data[3].split(" ");
                Node n1 = model.nodes.get(Integer.parseInt(nodes[0]));
                Node n2 = model.nodes.get(Integer.parseInt(nodes[1]));
                arrowx = (n1.x + n2.x) * 10 + MainGame.nodeRadius + MainGame.logWidth / 2;
                arrowy = (n1.y + n2.y) * 10 + MainGame.nodeRadius - arrow.getHeight() / 40 / 2;
            }else{
                isArrow=false;
            }
        }else{
            sm.prefs.putInteger("level", 1);
            sm.mainmenu();
            return;
        }
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        super.touchDown(screenX,screenY,pointer,button);
        switch (level){
            case 1:
                if(model.selected!=null) {
                    //Good, they have picked it up, tell them to place
                    nextLevel();
                }
                break;
            case 2:
                if(model.selected==null){
                    //Okay, they failed... pick it back up
                    prevLevel();
                }
                break;
        }
        return true;
    }
}