package ee.taltech.swmg.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.esotericsoftware.kryonet.Client;
import ee.taltech.swmg.GameClient;

import java.io.IOException;

public class ChoiceScreen extends ScreenAdapter {
    private final GameClient gameClient;
    private final Stage stage;
    private final Texture background;
    private final Texture singlePlayerActive;
    private final Texture singlePlayerInactive;
    private final ImageButton singlePlayerButton;
    private final Texture multiPlayerActive;
    private final Texture multiPlayerInactive;
    private final ImageButton multiPlayerButton;

    public ChoiceScreen(GameClient gameClient) {
        this.gameClient = gameClient;
        stage = new Stage(new ScreenViewport());
        background = new Texture("gameScreens/choose.png");

        singlePlayerActive = new Texture("buttons/sing.png");
        singlePlayerInactive = new Texture("buttons/singActive.jpg");

        Drawable singleplayerActiveDrawable = new TextureRegionDrawable(new TextureRegion(singlePlayerActive));
        Drawable singleplayerInactiveDrawable = new TextureRegionDrawable(new TextureRegion(singlePlayerInactive));

        singleplayerActiveDrawable.setMinHeight(singleplayerActiveDrawable.getMinHeight() * 2);
        singleplayerActiveDrawable.setMinWidth(singleplayerActiveDrawable.getMinWidth() * 2);


        // Set height and width for button when it is inactive
        singleplayerInactiveDrawable.setMinHeight(singleplayerInactiveDrawable.getMinHeight() * 2);
        singleplayerInactiveDrawable.setMinWidth(singleplayerInactiveDrawable.getMinWidth() * 2);


        singlePlayerButton = new ImageButton(singleplayerActiveDrawable, singleplayerInactiveDrawable);
        singlePlayerButton.setWidth(Gdx.graphics.getWidth() / 4f * 1.5f);
        singlePlayerButton.setHeight(Gdx.graphics.getWidth() / 9f);
        singlePlayerButton.setPosition(Gdx.graphics.getWidth() / 2.25f - singlePlayerButton.getWidth(),
                Gdx.graphics.getHeight() / 3f - singlePlayerButton.getHeight() / 2);

        singlePlayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameClient.setMultiplayer(false);
                gameClient.setScreen(new ShadowWizardMoneyGang(gameClient, null));
                ///gameClient.setScreen( new ShadowWizardMoneyGang(gameClient));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                singlePlayerButton.getImage().setDrawable(new TextureRegionDrawable(singlePlayerActive));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                singlePlayerButton.getImage().setDrawable(new TextureRegionDrawable(singlePlayerInactive));
            }
        });
        stage.addActor(singlePlayerButton);

        multiPlayerActive = new Texture("buttons/multi.png");
        multiPlayerInactive = new Texture("buttons/multiActive.jpg");

        Drawable multiplayerActiveDrawable = new TextureRegionDrawable(new TextureRegion(multiPlayerActive));
        Drawable multiplayerInactiveDrawable = new TextureRegionDrawable(new TextureRegion(multiPlayerInactive));

        multiplayerActiveDrawable.setMinHeight(multiplayerActiveDrawable.getMinHeight() * 2);
        multiplayerActiveDrawable.setMinWidth(multiplayerActiveDrawable.getMinWidth() * 2);


        // Set height and width for button when it is inactive
        multiplayerInactiveDrawable.setMinHeight(multiplayerInactiveDrawable.getMinHeight() * 2);
        multiplayerInactiveDrawable.setMinWidth(multiplayerInactiveDrawable.getMinWidth() * 2);


        multiPlayerButton = new ImageButton(multiplayerActiveDrawable, multiplayerInactiveDrawable);
        multiPlayerButton.setWidth(Gdx.graphics.getWidth() / 4f * 1.5f);
        multiPlayerButton.setHeight(Gdx.graphics.getWidth() / 9f);
        multiPlayerButton.setPosition(Gdx.graphics.getWidth() / 1.75f - multiPlayerButton.getWidth() / 35,
                Gdx.graphics.getHeight() / 3f - multiPlayerButton.getHeight() / 2);

        multiPlayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameClient.setMultiplayer(true);
                gameClient.setScreen(new WaitingRoom(gameClient));
                ///gameClient.setScreen( new ShadowWizardMoneyGang(gameClient));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                multiPlayerButton.getImage().setDrawable(new TextureRegionDrawable(multiPlayerActive));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                multiPlayerButton.getImage().setDrawable(new TextureRegionDrawable(multiPlayerInactive));
            }
        });
        stage.addActor(multiPlayerButton);
    }
        @Override
        public void show () {
            Gdx.input.setInputProcessor(stage);
        }

        @Override
        public void render ( float delta){
            // Clear the screen and update/draw the stage
            Gdx.gl.glClearColor(1, 1, 1, 1);
            stage.act();

            stage.getBatch().begin();
            stage.getBatch().draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            stage.getBatch().end();

            stage.draw();
        }

        @Override
        public void dispose() {
            stage.dispose(); // Dispose the stage resources when the screen is disposed
        }
}
