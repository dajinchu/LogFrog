package com.gmail.dajinchu.logfrog;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.gmail.dajinchu.MainGame;
import com.gmail.dajinchu.ScreenManager;
import com.google.android.gms.common.api.GoogleApiClient;

public class AndroidLauncher extends AndroidApplication {

    private GoogleApiClient mGoogleApiClient;

    private MainGame game;
    private AndroidAnalyticsHelper aah;
    private AndroidSavedGameHelper savehelper;
    private AndroidMainMenu mainmenu;

    @Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useImmersiveMode=true;
        mainmenu = new AndroidMainMenu(this);
        initialize(new ScreenManager(mainmenu), config);
	}

    @Override
    public void onStart(){
        super.onStart();
        mainmenu.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainmenu.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        mainmenu.onActivityResult(requestCode,resultCode,intent);
    }
}
