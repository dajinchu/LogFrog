package com.gmail.dajinchu;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MainGame extends ApplicationAdapter {
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
	}

	@Override
	public void render () {
        cam.update();
        renderer.setProjectionMatrix(cam.combined);
        batch.setProjectionMatrix(cam.combined);
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
        for(Node n:model.nodes){
            batch.draw(img,n.x*20,n.y*20,10,10);
        }
        batch.end();
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        for(Link l:model.links){
            renderer.rectLine(l.n1.x*20,l.n1.y*20,l.n2.x*20,l.n2.y*20,2);
        }
        renderer.end();
	}

    @Override
    public void resize(int w, int h){
        Gdx.app.log("Main", w+" "+h);
        cam.viewportWidth=mapWidth*w/h;
        cam.viewportHeight = mapHeight;
        cam.update();
    }
}
