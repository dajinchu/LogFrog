package com.gmail.dajinchu;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.TimeUtils;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by Da-Jin on 5/23/2015.
 */
public class Model {
    IntMap<Node> nodes = new IntMap<Node>();
    Array<LinkSpace> linknodes = new Array<LinkSpace>();
    SnapshotArray<Link> links = new SnapshotArray<Link>();
    Node[][] nodegrid = new Node[5][7];

    int playerNode = 0;
    Link selected;

    //Analytics
    long loadedMillis;
    int movesToComplete=0;

    public Model(FileHandle level){
        BufferedReader reader = level.reader(64);
        String in, data[];
        int[] data1;
        int id=0;
        boolean readingNodes = true;

        try {
            while((in=reader.readLine())!=null) {
                if(in.equals("LINK")){
                    readingNodes=false;
                    continue;
                }
                data = in.split(" ");

                if (readingNodes) {
                    //make a Node
                    data1 = listparse(data);
                    nodes.put(id,new Node(id, data1[0], data1[1], this));
                } else {
                    //Link
                    data1 = listparse(data);

                    Node n1 = nodes.get(data1[0]), n2 = nodes.get(data1[1]);
                    new Link(n1, n2, this);
                }

                id++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        updateHighlight();

        loadedMillis= TimeUtils.millis();
    }

    public void selectLink(Link link){
        for(LinkSpace ls:linknodes)nodegrid[ls.x][ls.y]=null;
        linknodes.clear();

        if(link.state== Link.STATE.POTENTIAL){
            //Place it
            links.removeValue(selected,true);
            selected=null;
            link.state= Link.STATE.DISCONNECTED;
            playerNode=link.n1.id;

            //Record for Analytics
            movesToComplete++;
            return;
        }
        if(link.selected){
            //Deselect currently selected
            //Clicked on already selected
            clearSelection();
            return;
        }
        //Pick it up
        //Must be a connected link to be selected aka picked up
        if(link.state!= Link.STATE.CONNECTED)return;
        //If there is already someone selected, get rid of it
        if(selected!=null)selected.selected=false;
        selected = link;
        link.selected = true;
    }

    public void updateHighlight(){
        //Prevent overlap/intersection
        for(Link link:links){
            //Ignore potential links, and if it's selected, it'll get moved out of the way when replaced
            if(link.state== Link.STATE.POTENTIAL||link.selected)continue;
            int deltax = (link.n1.x-link.n2.x)/link.distance;
            int deltay = (link.n1.y-link.n2.y)/link.distance;
            for(int i = 1; i < link.distance; i++){
                new LinkSpace(link.n1.x-deltax*i,link.n1.y-deltay*i, this);
            }
        }
        Object[] l = links.begin();
        for(int i = 0, n = links.size; i < n; i++){
            if(((Link)l[i]).state == Link.STATE.POTENTIAL){
                links.removeValue((Link)l[i],true);
            }else {
                ((Link) l[i]).state = Link.STATE.DISCONNECTED;
            }
        }
        for(Node node: nodes.values()){
            node.on=false;
        }
        links.end();
        nodes.get(playerNode).traverseNode(selected);
    }

    public void clearSelection(){
        if(selected==null)return;
        selected.selected=false;
        selected=null;
        return;
    }

    private int[] listparse(String[] a){
        int[] ret = new int[a.length];
        for(int i = 0; i < a.length; i++){
            ret[i]=Integer.parseInt(a[i]);
        }
        return ret;
    }
}
