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
    private Vector2 center = new Vector2();

    private static final float MOVE_LOG_ANIMATION_TIME = 100;

    private float animationProgress;
    Link movingLink;
    long beginMove;
    int rotate1, rotate2;
    Vector2 oldpos, newpos;

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

            l.rect.getCenter(center);
            if(l==movingLink&&animationProgress<MOVE_LOG_ANIMATION_TIME){
                float alpha = animationProgress/MOVE_LOG_ANIMATION_TIME;
                batch.draw(linkregion, MainGame.logWidth, l.distance*20, new Affine2().translate(oldpos.cpy().lerp(newpos,alpha)).rotate(MathUtils.lerp(rotate2,rotate1,alpha)));
            }else if(l.horizontal) {
                batch.draw(linkregion, MainGame.logWidth, l.distance * 20, new Affine2().translate(l.rect.x, center.y + MainGame.logWidth / 2).rotate(-90));
            }else {
                batch.draw(linkregion, MainGame.logWidth, l.distance*20, new Affine2().translate(center.x - MainGame.logWidth / 2, l.rect.y));
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

        Vector2 center = new Vector2();
        rotate1=0;
        rotate2=0;
        newLink.rect.getCenter(center);
        newpos = new Vector2(center.x - MainGame.logWidth / 2, newLink.rect.y);
        if(newLink.horizontal){
            rotate1 = -90;
            newpos.set(newLink.rect.x, center.y + MainGame.logWidth / 2);
        }
        oldLink.rect.getCenter(center);
        oldpos = new Vector2(center.x - MainGame.logWidth / 2, oldLink.rect.y);
        if(oldLink.horizontal){
            rotate2 = -90;
            oldpos.set(oldLink.rect.x, center.y + MainGame.logWidth / 2);
        }
    }
}
