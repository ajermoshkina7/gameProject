package ee.taltech.swmg.packages;

import com.badlogic.gdx.math.Vector2;
import ee.taltech.swmg.Objects.Bullet;

import java.io.Serializable;

import java.util.List;

public class PlayerPackage implements Serializable {
    private Vector2 position;
    private List<Bullet> bullets;
    private int id;
    private boolean dead = false;
    private float currentTime;


    public float getCurrentTime() {
        return currentTime;
    }

    public PlayerPackage(Vector2 position, List<Bullet> bullets, int id, float currentTime) {
        this.position = position;
        this.bullets = bullets;
        this.id = id;
        this.currentTime = currentTime;
    }
    public PlayerPackage(boolean dead) {
        this.dead = dead;
    }
    public PlayerPackage() {

    }

    public Vector2 getPosition() {
        return position;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public int getId() {
        return id;
    }

    public boolean isDead() {
        return dead;
    }
}
