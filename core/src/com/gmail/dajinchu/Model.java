package com.gmail.dajinchu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.SnapshotArray;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by Da-Jin on 5/23/2015.
 */
public class Model {

    IntMap<Node> nodes = new IntMap<Node>();
    public static SnapshotArray<Link> links = new SnapshotArray<Link>();

    int playerNode = 0;

    Link selected;

    public Model(FileHandle level){
        BufferedReader reader = level.reader(64);
        String in, data[];
        int[] data1;

        try {
            while((in=reader.readLine())!=null) {
                data = in.split(" ");

                if (data[0].equals("N")) {
                    //0 is N, so make a Node
                    data[0] = "0";
                    data1 = listparse(data);
                    nodes.put(data1[1],new Node(data1[1], data1[2], data1[3]));
                } else if (data[0].equals("L")) {
                    //Link
                    data[0] = "0";
                    data1 = listparse(data);

                    Node n1 = nodes.get(data1[1]), n2 = nodes.get(data1[2]);
                    new Link(n1, n2);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gdx.app.log("Model",level.readString());

        for(Node n:nodes.values()){
            Gdx.app.log("Model", "Node at "+n.x+","+n.y);
        }
        for(Link l:links){
            Gdx.app.log("Model", "Link at "+l.n1.x+","+l.n1.y+"->"+l.n2.x+","+l.n2.y);
        }

        updateHighlight();
    }

    public void selectLink(Link link){
        if(link.state== Link.STATE.POTENTIAL){
            links.removeValue(selected,true);
            selected=null;
            link.state= Link.STATE.DISCONNECTED;
            playerNode=link.n1.id;
            updateHighlight();
            return;
        }
        if(link.selected){
            //Clicked on already selected
            link.selected=false;
            selected=null;
            updateHighlight();
            return;
        }
        //Must be a connected link to be selected aka picked up
        if(link.state!= Link.STATE.CONNECTED)return;
        //If there is already someone selected, get rid of it
        if(selected!=null)selected.selected=false;
        selected = link;
        link.selected = true;
        updateHighlight();
    }

    private void updateHighlight(){
        Object[] l = links.begin();
        for(int i = 0, n = links.size; i < n; i++){
            if(((Link)l[i]).state== Link.STATE.POTENTIAL){
                links.removeIndex(i);
            }else {
                ((Link) l[i]).state = Link.STATE.DISCONNECTED;
            }
        }
        links.end();
        nodes.get(playerNode).traverseNode(selected);
    }

    private int[] listparse(String[] a){
        int[] ret = new int[a.length];
        for(int i = 0; i < a.length; i++){
            ret[i]=Integer.parseInt(a[i]);
        }
        return ret;
    }
}
