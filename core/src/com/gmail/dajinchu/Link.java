package com.gmail.dajinchu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Da-Jin on 5/23/2015.
 */
public class Link {
    private final Model model;
    Node n1,n2;
    int distance;
    boolean selected=false;

    enum STATE{CONNECTED,POTENTIAL,DISCONNECTED};
    STATE state=STATE.DISCONNECTED;

    Rectangle rect;

    private static final int WIDTH= 10;

    public Link(Node n1, Node n2, Model model) {
        this.model = model;
        for(Link l:model.links){
            if((l.n1.equals(n1)&&l.n2.equals(n2))||
                    l.n1.equals(n2)&&l.n2.equals(n1))return;
        }
        model.links.add(this);
        boolean horizontal;
        this.n1=n1;
        this.n2=n2;
        if(n1.x==n2.x){
            horizontal=false;
            distance=Math.abs(n1.y-n2.y);
        }else if(n1.y==n2.y){
            horizontal=true;
            distance=Math.abs(n1.x-n2.x);
        }else{
            throw new RuntimeException("Fuck me, tried to connect log diagonally! DAMN");
        }
        n1.connected.add(this);
        n2.connected.add(this);

        int left,bottom,width,height;
        //Find the left node
        if(n1.x<n2.x){
            //n1 is left
            left=n1.x;
        }else{
            //n2 is left
            left=n2.x;
        }
        //Find the bottom node
        if(n1.y<n2.y){
            //n1 is bottom
            bottom=n1.y;
        }else{
            //n2 is bottom
            bottom=n2.y;
        }
        if(horizontal){
            //Horizontal, so the "width" of rect is the long side
            width=Math.abs(n1.x-n2.x)*20;
            height=WIDTH;
            left*=20;
            bottom*=20;
            bottom+=(8-WIDTH)/2;

        }else{
            height=Math.abs(n1.y-n2.y)*20;
            width=WIDTH;
            left*=20;
            bottom*=20;
            left-=WIDTH/2;
            bottom+=8/2;
        }
        Gdx.app.log("Rect Create",horizontal+" "+left+" "+bottom+" "+width+" "+height);
        rect = new Rectangle(left, bottom, width, height);


    }

    public Node getOther(Node node){
        if(n1.equals(node)){
            return n2;
        }
        if(n2.equals(node)){
            return n1;
        }
        throw new RuntimeException("used getOTher with a neither");
    }
}
