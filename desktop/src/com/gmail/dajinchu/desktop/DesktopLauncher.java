package com.gmail.dajinchu.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gmail.dajinchu.ScreenManager;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width=400;
        config.height=550;
        config.addIcon("windowsicon.png", Files.FileType.Internal);
		new LwjglApplication(new ScreenManager(new DesktopMainMenu()), config);
	}
}
