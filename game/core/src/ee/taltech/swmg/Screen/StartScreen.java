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
import org.w3c.dom.Text;

public class StartScreen extends ScreenAdapter {
    private final GameClient gameClient;
    private final Stage stage;
    private final Texture playButtonActive;
    private final Texture playButtonInactive;
    private final ImageButton playButton;
    private final Texture exitButtonActive;
    private final Texture exitButtonInactive;
    private final ImageButton exitButton;
    private final Texture background;


    public StartScreen(GameClient gameClient) {
        this.gameClient = gameClient;
        stage = new Stage(new ScreenViewport());
        background = new Texture("gameScreens/gam.png");

        // Create start game button textures
        playButtonActive = new Texture("buttons/PlayButtonNew.png");
        playButtonInactive = new Texture("buttons/pppppp.jpg");


        // Create and draw start game button
        Drawable playButtonActiveDrawable = new TextureRegionDrawable(new TextureRegion(playButtonInactive));
        Drawable playButtonInactiveDrawable = new TextureRegionDrawable(new TextureRegion(playButtonActive));

        // Set height and width for button when it is active
        playButtonActiveDrawable.setMinHeight(playButtonActiveDrawable.getMinHeight() * 2);
        playButtonActiveDrawable.setMinWidth(playButtonActiveDrawable.getMinWidth() * 2);


        // Set height and width for button when it is inactive
        playButtonInactiveDrawable.setMinHeight(playButtonInactiveDrawable.getMinHeight() * 2);
        playButtonInactiveDrawable.setMinWidth(playButtonInactiveDrawable.getMinWidth() * 2);


        playButton = new ImageButton(playButtonInactiveDrawable, playButtonActiveDrawable);
        playButton.setWidth(Gdx.graphics.getWidth() / 4f * 2);
        playButton.setHeight(Gdx.graphics.getWidth() / 7f);
        playButton.setPosition(Gdx.graphics.getWidth() / 2f - playButton.getWidth() / 2,
                Gdx.graphics.getHeight() / 2.2f - playButton.getHeight() / 1.5f);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                gameClient.setScreen(new ChoiceScreen(gameClient));
                ///gameClient.setScreen( new ShadowWizardMoneyGang(gameClient));
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                playButton.getImage().setDrawable(new TextureRegionDrawable(playButtonActive));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                playButton.getImage().setDrawable(new TextureRegionDrawable(playButtonInactive));
            }
        });
        stage.addActor(playButton);

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

        exitButton.setWidth(Gdx.graphics.getWidth() / 4f * 2);
        exitButton.setHeight(Gdx.graphics.getWidth() / 7f);
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
