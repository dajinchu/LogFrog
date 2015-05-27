package com.gmail.dajinchu;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainGame extends ApplicationAdapter implements InputProcessor{
	SpriteBatch batch;
	private Model model;
    private ShapeRenderer renderer;
    private Viewport viewport;

    float nodeRadius=4, logWidth=4;
    int mapWidth, mapHeight;

    Preferences prefs;
    int level;

    @Override
	public void create () {
		batch = new SpriteBatch();
        renderer = new ShapeRenderer();

        mapWidth = (int) (4*20+nodeRadius*2);
        mapHeight = (int) (6*20+nodeRadius*2);
        viewport = new ExtendViewport(mapWidth, mapHeight);

        //cam.rotate(90, 0, 0, 1);

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
		renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.rect(0,0,mapWidth,mapHeight);
        for(Link l:model.links){
            switch (l.state){
                case CONNECTED:renderer.setColor(Color.YELLOW);break;
                case DISCONNECTED:renderer.setColor(Color.BLACK);break;
                case POTENTIAL:renderer.setColor(Color.LIGHT_GRAY);break;
            }
            if(l.selected)renderer.setColor(Color.MAROON);
            renderer.rectLine(l.n1.x*20,l.n1.y*20,l.n2.x*20,l.n2.y*20,logWidth);
        }
        renderer.setColor(Color.LIGHT_GRAY);
        for(Node n:model.nodes.values()){
            if(n.on) {
                renderer.setColor(Color.YELLOW);
            }else{
                renderer.setColor(Color.DARK_GRAY);
            }
            if(n.id==model.nodes.size-1)renderer.setColor(Color.GREEN);
            renderer.circle(n.x*20,n.y*20,nodeRadius);
        }
        renderer.end();
        }

    @Override
    public void resize(int w, int h){
        Gdx.app.log("Main", w+" "+h);
        if(Gdx.input.getNativeOrientation().equals(Input.Orientation.Portrait)){

            viewport.update(w,h,true);
            viewport.setWorldSize();
            viewport.getCamera().up.set(0, 1, 0);
            viewport.getCamera().rotate(-Gdx.input.getRotation(), 0, 0, 1);
            Gdx.app.log("Main","landscape"+Gdx.input.getRotation());
        }else {
            viewport.update(w, h, true);
        }
        viewport.getCamera().translate(-4,-4,0);
        viewport.getCamera().update();

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
        Link clickedLink = null;

        //Debug:
        Gdx.app.log("Input", screenX+","+screenY);
        Gdx.app.log("Input", v.x+","+v.y);
        for(Node n:model.nodes.values())Gdx.app.log("Nodes", n.id+" "+n.x+" "+n.y);
        for(Link link:model.links)Gdx.app.log("Links", link.n1.id+" "+link.n2.id+" "+link.state);
        for(LinkSpace ln:model.linknodes)Gdx.app.log("Placeholder Nodes", ln.id+" "+ln.x+" "+ln.y);


        for(Link l : model.links){
            if(l.rect.overlaps(clickBox)){
                Gdx.app.log("Clicked", "y");
                clickedLink = l;
                break;
            }
        }
        if(clickedLink!=null){
            model.selectLink(clickedLink);
            model.updateHighlight();
            if(model.nodes.get(model.nodes.size-1).on){
                //Goal has been reached!
                nextLevel();
            }
        }
        return false;
    }

    public void nextLevel(){
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
