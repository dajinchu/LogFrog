package com.gmail.dajinchu;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainGame extends ApplicationAdapter implements InputProcessor{
	SpriteBatch batch;
	private Model model;
    private ShapeRenderer renderer;

    float nodeRadius=4, logWidth=4;
    int mapHeight, mapWidth;


    Preferences prefs;
    int level;
    private Viewport viewport;

    public static AnalyticsHelper ah;

    public MainGame(AnalyticsHelper ah) {
        this.ah=ah;
    }

    @Override
	public void create () {
		batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        mapWidth = (int) (4*20+nodeRadius*2);
        mapHeight = (int) (6*20+nodeRadius*2);
        viewport = new FitViewport(mapWidth, mapHeight);


        prefs=Gdx.app.getPreferences("My Prefs");
        level = prefs.getInteger("level",1);

        loadLevel(level);

        Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {
        renderer.setProjectionMatrix(viewport.getCamera().combined);
        batch.setProjectionMatrix(viewport.getCamera().combined);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.BLACK);
        renderer.rect(0,0,mapWidth,mapHeight);
        renderer.end();
		renderer.begin(ShapeRenderer.ShapeType.Filled);
        for(Link l:model.links){
            switch (l.state){
                case CONNECTED:renderer.setColor(Color.YELLOW);break;
                case DISCONNECTED:renderer.setColor(Color.BLACK);break;
                case POTENTIAL:renderer.setColor(Color.LIGHT_GRAY);break;
            }
            if(l.selected)renderer.setColor(Color.MAROON);
            renderer.rectLine(l.n1.x * 20 + 4, l.n1.y * 20 + 4, l.n2.x * 20 + 4, l.n2.y * 20 + 4, logWidth);
        }
        renderer.setColor(Color.LIGHT_GRAY);
        for(Node n:model.nodes.values()){
            if(n.on) {
                renderer.setColor(Color.YELLOW);
            }else{
                renderer.setColor(Color.DARK_GRAY);
            }
            if(n.id==model.nodes.size-1)renderer.setColor(Color.GREEN);
            renderer.circle(n.x*20+4,n.y*20+4,nodeRadius);
        }
        renderer.end();
        }

    @Override
    public void resize(int w, int h){
        Gdx.app.log("Main", w + " " + h);
        viewport.update(w, h, true);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 v = viewport.unproject(new Vector3(screenX,screenY,0));
        Rectangle clickBox = new Rectangle(v.x,v.y,1,1);
        Link[] clickedLinks = new Link[3];

        //Debug:
        Gdx.app.log("Input", screenX+","+screenY);
        Gdx.app.log("Input", v.x+","+v.y);
        for(Node n:model.nodes.values())Gdx.app.log("Nodes", n.id+" "+n.x+" "+n.y);
        for(Link link:model.links)Gdx.app.log("Links", link.n1.id+" "+link.n2.id+" "+link.state);
        for(LinkSpace ln:model.linknodes)Gdx.app.log("Placeholder Nodes", ln.id+" "+ln.x+" "+ln.y);

        //lower the clickedLinks[x] number, the higher priority
        for(Link l : model.links){
            if(l.rect.overlaps(clickBox)){
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
                model.selectLink(clickedLinks[i]);
                model.updateHighlight();
                if(model.nodes.get(model.nodes.size-1).on){
                    //Goal has been reached!
                    nextLevel();
                }
                return true;
            }
        }
        //Since we haven't returned from the for loop above, it means user clicked on nothing
        //If we just clicked away, clear the selected link
        model.clearSelection();
        model.updateHighlight();
        return true;
    }

    public void nextLevel(){
        ah.sendCustomTiming("User Interaction", (int) TimeUtils.timeSinceMillis(model.loadedMillis),
                "Time to finish a level", "Level "+level);

        ah.sendEvent("Level Balance",
                "Moves to finish a level", "Level "+level, model.movesToComplete);

        level++;
        prefs.putInteger("level",level);
        loadLevel(level);
    }

    private void loadLevel(int lvl){
        model = new Model(Gdx.files.internal("level"+lvl+".txt"));
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void pause(){
        prefs.flush();
    }
}
