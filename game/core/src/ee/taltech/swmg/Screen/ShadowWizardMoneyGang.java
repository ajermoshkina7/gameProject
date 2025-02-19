package ee.taltech.swmg.Screen;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.swmg.Enemy.Enemy;
import ee.taltech.swmg.GameClient;
import ee.taltech.swmg.Player.KeyboardAdapter;
import ee.taltech.swmg.Player.Opponent;
import ee.taltech.swmg.Player.Wizzard;
import ee.taltech.swmg.Stages.PlayerInterface;
import ee.taltech.swmg.Stages.PopUpMenu;
import ee.taltech.swmg.packages.EnemyPackage;
import ee.taltech.swmg.packages.PackageHandler;
import ee.taltech.swmg.packages.PlayerPackage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The ShadowWizardMoneyGang class extends the ApplicationAdapter class.
 * It represents a client in a multiplayer game, handling the creation, rendering, and disposal of game objects.
 */
public class ShadowWizardMoneyGang extends ApplicationAdapter implements Screen {
	private final List<String> textureList = new ArrayList<>(Arrays.asList("enemies/enemy1.png","enemies/enemy2.png",
			"enemies/enemy3.png", "enemies/enemy4.png"));
	GameClient gameClient;
	private Client client; // The client object for the multiplayer game.
	public OrthographicCamera gameCam;
	private TmxMapLoader mapLoader = new TmxMapLoader();
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	public static ArrayList<Rectangle> obstacles = new ArrayList<>();
	private SpriteBatch batch; // Used to draw 2D rectangles that reference a texture (region) and implement the Batch interface.
	public static final int V_WIDTH = Gdx.graphics.getWidth(); // The width of the viewport.
	public static final int V_HEIGHT = Gdx.graphics.getHeight(); // The height of the viewport.
	private Wizzard wizard;
	private Map<Integer, PlayerPackage> receivedGamePackages = new HashMap<>();
	private List<Enemy> enemies = new ArrayList<>();
	private final KeyboardAdapter inputAdapter = new KeyboardAdapter();
	private PlayerInterface playerInterface;
	private Map<Integer, Opponent> opponents = new HashMap<>();
	private List<EnemyPackage> enemyPackages = new ArrayList<>();
	private PackageHandler handler = new PackageHandler();
	private boolean waitForStart = true;
	private float elapsedTime;
	private PopUpMenu popUpMenu = new PopUpMenu(inputAdapter, this);
	Music gameMusic = Gdx.audio.newMusic(Gdx.files.internal("music/gameMusic.mp3"));


	public ShadowWizardMoneyGang (GameClient gameClient, Client client) {
		this.gameClient = gameClient;
		this.client= client;
		handler.setShadowWizardMoneyGang(this);
	}

	/**
	 * Client connects to the localhost.
	 * This method is called when the application is created.
	 * It initializes the game objects and sets up the client-server connection.
	 */
	@Override
	public void show() {
		elapsedTime = 0f;
		Gdx.input.setInputProcessor(inputAdapter);
		batch = new SpriteBatch();
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		gameCam = new OrthographicCamera(30, 30 * (h / w));
		// map
		map = mapLoader.load("map.tmx");
		renderer = new OrthogonalTiledMapRenderer(map);
		MapObjects objects = map.getLayers().get(1).getObjects();
		objects.forEach(obj -> {
			if (obj instanceof RectangleMapObject) {
				Rectangle rect = ((RectangleMapObject) obj).getRectangle();
				obstacles.add(rect);

			} else {
				System.out.println(obj.getClass().getName());
			}
		});

		// The background music for the game.
		gameMusic.setVolume(0.25f);
		gameMusic.setLooping(true);
		gameMusic.play();
		// Netwoork Setup
		if (gameClient.isMultiplayer()) {
			client.addListener(new Listener.ThreadedListener(new Listener() {
				@Override
				public void received(Connection connection, Object object) {
					if (object instanceof Map) {
						receivedGamePackages = (Map<Integer, PlayerPackage>) object;
					}
					if (object instanceof List) {
						enemyPackages = (List<EnemyPackage>) object;
					}
				}
				public void disconnected(Connection connection) {

				}
			}));
			handler.setClient(client);
			handler.setGameClient(gameClient);
		} else {
			enemies = IntStream.range(0, 4).mapToObj(i -> {
				int x = MathUtils.random(1024), y = MathUtils.random(768);
				return new Enemy(x, y, textureList.get(MathUtils.random(textureList.size() - 1)));
			}).collect(Collectors.toList());

		}
		// Player
		wizard = new Wizzard(3500, 4000, gameCam);
		handler.setBatch(batch);
		handler.setWizard(wizard);
		// Create the player interface
		playerInterface = new PlayerInterface(new ScreenViewport(), wizard);

	}

