package com.gmail.dajinchu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Da-Jin on 7/2/2015.
 */
public class GameController {
    public void touchDown(Rectangle clickBox, Model model, boolean linkAnimation, GameView view){

        Link[] clickedLinks = new Link[3];

        //lower the clickedLinks[x] number, the higher priority
        for(Link l : model.links){
            if(l.hitBox.overlaps(clickBox)){
                Gdx.app.log("Clicked", "y");
                if(l.state== Link.STATE.POTENTIAL){
                    clickedLinks[0]=l;
                }else if(l.selected){
                    clickedLinks[2]=l;
                }else{
                    clickedLinks[1]=l;
                }
            }
        }
        for(int i = 0; i < clickedLinks.length; i++){
            //clickedLinks is sorted by priority, lowest index is used and we return since we're done
            if(clickedLinks[i]!=null){
                Link oldLink = model.selectLink(clickedLinks[i]);
                if(linkAnimation) {
                    view.animateLinks(oldLink, clickedLinks[i]);
                }
                return;
            }
        }
        //Since we haven't returned from the for loop above, it means user clicked on nothing
        //If we just clicked away, clear the selected link
        model.clearSelection();
        model.updateHighlight();
    }
}
