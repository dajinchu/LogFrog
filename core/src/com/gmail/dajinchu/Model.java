package com.gmail.dajinchu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by Da-Jin on 5/23/2015.
 */
public class Model {

    Array<Node> nodes = new Array<Node>();
    Array<Link> links = new Array<Link>();


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
                    nodes.add(new Node(data1[1], data1[2], data1[3]));
                } else if (data[0].equals("L")) {
                    //Link
                    data[0] = "0";
                    data1 = listparse(data);

                    Node n1 = nodes.get(data1[1]), n2 = nodes.get(data1[2]);
                    links.add(new Link(n1, n2));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gdx.app.log("Model",level.readString());

        for(Node n:nodes){
            Gdx.app.log("Model", "Node at "+n.x+","+n.y);
        }
        for(Link l:links){
            Gdx.app.log("Model", "Link at "+l.n1.x+","+l.n1.y+"->"+l.n2.x+","+l.n2.y);
        }

    }

    private int[] listparse(String[] a){
        int[] ret = new int[a.length];
        for(int i = 0; i < a.length; i++){
            ret[i]=Integer.parseInt(a[i]);
        }
        return ret;
    }
}
