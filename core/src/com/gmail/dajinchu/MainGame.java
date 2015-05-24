package com.gmail.dajinchu;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class MainGame extends ApplicationAdapter implements InputProcessor{
	SpriteBatch batch;
	Texture img;
    private Model model;
    private ShapeRenderer renderer;
    private Camera cam;

    float mapHeight=140f, mapWidth = 140f;

    @Override
	public void create () {
		batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        cam = new OrthographicCamera(mapWidth * (w / h), mapHeight);
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        cam.update();
		img = new Texture("badlogic.jpg");
        model = new Model(Gdx.files.internal("level1.txt"));

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
        batch.begin();
        for(Node n:model.nodes.values()){
            batch.draw(img,n.x*20-5,n.y*20,10,10);
        }
        batch.end();
        for(Link l:model.links){
            switch (l.state){
                case CONNECTED:renderer.setColor(Color.YELLOW);break;
                case DISCONNECTED:renderer.setColor(Color.BLACK);break;
                case POTENTIAL:renderer.setColor(Color.GRAY);break;
            }
            if(l.selected)renderer.setColor(Color.MAROON);
            renderer.rect(l.rect.x, l.rect.y, l.rect.width, l.rect.height);
        }
        renderer.end();
        }

    @Override
    public void resize(int w, int h){
        Gdx.app.log("Main", w+" "+h);
        cam.viewportWidth = mapWidth*w/h;
        cam.viewportHeight = mapHeight;
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
        Rectangle click = new Rectangle(v.x,v.y,1,1);
        Gdx.app.log("Input", screenX+","+screenY);
        Gdx.app.log("Input", v.x+","+v.y);
        Object[] l =model.links.begin();
        for(int i = 0, n = model.links.size; i<n;i++){
            if(((Link)l[i]).rect.overlaps(click)){
                Gdx.app.log("Clicked", "y");
                model.selectLink(((Link)l[i]));
            }
        }
        return false;
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
}
