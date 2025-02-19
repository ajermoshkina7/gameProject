package ee.taltech.swmg.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ee.taltech.swmg.GameClient;

public class GameOverScreen extends ScreenAdapter {

    private final GameClient gameClient;
    private final Stage stage;
    private final ImageButton replayButton;
    private final Texture replayButtonActive;
    private final Texture replayButtonInactive;
    private final Texture exitButtonActive;
    private final Texture exitButtonInactive;
    private final ImageButton exitButton;
    private final Texture background;

    public GameOverScreen(GameClient gameClient) {
        this.gameClient = gameClient;
        stage = new Stage(new ScreenViewport());
        background = new Texture("gameScreens/GameOverScreen.png");

        // Create replay button textures
        replayButtonActive = new Texture("buttons/replayInactive.png");
        replayButtonInactive = new Texture("buttons/replayActive.jpg");

        // Create and draw replay game button
        Drawable replayButtonActiveDrawable = new TextureRegionDrawable(new TextureRegion(replayButtonInactive));
        Drawable replayButtonInactiveDrawable = new TextureRegionDrawable(new TextureRegion(replayButtonActive));

        // Set height and width for button when it is active
        replayButtonActiveDrawable.setMinHeight(replayButtonActiveDrawable.getMinHeight() * 2);
        replayButtonActiveDrawable.setMinWidth(replayButtonActiveDrawable.getMinWidth() * 2);


        // Set height and width for button when it is inactive
        replayButtonInactiveDrawable.setMinHeight(replayButtonInactiveDrawable.getMinHeight() * 2);
        replayButtonInactiveDrawable.setMinWidth(replayButtonInactiveDrawable.getMinWidth() * 2);

        replayButton = new ImageButton(replayButtonInactiveDrawable, replayButtonActiveDrawable);
        replayButton.setWidth(Gdx.graphics.getWidth() / 2.5f);
        replayButton.setHeight(Gdx.graphics.getWidth() / 9f);
        replayButton.setPosition(Gdx.graphics.getWidth() / 2f - replayButton.getWidth() / 2,
                Gdx.graphics.getHeight() / 1.5f - replayButton.getHeight() / 4);

        replayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                gameClient.restartGame();
                /// gameClient.setScreen( new ShadowWizardMoneyGang(gameClient));
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                replayButton.getImage().setDrawable(new TextureRegionDrawable(replayButtonActive));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                replayButton.getImage().setDrawable(new TextureRegionDrawable(replayButtonInactive));
            }
        });
        stage.addActor(replayButton);

        // Create and draw exit button textures
        exitButtonActive = new Texture("buttons/ee.jpg");
        exitButtonInactive = new Texture("buttons/ExitButtonNew.png");

        // Create the exit button
        Drawable exitButtonActiveDrawable = new TextureRegionDrawable(new TextureRegion(exitButtonInactive));
        Drawable exitButtonInactiveDrawable = new TextureRegionDrawable(new TextureRegion(exitButtonActive));

        // Set height and width for button when it is active
        exitButtonActiveDrawable.setMinHeight(exitButtonActiveDrawable.getMinHeight() * 2);
        exitButtonActiveDrawable.setMinWidth(exitButtonActiveDrawable.getMinWidth() * 2);


        // Set height and width for button when it is inactive
        exitButtonInactiveDrawable.setMinHeight(exitButtonInactiveDrawable.getMinHeight() * 2);
        exitButtonInactiveDrawable.setMinWidth(exitButtonInactiveDrawable.getMinWidth() * 2);


        exitButton = new ImageButton(exitButtonActiveDrawable, exitButtonInactiveDrawable);

        exitButton.setWidth(Gdx.graphics.getWidth() / 2.5f);
        exitButton.setHeight(Gdx.graphics.getWidth() / 9f);
        exitButton.setPosition(Gdx.graphics.getWidth() / 2f - exitButton.getWidth() / 2,
                Gdx.graphics.getHeight() / 5f - exitButton.getHeight() / 2);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                exitButton.getImage().setDrawable(new TextureRegionDrawable(exitButtonActive));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                exitButton.getImage().setDrawable(new TextureRegionDrawable(exitButtonInactive));
            }
        });
        stage.addActor(exitButton);
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
    public void dispose () {
        stage.dispose(); // Dispose the stage resources when the screen is disposed
    }
}
