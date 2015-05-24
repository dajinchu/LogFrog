package com.gmail.dajinchu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.SnapshotArray;

/**
 * Created by Da-Jin on 5/23/2015.
 */
public class Node {

    int x,y;
    int id;
    SnapshotArray<Link> connected = new SnapshotArray<Link>();

    private final static Node[][] nodegrid = new Node[5][7];

    public Node(int id, int x, int y){
        this.id=id;
        this.x=x;
        this.y=y;
        nodegrid[x][y]=this;
    }

    public void traverseNode(Link selected){
        Gdx.app.log("traversingNode", selected+"");
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
        Gdx.app.log("Checking dir", "me "+x+","+y+" to "+(x + dirx * dist)+" "+(y + diry * dist));
        //Everything on the way must be empty
        for(int i = 1; i < dist-1; i++){
            try {
                Node temp =nodegrid[x + i * dirx][y + i * diry];
                Gdx.app.log("Checking in the way", temp.x+" "+temp.y);
                if (temp != null) {
                    Gdx.app.log("Way", "its in the way");
                    return;
                }
            }catch (ArrayIndexOutOfBoundsException e){

            }
        }
        try {
            if (nodegrid[x + dirx * dist][y + diry * dist] != null) {
                Gdx.app.log("Making link",(x + dirx * dist)+" "+(y + diry * dist));
                new Link(this, nodegrid[x + dirx * dist][y + diry * dist]).state = Link.STATE.POTENTIAL;
            }
        }catch (ArrayIndexOutOfBoundsException e){

        }
    }
}
