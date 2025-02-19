package ee.taltech.swmg.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.swmg.GameClient;
import ee.taltech.swmg.Objects.Bullet;
import ee.taltech.swmg.Player.Wizzard;
import ee.taltech.swmg.packages.EnemyPackage;
import ee.taltech.swmg.packages.PlayerPackage;
import ee.taltech.swmg.packages.Vector2Serializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WaitingRoom extends ScreenAdapter {
    private GameClient gameClient;
    private final Stage stage;
    private final Texture background;
    private Client client;
    private boolean access;
    private Music gameMusic = Gdx.audio.newMusic(Gdx.files.internal("music/elevatorMusic.mp3"));

    public WaitingRoom(GameClient gameClient) {
        this.gameClient = gameClient;
        stage = new Stage(new ScreenViewport());
        background = new Texture("gameScreens/pleaseWait.png");
    }
    @Override
    public void show () {
        Gdx.input.setInputProcessor(stage);
        client = new Client();
        client.start();
        System.out.println("Client is ready");
        // 193.40.255.17
        try {
            client.connect(5000, "193.40.255.17", 8082, 8083);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Kryo kryo = client.getKryo();
        kryo.setRegistrationRequired(true);
        kryo.register(Boolean.class);
        kryo.register(HashMap.class);
        kryo.register(Vector2.class, new Vector2Serializer());
        kryo.register(ArrayList.class);
        kryo.register(EnemyPackage.class);
        kryo.register(Rectangle.class);
        kryo.register(Bullet.class);
        kryo.register(LinkedList.class);
        kryo.register(PlayerPackage.class);
        client.addListener(new Listener.ThreadedListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof Boolean) {
                    System.out.println((boolean) object);
                    access = (Boolean) object;
                }

            }
            public void connected(Connection connection) {
                if (!connection.isConnected()) {
                    try {
                        client.connect(5000, "193.40.255.17", 8082, 8083);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                };
            }

            public void disconnected(Connection connection) {

            }
        }));
        client.sendTCP(true);
        gameMusic.setVolume(0.25f);
        gameMusic.setLooping(true);
        gameMusic.play();
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
        if (access) {
            gameMusic.stop();
            gameClient.setScreen(new ShadowWizardMoneyGang(gameClient, client));
        }
    }
    @Override
    public void dispose() {
        stage.dispose(); // Dispose the stage resources when the screen is disposed
        try {
            client.dispose();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        client.close();
    }
}
