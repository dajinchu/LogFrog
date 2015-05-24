package com.gmail.dajinchu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Da-Jin on 5/23/2015.
 */
public class Link {
    Node n1,n2;
    int distance;
    enum STATE{SELECTED,CONNECTED,POTENTIAL,DISCONNECTED};
    STATE state=STATE.DISCONNECTED;

    Rectangle rect;

    public Link(Node n1, Node n2) {
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
            left=n1.x * 20;
        }else{
            //n2 is left
            left=n2.x * 20;
        }
        //Find the bottom node
        if(n1.y<n2.y){
            //n1 is bottom
            bottom=n1.y * 20;
        }else{
            //n2 is bottom
            bottom=n2.y * 20;
        }
        if(horizontal){
            //Horizontal, so the "width" of rect is the long side
            width=Math.abs(n1.x-n2.x)*20;
            height=6;
            bottom+=(10-6)/2;
        }else{
            height=Math.abs(n1.y-n2.y)*20;
            width=6;
            left-=6/2;
            bottom+=10/2;
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
