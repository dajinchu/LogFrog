package com.gmail.dajinchu.logfrog;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.gmail.dajinchu.AnalyticsHelper;
import com.gmail.dajinchu.MainGame;

public class AndroidLauncher extends AndroidApplication {

    @Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        AnalyticsHelper aah = new AndroidAnalyticsHelper(this);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useImmersiveMode=true;
		initialize(new MainGame(aah), config);
	}
}