	/**
	 * This method is called when the application is created.
	 * It initializes the game objects and sets up the client-server connection.
	 */
	@Override
	public void create () {
	}

	/**
	 * This method is called every frame to render the game objects.
	 * It handles user input and updates the game objects on the screen.
	 */
	@Override
	public void render (float delta) {
		ScreenUtils.clear(0, 1, 0, 1);
		elapsedTime += delta;
		gameCam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		gameCam.update();
		wizard.moveTo(inputAdapter.getDirection(), obstacles);
		wizard.camUpdate();
		wizard.shoot(inputAdapter.getFirePressed());
		batch.setProjectionMatrix(gameCam.combined);
		if (gameClient.isMultiplayer()) {
			client.sendUDP(new PlayerPackage(wizard.getPosition(), wizard.getBullets(), client.getID(), elapsedTime));
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			this.dispose();
			gameClient.setScreen(new StartScreen(gameClient));
		}
		batch.begin();
		renderer.setView(gameCam);
		renderer.render();
		if (wizard.getCurrentHealth() <= 0) {
			if (gameClient.isMultiplayer()) {
				client.sendUDP(new PlayerPackage(true));
				this.dispose();
				gameMusic.stop();
				gameClient.setScreen(new YouLostScreen(gameClient));
			} else {
				this.dispose();
				gameMusic.stop();
				gameClient.setScreen(new GameOverScreen(gameClient));
			}
		}
		if (gameClient.isMultiplayer()) {
            try {
                handler.handlePackages(receivedGamePackages, enemyPackages, enemies, opponents);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
			handler.handleEnemies(enemies, elapsedTime);
		}
		wizard.render(batch);
		batch.end();
		playerInterface.draw(Gdx.graphics.getDeltaTime(), elapsedTime);
		if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
			popUpMenu.toggleVisibility();
		}

		if (wizard.getUpgradeQueue() > 0 && !popUpMenu.isVisible()) {
			popUpMenu.toggleVisibility();
			wizard.setUpgradeQueue(wizard.getUpgradeQueue() - 1);
		}

		if (popUpMenu.isVisible()) {
			popUpMenu.act(Gdx.graphics.getDeltaTime());
			popUpMenu.draw();

		}

	}

	@Override
	public void hide() {

	}

	@Override
	public void resize(int width, int height) {
		// Update the stage's viewport to handle the screen resizing.
		playerInterface.getViewport().update(width, height, true);
	}

	/**
	 * This method is called when the application is disposed.
	 * It releases all the resources of the game.
	 */
	@Override
	public void dispose () {
		gameMusic.stop();
		if (gameClient.isMultiplayer()) {
			System.out.println("stop");
			client.stop();
			System.out.println("close");
			client.close();
			try {
				client.dispose();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			opponents.values().stream().forEach(Opponent::dispose);
			opponents.clear();
		}
		batch.dispose();
		wizard.dispose();
		enemies.stream().forEach(Enemy::dispose);
		enemies.clear();
		playerInterface.dispose(); // Dispose of the stage and its elements.
	}


	public Wizzard getWizard() {
		return wizard;
	}

	public KeyboardAdapter getInputAdapter() {
		return inputAdapter;
	}

	public void stopMusic() {
		gameMusic.stop();
	}
}
