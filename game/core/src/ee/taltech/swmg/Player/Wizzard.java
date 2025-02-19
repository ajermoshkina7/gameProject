package ee.taltech.swmg.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import ee.taltech.swmg.Objects.Bullet;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Wizzard {

    private OrthographicCamera cam;
    protected final float size = 64;
    protected final float halfSize = size / 2;
    protected Vector2 position = new Vector2();
    protected Texture texture;
    protected TextureRegion textureRegion;
    protected final Vector2 angle = new Vector2();
    protected List<Bullet> bullets = new LinkedList<>();
    private Rectangle hitBox = new Rectangle();
    protected TextureRegion bulletTextureRegion;
    private float mana = 100f;
    private float maxMana = 100f;
    private float manaRestoration = 0.3f;
    private float manaConsumption = 10f;
    private int damage = 10;
    private int upgradeQueue = 0;


    /**
     * Introducing multiple health variables for futureproofing
     */
    private double currentHealth;
    private double baseMaxHealth;
    private double currentMaxHealth;
    private double startingHealth;
    private double healthRegen = 0.1;

    // Leveling variables
    private int xp = 0;
    private int level = 1;

//    private final Color healthBarColor = Color.RED;4

    public OrthographicCamera getCam() {
        return cam;
    }

    public Wizzard(float x, float y, OrthographicCamera cam) {
        texture = new Texture(Gdx.files.internal("players/Wizard1.gif"));
        textureRegion = new TextureRegion(texture);
        position.set(x, y);
        this.cam = cam;
        this.bulletTextureRegion = new TextureRegion(new Texture(Gdx.files.internal("objects/fireball.png")));
        update();
        this.baseMaxHealth = 100;
        this.currentMaxHealth = baseMaxHealth;
        this.startingHealth = currentMaxHealth;
        this.currentHealth = startingHealth;

    }
    /**
     * Constructor for opponent.
     */
    Wizzard() {
    }
    /**
     * Update the hitbox of entity using the position.
     *
     */
    protected void update() {
        hitBox.set(position.x, position.y, size, size);
    }
    /**
     * Pose player in the center of camera
     */
    public void camUpdate() {
        this.cam.position.set(position.x + halfSize, position.y + halfSize, 0);
        cam.update();
    }
    /**
     * Render.
     *
     * @param batch to render
     */
    public void render(Batch batch) {
        if (mana < maxMana) {
            mana += manaRestoration;
        }
        if (currentHealth < currentMaxHealth) {
            currentHealth += healthRegen;
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
                angle.angleDeg()
                );
        bullets.forEach(bullet -> {
            bullet.update(this);
            bullet.updateHitbox();
            bullet.render(batch, bulletTextureRegion, this);
        });
        List<Bullet> copyBullets = new LinkedList<>(bullets);
        copyBullets.forEach(bullet -> {
            if (!bullet.isAlive()) {
                bullets.remove(bullet);
            }
        });
        /* float healthBarWidth = size * (health / 100.0f);
        batch.setColor(healthBarColor);
        batch.draw(new TextureRegion(new Texture(Gdx.files.internal("Nightmare.jpg"))),
                position.x, position.y + size + 5, healthBarWidth, 5);
        batch.setColor(Color.WHITE); */
    }
    public void dispose() {
        if (texture != null) texture.dispose();
    }
    /**
     * change position of player
     *
     * @param direction
     */
    public void moveTo(Vector2 direction, List<Rectangle> obstacles) {
        position.add(direction);
        update();
        obstacles.stream().forEach(rect -> {
            if (rect.overlaps(hitBox)) {
                position.sub(direction);
            }
        });
    }

    /**
     * if mouse were clicked create a bullet object.
     *
     * @param mousePosition
     */
    public void shoot(Optional<Vector2> mousePosition) {
        if (mousePosition.isPresent() && mana >= manaConsumption) {
            System.out.println(position);
            mana -= manaConsumption;
            Vector3 mouse = new Vector3(mousePosition.get().x, mousePosition.get().y, 0);
            cam.unproject(mouse);
            bullets.add(new Bullet(new Vector2(position.x, position.y), new Vector2(mouse.x, mouse.y)));
        }
    }

//    public void rotateTo(Vector2 mousePosition) {
//        angle.set(mousePosition).sub(position.x + halfSize, position.y + halfSize);
//    }

    public void addXp(int amount) {
        xp += amount;
        checkLevelUp();
    }

    private void checkLevelUp() {
        int xpThreshold = calculateXpThreshold(level);
        if (xp >= xpThreshold) {
            xp -= xpThreshold; // Keeps the surplus xp for the next level
            level++;
            upgradeQueue++;
            // Implement level-up effects here
        }
    }

    private int calculateXpThreshold(int level) {
        return 100 * level; // Adjust formula as needed
    }


    public Vector2 getPosition() {
        return position;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public double getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(double currentHealth) {
        this.currentHealth = currentHealth;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public void setCurrentMaxHealth(double currentMaxHealth) {
        this.currentMaxHealth = currentMaxHealth;
    }

    public double getCurrentMaxHealth() {
        return currentMaxHealth;
    }

    public double getBaseMaxHealth() {
        return baseMaxHealth;
    }

    public void setBaseMaxHealth(double baseMaxHealth) {
        this.baseMaxHealth = baseMaxHealth;
    }


    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getNextLevelXp() {
        return calculateXpThreshold(level++);
    }

    public int getCurrentLevelXp() {
        return calculateXpThreshold(level);
    }

    public float getMana() {
        return mana;
    }

    public int getDamage() {
        return damage;
    }

    public double getHealthRegen() {
        return healthRegen;
    }

    public void setHealthRegen(double healthRegen) {
        this.healthRegen = healthRegen;
    }

    public float getMaxMana() {
        return maxMana;
    }

    public void setMaxMana(float maxMana) {
        this.maxMana = maxMana;
    }

    public float getManaRestoration() {
        return manaRestoration;
    }

    public void setManaRestoration(float manaRestoration) {
        this.manaRestoration = manaRestoration;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getUpgradeQueue() {
        return upgradeQueue;
    }

    public void setUpgradeQueue(int upgradeQueue) {
        this.upgradeQueue = upgradeQueue;
    }

    /*
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
     */
}
