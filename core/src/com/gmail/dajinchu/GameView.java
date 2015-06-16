package com.gmail.dajinchu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by Da-Jin on 6/2/2015.
 */
public class GameView {

    private final MainGame game;
    private final Texture link, node;
    private final TextureRegion linkregion;

    private float animationProgress;
    Link movingLink;
    long beginMove;
    private Vector2 newCenter = new Vector2(), oldCenter = new Vector2();
    int newRotate, oldRotate;
    Vector2 oldpos, newpos;
    private Vector2 movingpos;
    private Vector2 tempCenter = new Vector2();

    public GameView(MainGame game){
        this.game = game;
        link = new Texture("white.png");
        node = new Texture("ship2.png");
        linkregion = new TextureRegion(link);
    }


    public void draw(Batch batch){
        Model model = game.model;
        animationProgress= TimeUtils.millis()-beginMove;
        for(Link l:model.links){
            switch (l.state){
                case CONNECTED:batch.setColor(Color.YELLOW);break;
                case DISCONNECTED:batch.setColor(Color.WHITE);break;
                case POTENTIAL:
                    if(!game.hints)continue;
                    batch.setColor(Color.LIGHT_GRAY);
                    break;
            }
            if(l.selected)batch.setColor(Color.MAROON);

            l.rect.getCenter(tempCenter);
            if(l==movingLink&&animationProgress<MainGame.MOVE_LOG_ANIMATION_TIME){
                float alpha = animationProgress/MainGame.MOVE_LOG_ANIMATION_TIME;
                movingpos = oldpos.cpy().lerp(newpos, alpha);
                batch.draw(linkregion,
                        movingpos.x,movingpos.y-l.distance*10,
                        MainGame.logWidth/2, l.distance*20/2,
                        MainGame.logWidth, l.distance*20,
                        1,1,
                        MathUtils.lerp(oldRotate, newRotate,alpha));
            }else if(l.horizontal) {
                batch.draw(linkregion, MainGame.logWidth, l.distance * 20, new Affine2().translate(l.rect.x, tempCenter.y + MainGame.logWidth / 2).rotate(-90));
            }else {
                batch.draw(linkregion, MainGame.logWidth, l.distance*20, new Affine2().translate(tempCenter.x - MainGame.logWidth / 2, l.rect.y));
            }
        }
        for(Node n:model.nodes.values()){
            if(n.on) {
                batch.setColor(Color.YELLOW);
            }else{
                batch.setColor(Color.WHITE);
            }
            if(n.id==model.nodes.size-1)batch.setColor(Color.GREEN);
            batch.draw(node, n.x * 20, n.y * 20, MainGame.nodeRadius * 2, MainGame.nodeRadius * 2);
        }
    }

    public void animateLinks(Link oldLink, Link newLink){
        if(oldLink==null||newLink==null)return;
        movingLink = newLink;
        beginMove = TimeUtils.millis();


        newLink.rect.getCenter(newCenter);
        oldLink.rect.getCenter(oldCenter);

        Vector2 delta = newCenter.cpy().sub(oldCenter);
        //Slope is 1 or negative 1
        float slope;
        if(delta.x==0||delta.y==0){
            slope = 1;
        }else {
            slope = delta.y / delta.x;
            slope /= Math.abs(slope);
        }

        newRotate =0;
        oldRotate =0;

        newpos=newCenter.cpy();
        if(newLink.horizontal){
            newRotate = (int) (90*slope);
        }
        oldpos=oldCenter.cpy();
        if(oldLink.horizontal){
            oldRotate = (int) (90*slope);
        }
    }
}
