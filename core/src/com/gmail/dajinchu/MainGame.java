package com.gmail.dajinchu;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class MainGame extends ApplicationAdapter implements InputProcessor{
	SpriteBatch batch;
	private Model model;
    private ShapeRenderer renderer;
    private OrthographicCamera cam;

    float mapHeight=140f, mapWidth = 140f;


    Preferences prefs;
    int level;
    @Override
	public void create () {
		batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        cam = new OrthographicCamera(mapWidth * (w / h), mapHeight);
        cam.rotate(90, 0, 0, 1);
        cam.update();

        prefs=Gdx.app.getPreferences("My Prefs");
        level = prefs.getInteger("level",1);

        loadLevel(level);
        Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {
        cam.update();
        renderer.setProjectionMatrix(cam.combined);
        batch.setProjectionMatrix(cam.combined);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		renderer.begin(ShapeRenderer.ShapeType.Filled);
        for(Link l:model.links){
            switch (l.state){
                case CONNECTED:renderer.setColor(Color.YELLOW);break;
                case DISCONNECTED:renderer.setColor(Color.BLACK);break;
                case POTENTIAL:renderer.setColor(Color.LIGHT_GRAY);break;
            }
            if(l.selected)renderer.setColor(Color.MAROON);
            renderer.rectLine(l.n1.x*20,l.n1.y*20,l.n2.x*20,l.n2.y*20,4);
        }
        renderer.setColor(Color.LIGHT_GRAY);
        for(Node n:model.nodes.values()){
            if(n.on) {
                renderer.setColor(Color.YELLOW);
            }else{
                renderer.setColor(Color.DARK_GRAY);
            }
            if(n.id==model.nodes.size-1)renderer.setColor(Color.GREEN);
            renderer.circle(n.x*20,n.y*20,4);
        }
        renderer.end();
        }

    @Override
    public void resize(int w, int h){
        Gdx.app.log("Main", w+" "+h);
        cam.viewportWidth = mapHeight*w/h;
        cam.viewportHeight = mapWidth;
        cam.position.set(50, mapHeight / 2f, 0);
        cam.update();
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
        Vector3 v = cam.unproject(new Vector3(screenX,screenY,0));
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
