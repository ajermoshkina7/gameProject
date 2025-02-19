package ee.taltech.swmg;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import ee.taltech.swmg.Screen.ShadowWizardMoneyGang;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument

/**
 * This method launches the client application with a FPS of 60, windowed mode with
 * dimensions 800x600, and a title of "Shadow Wizard Money Gang".
 */
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setWindowedMode(1024, 768);
		config.setTitle("Shadow Wizard Money Gang");
		new Lwjgl3Application(new GameClient(), config);
	}
}
