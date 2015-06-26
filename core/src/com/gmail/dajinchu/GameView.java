package com.gmail.dajinchu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by Da-Jin on 6/2/2015.
 */
public class GameView {
    public static float MOVE_LOG_ANIMATION_TIME = 150;//milliseconds

    private final MainGame game;
    private final Texture link, node;
    private final TextureRegion linkregion, shadowregion;
    private final Texture shadow;

    private float animationProgress;
    Link movingLink;
    long beginMove;
    float newRotate;
    float oldRotate;
    Vector2 oldpos, newpos;
    private Vector2 movingpos;

    Color connected = new Color(238f/255f,215f/255f,81f/255f,1);
    Color selected = new Color(240f/255f,188f/255f,79f/255f,1);
    Color disconnected = new Color(149f/255f,178f/255f,220f/255f,1);
    Color nodeGoal = new Color(167f/255f,202f/255f,85f/255f,1);

    public GameView(MainGame game){
        this.game = game;
        link = new Texture("white.png");
        node = new Texture("aa-node.png");
        shadow = new Texture("shadow1.png");
        linkregion = new TextureRegion(link);
        shadowregion = new TextureRegion(shadow);
    }


    public void draw(Batch batch, ShapeRenderer shapeRenderer){
        Model model = game.model;
        animationProgress= TimeUtils.millis()-beginMove;

        //if we're animating, step forward animation
        if(movingLink != null){
            if(animationProgress<=MOVE_LOG_ANIMATION_TIME) {
                float alpha = animationProgress / MOVE_LOG_ANIMATION_TIME;
                movingLink.center = oldpos.cpy().lerp(newpos, alpha);
                movingLink.rotation = MathUtils.lerp(oldRotate, newRotate, alpha);
            }else{
                movingLink.center = newpos;
                movingLink.rotation = newRotate;
                model.links.add(movingLink);
                movingLink=null;
                model.clearSelection();
                model.updateHighlight();
                if(model.isLevelBeaten())game.nextLevel();
            }
        }
        for(Link l:model.links){

            if(l.selected){
                //We draw selected later after everything else so that it appears on top.
                continue;
            }
            switch (l.state){
                case CONNECTED:batch.setColor(connected);break;
                case DISCONNECTED:batch.setColor(disconnected);break;
                case POTENTIAL:
                    if(!game.hints)continue;
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                    shapeRenderer.setColor(selected);
                    shapeRenderer.rectLine(l.n1.x*20+MainGame.nodeRadius,l.n1.y*20+MainGame.nodeRadius,
                            l.n2.x*20+MainGame.nodeRadius,l.n2.y*20+MainGame.nodeRadius,
                            MainGame.logWidth);
                    shapeRenderer.end();
                    continue;
            }
            drawLink(batch,l);
        }
        for(Node n:model.nodes.values()){
            if(n.on) {
                batch.setColor(connected);
            }else{
                batch.setColor(disconnected);
            }
            if(n.id==model.nodes.size-1)batch.setColor(nodeGoal);
            batch.begin();
            batch.draw(node, n.x * 20, n.y * 20, MainGame.nodeRadius * 2, MainGame.nodeRadius * 2);
            batch.end();
        }
        if(model.selected!=null) {
            batch.setColor(Color.BLACK);
            drawShadow(batch, model.selected);
            batch.setColor(selected);
            drawLink(batch, model.selected);
        }
    }

    public void drawLink(Batch batch, Link l){
        batch.begin();
        batch.draw(linkregion,
                l.center.x-MainGame.logWidth/2,l.center.y-l.distance*10,
                MainGame.logWidth/2, l.distance*20/2,
                MainGame.logWidth, l.distance*20,
                1,1,
                l.rotation);
        batch.end();
    }

    public void drawShadow(Batch batch, Link l){
        batch.begin();
        batch.enableBlending();
        batch.draw(shadowregion,
                l.center.x-(MainGame.logWidth+2f)/2,l.center.y-l.distance*20f/2-1f,
                (MainGame.logWidth+2f)/2, (l.distance*20f+2f)/2,
                MainGame.logWidth+2, l.distance*20+2,
                1,1,
                l.rotation);
        batch.end();
    }

    public void animateLinks(Link oldLink, Link newLink){
        if(oldLink==null||newLink==null){
            Gdx.app.log("GameView","links null");
            return;
        }

        movingLink = newLink;
        beginMove = TimeUtils.millis();

        newLink.hitBox.getCenter(newLink.center);
        oldLink.hitBox.getCenter(oldLink.center);

        Vector2 delta = newLink.center.cpy().sub(oldLink.center);
        //Slope is 1 or negative 1
        float slope;
        if(delta.x==0||delta.y==0){
            slope = 1;
        }else {
            slope = delta.y / delta.x;
            slope /= Math.abs(slope);
        }

        newpos=newLink.center.cpy();
        oldpos=oldLink.center.cpy();
        oldRotate=oldLink.rotation*slope;
        if(Math.abs(oldLink.rotation)==Math.abs(newLink.rotation)){
            newRotate=oldRotate;
        } else {
            newRotate = newLink.rotation * slope;
        }
        Gdx.app.log("GameView","slope "+slope+"  "+ oldRotate+"->"+newRotate);

        if(movingLink.selected){
            Gdx.app.log("GameView", "moving link is selected!");
        }
    }
}
