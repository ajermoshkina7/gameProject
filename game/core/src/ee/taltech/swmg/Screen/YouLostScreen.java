package ee.taltech.swmg.Screen;

import com.badlogic.gdx.Gdx;
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

public class YouLostScreen extends ScreenAdapter {
    private final GameClient gameClient;
    private final Stage stage;
    private final Texture exitButtonActive;
    private final Texture exitButtonInactive;
    private final ImageButton exitButton;
    private final ImageButton backToMenuButton;
    private final Texture backToMenuInactive;
    private final Texture backToMenuActive;
    private final Texture background;

    public YouLostScreen(GameClient gameClient) {
        this.gameClient = gameClient;
        stage = new Stage(new ScreenViewport());
        background = new Texture("gameScreens/youLost.png");

        backToMenuInactive = new Texture("buttons/backToMenu.png");
        backToMenuActive = new Texture("buttons/backToMenuActive.jpg");

        Drawable replayButtonActiveDrawable = new TextureRegionDrawable(new TextureRegion(backToMenuActive));
        Drawable replayButtonInactiveDrawable = new TextureRegionDrawable(new TextureRegion(backToMenuInactive));

        replayButtonActiveDrawable.setMinHeight(replayButtonActiveDrawable.getMinHeight() * 2);
        replayButtonActiveDrawable.setMinWidth(replayButtonActiveDrawable.getMinWidth() * 2);


        replayButtonInactiveDrawable.setMinHeight(replayButtonInactiveDrawable.getMinHeight() * 2);
        replayButtonInactiveDrawable.setMinWidth(replayButtonInactiveDrawable.getMinWidth() * 2);

        backToMenuButton = new ImageButton(replayButtonInactiveDrawable, replayButtonActiveDrawable);
        backToMenuButton.setWidth(Gdx.graphics.getWidth() / 2.5f);
        backToMenuButton.setHeight(Gdx.graphics.getWidth() / 9f);
        backToMenuButton.setPosition(Gdx.graphics.getWidth() / 2f - backToMenuButton.getWidth() / 2,
                Gdx.graphics.getHeight() / 1.5f - backToMenuButton.getHeight() / 4);

        backToMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                gameClient.setScreen(new StartScreen(gameClient));
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                backToMenuButton.getImage().setDrawable(new TextureRegionDrawable(backToMenuInactive));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                backToMenuButton.getImage().setDrawable(new TextureRegionDrawable(backToMenuActive));
            }
        });
        stage.addActor(backToMenuButton);

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
