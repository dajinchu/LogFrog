package com.gmail.dajinchu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Da-Jin on 5/23/2015.
 */
public class Link {
    private final Model model;
    boolean horizontal;
    Node n1,n2;
    int distance;
    boolean selected=false;

    enum STATE{CONNECTED,POTENTIAL,DISCONNECTED};
    STATE state=STATE.DISCONNECTED;

    Rectangle hitBox;
    Vector2 center=new Vector2();
    float rotation;

    private static final int WIDTH= 10;

    public Link(Node n1, Node n2, Model model) {
        this.model = model;
        for(Link l:model.links){
            if((l.n1.equals(n1)&&l.n2.equals(n2))||
                    l.n1.equals(n2)&&l.n2.equals(n1))return;
        }
        model.links.add(this);
        this.n1=n1;
        this.n2=n2;
        if(n1.x==n2.x){
            horizontal=false;
            distance=Math.abs(n1.y-n2.y);
        }else if(n1.y==n2.y){
            horizontal=true;
            distance=Math.abs(n1.x-n2.x);
        }else{
            Gdx.app.error("Link", "diaganal between link "+n1.id+" and "+n2.id);
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
            //Horizontal, so the "width" of hitBox is the long side
            width=Math.abs(n1.x-n2.x)*20;
            height=WIDTH;
            left*=20;
            bottom*=20;
            bottom+=(8-WIDTH)/2;
            rotation = 90;

        }else{
            height=Math.abs(n1.y-n2.y)*20;
            width=WIDTH;
            left*=20;
            bottom*=20;
            left-=WIDTH/2;
            bottom+=8/2;
            rotation = 0;
        }
        hitBox = new Rectangle(left+4, bottom, width, height);
        hitBox.getCenter(center);
    }

    public void pickup(){
        rotation+=3;
    }
    public void drop(){
        rotation-=3;
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
