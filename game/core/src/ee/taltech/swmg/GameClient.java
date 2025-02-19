package ee.taltech.swmg;

import com.badlogic.gdx.Game;
import ee.taltech.swmg.Screen.StartScreen;

public class GameClient extends Game {
    private boolean Multiplayer;
    private StartScreen startScreen;

    /**
     *Set the start screen.
     *
     */
    @Override
    public void create() {
        setScreen(new StartScreen(this));
    }

    public void restartGame() {
        // Implement code to reset the game state to its initial state

        setScreen(new StartScreen(this));
    }

    public boolean isMultiplayer() {
        return Multiplayer;
    }

    public void setMultiplayer(boolean multiplayer) {
        Multiplayer = multiplayer;
    }
}
