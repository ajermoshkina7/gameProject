package ee.taltech.swmg.packages;

import com.badlogic.gdx.math.Vector2;

public class EnemyPackage {
    private String texturePath;
    private Vector2 position;
    private int id;
    private float enemyHealth;
    private float damage;
    public EnemyPackage(Vector2 position, int id, String texturePath, float health) {
        this.position = position;
        this.id = id;
        this.texturePath = texturePath;
        this.enemyHealth = health;
    }
    public EnemyPackage(int id, float damage) {
        this.id = id;
        this.damage = damage;
    }
    public EnemyPackage() {
    }

    public Vector2 getPosition() {
        return position;
    }

    public int getId() {
        return id;
    }

    public String getTexturePath() {
        return texturePath;
    }
    public float getEnemyHealth() {
        return enemyHealth;
    }
    public float getDamage() {
        return damage;
    }


}
