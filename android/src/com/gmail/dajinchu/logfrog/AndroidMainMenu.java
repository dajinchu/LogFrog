package com.gmail.dajinchu.logfrog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.gmail.dajinchu.MainGame;
import com.gmail.dajinchu.MainMenu;
import com.gmail.dajinchu.ScreenManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;
import com.google.example.games.basegameutils.BaseGameUtils;

/**
 * Created by Da-Jin on 5/31/2015.
 */
public class AndroidMainMenu implements MainMenu, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    //debug
    private ShapeRenderer shapeRenderer;

    private Table table;
    private Stage stage;

    private final GoogleApiClient mGoogleApiClient;
    private final AndroidLauncher context;
    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInFlow = true;
    private boolean mSignInClicked = false;
    private static int RC_SIGN_IN = 9001;
    private ScreenManager sm;
    private TextButton startGame;
    private MainGame maingame;
    private TextButton GPGS;

    public AndroidMainMenu(AndroidLauncher context){
        this.context = context;

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .addApi(Drive.API).addScope(Drive.SCOPE_APPFOLDER)
                .build();

    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        table.drawDebug(shapeRenderer);

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        table = new Table();
        table.setFillParent(true);

        GPGS = new TextButton("Sign Out of G+", sm.buttonStyle);
        GPGS.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                toggleSignIn();
            }
        });
        startGame = new TextButton("Start", sm.buttonStyle);
        startGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AndroidMainMenu.this.dispose();
                sm.setScreen(maingame);
            }
        });
        startGame.setVisible(false);
        GPGS.setVisible(false);

        table.add(GPGS);
        table.row();
        table.add(startGame);

        stage.addActor(table);

        shapeRenderer = new ShapeRenderer();

    }

    public void toggleSignIn(){
        if(mSignInClicked){
            mSignInClicked=false;
            mGoogleApiClient.disconnect();
            startGame.setVisible(false);
            GPGS.setText("Sign Into G+");
        }else{
            GPGS.setText("Sign Out of G+");
            mGoogleApiClient.connect();
            mSignInClicked=true;
        }

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
    }

    public void onStart(){
        if (mGoogleApiClient != null) {

            mGoogleApiClient.connect();
        }else{
            Log.d("ALauncher", "GoogleApiClient not created");
        }
    }

    public void onStop(){
        mGoogleApiClient.disconnect();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        if (requestCode == RC_SIGN_IN) {
            mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if (resultCode == Activity.RESULT_OK) {
                mGoogleApiClient.connect();
            } else {
                // Bring up an error dialog to alert the user that sign-in
                // failed. The R.string.signin_failure should reference an error
                // string in your strings.xml file that tells the user they
                // could not be signed in, such as "Unable to sign in."
                BaseGameUtils.showActivityResultError(context,
                        requestCode, resultCode, R.string.signin_failure, R.string.generic_error);
            }
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
        shapeRenderer.dispose();
    }

    @Override
    public void onConnected(Bundle bundle) {
        startGame.setVisible(true);//TODO getting null pointers!
        GPGS.setVisible(true);
        maingame = new MainGame(sm,
                new AndroidAnalyticsHelper(context),
                new AndroidSavedGameHelper(mGoogleApiClient));
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        if (mResolvingConnectionFailure) {
            // Already resolving
            return;
        }

        // If the sign in button was clicked or if auto sign-in is enabled,
        // launch the sign-in flow
        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;

            // Attempt to resolve the connection failure using BaseGameUtils.
            // The R.string.signin_other_error value should reference a generic
            // error string in your strings.xml file, such as "There was
            // an issue with sign in, please try again later."
            if (!BaseGameUtils.resolveConnectionFailure(context,
                    mGoogleApiClient, connectionResult,
                    RC_SIGN_IN, context.getString(R.string.signin_other_error))) {
                mResolvingConnectionFailure = false;
            }
        }
    }

    @Override
    public void setScreenManager(ScreenManager sm) {
        this.sm=sm;
    }
}
