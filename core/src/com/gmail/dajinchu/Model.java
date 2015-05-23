package com.gmail.dajinchu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;

/**
 * Created by Da-Jin on 5/23/2015.
 */
public class Model {

    private ArrayList<Node> nodes = new ArrayList<Node>();


    public Model(FileHandle level){
        Gdx.app.log("Model",level.readString());
    }
}
