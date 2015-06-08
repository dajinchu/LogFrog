package com.gmail.dajinchu;

import com.badlogic.gdx.utils.SnapshotArray;

/**
 * Created by Da-Jin on 5/23/2015.
 */
public class Node {

    private final Model model;
    int x,y;
    int id;
    SnapshotArray<Link> connected = new SnapshotArray<Link>();

    boolean on = false;

    public Node(int id, int x, int y, Model model){
        this.model = model;
        this.id=id;
        this.x=x;
        this.y=y;
        model.nodegrid[x][y]=this;
    }

    public void traverseNode(Link selected){
        on=true;
        Object[] l = connected.begin();
        for(int i = 0, n = connected.size; i < n; i++){
            if(((Link)l[i]).state== Link.STATE.CONNECTED||((Link)l[i]).state== Link.STATE.POTENTIAL)continue;
            ((Link)l[i]).state= Link.STATE.CONNECTED;
            ((Link)l[i]).getOther(this).traverseNode(selected);
        }
        connected.end();
        if(selected!=null){
            for(int i = -1; i < 2; i+=2){
                checkDirection(i,0,selected.distance);
            }
            for(int i = -1; i < 2; i+=2){
                checkDirection(0,i,selected.distance);
            }
        }
    }
    public void checkDirection(int dirx, int diry, int dist){
        //Everything on the way must be empty
        for(int i = 1; i < dist; i++){
            try {
                Node temp =model.nodegrid[x + i * dirx][y + i * diry];
                //Gdx.app.log("Checking in the way", temp.x+" "+temp.y);
                if (model.nodegrid[x + i * dirx][y + i * diry] != null) {
                    return;
                }
            }catch (ArrayIndexOutOfBoundsException e){

            }
        }
        try {
            if (model.nodegrid[x + dirx * dist][y + diry * dist]!=null&&model.nodegrid[x + dirx * dist][y + diry * dist].solid()) {
                new Link(this, model.nodegrid[x + dirx * dist][y + diry * dist], model).state = Link.STATE.POTENTIAL;
            }
        }catch (ArrayIndexOutOfBoundsException e){

        }
    }
    public boolean solid(){
        return true;
    }
}
