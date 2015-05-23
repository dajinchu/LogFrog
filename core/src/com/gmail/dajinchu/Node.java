package com.gmail.dajinchu;

import java.util.ArrayList;

/**
 * Created by Da-Jin on 5/23/2015.
 */
public class Node {

    int x,y;
    int id;
    ArrayList<Link> connected = new ArrayList<Link>();

    public Node(int id, int x, int y){
        this.id=id;
        this.x=x;
        this.y=y;
    }
}
