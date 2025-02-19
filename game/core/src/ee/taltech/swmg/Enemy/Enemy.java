package ee.taltech.swmg.Enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.esotericsoftware.kryonet.Client;
import ee.taltech.swmg.Objects.Bullet;
import ee.taltech.swmg.Player.Wizzard;
import ee.taltech.swmg.Screen.ShadowWizardMoneyGang;
import ee.taltech.swmg.Stages.HealthBar;
import ee.taltech.swmg.packages.EnemyPackage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Enemy extends Actor {
    private final List<String> textureList = new ArrayList<>(Arrays.asList("enemies/enemy1.png","enemies/enemy2.png",
            "enemies/enemy3.png", "enemies/enemy4.png"));
    private transient Vector2 enemyDirection = new Vector2();
    private boolean alive = true;
    private Vector2 position = new Vector2();
    private transient Texture texture;
    private transient TextureRegion textureRegion;
    private String texturePath;
    private transient Rectangle hitBox = new Rectangle();
    private final float size = 64;
    private final float halfSize = size / 2;
    private static int nextId = 0;
    private int id;
    private final HealthBar healthBar = new HealthBar((int) size, 10);
    private float health = 100f;
    private int xpValue = 10;

    public Enemy() {
        position.set(MathUtils.random(1036, 5251), MathUtils.random(1145, 5281));
        this.id = nextId++;
        this.textureRegion = new TextureRegion(new Texture(Gdx.files.internal(textureList.get(MathUtils.random(0, 3)))));
    }
    public Enemy(EnemyPackage enemyPackage) {
        this.id = enemyPackage.getId();
        this.position.set(enemyPackage.getPosition());
        this.texturePath = enemyPackage.getTexturePath();
    }
    public Enemy(float x, float y, String texturePath) {
        position.set(x, y);
        this.id = nextId++;
        texture = new Texture(Gdx.files.internal(texturePath));
        textureRegion = new TextureRegion(texture);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Enemy) {
            return ((Enemy) obj).getId() == id;
        } else {
            return false;
        }
    }

    protected void updateHitBox() {
        hitBox.set(position.x, position.y, size, size);
    }
    /**
     * Move enemy to the position of player.
     *
     * @param playerPos position of player
     */
    public void moveTo(Vector2 playerPos) {
        enemyDirection.set(0, 0);
        if (playerPos.x > position.x) enemyDirection.add(1, 0);
        if (playerPos.x < position.x) enemyDirection.add(-1, 0);
        if (playerPos.y > position.y) enemyDirection.add(0, 1);
        if (playerPos.y < position.y) enemyDirection.add(0, -1);

        // Normalize the direction vector to ensure consistent speed in all directions
        if (enemyDirection.len() > 0) {
            enemyDirection.nor(); // Normalize the vector to have a length of 1
        }

        // Assuming you have a defined speed variable for how fast the enemy should move
        float speed = 3; // Example speed value
        enemyDirection.scl(speed); // Scale the normalized vector by the speed

        position.add(enemyDirection);
    }
    public void attack(Wizzard player) {
        updateHitBox();
        if (hitBox.overlaps(player.getHitBox())) {
            player.setCurrentHealth(player.getCurrentHealth() - 0.02f);
        }
    }
    // Multiplayer version
    public void takeDamage(Wizzard player, Client client) {
        for (Bullet bullet : player.getBullets()) {
            if (hitBox.overlaps(bullet.getHitbox())) {
                System.out.println("get hit");
                bullet.setAlive(false);
                client.sendUDP(new EnemyPackage(this.id, player.getDamage()));
                if ((health - player.getDamage()) <= 0) {
                    player.addXp(this.getXpValue());
                }
            }
        }
    }
    // SinglePlayer version
    public void takeDamage(Wizzard player) {
        for (Bullet bullet : player.getBullets()) {
            if (hitBox.overlaps(bullet.getHitbox())) {
                System.out.println("get hit");
                bullet.setAlive(false);
                this.health -= player.getDamage();
                System.out.println(health);
                if (health <= 0) {
                    this.setAlive(false);
                    player.addXp(this.getXpValue());
                }
            }
        }
    }
    public double getDistanceBetween(Vector2 playerPos) {
        return Math.sqrt(Math.pow(playerPos.x - position.x, 2) + Math.pow(playerPos.y - position.y, 2));
    }
    public void setTextureToEnemy() {
        texture = new Texture(Gdx.files.internal(texturePath));
        textureRegion = new TextureRegion(texture);
    }
    public void render(Batch batch,  Wizzard player) {
        Vector3 screenCoordinates = new Vector3(position.x, position.y, 0);
        player.getCam().project(screenCoordinates);
        if (screenCoordinates.x < -size
                || screenCoordinates.x > (ShadowWizardMoneyGang.V_WIDTH + size)
                || screenCoordinates.y < -size
                || screenCoordinates.y > (ShadowWizardMoneyGang.V_HEIGHT + size)) {
            return;
        }
        batch.draw(textureRegion,
                position.x,
                position.y,
                halfSize,
                halfSize,
                size,
                size,
                1,
                1,
                0
        );
        healthBar.setPosition(position.x, position.y + size);
        healthBar.setValue(health / 100f);
        healthBar.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        healthBar.draw(batch, 1f);
    }
    public EnemyPackage createPackage() {
        return new EnemyPackage(this.position, this.id, this.texturePath, this.health);
    }
    public void assimilatePackage(EnemyPackage enemyPackage) {
        this.position = enemyPackage.getPosition();
        this.health = enemyPackage.getEnemyHealth();
    }

    public boolean isAlive() {
        return alive;
    }
    public void dispose() {
        if (texture != null) texture.dispose();
    }

    public Optional<Texture> getTexture() {
        if (texture == null) return Optional.empty();
        return Optional.of(texture);
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public int getId() {
        return id;
    }

    public int getXpValue() {
        return xpValue;
    }

    public void setXpValue(int xpValue) {
        this.xpValue = xpValue;
    }

    public float getHealth() {
        return health;
    }
}
