package com.gmail.dajinchu;

import com.badlogic.gdx.Gdx;

/**
 * Created by Da-Jin on 5/24/2015.
 */
public class LinkSpace extends Node {

    public LinkSpace(int x, int y, Model model) {
        super(0,x,y, model);
        model.linknodes.add(this);
        Gdx.app.log("LinkSpace", x+" "+y);
    }
    @Override
    public boolean solid(){
        return false;
    }
}
