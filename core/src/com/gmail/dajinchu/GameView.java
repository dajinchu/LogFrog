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
    public static float MOVE_LOG_ANIMATION_TIME = 100;//milliseconds

    private final MainGame game;
    private final Texture link, node;
    private final TextureRegion linkregion;
    private final Texture shadow;

    private float animationProgress;
    Link movingLink;
    long beginMove;
    private Vector2 newCenter = new Vector2(), oldCenter = new Vector2();
    int newRotate, oldRotate;
    Vector2 oldpos, newpos;
    private Vector2 movingpos;
    private Vector2 tempCenter = new Vector2();

    Color connected = new Color(238f/255f,215f/255f,81f/255f,1);
    Color disconnected = new Color(149f/255f,178f/255f,220f/255f,1);
    Color nodeGoal = new Color(167f/255f,202f/255f,85f/255f,1);

    public GameView(MainGame game){
        this.game = game;
        link = new Texture("white.png");
        node = new Texture("ship2.png");
        shadow = new Texture("buttondown.png");
        linkregion = new TextureRegion(link);
    }


    public void draw(Batch batch){
        Model model = game.model;
        animationProgress= TimeUtils.millis()-beginMove;
        for(Node n:model.nodes.values()){
            if(n.on) {
                batch.setColor(connected);
            }else{
                batch.setColor(disconnected);
            }
            if(n.id==model.nodes.size-1)batch.setColor(nodeGoal);
            batch.draw(node, n.x * 20, n.y * 20, MainGame.nodeRadius * 2, MainGame.nodeRadius * 2);
        }
        for(Link l:model.links){
            l.rect.getCenter(tempCenter);

            if(l.selected){
                batch.setColor(Color.BLACK);
                batch.draw(shadow,tempCenter.x-MainGame.logWidth-2,tempCenter.y-l.distance*20-2,MainGame.logWidth+4,l.rect.height+4);
            }
            switch (l.state){
                case CONNECTED:batch.setColor(connected);break;
                case DISCONNECTED:batch.setColor(disconnected);break;
                case POTENTIAL:
                    if(!game.hints)continue;
                    batch.setColor(Color.LIGHT_GRAY);
                    break;
            }

            if(l==movingLink&&animationProgress<MOVE_LOG_ANIMATION_TIME){
                float alpha = animationProgress/MOVE_LOG_ANIMATION_TIME;
                movingpos = oldpos.cpy().lerp(newpos, alpha);
                batch.draw(linkregion,
                        movingpos.x-MainGame.logWidth/2,movingpos.y-l.distance*10,
                        MainGame.logWidth/2, l.distance*20/2,
                        MainGame.logWidth, l.distance*20,
                        1,1,
                        MathUtils.lerp(oldRotate, newRotate, alpha));
            }else if(l.horizontal) {
                batch.draw(linkregion, MainGame.logWidth, l.distance * 20, new Affine2().translate(l.rect.x, tempCenter.y + MainGame.logWidth / 2).rotate(-90));
            }else {
                batch.draw(linkregion, MainGame.logWidth, l.distance*20, new Affine2().translate(tempCenter.x - MainGame.logWidth / 2, l.rect.y));
            }
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
