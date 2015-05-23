package com.gmail.dajinchu;

/**
 * Created by Da-Jin on 5/23/2015.
 */
public class Link {
    Node n1,n2;
    int distance;
    boolean on = false;

    public Link(Node n1, Node n2) {
        this.n1=n1;
        this.n2=n2;
        if(n1.x==n2.x){
            distance=Math.abs(n1.y-n2.y);
        }else if(n1.y==n2.y){
            distance=Math.abs(n1.x-n2.x);
        }else{
            throw new RuntimeException("Fuck me, tried to connect log diagonally! DAMN");
        }
        n1.connected.add(this);
        n2.connected.add(this);
    }
}
