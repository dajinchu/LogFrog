package com.gmail.dajinchu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainGame implements InputProcessor, Screen, SavedGameListener{
    private final ScreenManager sm;
    SpriteBatch batch;
    Model model;
    private ShapeRenderer renderer;
    private ShapeRenderer debugRenderer;
    private Stage uistage;
    private Table infotable;

    static float nodeRadius=4, logWidth=4;
    static int mapHeight, mapWidth;


    public int level;
    private Viewport viewport;

    public static AnalyticsHelper ah;
    public final SavedGameHelper sgh;
    private TextButton options;
    private GameView view;
    private ScreenViewport uiviewport;
    private Label levelinfo;
    private Table optionmenu;

    public MainGame(ScreenManager sm, AnalyticsHelper ah, SavedGameHelper sgh) {
        this.ah=ah;
        this.sgh=sgh;
        this.sm = sm;
        level = sm.prefs.getInteger("level",1);

    }

    @Override
	public void show () {
		batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        debugRenderer = new ShapeRenderer();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        mapWidth = (int) (4*20+nodeRadius*2);
        mapHeight = (int) (6*20+nodeRadius*2);
        viewport = new FitViewport(mapWidth, mapHeight);

        uiviewport = new ScreenViewport();
        uistage = new Stage();

        //info table resides in a sort of upper bar above the gameplay, and has the options button
        //and level/moves info
        infotable = new Table();
        infotable.setFillParent(true);
        infotable.top();
        options = new TextButton("Options", sm.buttonStyle);
        options.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                optionmenu.setVisible(true);
                final InputListener optionlistener = new InputListener(){
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                        //Prevents events that are for the options menu from leaking to the game
                        //listener or the tapping-away-from-menu-box listener
                        event.stop();
                        return true;
                    }
                };
                optionmenu.addListener(optionlistener);
                uistage.addListener(new InputListener(){
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                        //Closes the options menu and unregisters these two listeners when anything
                        //is clicked, except if it is the menu box getting clicked, in which case
                        //the event is stopped by the previous listener
                        optionmenu.setVisible(false);
                        uistage.removeListener(this);
                        optionmenu.removeListener(optionlistener);
                        return false;
                    }
                });
            }
        });
        levelinfo = new Label("Level "+level+"  Moves: 0",sm.labelStyle);

        infotable.add(levelinfo).left().top().expandX();
        infotable.add(options).right().top().expandX();
        uistage.addActor(infotable);

        //option table is the menu overlay that pops up after hitting the options button
        optionmenu = new Table();
        optionmenu.setFillParent(true);
        optionmenu.center();
        Stack optionstack = new Stack();
        VerticalGroup options = new VerticalGroup();
        Image optionbackground = new Image(sm.buttonStyle.up);
        Label optiontitle = new Label("Options",sm.labelStyle);
        TextButton sync = new TextButton("Sync Play Games", sm.buttonStyle);
        TextButton mainmenu = new TextButton("Main Menu", sm.buttonStyle);
        sync.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sgh.load(MainGame.this);
            }
        });
        mainmenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sm.mainmenu();
            }
        });
        options.pad(20);
        options.space(20);
        options.addActor(optiontitle);
        options.addActor(sync);
        options.addActor(mainmenu);

        optionstack.add(optionbackground);
        optionstack.add(options);
        optionmenu.add(optionstack);
        optionmenu.setVisible(false);
        optionmenu.setDebug(true);
        uistage.addActor(optionmenu);

        //This draws the actual gameplay
        view = new GameView(this);



        //Now that levelinfo is instantiated, it is safe to load level
        sgh.load(this);
        loadLevel();

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(uistage);
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);
	}

    @Override
	public void render (float delta) {
        viewport.apply();

        renderer.setProjectionMatrix(viewport.getCamera().combined);
        batch.setProjectionMatrix(viewport.getCamera().combined);
        debugRenderer.setProjectionMatrix(viewport.getCamera().combined);
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(model==null){
            return;
        }
        batch.begin();
        view.draw(batch);
        batch.end();

        //.apply changes the "active" viewport. SO IMPORTANT, and NOT ON DOCUMENTATION
        uiviewport.apply();
        uistage.act(Gdx.graphics.getDeltaTime());
        uistage.draw();
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        optionmenu.drawDebug(debugRenderer);
        debugRenderer.end();

    }

    @Override
    public void resize(int w, int h){
        uiviewport.update(w, h, true);
        viewport.update(w, (int) (h - options.getHeight()), true);
        uistage.setViewport(uiviewport);
        /*
        float translate =viewport.unproject(new Vector3(0,h-viewport.project(new Vector2(0,mapHeight)).y,0)).y;
        Gdx.app.log("Main", "translate "+translate+"height "+h+" map "+viewport.project(new Vector2(0,mapHeight)).y);
        viewport.getCamera().translate(0,5,0);
        viewport.getCamera().update();*/
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode== Input.Keys.BACK){
            sm.mainmenu();
        }
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
                levelinfo.setText("Level "+level+"  Moves: "+model.movesToComplete);
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
        loadLevel();

        sgh.write(new byte[]{(byte) level});
        sm.prefs.putInteger("level", level);
    }

    private void loadLevel(){
        model = new Model(Gdx.files.internal("level"+level+".txt"));
        levelinfo.setText("Level "+level+"  Moves: 0");
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
        //sgh.write(new byte[]{(byte) level});

    }

    @Override
    public void resume(){

    }

    @Override
    public void dispose() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void onGameLoad(byte[] data) {
        if(level==data[0]){
            //Local and cloud copy are the same, don't load level
            return;
        }
        if(level>data[0]){
            //local copy is ahead of cloud. update the cloud version
            sgh.write(new byte[]{(byte) level});
        }else{
            //catch up with cloud copy
            level = data[0];
            loadLevel();
            sm.prefs.putInteger("level", level);
        }

    }
}
